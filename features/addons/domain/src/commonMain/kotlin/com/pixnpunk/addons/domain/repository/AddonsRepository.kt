package com.pixnpunk.addons.domain.repository

import com.pixnpunk.addons.domain.model.AddonData

interface AddonsRepository {
    suspend fun getAllAddons(): List<AddonData>
}