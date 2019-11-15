package com.example.api.response

class NotMyAppsResponse(apps: List<App>)

class App(val appPackage: String, val people: List<Person>)

class Person(id: Int, displayName: String, country: String,
             displayNameCanBeShownForUnknownPeople: Boolean,
             personProfileCanBeShownForUnknownPeople: Boolean)