package com.elitec.satexplorer.feature.auth.domain.entity

data class User(
    val id: Long,
    val userName: String,
    val email: String,
    val passHash: String,
    val photoUrl: String,
    val rank: UserRank,
    val accountState: AccountState,
    val settingsConfiguration: SystemSettingsConfiguration
)
