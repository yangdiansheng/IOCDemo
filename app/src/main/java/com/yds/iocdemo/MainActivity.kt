package com.yds.iocdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.yds.iocdemo.java.*


@ContentView(R.layout.activity_main)
class MainActivity : AppCompatActivity() {

    @ViewInject(R.id.btn_b1)
    var btn1: Button? = null

    @ViewInject(R.id.btn_b2)
    var btn2: Button? = null

    @OnClick(R.id.btn_b1,R.id.btn_b2)
    fun onBtnClick(view:View){
        Log.d("yyy","点击我了")
    }

    @OnLongClick(R.id.btn_b2)
    fun onLongBtnClick(view:View):Boolean{
        Log.d("yyy","点击长按我了")
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InjectUtils.inject(this)
        btn1?.text = "按钮1"
        btn2?.text = "按钮2"
    }
}