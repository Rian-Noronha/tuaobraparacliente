package com.rn.tuaobraparacliente.ui.casa
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rn.tuaobraparacliente.adapter.CasaConstrucaoAdapter
import com.rn.tuaobraparacliente.databinding.FragmentCasaBinding
import com.rn.tuaobraparacliente.model.CasaConstrucao
import com.rn.tuaobraparacliente.model.Demanda

class CasaFragment : Fragment() {

    private var _binding: FragmentCasaBinding? = null
    private val binding get() = _binding!!
    private lateinit var casaViewModel: CasaViewModel
    private lateinit var casasAdapter: CasaConstrucaoAdapter
    private var casaSelecionada: CasaConstrucao? = null
    private var demandaSelecionada: Demanda? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCasaBinding.inflate(inflater, container, false)
        casaViewModel = ViewModelProvider(this).get(CasaViewModel::class.java)

        configurarRecyclerView()

        casaViewModel.casas.observe(viewLifecycleOwner) { casas ->
            casasAdapter.updateCasas(casas)
        }


        casaViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }


    private fun configurarRecyclerView() {

        casasAdapter = CasaConstrucaoAdapter(emptyList()) { casa ->
            casaSelecionada = casa
            Log.d("Casa em fragment", "${casa}")
            // Ao clicar na casa, abrir o BottomSheet
            val bottomSheet = DemandaBottomSheet(casa) { demanda ->
                demandaSelecionada = demanda
                // Ao selecionar uma demanda, fazer algo com ela (passar para CasaFragment)
                // Exemplo: Mostrar um Toast ou realizar algum outro processamento
                Toast.makeText(context, "Demanda Selecionada: ${demanda.trabalhoSerFeito}", Toast.LENGTH_SHORT).show()

                // Fechar o BottomSheet automaticamente
            }

            bottomSheet.mostrarBottomSheet(childFragmentManager)
        }

        binding.recyclerViewCasa.layoutManager = LinearLayoutManager(context)

        binding.recyclerViewCasa.adapter = casasAdapter
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}