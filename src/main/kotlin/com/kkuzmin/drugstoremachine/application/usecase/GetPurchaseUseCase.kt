package com.kkuzmin.drugstoremachine.application.usecase

import com.kkuzmin.drugstoremachine.application.dto.ProductResponseDto
import com.kkuzmin.drugstoremachine.application.dto.purchase.GetPurchaseResponseDto
import com.kkuzmin.drugstoremachine.application.port.PurchaseRepository
import com.kkuzmin.drugstoremachine.domain.inventory.exception.ProductNotFoundException
import com.kkuzmin.drugstoremachine.domain.product.Product
import com.kkuzmin.drugstoremachine.domain.purchase.PurchaseItem
import org.springframework.stereotype.Service

@Service
class GetPurchaseUseCase(
    private val purchaseRepository: PurchaseRepository,
    private val productCatalog: Map<Int, Product>
) {

    fun execute() =
        purchaseRepository.load().map {
            val productResponseDtos = mapPurchaseItemToProductResponseDto(it.items)
            GetPurchaseResponseDto(it.id.value, productResponseDtos, it.total.amount)
        }

    private fun mapPurchaseItemToProductResponseDto(purchaseItems: List<PurchaseItem>): List<ProductResponseDto> =
        purchaseItems.map {
            val product = productCatalog[it.productId.value] ?: throw ProductNotFoundException(it.productId)
            ProductResponseDto(it.productId.value, product.name, product.group.name, product.price.amount, it.amount)
        }
}