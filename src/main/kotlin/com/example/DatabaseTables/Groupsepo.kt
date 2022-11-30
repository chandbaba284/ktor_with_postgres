package com.example.DatabaseTables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Groupsepo : Table("groups") {
    val id: Column<Int> = integer("id").autoIncrement()
}