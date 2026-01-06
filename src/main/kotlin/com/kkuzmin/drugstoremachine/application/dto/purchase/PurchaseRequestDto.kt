package com.kkuzmin.drugstoremachine.application.dto.purchase

data class PurchaseRequestDto(val items: Map<
        /* productId */ Int,
        /* amount */Int>)