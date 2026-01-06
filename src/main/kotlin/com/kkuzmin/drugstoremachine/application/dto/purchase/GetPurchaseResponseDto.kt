package com.kkuzmin.drugstoremachine.application.dto.purchase

import com.kkuzmin.drugstoremachine.application.dto.ProductResponseDto
import java.math.BigDecimal
import java.util.UUID

data class GetPurchaseResponseDto(val id: UUID, val items: List<ProductResponseDto>, val total: BigDecimal)