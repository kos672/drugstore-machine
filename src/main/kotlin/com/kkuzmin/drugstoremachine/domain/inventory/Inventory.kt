package com.kkuzmin.drugstoremachine.domain.inventory

import com.kkuzmin.drugstoremachine.domain.product.ProductId

class Inventory(
    private val availableItems: MutableMap<ProductId, InventoryItem> = mutableMapOf()
) {

    fun fill(productId: ProductId, quantity: ProductQuantity) {
        availableItems.computeIfAbsent(productId) {
            InventoryItem(productId, ProductQuantity.zero())
        }.increase(quantity)
    }

    fun currentState(): Map<ProductId, ProductQuantity> = availableItems.mapValues { it.value.quantity() }
}