package com.example.api

import com.example.JwtService
import com.example.api.request.LoginRequest
import com.example.api.response.Friend
import com.example.api.response.LoginResponse
import com.example.hash
import com.example.model.App
import com.example.neo4j.User
import com.example.repository.Neo4jRepository
import com.example.repository.WumfRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.response.respond

const val LOGIN_ENDPOINT = "/login"

@Location(LOGIN_ENDPOINT)
class Login

fun Route.login(db: Neo4jRepository, jwtService: JwtService, passwordDB: WumfRepository) {
    post<Login> {
        val request = call.receive<LoginRequest>()
        val isPasswordCorrect = passwordDB.checkUser(request.userId, hash(request.passwordHash))
        if (!isPasswordCorrect == null) {
            call.respond(HttpStatusCode.InternalServerError,"Invalid user")
        } else {
            val friendsList = request.friendsList.split(",").map {
                it.toLong()
            }
            val users = ArrayList(db.login(request.userId.toLong(), friendsList))
            val me = users.find { it.id == request.userId.toLong() }
            val myApps = me!!.apps.map { App(it.packageName, getUsersWithoutMeIncludeOnlyMyFriends(it.users, me.id, users)) }
            users.remove(me)
            val friends = users.map { convert(it) }
            if (users.isNotEmpty()) {
                val token = jwtService.generateToken(request.userId)
                call.respond(LoginResponse(token = token, friendsList = friends, myApps = myApps))
            } else {
                call.respond(HttpStatusCode.InternalServerError,"Invalid user")
            }
        }
    }
}

private fun convert(user: User): Friend {
    val apps = user.apps.joinToString(",") { it.packageName }
    return Friend(id=user.id, apps = apps)
}

private fun getUsersWithoutMeIncludeOnlyMyFriends(users: List<User>, myId: Long, friends: List<User>): List<Int> {
    val usersWithoutMe = ArrayList(users.map { it.id })
    usersWithoutMe.remove(myId)
    val existInUsersWithoutMeAndFriends = ArrayList<Long>()
    friends.forEach {
        if (usersWithoutMe.contains(it.id)) {
            existInUsersWithoutMeAndFriends.add(it.id)
        }
    }
    return existInUsersWithoutMeAndFriends.map { it.toInt() }
}