package br.com.linoo.notes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,
    val data: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val favorita: Boolean = false,
    var removida: Boolean = false
)