package com.wgz.ant.myappframework.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qwerr on 2015/11/27.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //类没有实例化,是不能用作父类构造器的参数,必须声明为静态

    private static final String Name = "count1"; //数据库名称

    private static final int Version = 1; //数据库版本
    private static final String FENZU_NAME = "fenzu";//分组名称表
    private static final String CONTENT = "content";//联系人列表

    public DatabaseHelper(Context context) {
        super(context, Name, null, Version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
		/*String sql = "CREATE TABLE " + CHANGYONG + " (" + PERSON_ID
				+ " INTEGER primary key autoincrement, " + PERSON_name + " text, "+ PERSON_phone +" text);";
		db.execSQL(sql);
		String sql2="CREATE TABLE " + NEARLY + " (" + PERSON_ID
				+ " INTEGER primary key autoincrement, " + PERSON_name + " text, "+ PERSON_phone +" text);";
		db.execSQL(sql2);
		String sql3="CREATE TABLE " + MEMORY + " (" + PERSON_ID
				+ " INTEGER primary key autoincrement, " + PERSON_name + " text, "+ PERSON_phone +" text);";
		db.execSQL(sql3);*/
//        String sql3="CREATE TABLE " + "Test" + " (" + PERSON_ID
//                + " INTEGER primary key autoincrement, " + PERSON_name + " text, "+ PERSON_phone +" text);";
//        db.execSQL(sql3);
        db.execSQL("CREATE TABLE IF NOT EXISTS fenzu (id integer primary key autoincrement, pid varchar(60),gname varchar(60))");
        db.execSQL("CREATE TABLE content1 (id integer primary key autoincrement, pid varchar(60),name varchar(60), phone varchar(60),rank varchar(60),date varchar(60))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
