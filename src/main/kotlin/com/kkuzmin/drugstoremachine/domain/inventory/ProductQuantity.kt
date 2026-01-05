package com.kkuzmin.drugstoremachine.domain.inventory

data class ProductQuantity(val value: Int) {

    init {
        require(value >= 0)
    }

    operator fun plus(other: ProductQuantity) = ProductQuantity(this.value + other.value)

    operator fun minus(other: ProductQuantity) = ProductQuantity(this.value - other.value)

    operator fun compareTo(other: ProductQuantity): Int = this.value.compareTo(other.value)

    companion object {
        fun zero() = ProductQuantity(0)
    }
}
