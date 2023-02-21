package br.com.linoo.notes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.linoo.notes.database.dao.NoteDAO
import br.com.linoo.notes.model.Note

private const val NOME_BANCO_DE_DADOS = "notes.db"

@Database(
    version = 1,
    entities = [Note::class],
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDAO(): NoteDAO

    companion object {
        @Volatile
        private var db: AppDatabase? = null
        fun instancia(context: Context): AppDatabase {
            return db ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                NOME_BANCO_DE_DADOS
            )
                .build().also {
                    db = it
                }
        }
    }
}