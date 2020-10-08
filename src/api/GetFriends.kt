package com.example.api

import com.example.api.request.GetFriendsRequest
import com.example.api.request.NotMyAppsRequest
import com.example.api.response.Friend
import com.example.api.response.GetFriendsResponse
import com.example.api.response.NotMyAppsResponse
import com.example.model.App
import com.example.repository.NotMyAppsRepository
import com.example.repository.WumfRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val GET_FRIENDS_ENDPOINT = "/getFriends"

@Location(GET_FRIENDS_ENDPOINT)
class GetFriends

fun Route.getFriends(db: WumfRepository) {
    post<GetFriends> {
        val request = call.receive<GetFriendsRequest>()
        val users = db.users(request.userIds)
        call.respond(GetFriendsResponse(users.map {
            Friend(id = it.telegramId, apps = it.apps)
        }))
    }
}

//fun Route.getNotMyApps(inMemory: NotMyAppsRepository) {
//    post<GetNotMyApps> {
//        val request = call.receive<NotMyAppsRequest>()
//        var apps: List<App>? = null
//        if (request.allWorld) {
//            apps = inMemory.getWorldApps()
//        } else if (request.inCountry) {
//            apps = inMemory.getCountryApps(request.country)
//        } else if (request.amongFriends) {
//            apps = inMemory.getAmongFriendsApps(request.friends)
//        }
//        apps?.let {
//            call.respond(NotMyAppsResponse(it))
//        } ?: run {
//            call.respond(HttpStatusCode.InternalServerError, "Bad request")
//        }
//    }
//}