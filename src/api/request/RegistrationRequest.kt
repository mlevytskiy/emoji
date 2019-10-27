package com.example.api.request

data class RegistrationRequest(val userId: String, val friendsList: String = "", val createdPasswordHash: String) {
}