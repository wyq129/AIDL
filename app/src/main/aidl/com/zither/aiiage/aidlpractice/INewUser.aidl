// INewUser.aidl
package com.zither.aiiage.aidlpractice;

import com.zither.aiiage.aidlpractice.UserBean;
// Declare any non-default types here with import statements

interface INewUser {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNewUserRegister(in UserBean newuser);
}
