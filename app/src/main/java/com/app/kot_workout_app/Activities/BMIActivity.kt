package com.app.kot_workout_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.app.kot_workout_app.R
import com.app.kot_workout_app.databinding.ActivityBmiactivityBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import java.math.RoundingMode
import java.math.BigDecimal

class BMIActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmiactivityBinding

    val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    val IMPERIAL_UNITS_VIEW = "IMPERIAL_UNIT_VIEW"
    var currentVisibleView: String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ads initialization
        MobileAds.initialize(this@BMIActivity)
        //Banner ad request
        val adReq = AdRequest.Builder().build()
        binding.adViewBMI.loadAd(adReq)

        setSupportActionBar(binding.toolbarBmiActivity)
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.title = "Calculate BMI"
        binding.toolbarBmiActivity.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnCalculateUnits.setOnClickListener {
            if (currentVisibleView.equals(METRIC_UNITS_VIEW)) {
                if (validateMetricUnits()) {
                    val heightValue: Float =
                        binding.etMetricUnitHeight.text.toString().toFloat() / 100
                    val weightValue: Float = binding.etMetricUnitWeight.text.toString().toFloat()

                    val bmi = weightValue / (heightValue * heightValue)
                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                if (validateImperialUnits()){
                    val ImperialUnitHeightValueFeet: String =
                        binding.etImperialUnitHeightFeet.text.toString()
                    val ImperialUnitHeightValueInch: String =
                        binding.etImperialUnitHeightInch.text.toString()
                    val ImperialUnitWeightValue: Float =
                        binding.etImperialUnitWeight.text.toString().toFloat()

                    val HeightValue = ImperialUnitHeightValueInch.toFloat() +
                            ImperialUnitHeightValueFeet.toFloat() * 12

                    val bmi = 703 * (ImperialUnitWeightValue / (HeightValue * HeightValue))
                    displayBMIResult(bmi)
                } else{
                    Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        makeVisibleMetricUnitsView()
        binding.rgUnits.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleImperialUnitsView()
            }
        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding.tilMetricUnitWeight.visibility = View.VISIBLE
        binding.tilMetricUnitHeight.visibility = View.VISIBLE

        binding.etMetricUnitHeight.text!!.clear()
        binding.etMetricUnitWeight.text!!.clear()

        binding.tilImperialUnitWeight.visibility = View.GONE
        binding.llImperialUnitsHeight.visibility = View.GONE

        binding.llDisplayBMIResult.visibility = View.INVISIBLE
    }

    private fun makeVisibleImperialUnitsView(){
        currentVisibleView = IMPERIAL_UNITS_VIEW
        binding.tilMetricUnitWeight.visibility = View.GONE
        binding.tilMetricUnitHeight.visibility = View.GONE

        binding.etImperialUnitWeight.text!!.clear()
        binding.etImperialUnitHeightFeet.text!!.clear()
        binding.etImperialUnitHeightInch.text!!.clear()

        binding.tilImperialUnitWeight.visibility = View.VISIBLE
        binding.llImperialUnitsHeight.visibility = View.VISIBLE

        binding.llDisplayBMIResult.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float){
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "You need to eat more so you can gain weight. " +
                    "Please take better care of yourself!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Severely underweight"
            bmiDescription = "You need to eat more so you can gain weight. " +
                    "Please take better care of yourself!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Underweight"
            bmiDescription = "You need to eat more so you can gain weight. " +
                    "Please take better care of yourself!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in good shape."
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight"
            bmiDescription = "You need to take care of yourself. Try to work out more."
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "Moderately Obese"
            bmiDescription = "You need to take care of yourself. Please work out more."
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "Severely Obese"
            bmiDescription = "You are in a dangerous condition. You need to act now!"
        } else {
            bmiLabel = "Very Severely Obese"
            bmiDescription = "You are in a dangerous condition. You need to act now!"
        }

        binding.llDisplayBMIResult.visibility = View.VISIBLE
        /*
        binding.tvYourBMI.visibility = View.VISIBLE
        binding.tvBMIValue.visibility = View.VISIBLE
        binding.tvBMIType.visibility = View.VISIBLE
        binding.tvBMIDescription.visibility = View.VISIBLE
        */

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(
            2, RoundingMode.HALF_EVEN).toString()

        binding.tvBMIValue.text = bmiValue
        binding.tvBMIType.text = bmiLabel
        binding.tvBMIDescription.text = bmiDescription
    }

    private fun validateMetricUnits(): Boolean{
        var isValid = true
        if (binding.etMetricUnitWeight.text.toString().isEmpty()){
            isValid = false
        } else if (binding.etMetricUnitHeight.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun validateImperialUnits(): Boolean{
        var isValid = true
        when {
            binding.etImperialUnitWeight.text.toString().isEmpty() -> isValid = false
            binding.etImperialUnitHeightFeet.text.toString().isEmpty() -> isValid = false
            binding.etImperialUnitHeightInch.text.toString().isEmpty() -> isValid = false
        }
        return isValid
    }
}