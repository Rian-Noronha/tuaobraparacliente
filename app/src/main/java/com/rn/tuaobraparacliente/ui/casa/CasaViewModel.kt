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
}