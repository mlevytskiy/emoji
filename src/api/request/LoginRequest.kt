package com.example.api.request

data class LoginRequest(val userId: Int, val friendsList: String = "", val passwordHash: String) {

}