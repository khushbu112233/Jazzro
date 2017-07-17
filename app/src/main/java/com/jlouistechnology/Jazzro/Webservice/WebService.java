package com.jlouistechnology.Jazzro.Webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jlouistechnology.Jazzro.Helper.MySSLSocketFactory1;
import com.jlouistechnology.Jazzro.Helper.ParsedResponse;
import com.jlouistechnology.Jazzro.Model.CardScannerModel;
import com.jlouistechnology.Jazzro.R;
import com.loopj.android.http.AsyncHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

/**
 * Created by aipxperts on 9/12/16.
 */
public class WebService {
    public static ProgressDialog mProgressDialog;
    // public static String BASE_URL ="http://dev.jazzro.com/api/";
    //dev url link
    ///public static String BASE_URL = "https://dev.jazzro.com/api/v1/";

    //live url link
    public static  String BASE_URL="https://dashboard.jazzro.com/api/v1/";

    public static String USER = BASE_URL + "user";
    public static String CONTACT = BASE_URL + "contact";
    public static String SINGLE_CONTACT = BASE_URL + "singleContact";

    public static String GROUP = BASE_URL + "group";
    public static String Login = BASE_URL + "token";
    public static String CONTACT_DELETE = BASE_URL + "contact/delete";
    public static TextView mProgressTitleTv;


    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void showProgress(Context context) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            dismissProgress();
        try {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.show();
            //  mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setContentView(R.layout.progressdialog);
            mProgressTitleTv=(TextView)mProgressDialog.findViewById(R.id.msg_title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissProgress() {
        if (mProgressDialog != null) {
            try {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    public static String PostData1Daynamic(JSONArray jsonArray, String url, String value,String data) {
        String s = "";
        Log.e("Bearer", "" + "Bearer " + value);
        String finalJsonArray;

        try {
            cz.msebera.android.httpclient.client.HttpClient httpClient = new cz.msebera.android.httpclient.impl.client.DefaultHttpClient();
            cz.msebera.android.httpclient.client.methods.HttpPost httpPost = new cz.msebera.android.httpclient.client.methods.HttpPost(url);

            httpPost.setHeader("Content-Type", "application/json");
            finalJsonArray=jsonArray.toString();
            Log.e("finalJsonArray",finalJsonArray+"--");
            httpPost.setEntity(new ByteArrayEntity(finalJsonArray.getBytes("UTF8")));

            httpPost.addHeader("Authorization", value);
            cz.msebera.android.httpclient.HttpResponse httpResponse = httpClient.execute(httpPost);

            cz.msebera.android.httpclient.HttpEntity httpEntity = httpResponse.getEntity();
            s = readResponse(httpResponse);
            Log.e("s", "" + s);
        } catch (Exception exception) {
        }
        return s;


    }


    public static String PostData1(String updateID, String[] values, String[] valuse, String url, String value) {
        String s = "";
        Log.e("Bearer", "" + "Bearer " + value);
        try {
            cz.msebera.android.httpclient.client.HttpClient httpClient = new cz.msebera.android.httpclient.impl.client.DefaultHttpClient();
            cz.msebera.android.httpclient.client.methods.HttpPost httpPost = new cz.msebera.android.httpclient.client.methods.HttpPost(url);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", updateID);


            Log.e("jsonObject", "" + jsonObject);
            String str = jsonObject.toString();
            Log.e("QQQ", " Json Array : " + jsonObject);

           /* List<cz.msebera.android.httpclient.NameValuePair> list = new ArrayList<cz.msebera.android.httpclient.NameValuePair>();
            for (int i = 0; i < valuse.length; i++) {
                //     list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(values[i],URLEncoder.encode(valuse[i], "UTF-8")));
                list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair("ID", updateID));
                Log.e("NNN : ", "Key :  " + values[i] + "  value " + updateID);

            }*/
            // httpPost.addHeader("Content-Type","application/x-www-form-urlencoded");

            httpPost.setEntity(new ByteArrayEntity(str.getBytes("UTF8")));
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", value);
            cz.msebera.android.httpclient.HttpResponse httpResponse = httpClient.execute(httpPost);
            cz.msebera.android.httpclient.HttpEntity httpEntity = httpResponse.getEntity();
            s = readResponse(httpResponse);
            Log.e("s", "" + s);
        } catch (Exception exception) {
        }
        return s;


    }

    public static String PostData2(ArrayList<String> group_ids,String value1, String value2,String value3,String value4,String value5,String value6, String url, String value) {

        String s="";
        try
        {
            cz.msebera.android.httpclient.client.HttpClient httpClient = new cz.msebera.android.httpclient.impl.client.DefaultHttpClient();
            cz.msebera.android.httpclient.client.methods.HttpPost httpPost = new cz.msebera.android.httpclient.client.methods.HttpPost(url);

            List<NameValuePair> list = new ArrayList<NameValuePair>();
            JSONObject jsonObject = new JSONObject();
            // for (int i =values.length;i>0;i--) {
            if(!value1.equalsIgnoreCase(""))
            {
                jsonObject.put("id", value1);
            }
            for(int i=0;i<group_ids.size();i++)
            /*{
                String first = group_ids.get(i);
                first = first.replaceAll("\"","");
                group_ids.add(first);
            }*/
            if(group_ids.size()>0)

            {

                jsonObject.put("groups", group_ids);

            }
            jsonObject.put("fname", value2);
            jsonObject.put("lname", value3);
            jsonObject.put("company_name", value4);
            jsonObject.put("phone1", value5);
            jsonObject.put("email1", value6);
            //     list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(values[i], URLEncoder.encode(valuse[i], "UTF-8")));
            //  list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(valuse[i],values[i]));

            //  }
            String str = jsonObject.toString().replace("\"[","[");
            Log.e("jsonObject", "" + str.replace("]\"","]"));

            httpPost.setHeader("Content-Type", "application/json");

            // String json = jsonObject.toString();
            //   httpPost.setEntity(new StringEntity(json, "UTF-8"));
            httpPost.setEntity(new ByteArrayEntity(str.replace("]\"","]").getBytes("UTF8")));

            httpPost.addHeader("Authorization", value);

            cz.msebera.android.httpclient.HttpResponse httpResponse=  httpClient.execute(httpPost);

            cz.msebera.android.httpclient.HttpEntity httpEntity=httpResponse.getEntity();
            s= readResponse(httpResponse);
            Log.e("s",""+s);
        }
        catch(Exception exception)  {}
        return s;





    }

    public static String PostData(String value, String value1, String url) {
        String s = "";
        try {
            cz.msebera.android.httpclient.client.HttpClient httpClient = new cz.msebera.android.httpclient.impl.client.DefaultHttpClient();
            cz.msebera.android.httpclient.client.methods.HttpPost httpPost = new cz.msebera.android.httpclient.client.methods.HttpPost(url);

            List<NameValuePair> list = new ArrayList<NameValuePair>();
            JSONObject jsonObject = new JSONObject();
            // for (int i =values.length;i>0;i--) {

            jsonObject.put("password", value1);
            jsonObject.put("email", value);
            //     list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(values[i], URLEncoder.encode(valuse[i], "UTF-8")));
            //  list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(valuse[i],values[i]));

            //  }
            Log.e("jsonObject", "" + jsonObject);


            httpPost.setHeader("Content-Type", "application/json");

            // String json = jsonObject.toString();
            //   httpPost.setEntity(new StringEntity(json, "UTF-8"));
            httpPost.setEntity(new ByteArrayEntity(jsonObject.toString().getBytes("UTF8")));


            // httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            //   httpPost.addHeader("Authorization","Bearer "+ value);
            // httpPost.setEntity(new cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity(list));
            cz.msebera.android.httpclient.HttpResponse httpResponse = httpClient.execute(httpPost);

            cz.msebera.android.httpclient.HttpEntity httpEntity = httpResponse.getEntity();
            s = readResponse(httpResponse);
            Log.e("token", "" + s);
        } catch (Exception exception) {
        }
        return s;


    }


    public static String forgotpassword(String email, String url, String token) {
        String s = "";
        try {
            cz.msebera.android.httpclient.client.HttpClient httpClient = new cz.msebera.android.httpclient.impl.client.DefaultHttpClient();
            cz.msebera.android.httpclient.client.methods.HttpPost httpPost = new cz.msebera.android.httpclient.client.methods.HttpPost(url);

            List<NameValuePair> list = new ArrayList<NameValuePair>();
            JSONObject jsonObject = new JSONObject();
            // for (int i =values.length;i>0;i--) {

            jsonObject.put("email", email);
            //     list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(values[i], URLEncoder.encode(valuse[i], "UTF-8")));
            //  list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(valuse[i],values[i]));

            //  }
            Log.e("jsonObject", "" + jsonObject);

            httpPost.addHeader("Authorization", token);
            httpPost.setHeader("Content-Type", "application/json");
            // String json = jsonObject.toString();
            //   httpPost.setEntity(new StringEntity(json, "UTF-8"));
            httpPost.setEntity(new ByteArrayEntity(jsonObject.toString().getBytes("UTF8")));


            // httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            //   httpPost.addHeader("Authorization","Bearer "+ value);
            // httpPost.setEntity(new cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity(list));
            cz.msebera.android.httpclient.HttpResponse httpResponse = httpClient.execute(httpPost);

            cz.msebera.android.httpclient.HttpEntity httpEntity = httpResponse.getEntity();
            s = readResponse(httpResponse);
            Log.e("token", "" + s);
        } catch (Exception exception) {
        }
        return s;


    }


    public static String readResponse(cz.msebera.android.httpclient.HttpResponse res) {
        InputStream is = null;
        String return_text = "";
        try {
            is = res.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return_text = sb.toString();
        } catch (Exception e) {

        }
        return return_text;

    }

    public static String GetData1(String url, String value) {
        InputStream inputStream = null;
        String result = "";
        try {
            //  URI uri = new URI(url);
            HttpClient httpclient = getNewHttpClient();
            //  HttpClient httpclient = new DefaultHttpClient();
            // Log.e("value",""+value);
            Log.e("url",url.replaceAll(" ","")+"--");
            HttpGet get = new HttpGet(url.replaceAll(" ",""));
            get.addHeader("Accept", "application/json");
            get.addHeader("Content-Type", "application/json");
            get.addHeader("Authorization", value);

            Log.e("value",value+" ");
            //  get.setHeader(new BasicHeader("Content-Type", "application/json"));
            //get.setHeader(new BasicHeader("Authorization",value));
            //  new BasicHeader("Authorization: Token","2hwhdh443hdhfh43j3jdej3j3")
           // Log.e("get ", "" + get.getAllHeaders().length);
            HttpResponse httpResponse = httpclient.execute(get);


            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }


        // optional default is GET
      /*  try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            //add request header
           // con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("Authorization",value);
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            Log.e("response_contact",""+response.toString());
            System.out.println(response.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return result;


    }

    public static HttpClient getNewHttpClient() {
        try {
            // Log.v("truststore","1------------------");
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            //  Log.v("truststore","2------------------"+trustStore);
            SSLSocketFactory sf = new MySSLSocketFactory1(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            //SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
            //  sslFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public static String GetData(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            //   HttpClient httpclient = getNewHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;


    }

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getResponseUsingHeader(String url, String header_data) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(url);

        httpGet.setHeader("Authorization", header_data);
        // httpGet.addHeader("Content-Type", "application/x-zip");
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();

        // Read content & Log
        InputStream inputStream = httpEntity.getContent();

        BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
        StringBuilder sBuilder = new StringBuilder();

        String line = null;
        while ((line = bReader.readLine()) != null) {
            sBuilder.append(line + "\n");
        }

        inputStream.close();
        return sBuilder.toString();
    }

    public static String cretaeGroup(String url, String label, String color, String token) {
        String s = "";
        try {
            cz.msebera.android.httpclient.client.HttpClient httpClient = new cz.msebera.android.httpclient.impl.client.DefaultHttpClient();
            cz.msebera.android.httpclient.client.methods.HttpPost httpPost = new cz.msebera.android.httpclient.client.methods.HttpPost(url);

            List<NameValuePair> list = new ArrayList<NameValuePair>();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            // for (int i =values.length;i>0;i--) {

            jsonObject.put("label", label);
            jsonObject.put("color", color);

            jsonArray.put(jsonObject);
            //     list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(values[i], URLEncoder.encode(valuse[i], "UTF-8")));
            //  list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(valuse[i],values[i]));

            //  }
            Log.e("jsonObject", "" + jsonObject);

            httpPost.addHeader("Authorization", token);
            httpPost.setHeader("Content-Type", "application/json");
            // String json = jsonObject.toString();
            //   httpPost.setEntity(new StringEntity(json, "UTF-8"));
            httpPost.setEntity(new ByteArrayEntity(jsonArray.toString().getBytes("UTF8")));


            // httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            //   httpPost.addHeader("Authorization","Bearer "+ value);
            // httpPost.setEntity(new cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity(list));
            cz.msebera.android.httpclient.HttpResponse httpResponse = httpClient.execute(httpPost);

            cz.msebera.android.httpclient.HttpEntity httpEntity = httpResponse.getEntity();
            s = readResponse(httpResponse);
            Log.e("token", "" + s);
        } catch (Exception exception) {
        }
        return s;
    }

    public static String deleteGroup(String url, String id, String token) {
        String s = "";
        try {
            cz.msebera.android.httpclient.client.HttpClient httpClient = new cz.msebera.android.httpclient.impl.client.DefaultHttpClient();
            cz.msebera.android.httpclient.client.methods.HttpPost httpPost = new cz.msebera.android.httpclient.client.methods.HttpPost(url);

            List<NameValuePair> list = new ArrayList<NameValuePair>();
            JSONObject jsonObject = new JSONObject();
            // for (int i =values.length;i>0;i--) {

            jsonObject.put("id", id);

            //     list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(values[i], URLEncoder.encode(valuse[i], "UTF-8")));
            //  list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(valuse[i],values[i]));

            //  }
            Log.e("jsonObject", "" + jsonObject);

            httpPost.addHeader("Authorization", token);
            httpPost.setHeader("Content-Type", "application/json");
            // String json = jsonObject.toString();
            //   httpPost.setEntity(new StringEntity(json, "UTF-8"));
            httpPost.setEntity(new ByteArrayEntity(jsonObject.toString().getBytes("UTF8")));


            // httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            //   httpPost.addHeader("Authorization","Bearer "+ value);
            // httpPost.setEntity(new cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity(list));
            cz.msebera.android.httpclient.HttpResponse httpResponse = httpClient.execute(httpPost);

            cz.msebera.android.httpclient.HttpEntity httpEntity = httpResponse.getEntity();
            s = readResponse(httpResponse);
            Log.e("token", "" + s);
        } catch (Exception exception) {
        }
        return s;


    }

    public static String updateFGroup(String url, String id, String label, String color, String token) {
        String s = "";
        try {
            cz.msebera.android.httpclient.client.HttpClient httpClient = new cz.msebera.android.httpclient.impl.client.DefaultHttpClient();
            cz.msebera.android.httpclient.client.methods.HttpPost httpPost = new cz.msebera.android.httpclient.client.methods.HttpPost(url);

            List<NameValuePair> list = new ArrayList<NameValuePair>();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            // for (int i =values.length;i>0;i--) {
            jsonObject.put("id", id);
            jsonObject.put("label", label);
            jsonObject.put("color", color);

            jsonArray.put(jsonObject);
            //     list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(values[i], URLEncoder.encode(valuse[i], "UTF-8")));
            //  list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(valuse[i],values[i]));

            //  }
            Log.e("jsonObject", "" + jsonObject);

            httpPost.addHeader("Authorization", token);
            httpPost.setHeader("Content-Type", "application/json");
            // String json = jsonObject.toString();
            //   httpPost.setEntity(new StringEntity(json, "UTF-8"));
            httpPost.setEntity(new ByteArrayEntity(jsonArray.toString().getBytes("UTF8")));


            // httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            //   httpPost.addHeader("Authorization","Bearer "+ value);
            // httpPost.setEntity(new cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity(list));
            cz.msebera.android.httpclient.HttpResponse httpResponse = httpClient.execute(httpPost);

            cz.msebera.android.httpclient.HttpEntity httpEntity = httpResponse.getEntity();
            s = readResponse(httpResponse);
            Log.e("token", "" + s);
        } catch (Exception exception) {
        }
        return s;
    }

    public static String cardReaderApi1(String front) {
        String s = "";
        String url = "https://api.fullcontact.com/v2/cardReader?webhookUrl=http://jazzro.com&verified=low";

        try {
            cz.msebera.android.httpclient.client.HttpClient httpClient = new cz.msebera.android.httpclient.impl.client.DefaultHttpClient();
            cz.msebera.android.httpclient.client.methods.HttpPost httpPost = new cz.msebera.android.httpclient.client.methods.HttpPost(url);


            List<NameValuePair> list = new ArrayList<NameValuePair>();
            JSONObject jsonObject = new JSONObject();
            // for (int i =values.length;i>0;i--) {

            jsonObject.put("front", front);

            //     list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(values[i], URLEncoder.encode(valuse[i], "UTF-8")));
            //  list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(valuse[i],values[i]));

            //  }
            Log.e("jsonObject", "" + jsonObject);

            httpPost.addHeader("X-FullContact-APIKey", "2b35f697b8bf302e");
            httpPost.setHeader("Content-Type", "application/json");
            // String json = jsonObject.toString();
            //   httpPost.setEntity(new StringEntity(json, "UTF-8"));
            httpPost.setEntity(new ByteArrayEntity(jsonObject.toString().getBytes("UTF8")));


            // httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            //   httpPost.addHeader("Authorization","Bearer "+ value);
            // httpPost.setEntity(new cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity(list));
            cz.msebera.android.httpclient.HttpResponse httpResponse = httpClient.execute(httpPost);

            cz.msebera.android.httpclient.HttpEntity httpEntity = httpResponse.getEntity();
            s = readResponse(httpResponse);
            Log.e("KKK 1", "" + s);
        } catch (Exception exception) {
        }
        return s;


    }

    public static String cardReaderApi2(String id) {
        String s = "";
        String url = "https://api.fullcontact.com/v2/cardReader?webhookUrl=http://jazzro.com&URID=" + id + "";

        try {
            cz.msebera.android.httpclient.client.HttpClient httpClient = new cz.msebera.android.httpclient.impl.client.DefaultHttpClient();
            cz.msebera.android.httpclient.client.methods.HttpPost httpPost = new cz.msebera.android.httpclient.client.methods.HttpPost(url);


            // List<NameValuePair> list = new ArrayList<NameValuePair>();
            // JSONObject jsonObject = new JSONObject();
            // for (int i =values.length;i>0;i--) {

            // jsonObject.put("front", front);

            //     list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(values[i], URLEncoder.encode(valuse[i], "UTF-8")));
            //  list.add(new cz.msebera.android.httpclient.message.BasicNameValuePair(valuse[i],values[i]));

            //  }
            //  Log.e("jsonObject", "" + jsonObject);

            httpPost.addHeader("X-FullContact-APIKey", "2b35f697b8bf302e");
            httpPost.setHeader("Content-Type", "application/json");
            // String json = jsonObject.toString();
            //   httpPost.setEntity(new StringEntity(json, "UTF-8"));
            //   httpPost.setEntity(new ByteArrayEntity(jsonObject.toString().getBytes("UTF8")));


            // httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            //   httpPost.addHeader("Authorization","Bearer "+ value);
            // httpPost.setEntity(new cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity(list));
            cz.msebera.android.httpclient.HttpResponse httpResponse = httpClient.execute(httpPost);

            cz.msebera.android.httpclient.HttpEntity httpEntity = httpResponse.getEntity();
            s = readResponse(httpResponse);
            Log.e("KKK 2", "" + s);
        } catch (Exception exception) {
        }
        return s;
    }

    public static String getResponseUsingHeaderPost(String url,
                                                    String header_data, List<org.apache.http.NameValuePair> params)
            throws IOException {
        HttpClient httpClient = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(url + "?" + URLEncodedUtils.format(params, "utf-8"));
        httpGet.addHeader("X-FullContact-APIKey", header_data);
        httpGet.addHeader("Content-Type", "application/json");
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();

        // Read content & Log
        InputStream inputStream = httpEntity.getContent();

        BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
        StringBuilder sBuilder = new StringBuilder();

        String line = null;
        while ((line = bReader.readLine()) != null) {
            sBuilder.append(line + "\n");
        }

        inputStream.close();
        return sBuilder.toString();
    }


    public static ParsedResponse cardReader2(String id) throws JSONException, IOException {

        ParsedResponse p = new ParsedResponse();

        // Making a request to url and getting response


        List<org.apache.http.NameValuePair> params = new ArrayList<org.apache.http.NameValuePair>();
        params.add(new BasicNameValuePair("webhookUrl", "http://jazzro.com"));
        params.add(new BasicNameValuePair("URID", "1487679423308-f9e48143c30dc1adb305f3ac88054fb329594f4a"));

        //  String jsonStr = getResponseUsingHeaderPost("https://api.fullcontact.com/v2/cardReader?", "12904ce8c0e8192e", params);
        String jsonStr = "{\n" +
                "  \"count\": 20,\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"id\": \"1487684200483-b73598b2819a6db889e34911225c64968cc65475\",\n" +
                "      \"lastWebhookAttempt\": null,\n" +
                "      \"status\": \"PROCESSING\",\n" +
                "      \"webhookAttempts\": 0,\n" +
                "      \"webhookUrl\": \"http://jazzro.com\",\n" +
                "      \"quality\": \"LOW\",\n" +
                "      \"submitted\": \"2017-02-21T13:36:40.000Z\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"1487684125670-1891b06e2f03ff45fcb21f0426ccd98b354a04c0\",\n" +
                "      \"lastWebhookAttempt\": null,\n" +
                "      \"status\": \"PROCESSING\",\n" +
                "      \"webhookAttempts\": 0,\n" +
                "      \"webhookUrl\": \"http://jazzro.com\",\n" +
                "      \"quality\": \"LOW\",\n" +
                "      \"submitted\": \"2017-02-21T13:35:25.000Z\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"1487683934452-c383df3fb88491cbb17891805094d76c496d4e84\",\n" +
                "      \"lastWebhookAttempt\": \"2017-02-21T13:36:00.000Z\",\n" +
                "      \"vCardUrl\": \"https://d1h3f0foa0xzdz.cloudfront.net/2890560/3SR6AEG6W50KPABC9CEO6235JXVYHU.vcf\",\n" +
                "      \"status\": \"CALLBACK_MADE\",\n" +
                "      \"webhookAttempts\": 1,\n" +
                "      \"webhookUrl\": \"http://jazzro.com\",\n" +
                "      \"quality\": \"LOW\",\n" +
                "      \"submitted\": \"2017-02-21T13:32:14.000Z\",\n" +
                "      \"contact\": {\n" +
                "        \"photos\": [\n" +
                "          {\n" +
                "            \"primary\": true,\n" +
                "            \"value\": \"https://d1h3f0foa0xzdz.cloudfront.net/2890560/1487683934452-c383df3fb88491cbb17891805094d76c496d4e84-front.jpeg\",\n" +
                "            \"type\": \"BusinessCard\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"organizations\": [\n" +
                "          {\n" +
                "            \"title\": \"ADVOCATE\",\n" +
                "            \"isPrimary\": true,\n" +
                "            \"name\": null\n" +
                "          }\n" +
                "        ],\n" +
                "        \"name\": {\n" +
                "          \"middleName\": \"R\",\n" +
                "          \"honorificPrefix\": null,\n" +
                "          \"familyName\": \"RATHVI\",\n" +
                "          \"givenName\": \"MANOJ\",\n" +
                "          \"honorificSuffix\": null\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": 200,\n" +
                "  \"totalRecords\": 29,\n" +
                "  \"currentPage\": 0,\n" +
                "  \"totalPages\": 2\n" +
                "}";
        Log.e("Responce : ", "" + jsonStr);
        if (jsonStr != null) {
            try {
                JSONObject jsonObjMain = new JSONObject(jsonStr);

                JSONArray jsonArray = jsonObjMain.getJSONArray("results");
                ArrayList<CardScannerModel> arrayList = new ArrayList<>();
                Gson gson = new Gson();

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject c = jsonArray.getJSONObject(i);
                    CardScannerModel model = new CardScannerModel();

                    model.id = jsonArray.getJSONObject(i).getString("id");
                    model.lastWebhookAttempt = jsonArray.getJSONObject(i).getString("lastWebhookAttempt");
                    model.status = jsonArray.getJSONObject(i).getString("status");
                    model.webhookAttempts = jsonArray.getJSONObject(i).getString("webhookAttempts");
                    model.webhookUrl = jsonArray.getJSONObject(i).getString("webhookUrl");
                    model.quality = jsonArray.getJSONObject(i).getString("quality");
                    model.submitted = jsonArray.getJSONObject(i).getString("submitted");

                    if (jsonArray.getJSONObject(i).has("contact")) {
                        JSONObject obj = c.getJSONObject("contact");
                        if (obj.has("name")) {
                            JSONObject objName = obj.getJSONObject("name");
                            model.fname = objName.getString("familyName");
                            model.lastName = objName.getString("givenName");
                        } else {
                            // model.contact.name.familyName = "";
                            //  model.contact.name.givenName = "";
                        }
                    } else {
                        // model.contact = null;
                    }


                    arrayList.add(model);
                }

                p.error = false;
                p.o = arrayList;


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            p.error = true;
            p.o = "Can not get any data from the url";
        }


        return p;
    }


}
