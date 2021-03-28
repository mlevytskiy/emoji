package com.example.api

import com.example.api.request.GetFriendsRequest
import com.example.api.request.NotMyAppsRequest
import com.example.api.response.Friend
import com.example.api.response.GetFriendsResponse
import com.example.api.response.NotMyAppsResponse
import com.example.model.App
import com.example.repository.Neo4jRepository
import com.example.repository.NotMyAppsRepository
import com.example.repository.WumfRepository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val GET_FRIENDS_ENDPOINT = "/getFriends"

@Location(GET_FRIENDS_ENDPOINT)
class GetFriends

fun Route.getFriends(db: Neo4jRepository) {
    authenticate("jwt") {
        post<GetFriends> {
            val request = call.receive<GetFriendsRequest>()
            val usersIds = request.userIds.map { it.toLong() }
            val users = db.getUsers(usersIds)
            call.respond(GetFriendsResponse(users.map {
                Friend(id = it.id, apps = it.apps.joinToString(","))
            }))
        }
    }
}