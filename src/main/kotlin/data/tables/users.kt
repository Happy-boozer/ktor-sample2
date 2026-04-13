package com.example.data.tables

import org.jetbrains.exposed.v1.core.Table

object Users: Table(){
    val id = integer("user_id")
    val phone_number = varchar("phone_number", 12)
    var password = varchar("password", 100)
    val name = varchar("name", 100)

    override val primaryKey = PrimaryKey(id)

}