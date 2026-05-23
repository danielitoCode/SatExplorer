package com.elitec.satexplorer.feature.auth.data.dto

import com.elitec.satexplorer.feature.auth.domain.entity.AuthSession

object ClerkAuthMapper {

    fun toDomain(dto: ClerkAuthResponseDto): AuthSession = AuthSession(
        userId = dto.userId,
        username = dto.username,
        email = dto.email,
        sessionId = dto.sessionId,
        provider = dto.provider
    )
}