package com.kkuzmin.drugstoremachine.application.usecase

import com.kkuzmin.drugstoremachine.application.dto.inventory.FillInventoryRequest
import com.kkuzmin.drugstoremachine.application.port.InventoryRepository
import com.kkuzmin.drugstoremachine.domain.inventory.ProductQuantity
import com.kkuzmin.drugstoremachine.domain.inventory.exception.ProductNotFoundException
import com.kkuzmin.drugstoremachine.domain.product.Product
import com.kkuzmin.drugstoremachine.domain.product.ProductId
import org.springframework.stereotype.Service

@Service
class FillInventoryUseCase(private val inventoryRepository: InventoryRepository,
                           private val productCatalog: Map<Int, Product>
    ) {

    fun execute(request: FillInventoryRequest) {
        productCatalog[request.productId] ?: throw ProductNotFoundException(ProductId(request.productId))
        val inventory = inventoryRepository.load()
        inventory.fill(ProductId(request.productId), ProductQuantity(request.quantity))
        inventoryRepository.save(inventory)
    }
}