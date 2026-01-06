package com.kkuzmin.drugstoremachine.application.dto.inventory

import com.kkuzmin.drugstoremachine.application.dto.ProductResponseDto

data class InventoryResponseDto(val items: List<ProductResponseDto>)