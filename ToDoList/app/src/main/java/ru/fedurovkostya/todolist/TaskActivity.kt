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

class TaskActivity : AppCompatActivity() {

    lateinit var dbHelper: DBHelper
    lateinit var context:Context

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        dbHelper = DBHelper(this)
        context = this
        // Менеджер расскладки
        rv_task.layoutManager = LinearLayoutManager(this)
            // пользовательский диалог для добавления задачи
        fab_task.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.activity_dialog_edit,null)
            var et_name = view.findViewById<EditText>(R.id.et_dialog_name)
            var et_description = view.findViewById<EditText>(R.id.et_dialog_description)
            var cb_priority = view.findViewById<CheckBox>(R.id.cb_dialog_priority)
            var iv_neg = view.findViewById<ImageView>(R.id.iv_dialog_neg)
            var tv_pos = view.findViewById<TextView>(R.id.tv_dialog_pos)
            iv_neg.setOnClickListener {
                dialog.dismiss()
            }
            tv_pos.setOnClickListener {
                var task = Task()
                task.name = et_name.text.toString()
                task.description = et_description.text.toString()
                // Проверка заносимого текста
                if(cb_priority.isChecked){
                    task.color = this.resources.getColor(R.color.colorAccent)
                } else{
                    task.color = this.resources.getColor(R.color.textColorPrimary)
                }
                dbHelper.addTask(task)
                refreshList()
                dialog.dismiss()
            }
            // Вызов конпки отмены
            dialog.setContentView(view)
            // Вызов метода show
            dialog.show()
        }
    }
// При добавлении нового элемента вызывается этот метод
    fun refreshList(){
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rv_task)
        rv_task.adapter = TaskAdapter(this,dbHelper.getTasks())
        hideFab()
    }
    // Скрывает кнопку, если больше пяти записей
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
// При открытии приложения вызывается этот метод
    override fun onResume() {
        refreshList()
        super.onResume()
    }
    // Возможно свайпа вправо или влево
    val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }
        // Удаление записи свайпом
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var task = dbHelper.getTasks()[viewHolder.adapterPosition]
            dbHelper.deleteTask(task.id)
            refreshList()
        }
    }
}