package com.example

import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength

@kotlinx.serialization.Serializable
data class UserResponse(
    var id :Int,var name : String,var password: String,var type :String
)