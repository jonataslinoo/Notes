package br.com.linoo.notes.ui.databinding

import androidx.lifecycle.MutableLiveData
import br.com.linoo.notes.model.Note

class NoteData(
    private var note: Note = Note(),
    val titulo: MutableLiveData<String> = MutableLiveData<String>().also { it.value = note.titulo },
    val descricao: MutableLiveData<String> = MutableLiveData<String>().also {
        it.value = note.descricao
    }
//    val favorita: MutableLiveData<Boolean> = MutableLiveData<Boolean>().also { it.value = note.favorita }
) {
    fun atualiza(note: Note) {
        this.note = note
        titulo.postValue(this.note.titulo)
        descricao.postValue(this.note.descricao)
//        favorita.postValue(this.note.favorita)

    }

    fun paraNote(): Note? {
        return this.note.copy(
            titulo = titulo.value ?: return null,
            descricao = descricao.value ?: return null
//            favorita = favorita.valuew ?: return null
        )
    }
}