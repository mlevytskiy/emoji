package com.example.api

import com.example.api.request.RemoveAppRequest
import com.example.api.response.AllAppsResponse
import com.example.apiUser
import com.example.repository.Neo4jRepository
import com.example.repository.WumfRepository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route

const val REMOVE_APP_ENDPOINT = "/removeApp"

@Location(REMOVE_APP_ENDPOINT)
class RemoveApp

fun Route.removeApp(db: Neo4jRepository) {
    authenticate("jwt") {
        post<RemoveApp> {
            val request = call.receive<RemoveAppRequest>()
            call.apiUser?.let { user ->
                db.removeApp(telegramId = user.telegramId.toLong(), packageName = request.app)
                call.respond(AllAppsResponse(request.app))
            }
        }
    }
}