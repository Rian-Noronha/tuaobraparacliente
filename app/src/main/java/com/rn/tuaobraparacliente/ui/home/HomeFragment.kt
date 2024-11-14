package com.rn.tuaobraparacliente.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.rn.tuaobraparacliente.R
import com.rn.tuaobraparacliente.adapter.DemandaAdapter
import com.rn.tuaobraparacliente.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var demandasAdapter: DemandaAdapter
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        auth = FirebaseAuth.getInstance()
        binding.fabDemanda.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_home_to_cadastroDemandaFragment)
        }

        configurarRecyclerView()

        val currentUser = auth.currentUser
        val clienteEmail = currentUser?.email

        homeViewModel.fetchDemandas(clienteEmail.toString())

        homeViewModel.demandas.observe(viewLifecycleOwner) { demandas ->
            demandasAdapter.updateDemandas(demandas)
        }

        homeViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }


    private fun configurarRecyclerView(){
        demandasAdapter = DemandaAdapter(emptyList())

        binding.recyclerDemanda.layoutManager = LinearLayoutManager(context)
        binding.recyclerDemanda.adapter = demandasAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}