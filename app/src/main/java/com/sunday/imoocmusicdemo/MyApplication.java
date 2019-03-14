package com.sunday.imoocmusicdemo;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.sunday.imoocmusicdemo.helps.RealmHelper;

import io.realm.Realm;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
        Realm.init(this);

        RealmHelper.migration();
    }
}
