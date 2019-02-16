package com.metro.gwuexplorer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class AlertAdapter constructor(private val alerts: List<Alert>) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        // Open & parse our XML file
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.row_alert, parent, false)

        // Create a new ViewHolder
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = alerts.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val currentAlerts = alerts[p1]

        p0.usernameTextView.text = currentAlerts.stationName
    }


    class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        val usernameTextView: TextView = view.findViewById(R.id.stationName)

        val iconImageView: ImageView = view.findViewById(R.id.icon)

    }
}