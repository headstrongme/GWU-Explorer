package com.metro.gwuexplorer

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.Toast

class RouteActivity : AppCompatActivity() {

    private val routeManager: RouteManager = RouteManager()
    private lateinit var share: Button
    private lateinit var direc: Button
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        share =findViewById(R.id.share)
        direc= findViewById(R.id.direc)

        recyclerView = findViewById(R.id.recyclerView)

        // Set the direction of our list to be vertical
        recyclerView.layoutManager = LinearLayoutManager(this)

        val intentt: Intent = intent
        val stationcodeNext:String =intentt.getStringExtra("StationCode")
        val locationName:String = intentt.getStringExtra("Name")

        val hm: HashMap<String, String> = hashMapOf( "SL" to "Silver", "GR" to "Green", "BL" to "Blue", "RD" to "Red","YL" to "Yellow", "OR" to "Orange")

        routeManager.retrieveStationList(
            codeNext=stationcodeNext,
            successCallback = { path,line ->
                runOnUiThread {
                    // Create the adapter and assign it to the RecyclerView

                    /* TODO change adapter below*/
                    recyclerView.adapter = RouteAdapter(path)
                    val temp: String? = hm.get(line)
                    Toast.makeText(this@RouteActivity, "Kindly take $temp line", Toast.LENGTH_LONG).show()

                }
            },
            errorCallback = {
                runOnUiThread {
                    // Runs if we have an error
                    Toast.makeText(this@RouteActivity, "Error retrieving Station List", Toast.LENGTH_LONG).show()
                }
            }
        )


        share.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "I am travelling from Foggy bottom to $locationName")
            }
            startActivity(sendIntent)

        }


        direc.setOnClickListener {

            // Create a Uri from an intent string. Use the result to create an Intent.
            val navigationUri = Uri.parse("google.navigation:q=$locationName")
            // Create an Intent from navigationUri. Set the action to ACTION_VIEW
            val mapIntent = Intent(Intent.ACTION_VIEW, navigationUri)
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

    }


}
