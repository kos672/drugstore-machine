package com.kkuzmin.drugstoremachine.application.util

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DefaultUuidProvider : UuidProvider {
    override fun generateUuid(): UUID = UUID.randomUUID()
}