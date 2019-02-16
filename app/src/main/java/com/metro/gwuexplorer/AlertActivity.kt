package com.metro.gwuexplorer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class AlertActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)


      recyclerView = findViewById(R.id.recyclerView)

        // Set the direction of our list to be vertical
        recyclerView.layoutManager = LinearLayoutManager(this)

        val alert = generateFakeAlert()

        // Create the adapter and assign it to the RecyclerView
        recyclerView.adapter = AlertAdapter(alert)
    }

    private fun generateFakeAlert(): List<Alert> {
        return listOf(
            Alert(
                stationName = "Bethesda ",
                icon = "https://...."
            ),
            Alert(
                stationName = "Dupont Circle ",
                icon = "https://...."
            ),
            Alert(
                stationName = "Bethesda ",
                icon = "https://...."
            ),
            Alert(
                stationName = "Dupont Circle ",
                icon = "https://...."
        )
        )

    }
}