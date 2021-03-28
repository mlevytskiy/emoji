package com.example.api

import com.example.api.request.NotMyAppsRequest
import com.example.api.response.NotMyAppsResponse
import com.example.model.App
import com.example.repository.Neo4jRepository
import com.example.repository.NotMyAppsRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import kotlin.reflect.KClass

const val GET_NOT_MY_APPS = "/getNotMyApps"

@Location(GET_NOT_MY_APPS)
class GetNotMyApps

fun Route.getNotMyApps(db: Neo4jRepository) {
    post<GetNotMyApps> {
        val request = call.receive<NotMyAppsRequest>()
            var apps: List<App>? = null
            if (request.allWorld) {
                apps = db.getWorldApps() convertTo App::class
            } else if (request.inCountry) {
                apps = db.getCountryApps(request.country) convertTo App::class
            } else if (request.amongFriends) {
                apps = db.getFriendsApps(request.friends.map { it.toLong() }) convertTo App::class
            }
            apps?.let {
                call.respond(NotMyAppsResponse(it))
            } ?: run {
                call.respond(HttpStatusCode.InternalServerError, "Bad request")
            }
    }
}

infix fun List<com.example.neo4j.App>.convertTo(clazz: KClass<App>): List<App> {
    return map {app->App(app.packageName, app.users.map { it.id.toInt() }) }
}