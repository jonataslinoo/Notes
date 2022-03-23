package br.com.linoo.notes.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.linoo.notes.model.Note
import br.com.linoo.notes.repository.NoteRepository
import br.com.linoo.notes.repository.Resource
import kotlinx.coroutines.Job

class FormularioNoteViewModel(private val repository: NoteRepository) : ViewModel() {

    private val job = Job()

    fun salva(note: Note): LiveData<Resource<Void?>> {
        return if (note.id > 0) {
            repository.edita(note, job)
        } else {
            repository.salva(note, job)
        }
    }

    fun buscaPorId(noteId: Long) = repository.buscaPorId(noteId)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}