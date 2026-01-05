package com.kkuzmin.drugstoremachine.application.dto

data class PurchaseRequestDto(val items: Map<
        /* productId */ Int,
        /* amount */Int>)