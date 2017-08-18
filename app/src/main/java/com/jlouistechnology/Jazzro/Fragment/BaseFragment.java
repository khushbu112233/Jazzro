package com.jlouistechnology.Jazzro.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jlouistechnology.Jazzro.R;

/**
 * Created by aipxperts on 3/2/17.
 */
public class BaseFragment extends Fragment {

    public ProgressDialog mProgressDialog;
    public Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof Activity) {
            mContext = activity;
        }
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setContentView(R.layout.progressdialog);
    }
    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
