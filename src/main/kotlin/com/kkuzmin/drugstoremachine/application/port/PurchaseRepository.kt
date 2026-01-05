package com.kkuzmin.drugstoremachine.application.port

import com.kkuzmin.drugstoremachine.domain.purchase.Purchase

interface PurchaseRepository {

    fun save(purchase: Purchase)
}