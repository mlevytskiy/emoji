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

const val ABOUT = "/about"

@Location(ABOUT)
class About

fun Route.about(db: Repository) {
    get<About> {
        val user = call.sessions.get("session")?.let { db.user((it as EPSession).userId) }
        call.respond(FreeMarkerContent("about.ftl", mapOf("user" to user)))
    }
}