package ru.fedurovkostya.todolist

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
//данный класс отвечает за заставку
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //меняем цвет statusBar на акцентный цвет, для того
        //чтобы сморелось лучше)
        if(android.os.Build.VERSION.SDK_INT >= 21){
            window.statusBarColor = this.resources.getColor(R.color.colorAccent)
        }
        //заставка появляется на 1 секунда, а затем запускоется другое окно
        Handler().postDelayed({
            startActivity(Intent(this,TaskActivity::class.java))
            finish()
        },1000)
    }
}
