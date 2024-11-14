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
            binding.txtHorarioPublicacao.text = demanda.dataPublicacao
        }

    }
}