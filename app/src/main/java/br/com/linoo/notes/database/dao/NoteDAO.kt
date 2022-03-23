package br.com.linoo.notes.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import br.com.linoo.notes.model.Note

@Dao
interface NoteDAO {

    @Query("SELECT * FROM Note ORDER BY id DESC")
    fun buscaTodas(): LiveData<List<Note>>

    @Insert(onConflict = REPLACE)
    fun salva(note: Note)

    @Delete
    fun remove(note: Note)

    @Query("SELECT *FROM Note WHERE id = :id")
    fun buscaPorId(id: Long): LiveData<Note?>

    @Insert(onConflict = REPLACE)
    fun salva(notes: List<Note>)
}
