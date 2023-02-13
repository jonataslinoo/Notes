package br.com.linoo.notes.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import br.com.linoo.notes.databinding.ItemNoteBinding
import br.com.linoo.notes.model.Note

class ListNotesAdapter(
    private val context: Context,
    private val notes: MutableList<Note> = mutableListOf(),
    var quandoItemClicado: (note: Note) -> Unit = {},
) : RecyclerView.Adapter<ListNotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val viewDataBinding = ItemNoteBinding.inflate(inflater, parent, false)
        return ViewHolder(viewDataBinding).also {
            viewDataBinding.lifecycleOwner = it
        }
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

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.marcaComoAtivado()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.marcaComoDesativado()
    }

    inner class ViewHolder(private val viewDataBinding: ItemNoteBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root),
        View.OnClickListener, LifecycleOwner {

        private lateinit var note: Note

        fun vincula(note: Note) {
            this.note = note
            viewDataBinding.nota = note
        }

        init {
            viewDataBinding.clicaNaNota = this
        }

        override fun onClick(view: View?) {
            if (::note.isInitialized) {
                quandoItemClicado(note)
            }
        }

        private val registry = LifecycleRegistry(this)
        override fun getLifecycle(): Lifecycle = registry

        init {
            registry.currentState = Lifecycle.State.INITIALIZED
        }

        fun marcaComoAtivado() {
            registry.currentState = Lifecycle.State.STARTED
        }

        fun marcaComoDesativado() {
            registry.currentState = Lifecycle.State.DESTROYED
        }
    }
}