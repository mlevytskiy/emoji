package com.example.api

import com.example.JwtService
import com.example.api.request.RegistrationRequest
import com.example.api.response.RegistrationResponse
import com.example.neo4j.User
import com.example.repository.Neo4jRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.response.respond

const val REGISTRATION_ENDPOINT = "/registration"

@Location(REGISTRATION_ENDPOINT)
class Registration

//fun Route.registration(db: WumfRepository, jwtService: JwtService) {
//    post<Registration> {
//        val request = call.receive<RegistrationRequest>()
//
//        var user = db.user(request.userId, hash(request.createdPasswordHash))
//
//        if (user == null) {
//            user = WumfUser(request.userId, request.displayName, hash(request.createdPasswordHash), "", request.country)
//            db.createUser(user)
//            val token = jwtService.generateToken(user)
//            val friendListStr = jwtService.getFriendsList(request.friendsList, db)
//            call.respond(RegistrationResponse(token = token, friendsList = friendListStr))
//        } else {
//            call.respond(HttpStatusCode.InternalServerError, "User already exist")
//        }
//    }
//}

fun Route.registration(db: Neo4jRepository, jwtService: JwtService) {
    post<Registration> {
        val request = call.receive<RegistrationRequest>()
        val result = db.getUser(request.userId.toLong())
        if (result == null) {
            val token = jwtService.generateToken(request.userId)
            val user = User(id= request.userId.toLong(), displayName = request.displayName, country = "ua")
            db.createUser(user)
            call.respond(RegistrationResponse(token = token, friendsList = emptyList()))
        } else {
            call.respond(HttpStatusCode.InternalServerError, "User already exist")
        }
//        var user = db.user(request.userId, hash(request.createdPasswordHash))



//        if (user == null) {
//            user = WumfUser(request.userId, request.displayName, hash(request.createdPasswordHash), "", request.country)
//            db.createUser(user)
//            val token = jwtService.generateToken(user)
//            val friendListStr = jwtService.getFriendsList(request.friendsList, db)
//            call.respond(RegistrationResponse(token = token, friendsList = friendListStr))
//        } else {
//            call.respond(HttpStatusCode.InternalServerError, "User already exist")
//        }
    }
}
//db: Noo4jRepository