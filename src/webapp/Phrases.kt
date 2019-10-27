package com.example.webapp

import com.example.model.EPSession
import com.example.model.EmojiPhrase
import com.example.model.User
import com.example.redirect
import com.example.repository.Repository
import com.example.securityCode
import com.example.verifyCode
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.sessions.sessions
import java.lang.IllegalArgumentException

const val PHRASES = "/phrases"

@Location(PHRASES)
class Phrases

fun Route.phrases(db: Repository, hashFunction: (String) -> String) {
//    get<Phrases> {
//        val user = call.sessions.get("session")?.let { db.user((it as EPSession).userId) }
//
//        if (user == null) {
//            call.redirect(Signin())
//        } else {
//            val phrases = db.phrases(user.userId)
//            val date = System.currentTimeMillis()
//            val code = call.securityCode(date, user, hashFunction)
//            call.respond(FreeMarkerContent("phrases.ftl", mapOf("phrases" to phrases,
//                "user" to user, "date" to date, "code" to code), user.userId))
//        }
//    }
//    post<Phrases> {
//        val user = call.sessions.get("session")?.let { db.user((it as EPSession).userId) }
//
//        val params = call.receiveParameters()
//        val date = params["date"]?.toLongOrNull() ?: return@post call.redirect(it)
//        val code = params["code"] ?: return@post call.redirect(it)
//        val action = params["action"] ?: throw IllegalArgumentException("Missing parameter: action")
//
//        if (user == null || !call.verifyCode(date, user, code, hashFunction)) {
//            call.redirect(Signin())
//        }
//        when (action) {
//            "delete" -> {
//                val id = params["id"] ?: throw IllegalArgumentException("Missing parameter: id")
//                db.remove(id)
//            }
//            "add" -> {
//                val emoji = params["emoji"] ?: throw IllegalAccessException("Missing parameter: emoji")
//                val phrase = params["phrase"] ?: throw IllegalArgumentException("Missing parameter: phrase")
//                db.add(user!!.userId, emoji, phrase)
//            }
//        }
//        call.redirect(Phrases())
//    }
}