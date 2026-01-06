package com.kkuzmin.drugstoremachine.application.usecase

import com.kkuzmin.drugstoremachine.application.dto.purchase.PurchaseRequestDto
import com.kkuzmin.drugstoremachine.application.dto.purchase.PurchaseResponseDto
import com.kkuzmin.drugstoremachine.application.port.InventoryRepository
import com.kkuzmin.drugstoremachine.application.port.PurchaseRepository
import com.kkuzmin.drugstoremachine.application.util.UuidProvider
import com.kkuzmin.drugstoremachine.domain.inventory.Inventory
import com.kkuzmin.drugstoremachine.domain.inventory.ProductQuantity
import com.kkuzmin.drugstoremachine.domain.inventory.exception.InsufficientProductAmountAvailableException
import com.kkuzmin.drugstoremachine.domain.inventory.exception.ProductNotFoundException
import com.kkuzmin.drugstoremachine.domain.product.Money
import com.kkuzmin.drugstoremachine.domain.product.Product
import com.kkuzmin.drugstoremachine.domain.product.ProductGroup
import com.kkuzmin.drugstoremachine.domain.product.ProductId
import com.kkuzmin.drugstoremachine.infrastructure.repository.InMemoryInventoryRepository
import spock.lang.Specification

class BuyProductsUseCaseTest extends Specification {

    InventoryRepository inventoryRepository = Spy(InMemoryInventoryRepository)
    PurchaseRepository purchaseRepository = Mock()
    Map<Integer, Product> productCatalog
    UuidProvider uuidProvider = Mock()

    BuyProductsUseCase useCase

    def setup() {
        productCatalog = [
                1: new Product(new ProductId(1), "Balea shower gel", ProductGroup.PERSONAL_HYGIENE, new Money(new BigDecimal("0.55"))),
                2: new Product(new ProductId(2), "SEINZ beard oil", ProductGroup.FACE, new Money(new BigDecimal("7.95"))),
        ]
        useCase = new BuyProductsUseCase(inventoryRepository, purchaseRepository, productCatalog, uuidProvider)
    }

    def "should buy products successfully and calculate total"() {
        given: "an inventory with stock"
        def inventory = inventoryRepository.load()
        inventory.fill(new ProductId(1), new ProductQuantity(10))
        inventory.fill(new ProductId(2), new ProductQuantity(2))

        def uuidString = "d2e2db22-64ae-4bf4-b399-aa7317e8edee"
        def purchaseId = UUID.fromString(uuidString)
        uuidProvider.generateUuid() >> purchaseId

        def items = [
                1: 3,
                2: 1
        ]

        when:
        PurchaseResponseDto response = useCase.execute(new PurchaseRequestDto(items))

        then: "inventory is updated correctly"
        inventory.currentState()[new ProductId(1)].value == 7
        inventory.currentState()[new ProductId(2)].value == 1

        and: "purchase is saved"
        1 * purchaseRepository.save(_)

        and: "total amount is calculated"
        response.total == "Your order with ID $uuidString was successfully created, its total is 9.60 €."
    }

    def "should throw InsufficientProductAmountAvailableException when trying to buy more products than available"() {
        given:
        def inventory = inventoryRepository.load()
        inventory.fill(new ProductId(1), new ProductQuantity(5))

        def items = [
                1: 10
        ]

        when:
        useCase.execute(new PurchaseRequestDto(items))

        then:
        def ex = thrown(InsufficientProductAmountAvailableException)
        ex.message.contains("There are no available products for id 1 " +
                "for the requested amount of 10, the current stock state is 5")
    }

    def "should throw exception when buying non-existing product"() {
        given:
        def inventory = new Inventory()
        inventoryRepository.load() >> inventory

        def itemsToBuy = [
                1234: 1
        ]

        when:
        useCase.execute(new PurchaseRequestDto(itemsToBuy))

        then:
        def ex = thrown(ProductNotFoundException)
        ex.message.contains("Product with the id 1234 is not found")
    }
}
