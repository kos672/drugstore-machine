package com.kkuzmin.drugstoremachine.application.usecase

import com.kkuzmin.drugstoremachine.application.dto.FillInventoryRequest
import com.kkuzmin.drugstoremachine.application.port.InventoryRepository
import com.kkuzmin.drugstoremachine.domain.inventory.Inventory
import com.kkuzmin.drugstoremachine.domain.inventory.ProductQuantity
import com.kkuzmin.drugstoremachine.domain.product.ProductId
import spock.lang.Specification

class FillInventoryUseCaseTest extends Specification {

    InventoryRepository inventoryRepository = Mock()
    FillInventoryUseCase useCase

    def setup() {
        useCase = new FillInventoryUseCase(inventoryRepository)
    }

    def "should add quantity for existing product in inventory"() {
        given: "an inventory with an existing product"
        def inventory = new Inventory()
        def productId = 1
        inventory.fill(new ProductId(productId), new ProductQuantity(2))

        inventoryRepository.load() >> inventory

        when: "inventory is filled with 3 products"
        useCase.execute(new FillInventoryRequest(1, 3))

        then: "quantity is increased by 3"
        inventory.currentState()[new ProductId(productId)].value == 5

        and: "inventory is saved"
        1 * inventoryRepository.save(inventory)
    }

    def "should create inventory item if product does not exist"() {
        given: "an empty inventory"
        def inventory = new Inventory()
        def productId = 2

        inventoryRepository.load() >> inventory

        when: "inventory is filled with the product with amount 4"
        useCase.execute(new FillInventoryRequest(productId, 4))

        then: "product is added with correct quantity 4"
        inventory.currentState()[new ProductId(productId)].value == 4

        and: "inventory is saved"
        1 * inventoryRepository.save(inventory)
    }

    def "should not allow negative quantity"() {
        when: "creating an invalid stock quantity, negative amount"
        new ProductQuantity(-1)

        then: "an exception is thrown"
        thrown(IllegalArgumentException)
    }
}
