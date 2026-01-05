package com.kkuzmin.drugstoremachine.config

import com.kkuzmin.drugstoremachine.domain.product.Money
import com.kkuzmin.drugstoremachine.domain.product.Product
import com.kkuzmin.drugstoremachine.domain.product.ProductGroup
import com.kkuzmin.drugstoremachine.domain.product.ProductId
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class PrefilledInventoryConfig {

    @PostConstruct
    fun init(): Map<Int, Product> =
        mapOf(
            Pair(1, Product(ProductId(1), "Balea shower gel", ProductGroup.PERSONAL_HYGIENE, Money(BigDecimal("0.55")))),
            Pair(2, Product(ProductId(2), "SEINZ beard oil", ProductGroup.FACE, Money(BigDecimal("7.95")))),
            Pair(3, Product(ProductId(3), "Balea body lotion", ProductGroup.PERSONAL_HYGIENE, Money(BigDecimal("0.75")))),
            Pair(4, Product(ProductId(4), "Fotoparadies USB stick 16GB", ProductGroup.PHOTO, Money(BigDecimal("6.95")))),
            Pair(5, Product(ProductId(5), "Nivea night cream Q10", ProductGroup.FACE, Money(BigDecimal("15.95")))),
            Pair(6, Product(ProductId(6), "Kinderhose (one-size)", ProductGroup.TEXTILE, Money(BigDecimal("9.90")))),
            Pair(7, Product(ProductId(7), "All-purpose cleaner Power degreaser", ProductGroup.HOUSEHOLD, Money(BigDecimal("1.75")))),
            Pair(8, Product(ProductId(8), "Profissimo Dirt eraser", ProductGroup.HOUSEHOLD, Money(BigDecimal("2.25"))))
        )

}