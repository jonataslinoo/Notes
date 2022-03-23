package br.com.linoo.notes.retrofit.webclient

import br.com.linoo.notes.model.Note
import br.com.linoo.notes.retrofit.AppRetrofit
import br.com.linoo.notes.retrofit.CallResponse
import br.com.linoo.notes.retrofit.service.NoteService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoteWebClient(private val service: NoteService = AppRetrofit().noteService) {

    fun buscaTodas(): CallResponse? {
        return service.buscaTodas().execute().body()
    }

    fun salva(note: Note): Note? {
        return service.salva(note.titulo, note.texto).execute().body()
    }

    fun edita(note: Note): Note? {
        return service.edita(note.id, note.titulo, note.texto).execute().body()
    }

    fun remove(note: Note): Void? {
        return service.remove(note.id).execute().body()
    }
}
