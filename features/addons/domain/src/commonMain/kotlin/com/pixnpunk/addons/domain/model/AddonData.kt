package com.pixnpunk.addons.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AddonData(
    val title: String,
    val author: String,
    val version: String
)