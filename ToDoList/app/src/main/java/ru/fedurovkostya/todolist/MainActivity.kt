package ru.fedurovkostya.todolist

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
// Класс заставки.
class MainActivity : AppCompatActivity() {
    /* После запуска приложения появляется заставка.
    Через 1000 миллисекунд запускается главное окно activity_task
    со списком задач. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(android.os.Build.VERSION.SDK_INT >= 21){
            window.statusBarColor = this.resources.getColor(R.color.colorAccent)
        }
        Handler().postDelayed({
            startActivity(Intent(this,TaskActivity::class.java))
            finish()
        },1000)
    }
}
