package br.com.linoo.notes.retrofit.service

import br.com.linoo.notes.model.Note
import br.com.linoo.notes.retrofit.CallResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NoteService {

    @GET("AKfycbyTr2HqQ4FQwCqryEAeMwqxC1GFWhy91WRoHX8gsfvs4kMyKdCVeYDkxsQ3sr1BQHMV/exec")
    fun buscaTodas(): Call<CallResponse>

    @POST("AKfycbzFAPeNDzeD0tipdiGcUtcpiS4IND52IoVd5Pjqp25wk7ZxaE-jVy3OcRPYaDWJlzwj/exec")
    fun salva(
        @Query("title") titulo: String,
        @Query("message") texto: String
    ): Call<Note>

    @POST("AKfycbwOKcCxozEc0ROTFQ3Pt-GvYCaEtAe2tlmO1O1b5POlJv6IrUVl4ofYuj2bk-gvQmCo/exec")
    fun edita(
        @Query("id") id: Long,
        @Query("title") titulo: String,
        @Query("message") texto: String
    ): Call<Note>

    @POST("AKfycbw2FRkg_-_qJIO41QuUJgdHGShE8ex8PsIHqdm5EmRIAlCSAizNCr4bFwk5BRqRNMAi/exec")
    fun remove(@Query("id") id: Long): Call<Void>
}
