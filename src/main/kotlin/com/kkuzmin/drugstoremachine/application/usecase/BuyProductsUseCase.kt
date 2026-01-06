package com.kkuzmin.drugstoremachine.application.usecase

import com.kkuzmin.drugstoremachine.application.dto.purchase.PurchaseRequestDto
import com.kkuzmin.drugstoremachine.application.dto.purchase.PurchaseResponseDto
import com.kkuzmin.drugstoremachine.application.port.InventoryRepository
import com.kkuzmin.drugstoremachine.application.port.PurchaseRepository
import com.kkuzmin.drugstoremachine.application.util.UuidProvider
import com.kkuzmin.drugstoremachine.domain.inventory.ProductQuantity
import com.kkuzmin.drugstoremachine.domain.product.Product
import com.kkuzmin.drugstoremachine.domain.product.ProductId
import com.kkuzmin.drugstoremachine.domain.purchase.Purchase
import com.kkuzmin.drugstoremachine.domain.purchase.PurchaseItem
import org.springframework.stereotype.Service

@Service
class BuyProductsUseCase(
    val inventoryRepository: InventoryRepository,
    val purchaseRepository: PurchaseRepository,
    val productCatalog: Map<Int, Product>,
    val uuidProvider: UuidProvider
) {

    fun execute(request: PurchaseRequestDto): PurchaseResponseDto? {
        var response: PurchaseResponseDto? = null
        inventoryRepository.update { inventory ->
            val purchaseItems: MutableList<PurchaseItem> = mutableListOf()
            request.items.forEach {
                val productId = ProductId(it.key)
                inventory.take(productId, ProductQuantity(it.value))
                purchaseItems.add(PurchaseItem(productId, it.value))
            }
            val purchase: Purchase =
                Purchase.of(uuidProvider.generateUuid(), purchaseItems, productCatalog.mapKeys { ProductId(it.key) })
            purchaseRepository.save(purchase)
            response = PurchaseResponseDto("Your order with ID ${purchase.id.value} was successfully created, its total is ${purchase.total().amount} €.")
        }
        return response
    }
}
