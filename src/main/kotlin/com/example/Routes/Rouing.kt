package com.example.Routes

import com.example.plugins.getallusersfromdatabase
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.getallroutes(){

    routing {
        getallusersfromdatabase()
    }
}