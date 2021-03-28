package com.example.api

import com.example.api.request.AddAppRequest
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

const val ADD_APP_ENDPOINT = "/addApp"

@Location(ADD_APP_ENDPOINT)
class AddApp

fun Route.addApp(db: Neo4jRepository) {
    authenticate("jwt") {
        post<AddApp> {
            val request = call.receive<AddAppRequest>()
            call.apiUser?.let { user ->
                db.addApp(telegramId = user.telegramId.toLong(), packageName = request.app)
                call.respond(AllAppsResponse(request.app))
            }
        }
    }
}