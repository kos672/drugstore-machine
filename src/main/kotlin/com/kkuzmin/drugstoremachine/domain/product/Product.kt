package com.kkuzmin.drugstoremachine.domain.product

class Product(
    val id: ProductId,
    val name: String,
    val group: ProductGroup,
    val price: Money
)