package br.com.linoo.notes.ui.databinding

import androidx.lifecycle.MutableLiveData
import br.com.linoo.notes.extensions.salvaDataFormatada
import br.com.linoo.notes.model.Note
import java.util.*

class NoteData(
    private var note: Note = Note(),
    val titulo: MutableLiveData<String> = MutableLiveData<String>().also { it.value = note.titulo },
    val descricao: MutableLiveData<String> = MutableLiveData<String>().also {
        it.value = note.descricao
    },
    val favorita: MutableLiveData<Boolean> = MutableLiveData<Boolean>().also {
        it.value = note.favorita
    }
) {
    fun atualiza(note: Note) {
        this.note = note
        titulo.postValue(this.note.titulo)
        descricao.postValue(this.note.descricao)
        favorita.postValue(this.note.favorita)
    }

    fun paraNote(): Note? {
        if (this.note.id == 0L) {
            return this.note.copy(
                id = Calendar.getInstance().timeInMillis,
                titulo = titulo.value ?: return null,
                descricao = descricao.value ?: return null,
                data = Calendar.getInstance().time.toString().salvaDataFormatada(),
                favorita = favorita.value ?: return null
            )
        }
        return this.note.copy(
            titulo = titulo.value ?: return null,
            descricao = descricao.value ?: return null,
            data = note.data.salvaDataFormatada(),
            favorita = favorita.value ?: return null
        )
    }
}