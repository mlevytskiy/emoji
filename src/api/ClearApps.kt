package com.example.api

import com.example.apiUser
import com.example.repository.WumfRepository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.response.respondText
import io.ktor.routing.Route

const val CLEAR_APPS_ENDPOINT = "/clearApps"

@Location(CLEAR_APPS_ENDPOINT)
class ClearApps

fun Route.clearApps(db: WumfRepository) {
//    authenticate("jwt") {
//        post<ClearApps> {
//            call.apiUser?.let {user ->
//                val appsStr = db.clearApps(user)
//                call.respondText { "success" }
//            }
//        }
//    }
}