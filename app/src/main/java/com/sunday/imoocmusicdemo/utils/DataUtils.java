package com.sunday.imoocmusicdemo.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataUtils {

    /**
     * 读取资源文件中的数据
     * @return
     */
    public static String getJsonFromAssets (Context context, String fileName) {

        /**
         * 1、StringBuilder 存放读取出的数据
         * 2、AssetManager 资源管理器，Open 方法 打开指定的资源文件，返回InputStream 输入流
         * 3、InputStreamReader（字节到字符的桥接器），BufferedReader（存放读取字符的缓冲区）
         * 4、循环利用 BufferedReader 的 readLine 方法读取每一行的数据，
         *      并且把读取出来的数据放入到 StringBuilder 里面。
         * 5、返回读取出来的所有数据。
         */

//        StringBuilder 存放读取出的数据
        StringBuilder stringBuilder = new StringBuilder();
//        AssetManager 资源管理器
        AssetManager assetManager = context.getAssets();
//        Open 方法 打开指定的资源文件，返回InputStream 输入流
        try {
            InputStream inputStream = assetManager.open(fileName);
//            InputStreamReader（字节到字符的桥接器），BufferedReader（存放读取字符的缓冲区）
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            循环利用 BufferedReader 的 readLine 方法读取每一行的数据
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();

    }

}
