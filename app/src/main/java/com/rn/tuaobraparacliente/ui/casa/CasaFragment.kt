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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.rn.tuaobraparacliente.adapter.CasaConstrucaoAdapter
import com.rn.tuaobraparacliente.databinding.FragmentCasaBinding
import com.rn.tuaobraparacliente.model.CasaConstrucao
import com.rn.tuaobraparacliente.model.Demanda
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import android.content.Intent
import android.net.Uri

class CasaFragment : Fragment() {

    private var _binding: FragmentCasaBinding? = null
    private val binding get() = _binding!!
    private lateinit var casaViewModel: CasaViewModel
    private lateinit var casasAdapter: CasaConstrucaoAdapter
    private var casaSelecionada: CasaConstrucao? = null
    private var demandaSelecionada: Demanda? = null
    private lateinit var auth: FirebaseAuth
    private val storage = FirebaseStorage.getInstance()
    private val abrirGaleria =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                if (imageUri != null) {
                    uploadImagemParaStorage(imageUri)
                } else {
                    Toast.makeText(context, "Falha ao selecionar a imagem", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCasaBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
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
            val bottomSheet = DemandaBottomSheet(casa) { demanda ->
                demandaSelecionada = demanda
                abrirGaleria()
            }
            bottomSheet.mostrarBottomSheet(childFragmentManager)
        }

        binding.recyclerViewCasa.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewCasa.adapter = casasAdapter
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        abrirGaleria.launch(intent)
    }

    private fun uploadImagemParaStorage(imageUri: Uri) {
        val userId = auth.currentUser?.uid ?: "user_desconhecido"
        val demandaId = demandaSelecionada?.id ?: "demanda_desconhecida"
        val storageRef = storage.reference.child("imagens/$userId/$demandaId.jpg")

        val uploadTask = storageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                Log.d("FirebaseStorage", "Imagem enviada com sucesso: $uri")
                demandaSelecionada?.urlOrcamento = uri.toString()
                val currentUser = auth.currentUser
                val emailCliente = currentUser?.email
                demandaSelecionada?.let { demanda ->
                    casaViewModel.atualizarDemandaComImagem(demanda.id!!,  emailCliente.toString(), demanda)
                        .observe(viewLifecycleOwner) { sucesso ->
                            Log.e("Demanda", "Demanda selecionada ${demanda}")
                            if (sucesso) {
                                Toast.makeText(context, "Demanda atualizada com sucesso ${emailCliente.toString()}!", Toast.LENGTH_SHORT).show()
                                casaViewModel.vincularCasaCliente(casaSelecionada!!.id!!, demandaSelecionada!!.id!!, emailCliente.toString())

                            } else {
                                Toast.makeText(context, "Erro ao atualizar demanda.", Toast.LENGTH_SHORT).show()
                            }
                        }
                }


            }
        }.addOnFailureListener { exception ->
            Log.e("FirebaseStorage", "Erro ao enviar imagem", exception)
            Toast.makeText(context, "Erro ao enviar imagem: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
