package com.kkuzmin.drugstoremachine.application.dto

import java.math.BigDecimal

data class ProductResponseDto(val id: Int,
                              val name: String,
                              val group: String,
                              val price: BigDecimal,
                              val quantity: Int)