package ru.skypaws.mobileapp.data.model.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.skypaws.mobileapp.data.model.dto.LogbookDto
import ru.skypaws.mobileapp.domain.model.YearMonth

@Entity
data class LogbookEntity(
    @PrimaryKey
    @Embedded
    val yearAndMonth: LogbookPrimaryKey,
    val timeFlight: Int,
    val timeBlock: Int,
    val timeNight: Int,
    val timeBiologicalNight: Int,
    val timeWork: Int,
    val type: Int
) {
    fun toDto(): LogbookDto = LogbookDto(
        yearAndMonth.toDto().year,
        yearAndMonth.toDto().month,
        timeFlight,
        timeBlock,
        timeNight,
        timeBiologicalNight,
        timeWork,
        type
    )

    companion object {
        fun fromDto(dto: LogbookDto): LogbookEntity = with(dto) {
            LogbookEntity(
                LogbookPrimaryKey.fromDto(YearMonth(year, month)),
                timeFlight,
                timeBlock,
                timeNight,
                timeBiologicalNight,
                timeWork,
                type
            )
        }
    }

}

data class LogbookPrimaryKey(
    val year: Int,
    val month: Int,
) {
    fun toDto() = YearMonth(year, month)

    companion object {
        fun fromDto(dto: YearMonth) = dto.let {
            LogbookPrimaryKey(it.year, it.month)
        }
    }
}

fun List<LogbookEntity>.toDto(): List<LogbookDto> = map(LogbookEntity::toDto)
fun List<LogbookDto>.toEntity(): List<LogbookEntity> = map(LogbookEntity.Companion::fromDto)