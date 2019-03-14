package com.sunday.imoocmusicdemo.helps;

import android.content.Context;
import android.util.Log;

import com.sunday.imoocmusicdemo.migration.Migration;
import com.sunday.imoocmusicdemo.models.AlbumModel;
import com.sunday.imoocmusicdemo.models.MusicModel;
import com.sunday.imoocmusicdemo.models.MusicSourceModel;
import com.sunday.imoocmusicdemo.models.UserModel;
import com.sunday.imoocmusicdemo.utils.DataUtils;

import java.io.FileNotFoundException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RealmHelper {

    private Realm mRealm;

    public RealmHelper () {
        mRealm  = Realm.getDefaultInstance();
    }

    /**
     * Realm数据库发生结构性变化（模型或者模型中的字段发生了新增、修改、删除）
     * 的时候，我们就需要对数据库进行迁移。
     */

    /**
     * 告诉Realm数据需要迁移，并且为Realm设置最新的配置
     */
    public static void migration () {
        RealmConfiguration conf = getRealmConf();

//        Realm设置最新的配置
        Realm.setDefaultConfiguration(conf);
//        告诉Realm数据需要迁移
        try {
            Realm.migrateRealm(conf);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回 RealmConfiguration
     * @return
     */
    private static RealmConfiguration getRealmConf () {
        return new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new Migration())
                .build();
    }

    /**
     * 关闭数据库
     */
    public void close () {
        if (mRealm != null && !mRealm.isClosed()) {
            mRealm.close();
        }
    }

    /**
     * 保存用户信息
     */
    public void saveUser (final UserModel userModel) {
        /**
         *  手动同步事务
         */
        mRealm.beginTransaction();
        mRealm.insert(userModel);
//        mRealm.insertOrUpdate(userModel);
        mRealm.commitTransaction();

//        /**
//         * 同步事务块
//         */
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.copyToRealm(userModel);
//            }
//        });

//        /**
//         * 异步事务块
//         */
//        mRealm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.insert(userModel);
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Log.d("RealmHelper", "信息保存成功");
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                Log.e("RealmHelper", "信息保存失败");
//            }
//        });

    }

    /**
     * 返回所有用户
     */
    public List<UserModel> getAllUser () {
        RealmQuery<UserModel> query = mRealm.where(UserModel.class);
        RealmResults<UserModel> results = query.findAll();
        return results;
    }

    /**
     * 验证用户信息
     */
    public boolean validateUser (String phone, String password) {
        boolean result = false;
        RealmQuery<UserModel> query = mRealm.where(UserModel.class);
        query = query.equalTo("phone", phone)
                .equalTo("password", password);
        UserModel userModel = query.findFirst();

        if (userModel != null) {
            result = true;
        }

        return result;
    }

    /**
     * 获取当期用户
     */
    public UserModel getUser () {
        RealmQuery<UserModel> query = mRealm.where(UserModel.class);
        UserModel userModel = query.equalTo("phone", UserHelper.getInstance().getPhone()).findFirst();
        return userModel;
    }

    /**
     * 修改密码
     */
    public void changePassword (String password) {
        UserModel userModel = getUser();
        mRealm.beginTransaction();
        userModel.setPassword(password);
        mRealm.commitTransaction();
    }

    /**
     * 1、用户登录，存放数据
     * 2、用户退出，删除数据
     */

    /**
     * 保存音乐源数据
     */
    public void setMusicSource (Context context) {
//        拿到资源文件中的数据
        String musicSourceJson = DataUtils.getJsonFromAssets(context, "DataSource.json");
        mRealm.beginTransaction();
        mRealm.createObjectFromJson(MusicSourceModel.class, musicSourceJson);
        mRealm.commitTransaction();
    }

    /**
     * 删除音乐源数据
     * 1、RealmResult delete
     * 2、Realm delete 删除这个模型下所有的数据
     */
    public void removeMusicSource () {
        mRealm.beginTransaction();
        mRealm.delete(MusicSourceModel.class);
        mRealm.delete(MusicModel.class);
        mRealm.delete(AlbumModel.class);
        mRealm.commitTransaction();
    }

    /**
     * 返回音乐源数据
     */
    public MusicSourceModel getMusicSource () {
        return mRealm.where(MusicSourceModel.class).findFirst();
    }

    /**
     * 返回歌单
     */
    public AlbumModel getAlbum (String albumId) {
        return mRealm.where(AlbumModel.class).equalTo("albumId", albumId).findFirst();
    }

    /**
     * 返回音乐
     */
    public MusicModel getMusic (String musicId) {
        return mRealm.where(MusicModel.class).equalTo("musicId", musicId).findFirst();
    }
}
