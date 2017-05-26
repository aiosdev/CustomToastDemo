package com.example.sinorama.customtoastdemo

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.lang.reflect.Method

/**
 * Created by sinorama on 2017-05-26.
 */
class ExToastKt(context: Context) {

    var mContext: Context = context
    var mToast: Toast? = null
    var mDuration: Int = 0
    var mNextView: View? = null

    //Toast类对象中的属性名及方法名
    var mTN: Any? = null
    var show: Method? = null
    var hide: Method? = null

    companion object {//创建一个静态区来实现静态方法

        fun makeText(context: Context, message: String, duration: Int): ExToastKt {
            //初始化Toast对象
            val toast = Toast.makeText(context, message, duration)

            val exToastKt = ExToastKt(context)
            exToastKt.mToast = toast
            exToastKt.mDuration = duration

            val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val v = inflate.inflate(R.layout.custom_toast, null)
            val tv = v.findViewById(R.id.message) as TextView
            tv.text = message
            exToastKt.mNextView = v

            return exToastKt
        }

        val LENGTH_ALWAYS = 0//总是显示
        val LENGTH_SHORT = 2//2秒显示
        val LENGTH_LONG = 4//4秒显示

    }

    private val mHide = Runnable { hide() }
    private var handler: Handler = Handler()


    fun show() {
        initTN()

        //调用原方法执行
        show?.invoke(mTN)


        if (mDuration > LENGTH_ALWAYS) {
            //handler?.postDelayed(mHide, (mDuration * 1000).toLong())
            handler?.postDelayed(mHide, mDuration.toLong())
        }

    }


    private fun hide() = hide?.invoke(mTN)



    private fun initTN() {

            //***   利用反射，获取Toast中的mTN域对象  ****
            var tnField = mToast ?. javaClass ?. getDeclaredField ("mTN")

            //设置mTN对象可用
            tnField?.isAccessible = true

            mTN = tnField?.get(mToast)//获取属性

            //利用反射，获取show方法
            show = mTN?.javaClass?.getMethod("show")
            hide = mTN?.javaClass?.getMethod("hide")


            //修改show及hide方法的逻辑
            val tnTextViewField = mTN?.javaClass?.getDeclaredField("mNextView")
            tnTextViewField?.isAccessible = true

            //用mTN改变mNextView的逻辑
            //tnTextViewField?.set(mTN, mToast?.getView());//修改mTN对象
            tnTextViewField?.set(mTN, mNextView)//修改mTN对象和view

    }



}