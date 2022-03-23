package br.com.linoo.notes.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.linoo.notes.repository.NoteRepository
import br.com.linoo.notes.ui.viewmodel.VisualizaNoteViewModel

class VisualizaNoteViewModelFactory(
    private val id: Long,
    private val repository: NoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VisualizaNoteViewModel(id, repository) as T
    }
}