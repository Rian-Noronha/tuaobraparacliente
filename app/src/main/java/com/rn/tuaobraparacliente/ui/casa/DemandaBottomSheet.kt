package com.rn.tuaobraparacliente.ui.casa
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.rn.tuaobraparacliente.adapter.DemandaAdapter
import com.rn.tuaobraparacliente.databinding.BottomSheetDemandasBinding
import com.rn.tuaobraparacliente.model.CasaConstrucao
import com.rn.tuaobraparacliente.model.Demanda
import com.rn.tuaobraparacliente.ui.home.HomeViewModel

class DemandaBottomSheet(
    private val casa: CasaConstrucao,
    private val onDemandaSelecionada: (Demanda) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetDemandasBinding
    private lateinit var demandasAdapter: DemandaAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetDemandasBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        configurarRecyclerView()

        val currentUser = auth.currentUser
        val clienteEmail = currentUser?.email

        homeViewModel.fetchDemandas(clienteEmail.toString())

        homeViewModel.demandas.observe(viewLifecycleOwner) { demandas ->
            demandasAdapter.updateDemandas(demandas)
        }

        homeViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun configurarRecyclerView() {
        demandasAdapter = DemandaAdapter(emptyList()) { demanda ->
            onDemandaSelecionada(demanda)
            dismiss()
        }
        binding.rvDemandas.layoutManager = LinearLayoutManager(context)
        binding.rvDemandas.adapter = demandasAdapter
    }

    fun mostrarBottomSheet(fragmentManager: FragmentManager) {
        super.show(fragmentManager, tag)
    }
}
