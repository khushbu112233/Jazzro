package com.jlouistechnology.Jazzro.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aipxperts on 3/2/17.
 */
public class GroupListDataDetailModel implements Parcelable{

    public String id;
    public String label;
    public String color;
    public String number_of_contacts;
    public String isClick = "false";

    public GroupListDataDetailModel(Parcel in) {
        id = in.readString();
        label = in.readString();
        color = in.readString();
        isClick = in.readString();
        number_of_contacts = in.readString();
    }

    public GroupListDataDetailModel() {

    }


    public static final Creator<GroupListDataDetailModel> CREATOR = new Creator<GroupListDataDetailModel>() {
        @Override
        public GroupListDataDetailModel createFromParcel(Parcel in) {
            return new GroupListDataDetailModel(in);
        }

        @Override
        public GroupListDataDetailModel[] newArray(int size) {
            return new GroupListDataDetailModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(label);
        dest.writeString(color);
        dest.writeString(isClick);
        dest.writeString(number_of_contacts);
    }
}
