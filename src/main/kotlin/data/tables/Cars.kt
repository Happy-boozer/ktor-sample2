package com.example.data.tables

import org.jetbrains.exposed.v1.core.Table

object Cars: Table(){
    val id = integer("car_id")
    val sign = varchar("sign", 9)
    val user_id = integer("user_id")
    val satatus = varchar("status", 15)
}