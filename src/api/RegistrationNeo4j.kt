package com.example.api

import com.example.JwtService
import com.example.api.request.RegistrationRequest
import com.example.repository.Neo4jRepository
import io.ktor.application.call
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.*
import io.ktor.routing.Route

const val REG2_ENDPOINT = "/reg2"

@Location(REG2_ENDPOINT)
class RegistrationNeo4j

fun Route.reg2(db: Neo4jRepository, jwtService: JwtService) {
    post<RegistrationNeo4j> {
        val request = call.receive<RegistrationRequest>()
        val result = db.test()
        call.respondText { "Is session for neo4j open = $result" }

    }
}