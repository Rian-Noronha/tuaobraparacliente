package com.rn.tuaobraparacliente.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rn.tuaobraparacliente.model.Demanda
import com.rn.tuaobraparacliente.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _demandas = MutableLiveData<List<Demanda>>()
    val demandas: LiveData<List<Demanda>> = _demandas

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _atualizarStatus = MutableLiveData<Boolean>()
    val atualizarStatus: LiveData<Boolean> = _atualizarStatus

    fun fetchDemandas(email: String) {
        RetrofitClient.instance.listarDemandasCliente(email)
            .enqueue(object : Callback<List<Demanda>> {
                override fun onResponse(
                    call: Call<List<Demanda>>,
                    response: Response<List<Demanda>>
                ) {
                    if (response.isSuccessful) {
                        _demandas.value = response.body() ?: emptyList()
                    } else {
                        _error.value = "Erro ao buscar demandas: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<List<Demanda>>, t: Throwable) {
                    Log.i("Falha demandas: ", "${t.message}")
                    _error.value = "Falha de conexão: ${t.message}"
                }

            })
    }


    fun atualizarDemandaComImagem(demandaId: Long, demanda: Demanda): LiveData<Boolean> {
        val resultado = MutableLiveData<Boolean>()


        RetrofitClient.instance.atualizarDemanda(demandaId, demanda)
            .enqueue(object : Callback<Demanda> {
                override fun onResponse(call: Call<Demanda>, response: Response<Demanda>) {
                    if (response.isSuccessful) {
                        resultado.value = true
                        Log.i("Atualizar Demanda", "Demanda $demandaId atualizada com sucesso.")
                    } else {
                        resultado.value = false
                        _error.value = "Erro ao atualizar demanda: ${response.message()}"
                        Log.e("Atualizar Demanda", "Erro: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Demanda>, t: Throwable) {
                    resultado.value = false
                    _error.value = "Falha de conexão: ${t.message}"
                    Log.e("Atualizar Demanda", "Falha: ${t.message}")
                }

            })

        return resultado
    }

}