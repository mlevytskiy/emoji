package com.example.api

import com.example.API_VERSION
import com.example.api.request.PhrasesApiRequest
import com.example.apiUser
import com.example.model.EPSession
import com.example.model.Request
import com.example.model.User
import com.example.repository.Repository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.sessions.sessions

const val PHRASES_API_ENDPOINT = "$API_VERSION/phrases"

@Location(PHRASES_API_ENDPOINT)
class PhrasesApi

fun Route.phrasesApi(db: Repository) {

//    authenticate("jwt") {
//        get<PhrasesApi> {
//            val user = call.apiUser!!
//            call.respond(db.phrases(user.userId))
//        }
//
//        post<PhrasesApi> {
//            val user = call.apiUser!!
//            try {
//                val request = call.receive<PhrasesApiRequest>()
//                val phrase = db.add(user.userId, request.emoji, request.phrase)
//                phrase?.let {
//                    call.respond(phrase)
//                } ?:run {
//                    call.respondText("Invalid data received", status = HttpStatusCode.InternalServerError)
//                }
//            } catch (e: Throwable) {
//                call.respondText("Invalid data received", status = HttpStatusCode.BadRequest)
//            }
//        }
//    }
}