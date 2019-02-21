package com.metro.gwuexplorer

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.*
import java.util.*

class MainActivity : AppCompatActivity() {


    private lateinit var stationname : EditText
    private lateinit var remember: CheckBox
    private lateinit var go : Button
    private lateinit var alert: Button
    private lateinit var checkedBox: CheckBox
    private lateinit var text: String
    private  var bool1: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stationname = findViewById(R.id.stationname)
        remember = findViewById(R.id.remember)
        go = findViewById(R.id.go)
        alert = findViewById(R.id.alert)



        checkedBox = findViewById(R.id.remember);

        AlertDialog.Builder(this)
            .setTitle("Welcome!")
            .setMessage("\n\nPlease enter location to travel from GWU and use Alert button to use metro outage" +
                    "\n\n\nNote: Currently works only for the location which does not require transfer")
            .setPositiveButton("OK"){dialog, which ->  }
            .show()



        if (checkedBox.isChecked()) {

            // Pass the name and the file-create mode (e.g. private to our app)
            val preferences = getSharedPreferences("gwu-explorer", Context.MODE_PRIVATE)
            // Writing to preferences (make sure you call apply)
            preferences.edit().putString("saved_stationName", stationname.text.toString()).apply()
            // Reading from preferences, indicate default if not present
            val savedStationName = preferences.getString("saved_stationName", "")

        }


        
        go.setOnClickListener {

           // val choices = listOf("667 M Street Washington, DC 20585", "668 M Street Washington, DC 20585")

            // Pass a context (e.g. Activity) and locale
            val geocoder = Geocoder(this, Locale.getDefault())
            val locationName = stationname.getText().toString()

            val maxResults = 2

            val results: List<Address> = geocoder.getFromLocationName(locationName,maxResults)



            val first = results[0]
            val first_go = first.getAddressLine(0)
//            val second = results[1]
//            val second_go = second.getAddressLine(0)

            val addr: List<String> = listOf(first_go)


            //val first = results as List<String>

            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
            arrayAdapter.addAll(addr)
            AlertDialog.Builder(this)
                .setTitle("Select an option")
                .setAdapter(arrayAdapter) { dialog, which ->
                    Toast.makeText(this, "You picked: ${addr[which]}", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()


            saveData();
            loadData();


            //val intent: Intent = Intent(this, RouteActivity::class.java)

           //startActivity(intent)
        }

        alert.setOnClickListener {
            val intent2: Intent = Intent(this, AlertActivity::class.java)

            startActivity(intent2)
        }

        loadData()
        updateText()

    }
   private  fun saveData ()
    {
        // Pass the name and the file-create mode (e.g. private to our app)
        val preferences = getSharedPreferences("gwu-explorer", Context.MODE_PRIVATE)
        // Writing to preferences (make sure you call apply)
        preferences.edit().putString("SAVED_STATIONNAME", stationname.text.toString()).apply()
        preferences.edit().putBoolean("CHECKBOX",remember.isChecked).apply()
        // Reading from preferences, indicate default if not present
        //val savedStationName = preferences.getString("SAVED_STATIONNAME", "")
    }
    private fun loadData ()
    {
        val preferences = getSharedPreferences("gwu-explorer", Context.MODE_PRIVATE)
        text = preferences.getString("SAVED_STATIONNAME","")
        bool1=preferences.getBoolean("CHECKBOX",false)
    }
     fun updateText(){
         stationname.setText(text)
         remember.setChecked(bool1)
    }
}
