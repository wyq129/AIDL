package com.zither.aiiage.aidlpractice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author wangyanqin
 * @date 2018/08/07
 */
public class UserBean implements Parcelable {
    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
    private int uid;
    private String name;
    private String gender;
    private int age;
    private boolean isChinese;

    public UserBean(int uid, String name, String gender, int age, boolean isChinese) {
        this.uid = uid;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.isChinese = isChinese;
    }

    public UserBean() {

    }

    protected UserBean(Parcel in) {
        uid = in.readInt();
        name = in.readString();
        gender = in.readString();
        age = in.readInt();
        isChinese = in.readByte() != 0;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", isChinese=" + isChinese +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isChinese() {
        return isChinese;
    }

    public void setChinese(boolean chinese) {
        isChinese = chinese;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(uid);
        parcel.writeString(name);
        parcel.writeString(gender);
        parcel.writeInt(age);
        parcel.writeByte((byte) (isChinese ? 1 : 0));
    }
}
