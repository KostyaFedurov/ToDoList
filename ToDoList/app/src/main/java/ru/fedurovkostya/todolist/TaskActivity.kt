package ru.fedurovkostya.todolist


import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_task.*
import ru.fedurovkostya.todolist.Other.DBHelper
import ru.fedurovkostya.todolist.Other.*
import java.net.ContentHandler
//главный класс, разметка которого появляется после заставки и находится постоянно.
class TaskActivity : AppCompatActivity() {

    lateinit var dbHelper: DBHelper
    lateinit var context:Context

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        dbHelper = DBHelper(this)
        context = this
        rv_task.layoutManager = LinearLayoutManager(this)
        //код ниже срабатывает при нажатии на кнопку, находящуюся внизу, справа
        fab_task.setOnClickListener({
            //открывается окно снизу, как и при редоктировании
            val dialog = BottomSheetDialog(this)
            //объявление и инициализация объектов разметки
            val view = layoutInflater.inflate(R.layout.activity_dialog_edit,null)
            var et_name = view.findViewById<EditText>(R.id.et_dialog_name)
            var et_description = view.findViewById<EditText>(R.id.et_dialog_description)
            var cb_priority = view.findViewById<CheckBox>(R.id.cb_dialog_priority)
            var iv_neg = view.findViewById<ImageView>(R.id.iv_dialog_neg)
            var tv_pos = view.findViewById<TextView>(R.id.tv_dialog_pos)
            //при нажатие на крестик окно закроется, также оно закроется и при нажатие на экран
            iv_neg.setOnClickListener {
                dialog.dismiss()
            }
            //сохранение в бд данных, при нажатии на надпись сохранить
            tv_pos.setOnClickListener {
                var task = Task()
                task.name = et_name.text.toString()
                task.description = et_description.text.toString()
                if(cb_priority.isChecked){
                    task.color = this.resources.getColor(R.color.colorAccent)
                }
                else{
                    task.color = this.resources.getColor(R.color.textColorPrimary)
                }
                dbHelper.addTask(task)
                refreshList()
                dialog.dismiss()
            }
            dialog.setContentView(view)
            dialog.show()
        })
    }
    //эта функция служит для корректного отображении данных в списке
    //и срабатывает при их изменении
    fun refreshList(){
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rv_task)
        rv_task.adapter = TaskAdapter(this,dbHelper.getTasks())
        hideFab()
    }
    //одна из идей проекта эта мимнималистичный туду лист, поэтому
    //при количестве задач больше 5 кнопка добавить задачу скрывается, тем
    //самым не позволяя пользователю добавлять новые задачи, распыляя свое время
    fun hideFab(){
        if(dbHelper.getTasks().size == 0){
            iv_main.visibility = View.VISIBLE
            iv_main.setImageResource(R.drawable.ic_kotyara)
        }
        else{
            iv_main.visibility = View.GONE
        }
        if(dbHelper.getTasks().size > 5){
            fab_task.hide()
        }else if(fab_task.visibility != View.VISIBLE){
            fab_task.show()
        }
    }

    override fun onResume() {
        refreshList()
        super.onResume()
    }
    //код ниже позволяет удалять задачи свайпом в бок, удаляя их
    val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var task = dbHelper.getTasks()[viewHolder.adapterPosition]
            dbHelper.deleteTask(task.id)
            refreshList()
        }
    }
}