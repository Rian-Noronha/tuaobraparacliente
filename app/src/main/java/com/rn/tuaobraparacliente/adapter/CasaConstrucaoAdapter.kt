package com.rn.tuaobraparacliente.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rn.tuaobraparacliente.databinding.ItemCasaBinding
import com.rn.tuaobraparacliente.model.CasaConstrucao

class CasaConstrucaoAdapter(
    private var casas: List<CasaConstrucao>,
    private val onItemClick: (CasaConstrucao) -> Unit
) : RecyclerView.Adapter<CasaConstrucaoAdapter.CasaConstrucaoViewHolder>() {


    inner class CasaConstrucaoViewHolder(private val binding: ItemCasaBinding, ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(casaConstrucao: CasaConstrucao) {
            binding.textNome.text = casaConstrucao.nome
            binding.textDescricao.text = casaConstrucao.descricao
            binding.textHorario.text = casaConstrucao.horario
            binding.textEmail.text = casaConstrucao.email
            binding.textFrete.text = casaConstrucao.frete
            binding.textWhatsApp.text = casaConstrucao.contatoWhatsApp

            binding.btnEnviarOrcamento.setOnClickListener{
                onItemClick(casaConstrucao)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CasaConstrucaoViewHolder {
        val binding = ItemCasaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CasaConstrucaoViewHolder(binding)
    }

    override fun getItemCount() = casas.size

    override fun onBindViewHolder(holder: CasaConstrucaoViewHolder, position: Int) {
        val casaConstrucao = casas[position]
        holder.bind(casaConstrucao)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCasas(newCasas: List<CasaConstrucao>){
        casas = newCasas
        notifyDataSetChanged()
    }


}