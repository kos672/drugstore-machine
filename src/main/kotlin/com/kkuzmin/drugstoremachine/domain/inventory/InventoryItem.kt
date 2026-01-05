package com.kkuzmin.drugstoremachine.domain.inventory

import com.kkuzmin.drugstoremachine.domain.product.ProductId

class InventoryItem(
    private val productId: ProductId,
    private var stockState: ProductQuantity
) {
    fun increase(amountToAdd: ProductQuantity) {
        stockState += amountToAdd
    }

    fun quantity(): ProductQuantity = stockState
}