package br.com.linoo.notes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.linoo.notes.database.dao.NoteDAO
import br.com.linoo.notes.model.Note

private const val NOME_BANCO_DE_DADOS = "notes.db"

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val noteDAO: NoteDAO

    companion object {

        private lateinit var db: AppDatabase

        fun getInstance(context: Context): AppDatabase {

            if (::db.isInitialized) return db

            db = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                NOME_BANCO_DE_DADOS
            ).build()

            return db
        }
    }
}
