package com.rn.tuaobraparacliente.ui.casa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rn.tuaobraparacliente.model.CasaConstrucao
import com.rn.tuaobraparacliente.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CasaViewModel : ViewModel() {

    private val _casas = MutableLiveData<List<CasaConstrucao>>()
    val casas: LiveData<List<CasaConstrucao>> = _casas

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    init {
        fetchCasas()
    }


    private fun fetchCasas() {
        RetrofitClient.instance.listarCasas().enqueue(object : Callback<List<CasaConstrucao>> {
            override fun onResponse(
                call: Call<List<CasaConstrucao>>,
                response: Response<List<CasaConstrucao>>
            ) {
                if (response.isSuccessful) {
                    _casas.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Erro ao buscar demandas: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<CasaConstrucao>>, t: Throwable) {
                Log.i("Falha casa: ", "${t.message}")
                _error.value = "Falha de conexão: ${t.message}"
            }


        })
    }

    fun vincularCasaCliente(casaId: Long, email: String){
        RetrofitClient.instance.vincularCasaCliente(casaId, email).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("VincularCasaCliente", "Casa vinculada com sucesso")
                } else {
                    _error.value = "Erro ao vincular a casa com o cliente: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _error.value = "Falha ao vincular a casa com o cliente: ${t.message}"
            }

        })
    }


}