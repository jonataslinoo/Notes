package br.com.linoo.notes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.linoo.notes.database.dao.NoteDAO
import br.com.linoo.notes.model.Note
import br.com.linoo.notes.retrofit.webclient.NoteWebClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class NoteRepository(
    private val dao: NoteDAO,
    private val webClient: NoteWebClient
) {

    fun buscaTodas(): LiveData<Resource<List<Note>?>> {
        val mediador = MediatorLiveData<Resource<List<Note>?>>()
        mediador.addSource(dao.buscaTodas()) { mediador.value = Resource(dado = it) }
        return mediador
    }

    fun buscaFavoritas(): LiveData<Resource<List<Note>?>> {
        val mediador = MediatorLiveData<Resource<List<Note>?>>()
        mediador.addSource(dao.buscaFavoritas()) { mediador.value = Resource(dado = it) }
        return mediador
    }

    fun buscaPorId(noteId: Long) = dao.buscaPorId(noteId)

    fun salva(note: Note, job: Job = Job()): LiveData<Resource<Void?>> {
        return MutableLiveData<Resource<Void?>>().also { livedata ->
            CoroutineScope(IO + job).launch {
                val resource: Resource<Void?> = try {
                    dao.salva(note)
                    Resource(dado = null)
                } catch (e: IOException) {
                    Resource(dado = null, erro = e.message)
                }
                livedata.postValue(resource)
            }
        }
    }

    fun remove(note: Note, job: Job = Job()): LiveData<Resource<Void?>> {
        return MutableLiveData<Resource<Void?>>().also { liveData ->
            CoroutineScope(IO + job).launch {
                val resource: Resource<Void?> = try {
                    dao.remove(note)
                    Resource(dado = null)
                } catch (e: IOException) {
                    Resource(dado = null, erro = e.message)
                }
                liveData.postValue(resource)
            }
        }
    }
}