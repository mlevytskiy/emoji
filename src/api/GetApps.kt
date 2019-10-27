package com.example.api

import com.example.api.response.AllAppsResponse
import com.example.apiUser
import com.example.repository.WumfRepository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route

const val GET_APPS_ENDPOINT = "/getApps"

@Location(GET_APPS_ENDPOINT)
class GetApps

fun Route.getApps(db: WumfRepository) {
    authenticate("jwt") {
        get<GetApps> {
            call.apiUser?.let {user ->
                val appsStr = db.getApps(user.telegramId)
                call.respond( AllAppsResponse(appsStr))
            }
        }
    }

}