package com.metro.gwuexplorer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RouteAdapter constructor(private val stationList: List<String>) : RecyclerView.Adapter<RouteAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        // Open & parse our XML file

        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.row_station, parent, false)
        // Create a new ViewHolder
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = stationList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentStation = stationList[position]

        holder.lineNameTextView.text = currentStation
    }

    class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        val lineNameTextView: TextView = view.findViewById(R.id.stationID)

    }
}