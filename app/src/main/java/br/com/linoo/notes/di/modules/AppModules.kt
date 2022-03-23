package br.com.linoo.notes.di.modules

import androidx.room.Room
import br.com.linoo.notes.database.AppDatabase
import br.com.linoo.notes.database.dao.NoteDAO
import br.com.linoo.notes.repository.NoteRepository
import br.com.linoo.notes.retrofit.webclient.NoteWebClient
import br.com.linoo.notes.ui.viewmodel.FormularioNoteViewModel
import br.com.linoo.notes.ui.viewmodel.ListaNotesViewModel
import br.com.linoo.notes.ui.viewmodel.VisualizaNoteViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val NOME_BANCO_DE_DADOS = "notes.db"

val appModules = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            NOME_BANCO_DE_DADOS
        ).build()
    }
    single<NoteDAO> {
        get<AppDatabase>().noteDAO
    }
    single<NoteWebClient> {
        NoteWebClient()
    }
    single<NoteRepository> {
        NoteRepository(get(), get())
    }
    viewModel<ListaNotesViewModel> {
        ListaNotesViewModel(get())
    }
    viewModel<VisualizaNoteViewModel> { (id: Long) ->
        VisualizaNoteViewModel(id, get())
    }
    viewModel<FormularioNoteViewModel> {
        FormularioNoteViewModel(get())
    }
}