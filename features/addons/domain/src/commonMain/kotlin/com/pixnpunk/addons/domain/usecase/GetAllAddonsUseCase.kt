package com.pixnpunk.addons.domain.usecase

import com.pixnpunk.addons.domain.model.AddonData
import com.pixnpunk.addons.domain.repository.AddonsRepository

class GetAllAddonsUseCase(
    private val addonsRepository: AddonsRepository
) {
    suspend fun execute(): List<AddonData> = addonsRepository.getAllAddons()
}