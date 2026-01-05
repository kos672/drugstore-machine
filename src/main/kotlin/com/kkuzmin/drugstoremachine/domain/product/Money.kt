package com.kkuzmin.drugstoremachine.domain.product

import java.math.BigDecimal
import java.math.RoundingMode

data class Money(val amount: BigDecimal) {

    init {
        require(amount >= BigDecimal.ZERO)
        amount.setScale(2, RoundingMode.HALF_UP)
    }

    operator fun plus(other: Money): Money = Money(this.amount.add(other.amount))

    companion object {
        fun zero() = Money(BigDecimal.ZERO)
    }
}