package br.com.linoo.notes.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import br.com.linoo.notes.R
import br.com.linoo.notes.databinding.VisualizaNoteBinding
import br.com.linoo.notes.model.Note
import br.com.linoo.notes.ui.fragment.extensions.mostraMensagem
import br.com.linoo.notes.ui.fragment.extensions.transacaoNavController
import br.com.linoo.notes.ui.viewmodel.AppViewModel
import br.com.linoo.notes.ui.viewmodel.ComponentesVisuais
import br.com.linoo.notes.ui.viewmodel.VisualizaNoteViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val NOTE_NAO_ENCONTRADA = "Anotação não encontrada!"
private const val MENSAGEM_FALHA_REMOCAO = "Não foi possível remover a anotação!"
private const val MENSAGEM_SUCESSO_REMOCAO = "Anotação removida com sucesso!"
private const val TITULO_APPBAR = "Anotação"

class VisualizaNoteFragment : Fragment() {

    private val arguments by navArgs<VisualizaNoteFragmentArgs>()
    private val noteId: Long by lazy { arguments.noteId }
    private val viewModel: VisualizaNoteViewModel by viewModel { parametersOf(noteId) }
    private lateinit var viewDataBinding: VisualizaNoteBinding
    private val appViewModel: AppViewModel by activityViewModel<AppViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = VisualizaNoteBinding.inflate(inflater, container, false)
        viewDataBinding.lifecycleOwner = this
        return viewDataBinding.root
    }

    override fun onResume() {
        super.onResume()
        verificaIdDaNote()
        buscaNoteSelecionada()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appViewModel.temComponentes = ComponentesVisuais()
        configfuraMenu()
    }

    private fun configfuraMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.visualiza_note_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.visualiza_note_menu_edita -> {
                        vaiParaEdicaoFormulario()
                        true
                    }

                    R.id.visualiza_note_menu_remove -> {
                        viewModel.buscaPorId(noteId).observe(viewLifecycleOwner, Observer {
                            it?.run {
                                removida = true
                                remove(this)
                            }
                        })
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun vaiParaEdicaoFormulario() {
        viewModel.buscaPorId(noteId).observe(this, Observer {
            it?.let { noteEncontrada ->
                transacaoNavController {
                    navigate(
                        VisualizaNoteFragmentDirections.acaoVisualizaNoteParaFormularioNote(
                            noteEncontrada.id
                        )
                    )
                }
            }
        })
    }

    private fun remove(note: Note) {
        viewModel.remove(note).observe(this, Observer { resource ->
            if (resource.erro == null) {
                endFragment()
                mostraMensagem(MENSAGEM_SUCESSO_REMOCAO)
            } else {
                mostraMensagem(MENSAGEM_FALHA_REMOCAO)
            }
        })
    }

    private fun buscaNoteSelecionada() {
        viewModel.buscaPorId(noteId).observe(this, Observer { noteFind ->
            noteFind?.let {
                viewDataBinding.note = it
            }
        })
    }

    private fun verificaIdDaNote() {
        if (!temIdValido()) {
            mostraMensagem(NOTE_NAO_ENCONTRADA)
            endFragment()
        }
    }

    private fun temIdValido() = noteId != 0L

    private fun endFragment() {
        transacaoNavController {
            popBackStack()
        }
    }
}