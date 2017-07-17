package com.jlouistechnology.Jazzro.network;

import com.jlouistechnology.Jazzro.Model.ForgotpasswordModel;
import com.jlouistechnology.Jazzro.Model.GroupListServiceModel;
import com.jlouistechnology.Jazzro.Model.PeticularGroupListServiceModel;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by aipxperts on 3/2/17.
 */
public interface ApiInterface {

    @GET("group")
    Call<GroupListServiceModel> groupList(@Header("Authorization") String Authorization,
                                          @Query("page") String page,
                                          @Query("perpage") String perpage,
                                          @Query("sortfield") String sortfield,
                                          @Query("sortdir") String sortdir);


   // https://dev.jazzro.com/api/v1/group/101014/?withContacts=1&page=1&perpage=10&sortdir=asc

    @GET("group/{id}/?withContacts={withContacts}&page={page}&perpage={perpage}&sortdir={sortdir}")
    Call<PeticularGroupListServiceModel> peticularGroup(@Path("id") String id,
                                                        @Header("Authorization") String Authorization,
                                                        @Query("withContacts") String withContacts1,
                                                        @Query("page") String page1,
                                                        @Query("perpage") String perpage1,
                                                        @Query("sortdir") String sortdir1);

    @Headers("Content-Type: application/json")
    @POST("user/forgot-password")
    Call<ForgotpasswordModel> forgotPassword(
            @Body String body);


}
