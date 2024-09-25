package br.com.linoo.notes.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.linoo.notes.model.Note
import br.com.linoo.notes.repository.NoteRepository
import br.com.linoo.notes.repository.Resource

class ListaNotesViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    fun buscaTodas(): LiveData<Resource<List<Note>?>> {
        return repository.buscaTodas()
    }
}