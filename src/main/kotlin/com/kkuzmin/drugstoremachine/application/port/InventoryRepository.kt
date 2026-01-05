package com.kkuzmin.drugstoremachine.application.port

import com.kkuzmin.drugstoremachine.domain.inventory.Inventory

interface InventoryRepository {

    fun load(): Inventory

    fun save(inventory: Inventory)

}