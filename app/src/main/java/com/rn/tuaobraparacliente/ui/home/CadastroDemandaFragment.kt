package com.rn.tuaobraparacliente.ui.home
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rn.tuaobraparacliente.databinding.FragmentCadastroDemandaBinding

class CadastroDemandaFragment : Fragment() {

    private var _binding: FragmentCadastroDemandaBinding? = null
    private val binding get() = _binding!!
    private lateinit var cadastroDemandaViewModel: CadastroDemandaViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCadastroDemandaBinding.inflate(inflater, container, false)

        cadastroDemandaViewModel = ViewModelProvider(this).get(CadastroDemandaViewModel::class.java)


        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}