package com.kkuzmin.drugstoremachine.domain.inventory

import com.kkuzmin.drugstoremachine.domain.inventory.exception.InsufficientProductAmountAvailableException
import com.kkuzmin.drugstoremachine.domain.product.ProductId

class InventoryItem(
    private val productId: ProductId,
    private var stockState: ProductQuantity
) {
    fun increase(amountToAdd: ProductQuantity) {
        stockState += amountToAdd
    }

    fun decrease(amountToSubtract: ProductQuantity) {
        if (amountToSubtract > stockState) {
            throw InsufficientProductAmountAvailableException("There are no available products for id ${productId.value} " +
                    "for the requested amount of ${amountToSubtract.value}, the current stock state is ${stockState.value}")
        }
        stockState -= amountToSubtract
    }

    fun quantity(): ProductQuantity = stockState
}