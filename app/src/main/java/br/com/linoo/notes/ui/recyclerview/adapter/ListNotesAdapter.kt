package br.com.linoo.notes.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.linoo.notes.R
import br.com.linoo.notes.databinding.ItemNoteBinding
import br.com.linoo.notes.model.Note

class ListNotesAdapter(
    private val context: Context,
    private val notes: MutableList<Note> = mutableListOf(),
    var quandoItemClicado: (note: Note) -> Unit = {}
) : RecyclerView.Adapter<ListNotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewCriada = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return ViewHolder(viewCriada)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.vincula(note)
    }

    override fun getItemCount() = notes.size

    fun atualiza(notes: List<Note>) {
        notifyItemRangeRemoved(0, this.notes.size)
        this.notes.clear()
        this.notes.addAll(notes)
        notifyItemRangeInserted(0, this.notes.size)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemNoteBinding.bind(itemView)

        private lateinit var note: Note

        init {
            itemView.setOnClickListener {
                if (::note.isInitialized) {
                    quandoItemClicado(note)
                }
            }
        }

        fun vincula(note: Note) {
            this.note = note
            binding.itemNoteTitulo.text = note.titulo
            binding.itemNoteTexto.text = note.texto
            binding.itemNoteData.text = note.data
        }
    }
}