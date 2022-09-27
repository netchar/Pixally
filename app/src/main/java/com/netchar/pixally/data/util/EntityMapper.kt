package com.netchar.pixally.data.util

interface EntityMapper<NetworkEntity, DatabaseEntity, DomainEntity> {
    fun NetworkEntity.toEntity(): DatabaseEntity
    fun DatabaseEntity.toDomain(databaseEntity: DatabaseEntity): DomainEntity
}