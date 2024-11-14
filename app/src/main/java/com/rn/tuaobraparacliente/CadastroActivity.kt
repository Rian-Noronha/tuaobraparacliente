package com.rn.tuaobraparacliente

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rn.tuaobraparacliente.databinding.ActivityCadastroBinding
import com.rn.tuaobraparacliente.model.Cliente
import com.rn.tuaobraparacliente.model.Endereco
import com.rn.tuaobraparacliente.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCadastrar.setOnClickListener{
            cadastrarCliente()
        }

    }

    private fun cadastrarCliente(){
        val endereco = Endereco(
            cep = binding.edtCEP.toString(),
            nomeLugar = binding.edtNomeLugar.text.toString(),
            numero = binding.edtNumero.text.toString()
        )

        val cliente = Cliente(
            nome = binding.edtNome.toString(),
            email = binding.edtEmail.toString(),
            contatoWhatsApp = binding.edtWhatsApp.toString(),
            endereco = endereco
        )

        RetrofitClient.instance.cadastrarCliente(cliente).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Toast.makeText(this@CadastroActivity, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@CadastroActivity, "Erro ao cadastrar: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CadastroActivity, "Falha na conexão: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.i("Info: ", t.message.toString())
            }

        })
    }

}