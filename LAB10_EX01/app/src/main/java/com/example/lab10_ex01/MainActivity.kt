package com.example.lab10_ex01

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val databaseName = "people"
    var database: SQLiteDatabase? = null
    val tableName = "student"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        doButton1.setOnClickListener {
            createDatabase()
        }
        doButton2.setOnClickListener {
            createTable()
        }
        doButton3.setOnClickListener {
            addData()
        }
        doButton4.setOnClickListener {
            updateData()
        }
        doButton5.setOnClickListener {
            queryData()
        }
        doButton6.setOnClickListener {
            deleteData()
        }
    }
    fun createDatabase() {
        database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null)
        output1.append("데이터베이스 생성 또는 오픈 함\n")
    }

    fun checkDatabase() : Boolean {
        if (database == null) {
            output1.append("데이터베이스를 먼저 오픈하세요.\n")
            return true
        }
        return false
    }

    fun closeDatabase() {
        database?.close()
    }

    fun createTable() {
        if(checkDatabase()) return
        database?.execSQL("DROP Table ${tableName}")
        val sql = "create table if not exists ${tableName}" +
                "(_id integer PRIMARY KEY autoincrement, " +
                "name text, " +
                "age integer, " +
                "mobile text)"
        database?.execSQL(sql)
        output1.append("테이블 생성함\n")
    }

    fun addData() {
        if(checkDatabase()) return

        val sql = "insert into ${tableName}(name,age,mobile)" +
                "values" +
                "('john', '20', '010-0000-0000')"
        database?.execSQL(sql)
        output1.append("데이터 추가\n")
    }

    fun updateData() {
        if(checkDatabase()) return

        val values = ContentValues()
        values.put("name", "mike")
        values.put("age", "24")
        values.put("mobile", "010-4000-4000")
        val arr : Array<String> = arrayOf("John")
        database?.update(tableName, values, "name=?", arr)
        output1.append("데이터 갱신\n")
    }

    fun queryData() {
        if(checkDatabase()) return

        val sql = "select _id,name,age,mobile from ${tableName}"
        val cursor = database?.rawQuery(sql, null)
        if(cursor != null) {
            for(index in 0 until cursor.count) {
                cursor.moveToNext()
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val age = cursor.getInt(2)
                val mobile = cursor.getString(3)
                output1.append("레코드#${index} : " + "$id,$name,$age,$mobile\n ")
            }
            cursor.close()
        }
        output1.append("데이터 조회 결과\n")
    }

    fun deleteData() {
        if(checkDatabase()) return

        val sql = "select _id,name,age,mobile from ${tableName}"
        val cursor = database?.rawQuery(sql, null)
        if (cursor != null) {
            cursor.count
            val count = cursor.count
            cursor.close()

            val delete = "delete from ${tableName} where _id = ${count}"
            database?.execSQL(delete)
            output1.append("데이터 삭제\n")
        }
    }
}