package com.metro.gwuexplorer

import android.widget.Toast
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class AlertManager {
    // OkHttp is a library used to make network calls
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



    fun retrieveAlert(
        successCallback: (List<Alert>) -> Unit,
        errorCallback: (Exception) -> Unit
    ) {
        // Data setup
        val primaryKey = "da0eb4c222b5442ea5a2a59b3677b73e"

        // Building the request, passing the OAuth token as a header
        val request = Request.Builder()
            .url("https://api.wmata.com/Incidents.svc/json/Incidents")
            .header("api_key", "$primaryKey")
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Same error handling to last time
                errorCallback(e)
            }

            override fun onResponse(call: Call, response: Response) {
                // Similar success / error handling to last time
                val alerts = mutableListOf<Alert>()
                val responseString = response.body()?.string()

                if (response.isSuccessful && responseString != null) {
                    val incidents = JSONObject(responseString).getJSONArray("Incidents")
                    for (i in 0 until incidents.length()) {
                        val curr = incidents.getJSONObject(i)
                        val linename = curr.getString("LinesAffected")
                        val desc = curr.getString("Description")

                        alerts.add(
                            Alert(
                                icon = "http image",
                                lineName = linename,
                                description =desc
                            )
                        )
                    }
                    successCallback(alerts)

                } else {
                    // Invoke the callback passed to our [retrieveAlerts] function.
                    errorCallback(Exception("Search alerts call failed"))

                }
            }
        })
    }
}