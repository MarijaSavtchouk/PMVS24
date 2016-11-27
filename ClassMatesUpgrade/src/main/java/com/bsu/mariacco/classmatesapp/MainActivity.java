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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    final int DB_VERSION = 2;
    DBHelper dbHelper;
    private String dbName = "dbName";
    private String tableName = "classmate";
    private String patronymic = "patronymic";
    private String lastname = "lastname";
    private String firstname = "firstname";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.close();
    }

    @Override
    public void onClick(View v) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.button_add:{
                Timestamp date = new Timestamp(new java.util.Date().getTime());
                String name = "Marija Savtchouk";
                if(DB_VERSION == 1) {
                    cv.put("name", name);
                }
                else if(DB_VERSION == 2){
                    String []nameParts = name.split(" ");
                    cv.put(firstname, nameParts[0]);
                    cv.put(patronymic, "");
                    cv.put(lastname, nameParts[1]);
                }
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
                String name = "Иван Иванович Иванов";
                if(DB_VERSION==1) {
                    cv.put("name", name);
                }else if(DB_VERSION==2){
                    String []nameParts = name.split(" ");
                    cv.put(firstname, nameParts[0]);
                    cv.put(patronymic, nameParts[1]);
                    cv.put(lastname, nameParts[2]);
                }
                db.update(tableName, cv, "id="+id, null);
                break;
            }
            case R.id.button_show: {
                ArrayList<String> values = new ArrayList<>();
                    Cursor c = db.query(tableName, null, null, null, null, null, null);
                    if (c.moveToFirst()) {
                        if(DB_VERSION==1) {
                            int idColIndex = c.getColumnIndex("id");
                            int nameColIndex = c.getColumnIndex("name");
                            int dateColIndex = c.getColumnIndex("addTime");
                            do {
                                values.add("ID = " + c.getInt(idColIndex) +
                                        ", name = " + c.getString(nameColIndex) +
                                        ", add date = " + c.getString(dateColIndex));
                            } while (c.moveToNext());
                        }
                        else if(DB_VERSION==2) {
                            int idColIndex = c.getColumnIndex("id");
                            int firtsNameColIndex = c.getColumnIndex(firstname);
                            int lastNameColIndex = c.getColumnIndex(lastname);
                            int patronymicCloumnIndex = c.getColumnIndex(patronymic);
                            int dateColIndex = c.getColumnIndex("addTime");
                            do {
                                values.add("ID = " + c.getInt(idColIndex) +
                                        ", firstname = " + c.getString(firtsNameColIndex)+
                                        ", patronymic = " + c.getString(patronymicCloumnIndex)+
                                        ", lastname = " + c.getString(lastNameColIndex)+
                                        ", add date = " + c.getString(dateColIndex));
                            } while (c.moveToNext());
                        }
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
        Context context;
        public DBHelper(Context context) {
            super(context, dbName, null, DB_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            if(DB_VERSION==1) {
                db.execSQL("create table classmate (id integer primary key autoincrement, name text, addTime datetime);");
                db.execSQL("insert into classmate (name, addTime) VALUES ('Ivan Petrov', datetime('now'))");
                db.execSQL("insert into classmate (name, addTime) VALUES ('Maria Ivanova', datetime('now'))");
                db.execSQL("insert into classmate (name, addTime) VALUES ('Petr Vasiliev', datetime('now'))");
                db.execSQL("insert into classmate (name, addTime) VALUES ('Alex Smirnov', datetime('now'))");
                db.execSQL("insert into classmate (name, addTime) VALUES ('Olga Slonova', datetime('now'))");
            }
            else if (DB_VERSION==2){
                db.execSQL("create table classmate (id integer primary key autoincrement, firstname text, patronymic text, lastname text, addTime datetime);");
                db.execSQL("insert into classmate (firstname, patronymic, lastname, addTime) VALUES ('Ivan',' Ivanovich', 'Petrov', datetime('now'))");
                db.execSQL("insert into classmate (firstname, patronymic, lastname, addTime) VALUES ('Maria', '', 'Ivanova', datetime('now'))");
                db.execSQL("insert into classmate (firstname, patronymic, lastname, addTime) VALUES ('Petr', 'Vasilievich', 'Vasiliev', datetime('now'))");
                db.execSQL("insert into classmate (firstname, patronymic, lastname, addTime) VALUES ('Alex', '', 'Smirnov', datetime('now'))");
                db.execSQL("insert into classmate (firstname, patronymic, lastname, addTime) VALUES ('Olga', 'Iosifivna', 'Slonova', datetime('now'))");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int
                newVersion) {
            if (oldVersion == 1 && newVersion > 1) {

                db.beginTransaction();
                try {

                    db.execSQL("alter table classmate add column lastname text;");
                    db.execSQL("alter table classmate add column firstname text;");
                    db.execSQL("alter table classmate add column patronymic name;");
                    final Cursor cursor = db.query(tableName, null, null, null, null, null, null);
                    if(cursor != null) {
                        if (cursor.moveToFirst()) {
                            final ContentValues values = new ContentValues();
                            int idColIndex = cursor.getColumnIndex("id");
                            int nameColIndex = cursor.getColumnIndex("name");
                            do {
                                int oldId = cursor.getInt(idColIndex);
                                String name = cursor.getString(nameColIndex);
                                String nameParts[] = name.split(" ");
                                if(nameParts.length>0) {
                                    values.clear();

                                    values.put(firstname, nameParts[0]);
                                    //Toast.makeText(context, nameParts.toString(), Toast.LENGTH_SHORT).show();

                                    values.put(patronymic, nameParts.length>2?nameParts[1]:"");
                                    //Toast.makeText(context, "Path"+(nameParts.length>2?nameParts[1]:""), Toast.LENGTH_SHORT).show();
                                    if(nameParts.length>1) {
                                        if (nameParts.length == 2) {
                                            values.put(lastname, nameParts[1]);
                                        } else {
                                            String newlastName = nameParts[2];
                                            for (int k = 3; k< nameParts.length; k++){
                                                newlastName+= " "+nameParts[k];
                                            }
                                            values.put(lastname, newlastName);
                                        }
                                    }
                                    db.update(tableName, values, "id" + "=?", new String[]{Integer.toString(oldId)});
                                }
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                    }

                    db.execSQL("create temporary table classmate_tmp (id integer, firstname text, patronymic text, lastname text, addTime datetime);");
                    db.execSQL("insert into classmate_tmp select id, firstname, patronymic, lastname, addTime from classmate;");
                    db.execSQL("drop table classmate;");

                    db.execSQL("create table classmate (id integer primary key autoincrement, firstname text, patronymic text, lastname text, addTime datetime);");

                    db.execSQL("insert into classmate select id, firstname, patronymic, lastname, addTime from classmate_tmp;");
                    db.execSQL("drop table classmate_tmp;");

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
        }
    }
}
