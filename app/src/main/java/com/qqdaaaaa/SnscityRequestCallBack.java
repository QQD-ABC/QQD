package com.qqdaaaaa;

import android.content.Context;

import com.lidroid.xutils.exception.HttpException;

/**
 * Created by Administrator on 2016/1/12.
 */
public abstract class SnscityRequestCallBack<T> {
    public abstract void onFailure(HttpException paramHttpException, String paramString);

    public abstract void onGetErrorCode(Context paramContext, T paramT);

    public abstract void onSuccess(T paramT);
}