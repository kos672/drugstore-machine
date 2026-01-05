package com.kkuzmin.drugstoremachine.interfaces.rest

import com.kkuzmin.drugstoremachine.application.dto.PurchaseRequestDto
import com.kkuzmin.drugstoremachine.application.usecase.BuyProductsUseCase
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class PurchaseController(
    val buyProductsUseCase: BuyProductsUseCase
) {

    @PostMapping("/purchase")
    fun buy(@RequestBody request: PurchaseRequestDto) = buyProductsUseCase.execute(request)

}