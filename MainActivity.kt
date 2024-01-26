package com.example.beautifulmind

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.health.services.client.*
import androidx.health.services.client.data.*
import com.example.beautifulmind.databinding.ActivityMainBinding
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.internal.GoogleApiManager
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable



class MainActivity : AppCompatActivity() {

    private lateinit var measureClient: MeasureClient
    private lateinit var binding: ActivityMainBinding
    private var supportsHeartRate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPermissionsHeartRate()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(Wearable.API)
            .build()

        mGoogleApiClient.connect()
        var hr_recorded= ArrayList<Int>()

        var heartRate = 0
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        val heartRateListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
                    heartRate = event.values[0].toInt()
                    if(heartRate!=0) {
                        hr_recorded += heartRate
                    }
                    Log.i("heartRate", "$hr_recorded")


                    var putDataMapReq = PutDataMapRequest.create("/heart_rate_data")
                    putDataMapReq.dataMap.putIntegerArrayList("heart_rate", hr_recorded)
                    var putDataReq = putDataMapReq.asPutDataRequest()
                    var result = Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq)
                    Log.i("heart", "$result")

                // Use the heart rate data
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Handle accuracy changes
            }
        }
        sensorManager.registerListener(
            heartRateListener,
            heartRateSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )





    }
    private fun getPermissionsHeartRate() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BODY_SENSORS,

                ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BODY_SENSORS), 0
            )
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.FOREGROUND_SERVICE,

                ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.FOREGROUND_SERVICE), 0
            )
        }
    }
}

