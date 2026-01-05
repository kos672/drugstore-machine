package com.kkuzmin.drugstoremachine.infrastructure.repository

import com.kkuzmin.drugstoremachine.application.port.InventoryRepository
import com.kkuzmin.drugstoremachine.domain.inventory.Inventory
import org.springframework.stereotype.Repository

@Repository
class InMemoryInventoryRepository : InventoryRepository {

    private var inventory = Inventory()

    override fun load(): Inventory = inventory

    override fun save(inventory: Inventory) {
        this.inventory = inventory
    }


}