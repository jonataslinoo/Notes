package br.com.linoo.notes.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.linoo.notes.databinding.ListaNotesBinding
import br.com.linoo.notes.model.Note
import br.com.linoo.notes.ui.fragment.extensions.mostraErro
import br.com.linoo.notes.ui.recyclerview.adapter.ListNotesAdapter
import br.com.linoo.notes.ui.viewmodel.ListaNotesViewModel
import org.koin.android.viewmodel.ext.android.viewModel

private const val MENSAGEM_FALHA_CARREGAR_NOTES = "Não foi possível carregar as anotações"
private const val TITULO_APPBAR = "Anotações"

class ListaNotesFragment : Fragment() {
    var quandoFabSalvaNoteClicada: (note: Note?) -> Unit = {}
    var quandoNoteSelecionada: (note: Note) -> Unit = {}

    private val viewModel: ListaNotesViewModel by viewModel()
    private lateinit var binding: ListaNotesBinding
    private val adapter by lazy {
        context?.let {
            ListNotesAdapter(context = it)
        } ?: throw IllegalArgumentException("Contexto Inválido")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buscaNotes()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListaNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraRecyclerView()
        configuraFabAdicionaNote()
        activity?.title = TITULO_APPBAR

    }

    private fun buscaNotes() {
        viewModel.buscaTodas().observe(this, Observer { resource ->
            resource.dado?.let { adapter.atualiza(it) }
            resource.erro?.let { mostraErro(MENSAGEM_FALHA_CARREGAR_NOTES) }
        })
    }

    private fun configuraRecyclerView() {
        val divisor = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        binding.listaNotesRecyclerview.addItemDecoration(divisor)
        binding.listaNotesRecyclerview.adapter = adapter
        configuraAdapter()
    }

    private fun configuraFabAdicionaNote() {
        binding.listaNotesFabSalvaNote.setOnClickListener {
            quandoFabSalvaNoteClicada(null)
        }
    }

    private fun configuraAdapter() {
        adapter.quandoItemClicado = quandoNoteSelecionada
    }
}