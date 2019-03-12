package com.metro.gwuexplorer

import android.location.Address
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit


class StationEntManager {

    private val okHttpClient: OkHttpClient


    init {
        val builder = OkHttpClient.Builder()

        // This sets network timeouts (in case the phone can't connect
        // to the server or the server is down)
        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)

        // This causes all network traffic to be logged to the console
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        okHttpClient = builder.build()
    }


    fun retrieveNearbyStation(

        address: Address,
        successCallback: (List<String>) -> Unit,
        errorCallback: (Exception) -> Unit
    ) {
        // Data setup
        val lat = address.latitude
        val lon = address.longitude
        val radius = "30"
        val primaryKey = "da0eb4c222b5442ea5a2a59b3677b73e"

        // Building the request, passing the OAuth token as a header
        val request = Request.Builder()
            .url("https://api.wmata.com/Rail.svc/json/jStationEntrances?$lat&$lon&$radius")
            .header("api_key", "$primaryKey")
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Same error handling to last time
                errorCallback(e)
            }

            override fun onResponse(call: Call, response: Response) {
                // Similar success / error handling to last time
                val ent = mutableListOf<String>()
                val responseString = response.body()?.string()

                if (response.isSuccessful && responseString != null) {
                    val statuses = JSONObject(responseString).getJSONArray("Entrances")
                    for (i in 0 until statuses.length()) {
                        val curr = statuses.getJSONObject(i)
                        val text = curr.getString("Name")

                        ent.add(text)
                    }
                    successCallback(ent)
                    //...
                } else {
                    // Invoke the callback passed to our [retrieveTweets] function.
                    errorCallback(Exception("Search Entrances call failed"))
                }
            }
        })
    }


}