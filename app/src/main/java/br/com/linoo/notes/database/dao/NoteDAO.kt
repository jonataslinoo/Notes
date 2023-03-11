package br.com.linoo.notes.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import br.com.linoo.notes.model.Note

@Dao
interface NoteDAO {

    @Query("SELECT * FROM Note WHERE removida = 0 AND favorita = 0 ORDER BY id DESC")
    fun buscaTodas(): LiveData<List<Note>>

    @Query("SELECT * FROM Note WHERE removida = 0 AND favorita = 1 ORDER BY id DESC")
    fun buscaFavoritas(): LiveData<List<Note>>

    @Insert(onConflict = REPLACE)
    fun salva(note: Note)

    @Update
    fun remove(note: Note)

    @Query("SELECT * FROM Note WHERE id = :id")
    fun buscaPorId(id: Long): LiveData<Note?>

    @Insert(onConflict = REPLACE)
    fun salva(notes: List<Note>)
}
