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
import androidx.navigation.NavDirections
import androidx.navigation.fragment.navArgs
import br.com.linoo.notes.R
import br.com.linoo.notes.databinding.FormularioNoteBinding
import br.com.linoo.notes.model.Note
import br.com.linoo.notes.ui.databinding.NoteData
import br.com.linoo.notes.ui.fragment.extensions.hideKeyboard
import br.com.linoo.notes.ui.fragment.extensions.mostraMensagem
import br.com.linoo.notes.ui.fragment.extensions.transacaoNavController
import br.com.linoo.notes.ui.viewmodel.AppViewModel
import br.com.linoo.notes.ui.viewmodel.ComponentesVisuais
import br.com.linoo.notes.ui.viewmodel.FormularioNoteViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TITULO_APPBAR_EDICAO = "Editando Anotação"
private const val TITULO_APPBAR_CRIACAO = "Criando Anotação"
private const val MENSAGEM_ERRO_SALVAR = "Não foi possível salvar a Anotação"
private const val MENSAGEM_ERRO_CAMPOS_VAZIO = "Não podem haver campos vazios."
private const val LISTA_ANOTACOES = 2131230946
private const val LISTA_FAVORITAS = 2131230947

class FormularioNoteFragment : Fragment() {

    private val arguments by navArgs<FormularioNoteFragmentArgs>()
    private val noteId: Long by lazy { arguments.noteId }
    private val noteData by lazy { NoteData() }
    private val viewModel: FormularioNoteViewModel by viewModel { parametersOf(noteId) }
    private lateinit var viewDataBinding: FormularioNoteBinding
    private val appViewModel: AppViewModel by activityViewModel<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun configuraMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.formulario_note_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.formulario_note_salva -> {
                        val noteCriada = criaNote()
                        noteCriada?.let {
                            if (validaCamposTextoVazio(it.titulo, it.descricao)) {
                                salva(noteCriada)
                            }
                        }
                        hideKeyboard(viewDataBinding.formularioNoteTitulo)
                        hideKeyboard(viewDataBinding.formularioNoteTexto)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FormularioNoteBinding.inflate(inflater, container, false)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.note = noteData
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appViewModel.temComponentes = ComponentesVisuais()
        configuraMenu()
        definindoTitulo()
        preencheFormulario()
    }

    private fun criaNote(): Note? {
        return noteData.paraNote()
    }

    private fun validaCamposTextoVazio(vararg listaCamposTexto: String): Boolean {
        for (campoTexto in listaCamposTexto) {
            if (campoTexto.equals("")) {
                mostraMensagem(MENSAGEM_ERRO_CAMPOS_VAZIO)
                return false
            }
        }

        return true
    }

    private fun definindoTitulo() {
        activity?.title = if (noteId > 0) {
            TITULO_APPBAR_EDICAO
        } else {
            TITULO_APPBAR_CRIACAO
        }
    }

    private fun preencheFormulario() {
        if (temIdValido()) {
            viewModel.buscaPorId(noteId).observe(viewLifecycleOwner, Observer {
                it?.let { noteEncontrada ->
                    noteData.atualiza(noteEncontrada)
                }
            })
        }
    }

    private fun salva(note: Note) {
        viewDataBinding.formularioNoteProgressbar.visibility = View.VISIBLE
        viewModel.salva(note).observe(this, Observer { resource ->
            if (resource.erro == null) {
                endFragment()
            } else {
                mostraMensagem(MENSAGEM_ERRO_SALVAR)
            }
            viewDataBinding.formularioNoteProgressbar.visibility = View.GONE
        })
    }

    private fun temIdValido() = noteId != 0L

    private fun endFragment() {
        appViewModel.selectedMenuId.observe(this, Observer { menuItemId ->
            when (menuItemId) {
                R.id.listaNotes -> {
                    transacaoNavController {
                        navigate(navDirections())
                    }
                }

                R.id.listaNotesFavoritas -> {
                    transacaoNavController {
                        navigate(FormularioNoteFragmentDirections.acaoFormularioNoteParaListaNotesFavoritas())
                    }
                }
            }
        })
    }

    private fun navDirections(): NavDirections =
        FormularioNoteFragmentDirections.acaoFormularioNoteParaListaNotes()
}