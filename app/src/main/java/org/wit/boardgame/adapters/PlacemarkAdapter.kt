package org.wit.boardgame.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.boardgame.databinding.CardPlacemarkBinding
import org.wit.boardgame.models.PlacemarkModel

interface PlacemarkListener {
    fun onPlacemarkClick(placemark: PlacemarkModel, position : Int)
}
class PlacemarkAdapter constructor(private var placemarks: List<PlacemarkModel>,
                                   private val listener: PlacemarkListener) :
    RecyclerView.Adapter<PlacemarkAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPlacemarkBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val placemark = placemarks[holder.adapterPosition]
        holder.bind(placemark, listener)
    }

    override fun getItemCount(): Int = placemarks.size

    class MainHolder(private val binding : CardPlacemarkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceType")
        fun bind(placemark: PlacemarkModel, listener: PlacemarkListener) {
            binding.placemarkTitle.text = placemark.title
            binding.description.text = placemark.description
            binding.Nump.text = placemark.numbP
            binding.TimeID.text = placemark.time
            Picasso.get().load(placemark.image).resize(200,200).into(binding.imageIcon)
            /**
             * Future Feature: Mark the games that the user joined in the overview list as green
            if(placemark.userList.contains(userOnline)) {
                binding.root.setBackgroundColor(R.color.green)
            }
            */
            binding.root.setOnClickListener { listener.onPlacemarkClick(placemark,adapterPosition) }
        }
    }


}