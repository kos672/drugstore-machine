package com.kkuzmin.drugstoremachine.application.usecase

import com.kkuzmin.drugstoremachine.application.dto.purchase.PurchaseRequestDto
import com.kkuzmin.drugstoremachine.application.port.InventoryRepository
import com.kkuzmin.drugstoremachine.application.port.PurchaseRepository
import com.kkuzmin.drugstoremachine.application.util.UuidProvider
import com.kkuzmin.drugstoremachine.domain.inventory.ProductQuantity
import com.kkuzmin.drugstoremachine.domain.inventory.exception.InsufficientProductAmountAvailableException
import com.kkuzmin.drugstoremachine.domain.product.Money
import com.kkuzmin.drugstoremachine.domain.product.Product
import com.kkuzmin.drugstoremachine.domain.product.ProductGroup
import com.kkuzmin.drugstoremachine.domain.product.ProductId
import com.kkuzmin.drugstoremachine.infrastructure.repository.InMemoryInventoryRepository
import spock.lang.Specification

import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class BuyProductsUseCaseConcurrencyTest extends Specification {

    InventoryRepository inventoryRepository
    PurchaseRepository purchaseRepository
    Map<Integer, Product> productCatalog
    UuidProvider uuidProvider

    ProductId productId = new ProductId(1)

    BuyProductsUseCase useCase

    def setup() {
        inventoryRepository = new InMemoryInventoryRepository()

        purchaseRepository = Mock(PurchaseRepository)
        uuidProvider = Mock(UuidProvider)

        productCatalog = [
                1: new Product(productId, "Balea shower gel", ProductGroup.PERSONAL_HYGIENE, new Money(new BigDecimal("0.55"))),
        ]

        useCase = new BuyProductsUseCase(
                inventoryRepository,
                purchaseRepository,
                productCatalog,
                uuidProvider
        )
    }

    def "should sell inventory only for available amount of products under multiple purchase requests"() {
        given:
        uuidProvider.generateUuid() >> UUID.randomUUID()
        inventoryRepository.update { inventory ->
            inventory.fill(productId, new ProductQuantity(10))
        }
        int quantityPerPurchase = 3

        when:
        def items = new HashMap()
        items[productId.value] = quantityPerPurchase


        def futures = (1..100).collect {
            CompletableFuture.runAsync {
                try {
                    useCase.execute(new PurchaseRequestDto(items))
                } catch (InsufficientProductAmountAvailableException ignore) {
                }
            }
        }

        CompletableFuture
                .allOf(futures as CompletableFuture[])
                .get(5, TimeUnit.SECONDS)

        then:
        def remainingProductsAmount = inventoryRepository
                .load()
                .currentState()[productId].value

        remainingProductsAmount >= 0
        remainingProductsAmount < quantityPerPurchase
    }

    def "only one purchase should succeed when inventory purchase amount exactly matches the inventory amount"() {
        given:
        uuidProvider.generateUuid() >> UUID.randomUUID()
        inventoryRepository.update { inventory ->
            inventory.fill(productId, new ProductQuantity(2))
        }
        def successCounter = new AtomicInteger(0)

        def items = new HashMap()
        items[productId.value] = 2


        when:
        def futures = (1..100).collect {
            CompletableFuture.runAsync {
                try {
                    useCase.execute(new PurchaseRequestDto(items))
                    successCounter.incrementAndGet()
                } catch (InsufficientProductAmountAvailableException ignore) {
                }
            }
        }

        CompletableFuture
                .allOf(futures as CompletableFuture[])
                .get(5, TimeUnit.SECONDS)

        then:
        successCounter.get() == 1
        inventoryRepository.load().currentState()[productId].value == 0
    }

    def "reads should never observe negative inventory stock quantity during multiple purchase requests"() {
        given: "product with quantity 4"
        uuidProvider.generateUuid() >> UUID.randomUUID()
        inventoryRepository.update { inventory ->
            inventory.fill(productId, new ProductQuantity(32))
        }

        def items = new HashMap()
        items[productId.value] = 4

        when:
        def writerFutures = (1..100).collect {
            CompletableFuture.runAsync {
                try {
                    useCase.execute(new PurchaseRequestDto(items))
                } catch (Exception ignored) {
                }
            }
        }

        def readerFuture = CompletableFuture.runAsync {
            100.times {
                assert inventoryRepository.load().currentState()[productId].value >= 0
            }
        }

        CompletableFuture
                .allOf((writerFutures + readerFuture) as CompletableFuture[])
                .get(5, TimeUnit.SECONDS)

        then:
        noExceptionThrown()
    }
}

