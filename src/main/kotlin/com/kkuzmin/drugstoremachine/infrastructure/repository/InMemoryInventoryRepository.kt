package com.kkuzmin.drugstoremachine.infrastructure.repository

import com.kkuzmin.drugstoremachine.application.port.InventoryRepository
import com.kkuzmin.drugstoremachine.domain.inventory.Inventory
import org.springframework.stereotype.Repository
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.withLock

@Repository
class InMemoryInventoryRepository : InventoryRepository {

    private val lock: ReadWriteLock = ReentrantReadWriteLock()

    private var inventory = Inventory()

    override fun load(): Inventory {
        lock.readLock().withLock { return inventory }
    }

    override fun update(logic: (Inventory) -> Unit) {
        lock.writeLock().withLock { logic(inventory) }
    }

}