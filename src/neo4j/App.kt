package com.example.neo4j

import org.neo4j.ogm.annotation.*

@NodeEntity("App")
class App(
    @Id var packageName: String = "",
    @Relationship(value = "HAS", direction = Relationship.INCOMING)
    var users: List<User> = emptyList()
)