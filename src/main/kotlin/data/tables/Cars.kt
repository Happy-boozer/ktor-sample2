package com.example.data.tables

import org.jetbrains.exposed.v1.core.Table

object Cars: Table(){
    val id = integer("car_id").autoIncrement()
    val sighn = varchar("sighn", 9)
    val user_id = integer("user_id")
    val vin = varchar("vin", 17)
    val satatus = varchar("status", 15)
    val name = varchar("name", 30)

    override val primaryKey = PrimaryKey(id)
}