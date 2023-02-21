package br.com.linoo.notes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Note(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") val id: Long = 0,
    @SerializedName("date") val data: String = "",
    @SerializedName("title") val titulo: String = "",
    @SerializedName("message") val descricao: String = ""
)