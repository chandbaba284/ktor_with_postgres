package com.example

import com.example.DatabaseTables.Groupsepo
import com.example.DatabaseTables.UsersRepository
import com.example.Databaseconfig.PostgresSetup
import com.example.plugins.resultrowtouserresponce
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

fun Routing.getallgroups() {

    post("/addgroup") {
        val response = call.receive<List<GroupsResponce>>()
        if (response.size > 0) {
            for (i in response) {
                val data = PostgresSetup.dbQuery {
                    Groupsepo.insert {
                        it[Groupsepo.id] = i.id
                    }
                }
            }
            call.respondText("Data added successfully")
        }

    }

    get("/getGroupIds") {

        val datafromDb = PostgresSetup.dbQuery {
            Groupsepo.selectAll().map {
                GroupsResponce(it[Groupsepo.id])
            }
        }
        call.respond(datafromDb)
    }

    get("/jointables") {

        val data =PostgresSetup.dbQuery {
            UsersRepository.select(UsersRepository.id eq Groupsepo.id)
                .map {
                    resultrowtouserresponce(it)
                }

        }
        println(data)

    }

}
