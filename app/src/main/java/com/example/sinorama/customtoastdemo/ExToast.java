package com.example.sinorama.customtoastdemo;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by sinorama on 2017-05-26.
 */

public class ExToast {

    private Context mContext;
    private Toast mToast;
    private int mDurantion;
    private View mNextView;

    //Toast类对象中的属性名及方法名
    private Object mTN;
    private Method show;
    private Method hide;

    private Handler handler = new Handler();


    public static final int LENGTH_ALWAYS = 0;//总是显示
    public static final int LENGTH_SHORT = 2;//2秒显示
    public static final int LENGTH_LONG = 4;//4秒显示



    public ExToast(Context context, Toast toast) {
        mContext = context;
        mToast = toast;


    }

    private Runnable mHide = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private void hide() {

        try {
            hide.invoke(mTN);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static ExToast makeText(Context context, String message, int duration) {
        //初始化Toast对象
        Toast toast = Toast.makeText(context, message, duration);

        ExToast exToast = new ExToast(context, toast);
        exToast.mDurantion = duration;

        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.custom_toast, null);
        TextView tv = (TextView)v.findViewById(R.id.message);
        tv.setText(message);
        exToast.mNextView = v;
        return exToast;
    }

    public void show() {
        initTN();

        //调用原方法执行
        try {
            show.invoke(mTN);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        if(mDurantion > LENGTH_ALWAYS) {
            handler.postDelayed(mHide, mDurantion * 1000);
        }

    }

    private void initTN() {

        Field tnField = null;
        try {
            //***   利用反射，获取Toast中的mTN域对象  ****
            tnField = mToast.getClass().getDeclaredField("mTN");

            //设置mTN对象可用
            tnField.setAccessible(true);

            mTN = tnField.get(mToast);//获取属性

            //利用反射，获取show方法
            show = mTN.getClass().getMethod("show");
            hide = mTN.getClass().getMethod("hide");


            //修改show及hide方法的逻辑
            Field tnTextViewField = mTN.getClass().getDeclaredField("mNextView");
            tnTextViewField.setAccessible(true);

            //用mTN改变mNextView的逻辑
            //tnTextViewField.set(mTN, mToast.getView());//修改mTN对象
            tnTextViewField.set(mTN, mNextView);//修改mTN对象和view

        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
