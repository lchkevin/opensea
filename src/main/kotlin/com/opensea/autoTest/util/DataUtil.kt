package com.opensea.autoTest.util

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDateTime

// 模拟保存数据
object DataUtil {

    private val cache = hashMapOf<String, Item>()

    lateinit var connection: Connection

    fun init(url: String) {
        Class.forName("org.sqlite.JDBC")
        connection = DriverManager.getConnection("jdbc:sqlite:$url")
        connection.autoCommit = true
    }

    fun initSQL(url: String) {
        init(url)

        try {
            val statement = connection.createStatement()
            val sql = "create table `item` (item_id Integer PRIMARY KEY autoincrement , item_url TEXT, status_Click Boolean, status_queued Boolean, status_error Boolean, create_time datetime);"
            val row = statement.executeUpdate(sql)
            println(row)
        } catch (e: Exception) {
        }
    }

    fun insert(item: Item) {

        val sql = "insert into `item` (item_id, item_url, status_Click, status_queued, status_error, create_time) values (?,?,?,?,?,?)"
        val ps = connection.prepareStatement(sql)

        ps.setInt(1, item.item_id)
        ps.setString(2, item.item_url)
        ps.setBoolean(3, item.status_click)
        ps.setBoolean(4, item.status_queued)
        ps.setBoolean(5, item.status_error)
        ps.execute()
    }


    fun mapper(rs: ResultSet): List<Item> {

        val list = arrayListOf<Item>()

        while (rs.next()) {
            val itemID = rs.getInt("item_id")
            val itemURL = rs.getString("item_url")
            val statusClick = rs.getBoolean("status_Click")
            val statusQueued = rs.getBoolean("status_queued")
            val statusError = rs.getBoolean("status_error")
            val createdTime = rs.getTimestamp("created_time").toLocalDateTime()

            val item = Item(item_id = itemID, item_url = itemURL, status_click = statusClick, status_queued = statusQueued, status_error = statusError, createdTime = createdTime)

            list.add(item)
        }

        return list
    }

    fun get(url: String): Item {

        val ps = connection.createStatement()

        val sql = "select * from `item` where item_url = '$url'"
        val rs = ps.executeQuery(sql)

        val item = this.mapper(rs = rs)
        return item.first()
    }

    fun getSum() : Int {
        val ps = connection.createStatement()
        val sql = "select * from `item` where 1 = 1"
        val rs = ps.executeQuery(sql)

        val item = this.mapper(rs = rs)
        return item.size
    }

    fun getById(item_id: Int) : Item {
        val ps = connection.createStatement()
        val sql = "select * from `item` where item_id = '$item_id'"
        val rs = ps.executeQuery(sql)

        val item = this.mapper(rs = rs)
        return item.first()
    }





    fun update(itemID: Int, status_click: Boolean = false, status_queued: Boolean = false, status_error: Boolean = false) {
        val sql = "update `item` set `status_click` = '$status_click', `status_queued` = '$status_queued', `status_error` = '$status_error' where id = '$itemID'"
        connection.createStatement().executeUpdate(sql)
    }

    fun close(ps: Statement) {

        if (!ps.isCloseOnCompletion) {
            ps.closeOnCompletion()
        }

    }

}

data class Item(

        //编号
        val item_id: Int,

        //item url 地址
        val item_url: String,

        //click状态
        val status_click: Boolean,

        //queued状态
        val status_queued: Boolean,

        //error状态
        val status_error: Boolean,

        val createdTime: LocalDateTime = LocalDateTime.now()



)