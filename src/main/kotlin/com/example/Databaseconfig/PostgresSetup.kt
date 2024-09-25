package com.example.Databaseconfig

import com.example.DatabaseTables.Groupsepo
import com.example.DatabaseTables.UsersRepository
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object PostgresSetup {

    fun init() {
        val dataSource = HikariDataSource(hikari())
        Database.connect(dataSource)
        transaction {
            SchemaUtils.create(UsersRepository, Groupsepo)
        }

    }

    fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = System.getenv("DRIVER_NAME")
        config.jdbcUrl = System.getenv("DATABASE_URL")
        config.username = System.getenv("USER_NAME")
        config.password = System.getenv("PASSWORD")
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()

        return HikariDataSource(config)
    }


    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }

}