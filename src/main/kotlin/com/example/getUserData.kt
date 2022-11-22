package com.example.plugins

import com.example.DatabaseTables.UsersRepository
import com.example.Databaseconfig.PostgresSetup
import com.example.UserResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.selects.select
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.getallusersfromdatabase() {
//    val database = Database.connect
    get("/getallusers") {
     val userdata = PostgresSetup.dbQuery {
         UsersRepository.selectAll() .map {
             UserResponse(it[UsersRepository.id],it[UsersRepository.name],it[UsersRepository.passwod],it[UsersRepository.grouptype])
         }
     }
        call.respond(userdata)
    }

    post("/insertuser") {

        val response = call.receive<UserResponse>()
        val user = PostgresSetup.dbQuery {
            transaction {
                UsersRepository.insert {
                   it[UsersRepository.id] = response.id
                   it[UsersRepository.name] = response.name
                   it[UsersRepository.passwod] = response.password
                   it[UsersRepository.grouptype] = response.type
                }
            }
        }
        call.respondText("Data added successfully")
    }

//    post("/updateuser") {
//        val database = Database.connect
//        val body = call.receive<Users>()
//        if (!body.equals(null)) {
//            val user = database.update(UsersRepository) {
//                set(it.name, body.name)
//                set(it.passwod, body.password)
//                where {
//                    it.id eq (body.id)
//                }
//
//            }
//            call.respond(user)
//            call.respondText("Data updated successfully")
//        }
//
//
//    }
//
//    post("/deleteUser") {
//        val database = Database.connect
//        val id = call.request.queryParameters["id"]
//        val delete = database.delete(UsersRepository) {
//            it.id eq id!!.toInt()
//        }
//        call.respond(delete)
//        call.respondText("Data deleted successfully")
//
//    }

}