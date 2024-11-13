package com.rn.tuaobraparacliente.ui.home
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rn.tuaobraparacliente.R
import com.rn.tuaobraparacliente.databinding.FragmentCadastroDemandaBinding
import com.rn.tuaobraparacliente.model.Cliente
import com.rn.tuaobraparacliente.model.Demanda
import com.rn.tuaobraparacliente.model.Endereco
import com.rn.tuaobraparacliente.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    private fun cadastrarDemandaCliente(){
        val endereco = Endereco(
            cep = binding.txtInputCep.text.toString(),
            nomeLugar = binding.txtInputNomeLugar.text.toString(),
            numero = binding.txtInputNumero.text.toString()
        )

        val cliente = Cliente(
            email = "paulo.miranda@gmail.com"
        )

        val demanda = Demanda(
            detalhes = binding.txtInputDetalhes.text.toString(),
            trabalhoSerFeito = binding.txtInputTrabalhoSerFeito.text.toString(),
            dataPublicacao = binding.txtInputDataPublicacao.text.toString(),
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


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}