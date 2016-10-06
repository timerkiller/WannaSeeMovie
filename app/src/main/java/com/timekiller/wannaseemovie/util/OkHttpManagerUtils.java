package com.timekiller.wannaseemovie.util;

import android.graphics.Bitmap;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by vke on 2016/8/24.
 */
public class OkHttpManagerUtils {
    private OkHttpClient mClient;
    private volatile static OkHttpManagerUtils manager;

    private static final MediaType TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    private static final MediaType TYPE_STRING = MediaType.parse("text/x-markdown;charset=utf-8");

    public static OkHttpManagerUtils getInstance(){
        if(manager == null){
            synchronized (OkHttpManagerUtils.class){
                manager = new OkHttpManagerUtils();
            }
        }

        return manager;
    }



    private OkHttpManagerUtils(){
        mClient = new OkHttpClient();
    }


    interface CallBackFunc{
        void onStringResponse(String result);
        void onByteReponse(byte[] result);
        void onBitmapResponse(Bitmap bitmap);
        void onJsonObjectResponse(JSONObject object);
    }
}
