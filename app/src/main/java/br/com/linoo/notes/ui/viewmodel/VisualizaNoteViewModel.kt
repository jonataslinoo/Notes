package br.com.linoo.notes.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.linoo.notes.repository.NoteRepository
import br.com.linoo.notes.repository.Resource

class VisualizaNoteViewModel(
    id: Long,
    private val repository: NoteRepository
) : ViewModel() {

    val noteEncontrada = repository.buscaPorId(id)

    fun remove(): LiveData<Resource<Void?>> {
        return noteEncontrada.value?.run {
            repository.remove(this)
        } ?: MutableLiveData<Resource<Void?>>().also {
            it.value = Resource(null, "Anotação não encontrada")
        }
    }
}