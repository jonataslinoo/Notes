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
import br.com.linoo.notes.ui.viewmodel.AppViewModel
import br.com.linoo.notes.ui.viewmodel.ComponentesVisuais
import br.com.linoo.notes.ui.viewmodel.ListaNotesViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val MENSAGEM_FALHA_CARREGAR_NOTES = "Não foi possível carregar as anotações"

class ListaNotesFragment : Fragment() {

    private val viewModel: ListaNotesViewModel by viewModel()
    private val listaNotasAdapter by lazy {
        context?.let {
            ListNotesAdapter(context = it)
        } ?: throw IllegalArgumentException("Contexto Inválido")
    }
    private val appViewModel: AppViewModel by activityViewModel<AppViewModel>()

    private lateinit var viewDataBinding: ListaNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        buscaNotes()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = ListaNotesBinding.inflate(inflater, container, false)
        viewDataBinding.vaiParaFormularioInsercao = View.OnClickListener {
            insereNovaNote()
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appViewModel.temComponentes = ComponentesVisuais(bottomNavigation = true)
        configuraRecyclerView()
        buscaNotes()
    }

    private fun buscaNotes() {
        viewModel.buscaTodas().observe(viewLifecycleOwner, Observer { resource ->
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
                navigate(ListaNotesFragmentDirections.acaoListaNotesParaVisualizaNote(noteSelected.id))
            }
        }
    }

    private fun insereNovaNote() {
        transacaoNavController {
            navigate(ListaNotesFragmentDirections.acaoListaNotesParaFormularioNote(0))
        }
    }
}