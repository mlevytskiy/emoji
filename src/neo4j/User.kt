package com.example.neo4j

import org.neo4j.ogm.annotation.*

@NodeEntity("User")
class User(@Id var id: Long = 0,
           var displayName: String = "",
           var country: String = "",
           @Relationship("HAS")
           var apps: List<App> = emptyList())