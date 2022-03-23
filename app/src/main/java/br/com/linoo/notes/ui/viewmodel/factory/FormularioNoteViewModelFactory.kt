package br.com.linoo.notes.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.linoo.notes.repository.NoteRepository
import br.com.linoo.notes.ui.viewmodel.FormularioNoteViewModel

class FormularioNoteViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FormularioNoteViewModel(repository) as T
    }
}