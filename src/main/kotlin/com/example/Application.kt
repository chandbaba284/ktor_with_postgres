package com.example

import com.example.DatabaseTables.Groupsepo
import com.example.DatabaseTables.UsersRepository
import com.example.Databaseconfig.PostgresSetup
import com.example.Routes.getallroutes
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    PostgresSetup.init()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    transaction {
        SchemaUtils.create(UsersRepository)
    }
    getallroutes()

}
