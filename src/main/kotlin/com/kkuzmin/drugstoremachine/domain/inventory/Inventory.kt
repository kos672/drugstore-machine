package com.kkuzmin.drugstoremachine.domain.inventory

import com.kkuzmin.drugstoremachine.domain.inventory.exception.ProductNotFoundException
import com.kkuzmin.drugstoremachine.domain.product.ProductId

class Inventory(
    private val availableItems: MutableMap<ProductId, InventoryItem> = mutableMapOf()
) {

    fun fill(productId: ProductId, quantity: ProductQuantity) {
        availableItems.computeIfAbsent(productId) {
            InventoryItem(productId, ProductQuantity.zero())
        }.increase(quantity)
    }

    fun take(productId: ProductId, quantity: ProductQuantity) {
        val item: InventoryItem? = availableItems[productId]
        item?.decrease(quantity) ?: throw ProductNotFoundException(productId)
    }

    fun currentState(): Map<ProductId, ProductQuantity> = availableItems.mapValues { it.value.quantity() }
}