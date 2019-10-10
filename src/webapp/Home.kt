package com.example.webapp

import com.example.model.EPSession
import com.example.repository.Repository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.sessions

const val HOME = "/"

@Location(HOME)
class Home

fun Route.home(db: Repository) {
    get<Home> {
        val user = call.sessions.get("session")?.let { db.user((it as EPSession).userId) }
        call.respond(FreeMarkerContent("home.ftl", mapOf("user" to user)))
    }
}