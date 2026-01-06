package com.kkuzmin.drugstoremachine.domain.purchase

import com.kkuzmin.drugstoremachine.domain.product.Money
import com.kkuzmin.drugstoremachine.domain.product.Product
import com.kkuzmin.drugstoremachine.domain.product.ProductId
import java.util.UUID

class Purchase private constructor(
    val id: PurchaseId,
    val items: List<PurchaseItem>,
    val total: Money
) {

    fun total() = total

    companion object {

        fun of(
            uuid: UUID,
            items: List<PurchaseItem>,
            productCatalog: Map<ProductId, Product>
        ): Purchase {
            val total = items.fold(Money.zero()) { acc, prod ->
                val product = productCatalog[prod.productId] ?: throw RuntimeException()
                acc + Money(product.price.amount.multiply(prod.amount.toBigDecimal()))
            }

            return Purchase(PurchaseId(uuid), items, total)
        }
    }
}
