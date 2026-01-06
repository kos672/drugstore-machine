package com.kkuzmin.drugstoremachine.application.usecase

import com.kkuzmin.drugstoremachine.application.port.PurchaseRepository
import com.kkuzmin.drugstoremachine.domain.product.Money
import com.kkuzmin.drugstoremachine.domain.product.Product
import com.kkuzmin.drugstoremachine.domain.product.ProductGroup
import com.kkuzmin.drugstoremachine.domain.product.ProductId
import com.kkuzmin.drugstoremachine.domain.purchase.Purchase
import com.kkuzmin.drugstoremachine.domain.purchase.PurchaseId
import com.kkuzmin.drugstoremachine.domain.purchase.PurchaseItem
import spock.lang.Specification

class GetPurchaseUseCaseTest extends Specification {

    PurchaseRepository purchaseRepository = Mock()
    Map<Integer, Product> productCatalog

    GetPurchaseUseCase useCase

    def setup() {
        productCatalog = [
                1: new Product(new ProductId(1), "Balea shower gel", ProductGroup.PERSONAL_HYGIENE, new Money(new BigDecimal("0.55"))),
                2: new Product(new ProductId(2), "SEINZ beard oil", ProductGroup.FACE, new Money(new BigDecimal("7.95"))),
        ]
        useCase = new GetPurchaseUseCase(purchaseRepository, productCatalog)
    }

    def "should retrieve nothing as there are no purchases made"() {
        given: "an empty inventory"
        purchaseRepository.load() >> Collections.emptyList()

        when:
        def response = useCase.execute()

        then:
        response.items.size() == 0
    }

    def "should retrieve all orders available"() {
        given: "an empty inventory"
        def uuid1 = "b677bcff-2022-4ebd-bf16-f4abdfe262a3"

        def total1 = new BigDecimal("9.99")
        def purchase1 = new Purchase(new PurchaseId(UUID.fromString(uuid1)), Arrays.asList(new PurchaseItem(new ProductId(1), 5)), new Money(total1))

        def uuid2 = "1e7427bf-2756-4aa2-8697-90dba4fd5fa3"

        def total2 = new BigDecimal("99.99")
        def purchase2 = new Purchase(new PurchaseId(UUID.fromString(uuid2)), Arrays.asList(
                new PurchaseItem(new ProductId(1), 2),
                new PurchaseItem(new ProductId(2), 3),
        ), new Money(total2))
        purchaseRepository.load() >> Arrays.asList(purchase1, purchase2)

        when:
        def response = useCase.execute()

        then:
        response.size() == 2

        response[0].id.toString() == uuid1
        response[0].items.size() == 1
        response[0].total == total1

        response[1].id.toString() == uuid2
        response[1].items.size() == 2
        response[1].total == total2
    }
}
