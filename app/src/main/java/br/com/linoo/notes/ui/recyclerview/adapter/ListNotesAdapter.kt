package br.com.linoo.notes.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.linoo.notes.R
import br.com.linoo.notes.databinding.ItemNoteBinding
import br.com.linoo.notes.extensions.formataDataHora
import br.com.linoo.notes.model.Note

class ListNotesAdapter(
    private val context: Context,
    private val notes: MutableList<Note> = mutableListOf(),
    var quandoItemClicado: (note: Note) -> Unit = {},
    var quandoStarClicado: (noteId: Long) -> Unit = {}
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
        this.notes.clear()
        this.notes.addAll(notes)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemNoteBinding.bind(itemView)

        private lateinit var note: Note

        init {
            binding.itemNotePrincipalCardview.setOnClickListener {
                if (::note.isInitialized) {
                    quandoItemClicado(note)
                }
            }

            binding.itemNoteStar.setOnClickListener {
                if (::note.isInitialized) {
                    it.setBackgroundResource(R.drawable.ic_star_blue)
                    quandoStarClicado(note.id)
                }
            }
        }

        fun vincula(note: Note) {
            this.note = note
            binding.itemNoteTitulo.text = note.titulo
            binding.itemNoteTexto.text = note.texto
            binding.itemNoteData.text = note.data.formataDataHora()
        }
    }
}