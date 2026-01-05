package com.kkuzmin.drugstoremachine.application.usecase

import com.kkuzmin.drugstoremachine.application.dto.FillInventoryRequest
import com.kkuzmin.drugstoremachine.application.port.InventoryRepository
import com.kkuzmin.drugstoremachine.domain.inventory.ProductQuantity
import com.kkuzmin.drugstoremachine.domain.product.ProductId
import org.springframework.stereotype.Service

@Service
class FillInventoryUseCase(private val inventoryRepository: InventoryRepository) {

    fun execute(request: FillInventoryRequest) {
        val inventory = inventoryRepository.load()
        inventory.fill(ProductId(request.productId), ProductQuantity(request.quantity))
        inventoryRepository.save(inventory)
    }
}