package com.kkuzmin.drugstoremachine.domain.purchase

import com.kkuzmin.drugstoremachine.domain.product.ProductId

data class PurchaseItem(
    val productId: ProductId,
    val amount: Int
)