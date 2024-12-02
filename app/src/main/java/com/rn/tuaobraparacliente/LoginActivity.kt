package com.rn.tuaobraparacliente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.rn.tuaobraparacliente.databinding.ActivityLoginBinding
import com.rn.tuaobraparacliente.model.Cliente
import com.rn.tuaobraparacliente.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var googleApiClient: GoogleApiClient? = null
    private var fbAuth = FirebaseAuth.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initGoogleSignIn()

        binding.btnGoogleSignIn.setOnClickListener {
            signIn()
        }

        binding.btnSignIn.setOnClickListener {
            if (validarCampos()) {

                auth.signInWithEmailAndPassword(
                    binding.editTextEmail.text.toString(),
                    binding.editTextPassword.text.toString()
                ).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser
                        user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful) {

                                val idToken = tokenTask.result?.token


                                idToken?.let {
                                    enviarTokenParaServidor(it)
                                }


                                startActivity(Intent(this, MainActivity::class.java))
                            } else {
                                Log.e("Firebase", "Falha ao obter o ID Token")
                            }
                        }
                    } else {
                        Log.w("EmailPassowrdFailure", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.txtIrTelaCadastro.setOnClickListener{
            startActivity(Intent(this, CadastroActivity::class.java))
        }



    }

    private fun initGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this) {
                showErrorSignIn()
            }
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

    private fun signIn(){
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient!!)
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_GOOGLE_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            if(result!!.isSuccess){
                val account = result.signInAccount
                if(account != null){
                    firebaseAuthWithGoogle(account)
                }else{
                    showErrorSignIn()
                }
            }else{
                showErrorSignIn()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        fbAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    val idToken = acct.idToken

                    idToken?.let{enviarTokenParaServidor(it)}

                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                }else{
                    showErrorSignIn()
                }
            }
    }

    private fun showErrorSignIn() {
        Toast.makeText(this, R.string.error_google_sign_in, Toast.LENGTH_SHORT).show()
    }

    private fun validarCampos(): Boolean {
        return binding.editTextEmail.text.toString().isNotEmpty()
                && binding.editTextPassword.text.toString().isNotEmpty()
    }

    private fun enviarTokenParaServidor(idToken: String) {
        val apiService = RetrofitClient.instance


        val call = apiService.autenticar("Bearer $idToken")

        call.enqueue(object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                if (response.isSuccessful) {
                    Log.d("Firebase", "Login realizado com sucesso: ${response.body()}")
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {

                    Log.e("Firebase", "Falha no login: ${idToken}")
                    renovarToken()
                }
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                Log.e("Firebase", "Erro ao autenticar no servidor", t)
                Toast.makeText(this@LoginActivity, "Erro ao conectar ao servidor.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun renovarToken() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
            if (tokenTask.isSuccessful) {

                val newIdToken = tokenTask.result?.token


                newIdToken?.let {
                    enviarTokenParaServidor(it)
                }
            } else {
                Log.e("Firebase", "Falha ao renovar o ID Token")
            }
        }
    }




    companion object {
        const val RC_GOOGLE_SIGN_IN = 1
    }

}