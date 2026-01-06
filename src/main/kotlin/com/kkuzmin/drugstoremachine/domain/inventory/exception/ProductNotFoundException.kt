package com.kkuzmin.drugstoremachine.domain.inventory.exception

import com.kkuzmin.drugstoremachine.domain.product.ProductId

class ProductNotFoundException(productId: ProductId) : RuntimeException("Product with the id ${productId.value} is not found") {
}