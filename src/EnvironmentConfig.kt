package com.example

import com.natpryce.konfig.Configuration
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.getValue
import com.natpryce.konfig.intType
import com.natpryce.konfig.stringType

class EnvironmentConfig(
        configuration: Configuration = EnvironmentVariables()
) {
    val neo4jUrl = configuration[NEO4J_URL]
    val neo4jUsername = configuration[NEO4J_USERNAME]
    val neo4jPassword = configuration[NEO4J_PASSWORD]

    companion object {
        val NEO4J_URL by stringType
        val NEO4J_USERNAME by stringType
        val NEO4J_PASSWORD by stringType
    }

}
