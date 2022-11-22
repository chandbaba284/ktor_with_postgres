package com.example.DatabaseTables

import com.example.DatabaseTables.UsersRepository.autoIncrement
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object UsersRepository : Table("userstable"){
    var id: Column<Int> = integer("id").autoIncrement()
    var name : Column<String> = varchar("name",512)
    var passwod : Column<String> = varchar("password",512)
    var grouptype : Column<String> = varchar("grouptype",512)
    override var primaryKey: PrimaryKey? = PrimaryKey(id)
}