package com.kkuzmin.drugstoremachine.application.usecase

import com.kkuzmin.drugstoremachine.application.dto.inventory.InventoryResponseDto
import com.kkuzmin.drugstoremachine.application.dto.ProductResponseDto
import com.kkuzmin.drugstoremachine.application.port.InventoryRepository
import com.kkuzmin.drugstoremachine.domain.inventory.exception.ProductNotFoundException
import com.kkuzmin.drugstoremachine.domain.product.Product
import org.springframework.stereotype.Service

@Service
class GetInventoryUseCase(
    private val inventoryRepository: InventoryRepository,
    private val productCatalog: Map<Int, Product>
) {

    fun execute(): InventoryResponseDto {
        val inventory = inventoryRepository.load()

        val products = inventory.currentState().map {
            val product = productCatalog[it.key.value] ?: throw ProductNotFoundException(it.key)
            ProductResponseDto(it.key.value, product.name, product.group.name, product.price.amount, it.value.value)
        }
        return InventoryResponseDto(products)
    }
}