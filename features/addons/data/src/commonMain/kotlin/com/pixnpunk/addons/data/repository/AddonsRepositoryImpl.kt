package com.pixnpunk.addons.data.repository

import com.pixnpunk.addons.domain.model.AddonData
import com.pixnpunk.addons.domain.repository.AddonsRepository
import com.pixnpunk.utils.AddonsUtils

class AddonsRepositoryImpl : AddonsRepository {
    override suspend fun getAllAddons(): List<AddonData> {
        return if (AddonsUtils.receiveEngineAddons()) {
            listOf(
                AddonData(
                    title = "QSPlugin",
                    author = "l3ger0j",
                    version = "1.0.0"
                )
            )
        } else {
            listOf()
        }
    }
}