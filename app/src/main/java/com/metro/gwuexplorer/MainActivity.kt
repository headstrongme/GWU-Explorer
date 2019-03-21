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
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val entManager: StationEntManager = StationEntManager()
    private lateinit var stationname : EditText
    private lateinit var remember: CheckBox
    private lateinit var go : Button
    private lateinit var alert: Button
    private lateinit var checkedBox: CheckBox
    private lateinit var text: String
    private  var bool1: Boolean = false
    private lateinit var first:Address
    private lateinit var temp:String


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


        go.setOnClickListener {

                // Pass a context (e.g. Activity) and locale
                val geocoder = Geocoder(this, Locale.getDefault())
                val locationName : String? = stationname.getText().toString()
                val maxResults = 3
                val results: List<Address>? = geocoder.getFromLocationName(locationName, maxResults)

              //  lateinit var addr: MutableList<String?>

                if (results != null && results.isNotEmpty()) {

//                var addString: String?
//
//                val firstAdd = results[0]
//
//                var n=firstAdd.maxAddressLineIndex
//                var x:Int=0
//                while ( n>=0){
//                    addString = firstAdd.getAddressLine(x)
//                    Log.d("itembelike",addString)
//                    addr.add(addString)
//                    n--
//                    x++
//                }

                first = results[0]


            entManager.retrieveNearbyStation(
                //passing the first result
                address = first,
                successCallback = { list,station ->
                    runOnUiThread {
                        // Create the adapter and assign it to the RecyclerView

                        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
                        arrayAdapter.addAll(list)

                        if(list.isEmpty()){
                            Toast.makeText(this@MainActivity, "List is empty...Try again", Toast.LENGTH_LONG).show()
                        }

                        AlertDialog.Builder(this)
                            .setTitle("Select an option")
                            .setAdapter(arrayAdapter) { dialog, which ->
                                Toast.makeText(this, "You picked: ${list[which]}", Toast.LENGTH_SHORT).show()

                            Log.d("code","${station[which]}")
                                temp="${station[which]}"

                                val intent = Intent(this, RouteActivity::class.java)
                                intent.putExtra("StationCode", temp)
                                intent.putExtra("Name","${list[which]}")
                                startActivity(intent)

                            }
                            .setNegativeButton("Cancel") { dialog, which ->
                                dialog.dismiss()
                            }
                            .show()

                    }
                },
                errorCallback = {
                    runOnUiThread {
                        // Runs if we have an error
                        Toast.makeText(this@MainActivity, "Error retrieving Station name", Toast.LENGTH_LONG).show()
                    }
                })
                        saveData()
                        loadData()

                }

                else{
                    //if result is empty or does not give station name, below toast will appear
                    Toast.makeText(this@MainActivity, "Try new name", Toast.LENGTH_LONG).show()

                }
        }

        alert.setOnClickListener {
            val intent2 = Intent(this, AlertActivity::class.java)

            startActivity(intent2)
        }
        loadData()
        updateText()
    }


    // Below code is for shared preference
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
