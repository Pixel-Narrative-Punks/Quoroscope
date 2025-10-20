package com.pixnpunk.addons.data.repository

import com.pixnpunk.addons.AddonsUtils
import com.pixnpunk.addons.domain.model.AddonData
import com.pixnpunk.addons.domain.repository.AddonsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AddonsRepositoryImpl : AddonsRepository, KoinComponent {

    private val addonsUtils: AddonsUtils = get()

    override suspend fun getAllAddons(): List<AddonData> {
        return if (addonsUtils.receiveEngineAddons()) {
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