// IUserManager.aidl
package com.zither.aiiage.aidlpractice;

import com.zither.aiiage.aidlpractice.UserBean;
import com.zither.aiiage.aidlpractice.INewUser;
// Declare any non-default types here with import statements

interface IUserManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
  List<UserBean> getUserList();
  void addUser(in UserBean user);
  void registerUserListener(INewUser inewuser);
  void unregisterUserListener(INewUser inewuser);
}
