package com.rn.tuaobraparacliente.ui.home
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rn.tuaobraparacliente.R
import com.rn.tuaobraparacliente.databinding.FragmentCadastroDemandaBinding
import com.rn.tuaobraparacliente.model.Cliente
import com.rn.tuaobraparacliente.model.Demanda
import com.rn.tuaobraparacliente.model.Endereco
import com.rn.tuaobraparacliente.network.RetrofitClient
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneOffset
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.ZoneId

class CadastroDemandaFragment : Fragment() {

    private var _binding: FragmentCadastroDemandaBinding? = null
    private val binding get() = _binding!!
    private lateinit var cadastroDemandaViewModel: CadastroDemandaViewModel
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCadastroDemandaBinding.inflate(inflater, container, false)

        cadastroDemandaViewModel = ViewModelProvider(this).get(CadastroDemandaViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)


       binding.btnCadastrarDemanda.setOnClickListener{
           cadastrarDemandaCliente()
       }

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cadastrarDemandaCliente(){
        val endereco = Endereco(
            cep = binding.txtInputCep.text.toString(),
            nomeLugar = binding.txtInputNomeLugar.text.toString(),
            numero = binding.txtInputNumero.text.toString()
        )

        val currentUser = auth.currentUser
        val clienteEmail = currentUser?.email

        if(clienteEmail != null){
            val cliente = Cliente(
                email = clienteEmail
            )

            val demanda = Demanda(
                detalhes = binding.txtInputDetalhes.text.toString(),
                trabalhoSerFeito = binding.txtInputTrabalhoSerFeito.text.toString(),
                dataPublicacao = obterDataAtual(),
                endereco = endereco,
                cliente = cliente
            )

            RetrofitClient.instance.salvarDemandaCliente(demanda).enqueue(object : Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful){
                        Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_cadastroDemandaFragment_to_navigation_home)
                    }else{
                        Toast.makeText(context, "Erro ao cadastrar: ${response.message()}", Toast.LENGTH_SHORT).show()
                        Log.i("Rn:", response.toString())
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Falha na conexão: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.i("Rian: ", t.message.toString())
                }

            })
        }else{
            Toast.makeText(
                context,
                "Erro: Não foi possível obter o email do cliente.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun obterDataAtual(): String {
        val zoneId = ZoneId.of("America/Recife")
        val dataAtual = ZonedDateTime.now(zoneId)
        val dataAtualUtc = dataAtual.withZoneSameInstant(ZoneOffset.UTC)
        return dataAtualUtc.format(DateTimeFormatter.ISO_INSTANT)
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}