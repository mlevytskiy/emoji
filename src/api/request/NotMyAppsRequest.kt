package com.example.api.request

class NotMyAppsRequest(val inCountry: Boolean = false, val country: String = "",
                       val allWorld: Boolean = false, val friends: List<Int>, val amongFriends: Boolean = false)