package com.kkuzmin.drugstoremachine.interfaces.rest

import com.kkuzmin.drugstoremachine.application.dto.FillInventoryRequest
import com.kkuzmin.drugstoremachine.application.usecase.FillInventoryUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class InventoryController(private val fillInventoryUseCase: FillInventoryUseCase) {

    @PostMapping("/fill")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun fillInventory(@RequestBody request: FillInventoryRequest) = fillInventoryUseCase.execute(request)
}