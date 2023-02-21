package br.com.linoo.notes.extensions

import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private val FORMATO_DATA_INICIAL = "E MMM d H:m:s O u"
private val FORMATO_DATA_FINAL = "dd/MM/yyyy HH:mm"

fun String.formataDataHora(): String {

    return try {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val parsed = ZonedDateTime.parse(this, formatter)
        val formatoSaida = DateTimeFormatter.ofPattern(FORMATO_DATA_FINAL)
        formatoSaida.format(parsed)

    } catch (e: Exception) {
//        val dtf = DateTimeFormatter.ofPattern(FORMATO_DATA_INICIAL, Locale.ENGLISH)
//        val odt: OffsetDateTime = OffsetDateTime.parse(this, dtf)
//        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
//        val parsed = ZonedDateTime.parse(odt.toString(), formatter)
//        val formatoSaida = DateTimeFormatter.ofPattern(FORMATO_DATA_FINAL)
//        formatoSaida.format(parsed)
        ""
    }
}

fun String.salvaDataFormatada(): String {
    return try {
        val dtf = DateTimeFormatter.ofPattern(FORMATO_DATA_INICIAL, Locale.ENGLISH)
        val odt: OffsetDateTime = OffsetDateTime.parse(this, dtf)
        odt.toString()
    } catch (e: Exception) {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val parsed = ZonedDateTime.parse(this, formatter)
        parsed.toString()
    }
}
