package com.kkuzmin.drugstoremachine.application.port

import com.kkuzmin.drugstoremachine.domain.inventory.Inventory

interface InventoryRepository {

    fun load(): Inventory

    fun update(logic: (Inventory) -> Unit)

}