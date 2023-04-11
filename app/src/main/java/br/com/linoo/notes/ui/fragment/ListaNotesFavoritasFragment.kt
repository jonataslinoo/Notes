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
import br.com.linoo.notes.ui.viewmodel.ListaNotesFavoritasViewModel
import org.koin.android.viewmodel.ext.android.viewModel

private const val MENSAGEM_FALHA_CARREGAR_NOTES = "Não foi possível carregar as anotações favoritas"
private const val TITULO_APPBAR = "Favoritas"

class ListaNotesFavoritasFragment : Fragment() {
    private val viewModel: ListaNotesFavoritasViewModel by viewModel()
    private val listaNotasAdapter by lazy {
        context?.let {
            ListNotesAdapter(context = it)
        } ?: throw IllegalArgumentException("Contexto Inválido")
    }

    private lateinit var viewDataBinding: ListaNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buscaNotesFavotiras()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = ListaNotesBinding.inflate(inflater, container, false)
        viewDataBinding.listaNotesFabSalvaNote.hide()
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraRecyclerView()
        activity?.title = TITULO_APPBAR
    }

    private fun buscaNotesFavotiras() {
        viewModel.buscaFavoritas().observe(this, Observer { resource ->
            resource.dado?.let { listaNotasAdapter.atualiza(it) }
            resource.erro?.let { mostraMensagem(MENSAGEM_FALHA_CARREGAR_NOTES) }
        })
    }

    private fun configuraRecyclerView() {
        viewDataBinding.listaNotesRecyclerview.run {
            adapter = listaNotasAdapter
        }
        listaNotasAdapter.quandoItemClicado = { noteSelected ->
            transacaoNavController {
                navigate(
                    ListaNotesFavoritasFragmentDirections.acaoListaNotesFavoritasParaVisualizaNote(
                        noteSelected.id
                    )
                )
            }
        }
    }
}