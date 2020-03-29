package ru.fedurovkostya.todolist.Other


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.concurrent.atomic.AtomicInteger
// Класс работы с базой данных и его методы.
class DBHelper(val context: Context): SQLiteOpenHelper(context,
    DB_NAME,null,
    DB_VERSION
){
    // Функция, которая создает базу данных.
    override fun onCreate(db: SQLiteDatabase) {
        val createTaskTable = "CREATE TABLE $TABLE_TASK (" +
                "$COL_ID integer PRIMARY KEY AUTOINCREMENT," +
                "$COL_DESCRIPTION varchar," +
                "$COL_NAME varchar,"+
                "$COL_COLOR integer,"+
                "$COL_THEME integer);"
        db.execSQL(createTaskTable)
        Log.d("Tag","createTaskTable")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
    //Функция, которая добавляет задачу в список дел.
    fun addTask(task: Task):Boolean{
        Log.d("Tag","addTask")
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME,task.name)
        cv.put(COL_DESCRIPTION,task.description)
        cv.put(COL_COLOR,task.color)
        val result = db.insert(TABLE_TASK,null,cv)
        return result != (-1).toLong()
    }
    //Функция, которая обнавляет задачу.
    fun updateTask(task: Task){
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME,task.name)
        cv.put(COL_DESCRIPTION,task.description)
        cv.put(COL_COLOR,task.color)
        db.update(TABLE_TASK,cv,"$COL_ID=?", arrayOf(task.id.toString()))
    }
    //Функция, которая удаляет задачу.
    fun deleteTask(taskId:Long){
        Log.d("Tag","deleteTask")
        val db = writableDatabase
        db.delete(TABLE_TASK,"$COL_ID=?", arrayOf(taskId.toString()))
    }
    // Функция, которая получает задачу.
    fun getTasks():MutableList<Task>{
        Log.d("Tag","getTasks")
        var result:MutableList<Task> = ArrayList()
        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * from $TABLE_TASK",null)
        if(queryResult.moveToFirst()){
            do{
                val task = Task()
                task.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                task.name = queryResult.getString(queryResult.getColumnIndex(COL_NAME))
                task.description = queryResult.getString(queryResult.getColumnIndex(COL_DESCRIPTION))
                task.color = queryResult.getInt(queryResult.getColumnIndex(COL_COLOR))
                result.add(task)
            }while (queryResult.moveToNext())
        }
        queryResult.close()
        return  result
    }
}