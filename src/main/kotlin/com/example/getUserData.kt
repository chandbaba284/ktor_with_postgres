package com.example.plugins

import com.example.DatabaseTables.UsersRepository
import com.example.Databaseconfig.PostgresSetup
import com.example.UserResponse
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import javax.security.sasl.AuthenticationException

fun Routing.getallusersfromdatabase() {
//    val database = Database.connect
    get("/getallusers") {
        val userdata = PostgresSetup.dbQuery {
            UsersRepository.selectAll().map {
                UserResponse(
                    it[UsersRepository.id],
                    it[UsersRepository.name],
                    it[UsersRepository.passwod],
                    it[UsersRepository.grouptype]
                )
            }
        }
        call.respond(userdata)
    }

    post("/insertusersList") {
        CoroutineScope(Dispatchers.IO).launch {
            val response = call.receive<List<UserResponse>>()
            PostgresSetup.dbQuery {
                transaction {
                    for (i in response) {
                        UsersRepository.insert {
                            it[UsersRepository.id] = i.id
                            it[UsersRepository.name] = i.name
                            it[UsersRepository.passwod] = i.password
                            it[UsersRepository.grouptype] = i.type

                        }
                    }
                }
            }
        }

        call.respondText("Data added successfully")
    }


    post("/insertuser") {
        val response = call.receive<UserResponse>()
        val validateUser = Validation<UserResponse> {
            UserResponse::name {
                minLength(2)
                maxLength(100)
            }
        }
        val invalidUser = UserResponse(response.id,response.name,response.password,response.type)
        val validationResult = validateUser(invalidUser)
        println(validationResult)
        if (validationResult.errors.size==0){
            PostgresSetup.dbQuery {
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
        }else{
            call.respondText(validationResult.toString())
        }

    }

    post("/finduserbyId") {
        val response = call.request.queryParameters["id"]?.toInt()
        val user = PostgresSetup.dbQuery {
            UsersRepository.select {
                UsersRepository.id eq (response ?: 0)
            }.map {
                resultrowtouserresponce(it)
            }
        }
        println(response)
        println(user)
        call.respond(user)

    }

    post("/updateuser") {
        val response = call.receive<UserResponse>()
        val database = PostgresSetup.dbQuery {
            UsersRepository.update({ UsersRepository.id eq response.id }) {
                it[UsersRepository.id] = response.id
                it[UsersRepository.name] = response.name
                it[UsersRepository.passwod] = response.password
                it[UsersRepository.grouptype] = response.type
            }
        }
        println(database)
        call.respondText("Fields updated successfully")
    }
//
    post("/deleteUser") {
        val responce = call.request.queryParameters["id"]
        val rowsdeleted = PostgresSetup.dbQuery {
            UsersRepository.deleteWhere { (UsersRepository.id eq (responce?.toInt() ?: 0)) }

        }
        if (rowsdeleted > 0) {
            call.respondText("Data deleted successfully")
        } else {
            call.respondText("No rows affected")
        }

    }

    get("/authorizationexception") {
        throw AuthenticationException("\"Forbidden Error\"")
    }

    get("/nullpointerexception") {
        throw NullPointerException("value can't be null")
    }


}

fun resultrowtouserresponce(resultRow: ResultRow): UserResponse {
    return UserResponse(
        resultRow[UsersRepository.id],
        resultRow[UsersRepository.name],
        resultRow[UsersRepository.passwod],
        resultRow[UsersRepository.grouptype]
    )
}