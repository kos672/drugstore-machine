package com.kkuzmin.drugstoremachine.infrastructure.repository

import com.kkuzmin.drugstoremachine.application.port.PurchaseRepository
import com.kkuzmin.drugstoremachine.domain.purchase.Purchase
import org.springframework.stereotype.Repository

@Repository
class InMemoryPurchaseRepository : PurchaseRepository {

    private val purchases = mutableListOf<Purchase>()

    override fun save(purchase: Purchase) {
        purchases.add(purchase)
    }

}