package com.netchar.pixally.data.util

interface EntityMapper<NetworkEntity, DatabaseEntity, DomainEntity> {
    fun NetworkEntity.toEntity(): DatabaseEntity
    fun DatabaseEntity.toDomain(): DomainEntity

    fun List<NetworkEntity>.toEntities(): List<DatabaseEntity> = map { it.toEntity() }
    fun List<DatabaseEntity>.toDomains(): List<DomainEntity> = map { it.toDomain() }
}