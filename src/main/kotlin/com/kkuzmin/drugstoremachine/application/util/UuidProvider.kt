package com.kkuzmin.drugstoremachine.application.util

import java.util.UUID

interface UuidProvider {
    fun generateUuid(): UUID
}