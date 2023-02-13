package br.com.linoo.notes.extensions

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun String.formataDataHora(): String {
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val parsed = ZonedDateTime.parse(this, formatter)
    val formatoSaida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return formatoSaida.format(parsed)
}
