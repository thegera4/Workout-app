package com.app.kot_workout_app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.kot_workout_app.constants.Constants
import com.app.kot_workout_app.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ads initialization
        MobileAds.initialize(this@MainActivity)
        //Banner ad request
        val adReq = AdRequest.Builder().build()
        binding.adView.loadAd(adReq)

        binding.llStart.setOnClickListener {
            when {
                binding.rbLevelOne.isChecked -> Constants.LEVELS = "1"
                binding.rbLevelTwo.isChecked -> Constants.LEVELS = "2"
                binding.rbLevelThree.isChecked -> Constants.LEVELS = "3"
            }
            val intent = Intent(this, ExerciseActivity::class.java)
            intent.putExtra(Constants.LEVELS,Constants.LEVELS )
            startActivity(intent)
        }

        binding.llBMI.setOnClickListener {
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
        }

        binding.llHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}