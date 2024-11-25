package com.rn.tuaobraparacliente

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.rn.tuaobraparacliente.databinding.ActivityCadastroBinding
import com.rn.tuaobraparacliente.model.Cliente
import com.rn.tuaobraparacliente.model.Endereco
import com.rn.tuaobraparacliente.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCadastrar.setOnClickListener{
            cadastrarCliente()
        }

    }

    private fun cadastrarCliente(){
        if(validarCampos()){
            val endereco = Endereco(
                cep = binding.edtCEP.text.toString(),
                nomeLugar = binding.edtNomeLugar.text.toString(),
                numero = binding.edtNumero.text.toString()
            )

            val cliente = Cliente(
                nome = binding.edtNome.text.toString(),
                email = binding.edtEmail.text.toString(),
                contatoWhatsApp = binding.edtWhatsApp.text.toString(),
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


    private fun validarCamposPreenchidos(): Boolean {
        val nome = binding.edtNome.text.toString().isNotBlank()
        val email = binding.edtEmail.text.toString().isNotBlank()
        val senha = binding.edtSenha.text.toString().isNotBlank()
        val confirmarSenha = binding.edtConfirmarSenha.text.toString().isNotBlank()
        val whatsApp = binding.edtWhatsApp.text.toString().isNotBlank()
        val cep = binding.edtCEP.text.toString().isNotBlank()
        val nomeLugar = binding.edtNomeLugar.text.toString().isNotBlank()
        val numero = binding.edtNumero.text.toString().isNotBlank()

        if (nome && email && senha && confirmarSenha && whatsApp && cep && nomeLugar && numero) {
            return true
        } else {
            Toast.makeText(binding.root.context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun validarSenhasIguais(): Boolean {
        return if (binding.edtSenha.text.toString() == binding.edtConfirmarSenha.text.toString()) {
            true
        } else {
            Toast.makeText(binding.root.context, "As senhas não coincidem!", Toast.LENGTH_SHORT).show()
            false
        }
    }


    private fun validarCampos(): Boolean {
        return validarCamposPreenchidos() && validarSenhasIguais()
    }


}