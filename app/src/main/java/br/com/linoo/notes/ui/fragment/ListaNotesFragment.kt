package br.com.linoo.notes.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import br.com.linoo.notes.databinding.ListaNotesBinding
import br.com.linoo.notes.ui.fragment.extensions.mostraMensagem
import br.com.linoo.notes.ui.fragment.extensions.transacaoNavController
import br.com.linoo.notes.ui.recyclerview.adapter.ListNotesAdapter
import br.com.linoo.notes.ui.viewmodel.ListaNotesViewModel
import org.koin.android.viewmodel.ext.android.viewModel

private const val MENSAGEM_FALHA_CARREGAR_NOTES = "Não foi possível carregar as anotações"
private const val TITULO_APPBAR = "Anotações"

class ListaNotesFragment : Fragment() {
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
        binding.listaNotesRecyclerview.adapter = adapter
        configuraAdapter()
    }

    private fun configuraFabAdicionaNote() {
        binding.listaNotesFabSalvaNote.setOnClickListener {
            transacaoNavController {
                transacaoNavController {
                    navigate(ListaNotesFragmentDirections.acaoListaNotesParaFormularioNote(0))
                }
            }
        }
    }

    private fun configuraAdapter() {
        adapter.quandoItemClicado = { noteSelected ->
            transacaoNavController {
                navigate(ListaNotesFragmentDirections.acaoListaNotesParaVisualizaNote(noteSelected.id))
            }
            adapter.quandoStarClicado = { noteId ->
                mostraMensagem("teste clique favorito")
            }
        }
    }
}