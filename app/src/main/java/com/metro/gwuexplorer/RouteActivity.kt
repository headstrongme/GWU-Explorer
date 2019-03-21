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

        // Using HashMap to get the value for line by using the LineCode received though API call
        val hm: HashMap<String, String> = hashMapOf( "SV" to "Silver", "GR" to "Green", "BL" to "Blue", "RD" to "Red","YL" to "Yellow", "OR" to "Orange")

        routeManager.retrieveStationList(
            codeNext=stationcodeNext,
            successCallback = { path,line ->
                runOnUiThread {
                    // Create the adapter and assign it to the RecyclerView
                    //Using same recycle view for Alerts as well
                    recyclerView.adapter = RouteAdapter(path)

                    //Below will tell user which line to take as a Toast
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


        //Implicit intent which allows us to choose application to share message

        share.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "I am travelling from Foggy bottom to $locationName")
            }
            startActivity(sendIntent)

        }


        // Explicit intent which shows google map direction

        direc.setOnClickListener {

            //HardCoding LatLng of Foggy Bottom and parsing destination location using encode method
            val navigationUri = Uri.parse ("geo:38.9009,77.0505?q=" + Uri.encode(locationName+ ", Washington, DC metro"))
            // Create an Intent from navigationUri. Set the action to ACTION_VIEW
            val mapIntent = Intent(Intent.ACTION_VIEW, navigationUri)
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

    }


}
