package ru.skypaws.mobileapp.data.mapper

import ru.skypaws.mobileapp.data.model.dto.PricesDto
import ru.skypaws.mobileapp.data.model.dto.UpdatesDto
import ru.skypaws.mobileapp.domain.model.Prices
import ru.skypaws.mobileapp.domain.model.Updates

fun UpdatesDto.toDomain(): Updates {
    return Updates(
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