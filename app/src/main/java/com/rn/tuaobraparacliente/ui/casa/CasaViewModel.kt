package com.rn.tuaobraparacliente.ui.casa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rn.tuaobraparacliente.model.CasaConstrucao
import com.rn.tuaobraparacliente.model.Demanda
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

    fun atualizarDemandaComImagem(demandaId: Long, emailCliente: String, demanda: Demanda): LiveData<Boolean> {
        val resultado = MutableLiveData<Boolean>()


        RetrofitClient.instance.atualizarDemanda(demandaId, emailCliente, demanda)
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
    
    fun vincularCasaCliente(casaId: Long, demandaId: Long, emailCliente: String){
        RetrofitClient.instance.vincularCasaCliente(casaId, demandaId, emailCliente).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
               if(response.isSuccessful){
                   Log.i("Vincular casa cliente", "CasaCliente ${casaId} e ${demandaId} e ${emailCliente} vinculados.")
               }else {
                   _error.value = "Erro ao vincular: ${response.message()}"
                   Log.e("Erro ao vincular", "Erro: ${response.message()}")
               }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _error.value = "Falha de conexão: ${t.message}"
                Log.e("Vincular casa cliente", "Falha: ${t.message}")
            }

        })
    }

}