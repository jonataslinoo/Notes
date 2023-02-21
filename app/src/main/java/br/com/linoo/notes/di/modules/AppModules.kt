package br.com.linoo.notes.di.modules

import br.com.linoo.notes.database.AppDatabase
import br.com.linoo.notes.database.dao.NoteDAO
import br.com.linoo.notes.repository.NoteRepository
import br.com.linoo.notes.retrofit.webclient.NoteWebClient
import br.com.linoo.notes.ui.viewmodel.FormularioNoteViewModel
import br.com.linoo.notes.ui.viewmodel.ListaNotesViewModel
import br.com.linoo.notes.ui.viewmodel.VisualizaNoteViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dbModule = module {
    single<AppDatabase> {
        AppDatabase.instancia(get())
    }
}

val repositoryModule = module {
    single<NoteRepository> { NoteRepository(get(), get()) }
}

val daoModule = module {
    single<NoteDAO> { get<AppDatabase>().noteDAO() }
}

val webClientModule = module {
    single<NoteWebClient> { NoteWebClient() }
}

val viewModelModule = module {
    viewModel<ListaNotesViewModel> { ListaNotesViewModel(get()) }
    viewModel<VisualizaNoteViewModel> { (id: Long) -> VisualizaNoteViewModel(id, get()) }
    viewModel<FormularioNoteViewModel> { FormularioNoteViewModel(get()) }
}

val appModules = listOf(
    dbModule,
    daoModule,
    repositoryModule,
    webClientModule,
    viewModelModule
)
