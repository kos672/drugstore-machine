package com.kkuzmin.drugstoremachine.interfaces.rest

import com.kkuzmin.drugstoremachine.application.dto.purchase.PurchaseRequestDto
import com.kkuzmin.drugstoremachine.application.usecase.BuyProductsUseCase
import com.kkuzmin.drugstoremachine.application.usecase.GetPurchaseUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class PurchaseController(
    val buyProductsUseCase: BuyProductsUseCase,
    val getPurchaseUseCase: GetPurchaseUseCase
) {

    @PostMapping("/purchase")
    fun buy(@RequestBody request: PurchaseRequestDto) = buyProductsUseCase.execute(request)

    @GetMapping("purchase")
    fun getPurchases() = getPurchaseUseCase.execute()

}