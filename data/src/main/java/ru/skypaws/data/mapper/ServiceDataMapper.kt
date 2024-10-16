package ru.skypaws.data.mapper

import ru.skypaws.data.source.dto.PricesDto
import ru.skypaws.data.source.dto.UpdatesDto
import ru.skypaws.domain.model.Prices
import ru.skypaws.domain.model.Updates

fun UpdatesDto.toDomain(): Updates {
    return Updates(
        version = this.version,
        revision = this.revision,
    )
}

fun Updates.fromDomain(): UpdatesDto {
    return UpdatesDto(
        version = this.version,
        revision = this.revision,
    )
}

fun PricesDto.toDomain(): Prices {
    return Prices(
        logbook = this.logbook,
        sync_month = this.sync_month,
        sync_quarter = this.sync_quarter,
        sync_year = this.sync_year
    )
}

fun Prices.fromDomain(): PricesDto {
    return PricesDto(
        logbook = this.logbook,
        sync_month = this.sync_month,
        sync_quarter = this.sync_quarter,
        sync_year = this.sync_year
    )
}