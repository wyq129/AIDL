package com.zither.aiiage.aidlpractice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author wangyanqin
 * @date 2018/08/07
 */
public class UserManagerService extends Service {
    private static final String TAG = "UserManagerService";
    /**
     * RemoteCallbackList系统专门提供的用于删除跨进程listener
     */
    private RemoteCallbackList<INewUser> mINewUsers = new RemoteCallbackList<INewUser>();
    private CopyOnWriteArrayList<UserBean> mUserBeans = new CopyOnWriteArrayList<UserBean>();
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
    private IUserManager.Stub mBundle = new IUserManager.Stub() {
        @Override
        public List<UserBean> getUserList() throws RemoteException {
            if (mUserBeans != null) {
                SystemClock.sleep(5000);
                return mUserBeans;
            }
            return new ArrayList<>();
        }

        @Override
        public void addUser(UserBean user) throws RemoteException {
            if (mUserBeans == null) {
                mUserBeans = new CopyOnWriteArrayList<>();
            }
            if (user == null) {
                Log.d(TAG, "addUser: user is null");
                user = new UserBean();
            }
            if (!mUserBeans.contains(user)) {
                mUserBeans.add(user);
            }
            Log.d(TAG, "addUser: now list is " + mUserBeans.toString());
        }

        @Override
        public void registerUserListener(INewUser inewuser) throws RemoteException {
            mINewUsers.register(inewuser);
        }

        @Override
        public void unregisterUserListener(INewUser inewuser) throws RemoteException {
            mINewUsers.unregister(inewuser);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mUserBeans.add(new UserBean(1, "zither", "女", 18, true));
        mUserBeans.add(new UserBean(2, "寄生不忧伤", "女", 18, true));
        new Thread(new ServiceWorker()).start();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void onNewUserRegister(UserBean userBean) throws RemoteException {
        mUserBeans.add(userBean);
        final int N = mINewUsers.beginBroadcast();
        for (int i = 0; i <= N; i++) {
            INewUser iNewUser = mINewUsers.getBroadcastItem(i);
            if (iNewUser != null) {
                try {
                    iNewUser.onNewUserRegister(userBean);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mINewUsers.finishBroadcast();
    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            //do background processing here....
            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int userId = mUserBeans.size() + 1;
                UserBean userBean = new UserBean(userId, "newUserName" + userId, "newUserGender" + userId, 18 + userId, true);
                try {
                    onNewUserRegister(userBean);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

