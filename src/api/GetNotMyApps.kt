package com.example.api

import com.example.api.request.NotMyAppsRequest
import com.example.api.response.NotMyAppsResponse
import com.example.model.App
import com.example.repository.NotMyAppsRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route

const val GET_NOT_MY_APPS = "/getNotMyApps"

@Location(GET_NOT_MY_APPS)
class GetNotMyApps

fun Route.getNotMyApps(inMemory: NotMyAppsRepository) {
    post<GetNotMyApps> {
        val request = call.receive<NotMyAppsRequest>()
            var apps: List<App>? = null
            if (request.allWorld) {
                apps = inMemory.getWorldApps()
            } else if (request.inCountry) {
                apps = inMemory.getCountryApps(request.country)
            } else if (request.amongFriends) {
                apps = inMemory.getAmongFriendsApps(request.friends)
            }
            apps?.let {
                call.respond(NotMyAppsResponse(it))
            } ?: run {
                call.respond(HttpStatusCode.InternalServerError, "Bad request")
            }
    }
}