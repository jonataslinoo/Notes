package br.com.linoo.notes.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.linoo.notes.R
import br.com.linoo.notes.databinding.ListaNotesBinding
import br.com.linoo.notes.model.Note
import br.com.linoo.notes.ui.activity.ActivityNotes
import br.com.linoo.notes.ui.activity.NOTE_ID_CHAVE
import br.com.linoo.notes.ui.fragment.extensions.mostraMensagem
import br.com.linoo.notes.ui.fragment.extensions.transacaoNavController
import br.com.linoo.notes.ui.recyclerview.adapter.ListNotesAdapter
import br.com.linoo.notes.ui.viewmodel.ListaNotesViewModel
import org.koin.android.viewmodel.ext.android.viewModel

private const val MENSAGEM_FALHA_CARREGAR_NOTES = "Não foi possível carregar as anotações"
private const val TITULO_APPBAR = "Anotações"

class ListaNotesFragment : Fragment() {
    var quandoFabAdicionaNoteClicada: (note: Note?) -> Unit = {}
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
            resource.erro?.let { mostraMensagem(MENSAGEM_FALHA_CARREGAR_NOTES) }
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
//            quandoFabAdicionaNoteClicada(null)
            transacaoNavController {
                navigate(R.id.formularioNote)
            }
        }
    }

    private fun configuraAdapter() {
//        adapter.quandoItemClicado = quandoNoteSelecionada
//        adapter.quandoItemClicado = { produtoSelecionado ->
//            val controller = findNavController()
//            val data = Bundle()
//            data.putLong(NOTE_ID_CHAVE, produtoSelecionado.id)
//            controller.navigate(R.id.visualizaNote, data)
//        }

        adapter.quandoItemClicado = { produtoSelecionado ->
            val data = Bundle()
            data.putLong(NOTE_ID_CHAVE, produtoSelecionado.id)
            transacaoNavController {
                navigate(R.id.visualizaNote, data)
            }
        }
    }
}