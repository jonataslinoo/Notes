package br.com.linoo.notes.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import br.com.linoo.notes.R
import br.com.linoo.notes.databinding.ActivityMainBinding
import br.com.linoo.notes.ui.viewmodel.AppViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val controlador by lazy {
        findNavController(R.id.main_activity_nav_host)
    }
    private val appViewModel: AppViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defineContentViewBinding()
    }

    private fun defineContentViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appViewModel.updateSelectedMenuId(binding.mainActivityBottomNavigation.selectedItemId)

        binding.mainActivityBottomNavigation.setOnNavigationItemSelectedListener {
            appViewModel.updateSelectedMenuId(it.itemId)
            it.onNavDestinationSelected(controlador)
        }
    }
}