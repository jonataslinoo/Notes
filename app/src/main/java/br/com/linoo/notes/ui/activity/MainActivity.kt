package br.com.linoo.notes.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.linoo.notes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defineContentViewBinding()
    }

    private fun defineContentViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}