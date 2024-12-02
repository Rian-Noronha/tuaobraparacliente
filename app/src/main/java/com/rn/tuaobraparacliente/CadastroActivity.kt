package com.rn.tuaobraparacliente
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
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

        auth = Firebase.auth

        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCadastrar.setOnClickListener{
            cadastrarCliente()
        }

    }

    private fun cadastrarCliente() {
        if (validarCampos()) {
            // Criar usuário no Firebase
            auth.createUserWithEmailAndPassword(binding.edtEmail.text.toString(), binding.edtSenha.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val endereco = Endereco(
                            cep = binding.edtCEP.text.toString(),
                            nomeLugar = binding.edtNomeLugar.text.toString(),
                            numero = binding.edtNumero.text.toString()
                        )

                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val firebaseUid = currentUser?.uid ?: throw IllegalStateException("Usuário não autenticado")

                        val cliente = Cliente(
                            nome = binding.edtNome.text.toString(),
                            email = binding.edtEmail.text.toString(),
                            contatoWhatsApp = binding.edtWhatsApp.text.toString(),
                            endereco = endereco,
                            firebaseUid = firebaseUid
                        )

                        // Enviar cliente ao backend
                        RetrofitClient.instance.cadastrarCliente(cliente).enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    // Realiza login automático após o cadastro no backend
                                    auth.signInWithEmailAndPassword(
                                        binding.edtEmail.text.toString(),
                                        binding.edtSenha.text.toString()
                                    ).addOnCompleteListener { signInTask ->
                                        if (signInTask.isSuccessful) {
                                            Log.d("FirebaseUID", "UID do Firebase: $firebaseUid")
                                            Toast.makeText(
                                                this@CadastroActivity,
                                                "Cadastro e login realizados com sucesso!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this@CadastroActivity,
                                                "Erro ao realizar login automático.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else {
                                    val errorMessage = when (response.code()) {
                                        400 -> "Erro nos dados enviados. Por favor, verifique as informações."
                                        409 -> "E-mail já cadastrado."
                                        else -> "Erro ao cadastrar: ${response.message()}"
                                    }
                                    Toast.makeText(this@CadastroActivity, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Toast.makeText(
                                    this@CadastroActivity,
                                    "Falha na conexão: ${t.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("CadastroActivity", "Erro: ${t.message}", t)
                            }
                        })
                    } else {
                        Toast.makeText(this, "Erro ao criar usuário no Firebase: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
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