package br.com.linoo.notes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.linoo.notes.database.dao.NoteDAO
import br.com.linoo.notes.model.Note
import br.com.linoo.notes.retrofit.webclient.NoteWebClient
import kotlinx.coroutines.*
import java.io.IOException

class NoteRepository(
    private val dao: NoteDAO,
    private val webClient: NoteWebClient
) {

    fun salva(note: Note, job: Job = Job()): LiveData<Resource<Void?>> {
        return MutableLiveData<Resource<Void?>>().also { livedata ->
            val scope = CoroutineScope(Dispatchers.IO + job)
            scope.launch {
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

    fun edita(note: Note, job: Job = Job()): LiveData<Resource<Void?>> {
        return MutableLiveData<Resource<Void?>>().also { livedata ->
            val scope = CoroutineScope(Dispatchers.IO + job)
            scope.launch {
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

    private val mediador = MediatorLiveData<Resource<List<Note>?>>()

    fun buscaTodas(): LiveData<Resource<List<Note>?>> {

//        mediador.addSource(dao.buscaTodas()) { mediador.value = Resource(dado = it) }
        mediador.addSource(dao.buscaTodas()) { mediador.value = Resource(dado = it) }

        val falhasRetornoApiLiveData = MutableLiveData<Resource<List<Note>?>>()
        mediador.addSource(falhasRetornoApiLiveData) { resourceDeFalha ->
            val resourceAtual = mediador.value

            mediador.value =
                criaResourceDeFalha(resourceAtual = resourceAtual, erro = resourceDeFalha.erro)
        }

        buscaNaApi(quandoFalha = { erro ->
            falhasRetornoApiLiveData.value = Resource(dado = null, erro = erro)
        })

        return mediador
    }

    fun buscaPorId(noteId: Long) = dao.buscaPorId(noteId)

    //    fun salva(note: Note, job: Job = Job()) = salvaNaApi(note, job)
//    fun edita(note: Note, job: Job = Job()) = editaNaApi(note, job)
    fun remove(note: Note) = removeNaApi(note)

    private fun buscaNaApi(quandoFalha: (erro: String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                webClient.buscaTodas()?.let { dao.salva(it.items) }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    quandoFalha(e.message)
                }
            }
        }
    }

    private fun salvaNaApi(note: Note, job: Job): LiveData<Resource<Void?>> {
        return MutableLiveData<Resource<Void?>>().also { livedata ->
            val scope = CoroutineScope(Dispatchers.IO + job)
            scope.launch {
                val resource: Resource<Void?> = try {
                    webClient.salva(note)?.let { noteSalva -> dao.salva(noteSalva) }
                    Resource(dado = null)
                } catch (e: IOException) {
                    Resource(dado = null, erro = e.message)
                }
                livedata.postValue(resource)
            }
        }
    }

    private fun editaNaApi(note: Note, job: Job): LiveData<Resource<Void?>> {
        return MutableLiveData<Resource<Void?>>().also { liveData ->
            CoroutineScope(Dispatchers.IO + job).launch {
                val resource: Resource<Void?> = try {
                    webClient.edita(note)?.let { noteEditada -> dao.salva(noteEditada) }
                    Resource(dado = null)
                } catch (e: IOException) {
                    Resource(dado = null, erro = e.message)
                }
                liveData.postValue(resource)
            }
        }
    }

    private fun removeNaApi(note: Note): LiveData<Resource<Void?>> {
        return MutableLiveData<Resource<Void?>>().also { liveData ->
            CoroutineScope(Dispatchers.IO).launch {
                val resource: Resource<Void?> = try {
                    webClient.remove(note).let { dao.remove(note) }
                    Resource(dado = null)
                } catch (e: IOException) {
                    Resource(dado = null, erro = e.message)
                }
                liveData.postValue(resource)
            }
        }
    }
}