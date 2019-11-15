package com.example.api

import com.example.api.request.AddAppRequest
import com.example.api.request.NotMyAppsRequest
import com.example.api.response.App
import com.example.api.response.NotMyAppsResponse
import com.example.api.response.Person
import com.example.apiUser
import com.example.repository.WumfRepository
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route

const val GET_NOT_MY_APPS = "/getNotMyApps"

@Location(GET_NOT_MY_APPS)
class GetNotMyApps

fun Route.getNotMyApps(db: WumfRepository, countryUsers: HashMap<String, List<String>>) {
    authenticate("jwt") {
        post<GetNotMyApps> {
            val request = call.receive<NotMyAppsRequest>()
            call.apiUser?.let {user ->
                call.respond(generateFakeResponse())
            }
        }
    }

}

private fun generateFakeResponse(): NotMyAppsResponse {
    val app1 = App("com.facebook.katana", arrayListOf(generateFakePerson()))
    val app2 = App("com.instagram.android", arrayListOf(generateFakePerson()))
    val app3 = App("com.dinarys.aromakava", arrayListOf(generateFakePerson()))
    val app4 = App("ru.yandex.yandexnavi", arrayListOf(generateFakePerson()))
    return NotMyAppsResponse(arrayListOf(app1, app2, app3, app4))
}

private fun generateFakePerson(): Person {
    return Person(3333, "aaaa", "ua",true, true)
}