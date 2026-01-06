package com.kkuzmin.drugstoremachine.application.usecase

import com.kkuzmin.drugstoremachine.application.port.InventoryRepository
import com.kkuzmin.drugstoremachine.domain.inventory.Inventory
import com.kkuzmin.drugstoremachine.domain.inventory.ProductQuantity
import com.kkuzmin.drugstoremachine.domain.product.Money
import com.kkuzmin.drugstoremachine.domain.product.Product
import com.kkuzmin.drugstoremachine.domain.product.ProductGroup
import com.kkuzmin.drugstoremachine.domain.product.ProductId
import spock.lang.Specification

class GetInventoryUseCaseTest extends Specification {

    InventoryRepository inventoryRepository = Mock()
    Map<Integer, Product> productCatalog

    GetInventoryUseCase useCase

    def setup() {
        productCatalog = [
                1: new Product(new ProductId(1), "Balea shower gel", ProductGroup.PERSONAL_HYGIENE, new Money(new BigDecimal("0.55"))),
                2: new Product(new ProductId(2), "SEINZ beard oil", ProductGroup.FACE, new Money(new BigDecimal("7.95"))),
        ]
        useCase = new GetInventoryUseCase(inventoryRepository, productCatalog)
    }

    def "should retrieve an empty inventory as there are no products available"() {
        given: "an empty inventory"
        def inventory = new Inventory()
        inventoryRepository.load() >> inventory

        when:
        def response = useCase.execute()

        then:
        response.items.size() == 0
    }

    def "should retrieve two products from an inventory"() {
        given: "an inventory with two products"
        def inventory = new Inventory()

        def productId1 = 1
        def productId2 = 2
        inventory.fill(new ProductId(productId1), new ProductQuantity(10))
        inventory.fill(new ProductId(productId2), new ProductQuantity(2))
        inventoryRepository.load() >> inventory


        when:
        def response = useCase.execute()


        then:
        response.items.size() == 2
        response.items.collect { it.id }.containsAll(productId1, productId2)
    }
}
