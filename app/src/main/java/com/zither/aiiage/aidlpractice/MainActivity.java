package com.zither.aiiage.aidlpractice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * @author wangyanqin
 * @date 2018/08/07
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final int MESSAGE_NEW_USER_REGISTER = 1;
    private IUserManager mIUserManager;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_USER_REGISTER:
                    Log.d(TAG, "handleMessage: have new user" + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };
    private INewUser mINewUser = new INewUser.Stub() {
        @Override
        public void onNewUserRegister(UserBean newuser) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_USER_REGISTER, newuser).sendToTarget();
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IUserManager manager = IUserManager.Stub.asInterface(iBinder);
            try {
                mIUserManager = manager;
                List<UserBean> list = manager.getUserList();
                Log.d(TAG, "onServiceConnected: " + list.getClass().getCanonicalName());
                Log.d(TAG, "onServiceConnected_list: " + list.toString());
                UserBean userBean = new UserBean(3, "金刚小琴琴", "女", 18, true);
                manager.addUser(userBean);
                List<UserBean> newlist = manager.getUserList();
                Log.d(TAG, "onServiceConnected_newlist: " + newlist.get(2).getName());
                manager.registerUserListener(mINewUser);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected: " + componentName);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, UserManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if (mIUserManager != null && mIUserManager.asBinder().isBinderAlive()) {
            try {
                mIUserManager.unregisterUserListener(mINewUser);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }

}

