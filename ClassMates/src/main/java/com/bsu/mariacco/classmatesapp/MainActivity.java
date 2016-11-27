package com.bsu.mariacco.classmatesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DBHelper dbHelper;
    private String dbName = "dbName";
    private String tableName = "classmate";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(tableName, null, null);
        db.execSQL("insert into classmate (name, addTime) VALUES ('Ivan Petrov', datetime('now'))");
        db.execSQL("insert into classmate (name, addTime) VALUES ('Maria Ivanova', datetime('now'))");
        db.execSQL("insert into classmate (name, addTime) VALUES ('Petr Vasiliev', datetime('now'))");
        db.execSQL("insert into classmate (name, addTime) VALUES ('Alex Smirnov', datetime('now'))");
        db.execSQL("insert into classmate (name, addTime) VALUES ('Olga Slonova', datetime('now'))");
        dbHelper.close();
    }

    @Override
    public void onClick(View v) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.button_add:{
                String name = "Marija Savtchouk";
                Date date = new Date(new java.util.Date().getTime());
                cv.put("name", name);
                cv.put("addTime", date.toString());
                long rowID = db.insert(tableName, null, cv);
                Toast toast = Toast.makeText(getApplicationContext(),
                        Long.toString(rowID), Toast.LENGTH_SHORT);
                toast.show();
                break;
            }
            case R.id.button_change:{
                String selectQuery= "SELECT * FROM " + tableName+" ORDER BY id DESC LIMIT 1";
                Cursor cursor = db.rawQuery(selectQuery, null);
                String id = "";
                if(cursor.moveToFirst())
                    id  =  cursor.getString( cursor.getColumnIndex("id") );
                cursor.close();
                cv.put("name","Иван Иванович Иванов");
                db.update(tableName, cv, "id="+id, null);
                break;
            }
            case R.id.button_show: {
                ArrayList<String> values = new ArrayList<>();
                    Cursor c = db.query(tableName, null, null, null, null, null, null);
                    if (c.moveToFirst()) {

                        int idColIndex = c.getColumnIndex("id");
                        int nameColIndex = c.getColumnIndex("name");
                        int dateColIndex = c.getColumnIndex("addTime");
                        do{
                            values.add("ID = " + c.getInt(idColIndex) +
                                            ", name = " + c.getString(nameColIndex) +
                                            ", add date = " + c.getString(dateColIndex));
                        } while (c.moveToNext());
                    }
                c.close();
                Intent intent = new Intent(this, ShowActivity.class);
                intent.putExtra("values", values);
                startActivity(intent);
                break;
            }
        }
        dbHelper.close();
    }

    class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, dbName, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table classmate (id integer primary key autoincrement, name text, addTime datetime);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int
                newVersion) {
        }
    }
}
