package com.example
@kotlinx.serialization.Serializable
data class UserResponse(
    var id :Int,var name : String,var password: String,var type :String
) {
}