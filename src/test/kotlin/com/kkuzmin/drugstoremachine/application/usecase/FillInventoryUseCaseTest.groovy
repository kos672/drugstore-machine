package com.kkuzmin.drugstoremachine.application.usecase

import com.kkuzmin.drugstoremachine.application.dto.inventory.FillInventoryRequest
import com.kkuzmin.drugstoremachine.application.port.InventoryRepository
import com.kkuzmin.drugstoremachine.domain.inventory.Inventory
import com.kkuzmin.drugstoremachine.domain.inventory.ProductQuantity
import com.kkuzmin.drugstoremachine.domain.inventory.exception.ProductNotFoundException
import com.kkuzmin.drugstoremachine.domain.product.Money
import com.kkuzmin.drugstoremachine.domain.product.Product
import com.kkuzmin.drugstoremachine.domain.product.ProductGroup
import com.kkuzmin.drugstoremachine.domain.product.ProductId
import com.kkuzmin.drugstoremachine.infrastructure.repository.InMemoryInventoryRepository
import spock.lang.Specification

class FillInventoryUseCaseTest extends Specification {

    InventoryRepository inventoryRepository = Spy(InMemoryInventoryRepository)
    Map<Integer, Product> productCatalog
    FillInventoryUseCase useCase

    def setup() {
        productCatalog = [
                1: new Product(new ProductId(1), "Balea shower gel", ProductGroup.PERSONAL_HYGIENE, new Money(new BigDecimal("0.55"))),
                2: new Product(new ProductId(2), "SEINZ beard oil", ProductGroup.FACE, new Money(new BigDecimal("7.95"))),
        ]
        useCase = new FillInventoryUseCase(inventoryRepository, productCatalog)
    }

    def "should add quantity for existing product in inventory"() {
        given: "an inventory with an existing product"
        def productId = 1
        inventoryRepository.load().fill(new ProductId(productId), new ProductQuantity(2))

        when: "inventory is filled with 3 products"
        useCase.execute(new FillInventoryRequest(productId, 3))

        then: "quantity is increased by 3"
        inventoryRepository.load().currentState()[new ProductId(productId)].value == 5

        and: "inventory was updated"
        1 * inventoryRepository.update(_)
    }

    def "should create inventory item if product does not exist"() {
        given: "an empty inventory"
        def productId = 2
        def quantity = 4

        when: "inventory is filled with 4 products"
        useCase.execute(new FillInventoryRequest(productId, quantity))

        then: "there is a product with the new quantity which matches exactly the number from the request"
        inventoryRepository.load().currentState()[new ProductId(productId)].value == quantity

        and: "update was called"
        1 * inventoryRepository.update(_)
    }

    def "should not create an inventory item for unknown product"() {
        given: "an inventory with productId 2"
        def inventory = new Inventory()
        def productId = 9999

        inventoryRepository.load() >> inventory

        when: "inventory is filled with the product with amount 4"
        useCase.execute(new FillInventoryRequest(productId, 4))

        then: "an exception is thrown"
        thrown(ProductNotFoundException)
    }

    def "should not allow negative quantity"() {
        when: "creating an invalid stock quantity, negative amount"
        new ProductQuantity(-1)

        then: "an exception is thrown"
        thrown(IllegalArgumentException)
    }
}
