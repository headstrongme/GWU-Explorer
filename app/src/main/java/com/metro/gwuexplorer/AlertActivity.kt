package com.metro.gwuexplorer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast

class AlertActivity : AppCompatActivity() {

    private val alertManager: AlertManager = AlertManager()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)


        recyclerView = findViewById(R.id.recyclerView)

        // Set the direction of our list to be vertical
        recyclerView.layoutManager = LinearLayoutManager(this)

       // val alert = generateFakeAlert()

        // Create the adapter and assign it to the RecyclerView



        alertManager.retrieveTweets(

            successCallback = { alerts ->
                runOnUiThread {
                    // Create the adapter and assign it to the RecyclerView
                    recyclerView.adapter = AlertAdapter(alerts)
                }
            },
            errorCallback = {
                runOnUiThread {
                    // Runs if we have an error
                    Toast.makeText(this@AlertActivity, "Error retrieving Tweets", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

//    private fun generateFakeAlert(): List<Alert> {
//        return listOf(
//            Alert(
//                lineName = "Bethesda ",
//                icon = "https://...."
//            ),
//            Alert(
//                lineName = "Dupont Circle ",
//                icon = "https://...."
//            ),
//            Alert(
//                lineName = "Bethesda ",
//                icon = "https://...."
//            ),
//            Alert(
//                lineName = "Dupont Circle ",
//                icon = "https://...."
//        )
//        )
//
//    }
}