package com.example.messengerapp.utils

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter



// chuyen timestamp sang ngay thang
fun Long.toFriendlyTimeString(): String {
    val now = Instant.now()
    val time = Instant.ofEpochMilli(this)
    val duration = Duration.between(time, now)

    val zone = ZoneId.of("Asia/Ho_Chi_Minh")  // hoặc ZoneId.systemDefault()
    val localDateTime = time.atZone(zone)
    val today = LocalDate.now(zone)

    return when {
        duration.toMinutes() < 1 -> "vừa xong"
        duration.toMinutes() < 60 -> "${duration.toMinutes()} phút trước"
        duration.toHours() < 6 -> "${duration.toHours()} giờ trước"
        localDateTime.toLocalDate().isEqual(today) ->
            localDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))

        localDateTime.toLocalDate().isEqual(today.minusDays(1)) -> "Hôm qua"
        else ->
            localDateTime.format(DateTimeFormatter.ofPattern("dd/MM"))
    }
}


