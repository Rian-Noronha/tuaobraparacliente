package com.rn.tuaobraparacliente.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rn.tuaobraparacliente.databinding.ItemDemandaBinding
import com.rn.tuaobraparacliente.model.Demanda

class DemandaAdapter(
    private var demandas: List<Demanda>
) : RecyclerView.Adapter<DemandaAdapter.DemandaViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemandaViewHolder {
        val binding = ItemDemandaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DemandaViewHolder(binding)
    }

    override fun getItemCount() = demandas.size

    override fun onBindViewHolder(holder: DemandaViewHolder, position: Int) {
        val demanda = demandas[position]
        holder.bind(demanda)
    }

    fun updateDemandas(newDemandas: List<Demanda>){
        demandas = newDemandas
        notifyDataSetChanged()
    }


    class DemandaViewHolder(private val binding: ItemDemandaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(demanda: Demanda) {
            binding.txtTrabalhoASerFeito.text = demanda.detalhes
            binding.txtTituloDemanda.text = demanda.trabalhoSerFeito
            binding.txtLocalizacao.text = demanda.endereco.cep
            binding.txtHorarioPublicacao.text = formatarData(demanda.dataPublicacao)
        }


        fun formatarData(data: String): String {
            val partesDataHora = data.split("T")
            val dataParte = partesDataHora[0]
            val horaParte = partesDataHora[1].split(":")
            val anoMesDia = dataParte.split("-")
            val horas = horaParte[0].toInt() - 3
            val minutos = horaParte[1].toInt()
            val horaAjustada = if (horas < 0) 24 + horas else horas
            val dia = anoMesDia[2]
            val mes = anoMesDia[1]
            val ano = anoMesDia[0]
            val horaFormatada = "${horaAjustada}h${if (minutos > 0) "${minutos}min" else ""}"
            return "Às $horaFormatada, $dia/$mes/$ano"
        }



    }
}