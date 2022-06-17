package com.example.mysnsaccount.todo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "todo.db";

    //생성자
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터베이스 생성 시 호출
        db.execSQL("CREATE TABLE IF NOT EXISTS ToDoList (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, content TEXT NOT NULL, writeDate TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //select문
    public ArrayList<ToDoItem> getToDoList() {
        ArrayList<ToDoItem> toDoItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM ToDoList ORDER BY writeDate DESC", null);
        if (cursor.getCount() != 0) { // 데이터가 있으면
            while (cursor.moveToNext()) { // 다음 데이터가 없을 때 까지
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));

                ToDoItem toDoItem = new ToDoItem();
                toDoItem.setId(id);
                toDoItem.setTitle(title);
                toDoItem.setContent(content);
                toDoItem.setWriteDate(writeDate);
                toDoItems.add(toDoItem);
            }
        }
        cursor.close();
        return toDoItems;
    }

    //insert(삽입)
    public void InsertToDo(String _title, String _content, String _writeDate) {
        SQLiteDatabase db = getWritableDatabase(); // 쓰기 가능
        db.execSQL("INSERT INTO ToDoList (title, content, writeDate) VALUES('" + _title + "','" + _content + "','" + _writeDate + "');");

    }

    //update(수정)
    public void UpdateToDo(int _id, String _title, String _content, String _writeDate) {
        SQLiteDatabase db = getWritableDatabase(); // 쓰기 가능
        db.execSQL("UPDATE ToDoList SET title ='" + _title + "', content = '" + _content + "', writeDate = '" + _writeDate + "' WHERE id = '" + _id + "'");

    }

    //delete(삭제)
    public void DeleteToDo(int _id) {
        SQLiteDatabase db = getWritableDatabase(); // 쓰기 가능
        db.execSQL("DELETE FROM ToDoList WHERE id = '" + _id + "'"); // 아이디만 지우면 됨

    }
}
