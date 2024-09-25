package br.com.linoo.notes.ui.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import br.com.linoo.notes.R
import br.com.linoo.notes.databinding.ActivityMainBinding
import br.com.linoo.notes.ui.viewmodel.AppViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val LISTA_FAVORITAS = 2131230947

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val controlador by lazy {
        findNavController(R.id.main_activity_nav_host)
    }
    private val viewModel: AppViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defineContentViewBinding()
    }

    private fun defineContentViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controlador.addOnDestinationChangedListener { controller, destination, arguments ->

            title = destination.label
            viewModel.componentes.observe(this, Observer {
                it?.let { temComponentes ->
                    if (temComponentes.bottomNavigation) {
                        binding.mainActivityBottomNavigation.visibility = VISIBLE
                    } else {
                        binding.mainActivityBottomNavigation.visibility = GONE
                    }
                }
            })
        }

//        viewModel.updateSelectedMenuId(binding.mainActivityBottomNavigation.selectedItemId)
        viewModel.updateSelectedMenuId(binding.mainActivityBottomNavigation.selectedItemId)

        binding.mainActivityBottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            viewModel.updateSelectedMenuId(menuItem.itemId)

//            if (menuItem.itemId == LISTA_FAVORITAS) controlador.popBackStack()
//            if (menuItem.itemId == R.id.listaNotesFavoritas) controlador.popBackStack()

            menuItem.onNavDestinationSelected(controlador)
        }
    }
}
