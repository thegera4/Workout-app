package com.app.kot_workout_app.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.kot_workout_app.models.ExerciseModel
import com.app.kot_workout_app.adapters.ExerciseStatusAdapter
import com.app.kot_workout_app.constants.Constants
import com.app.kot_workout_app.constants.Constants.Companion.defaultExerciseList
import com.app.kot_workout_app.R
import com.app.kot_workout_app.databinding.ActivityExerciseBinding
import com.app.kot_workout_app.databinding.DialogCustomBackConfirmationBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityExerciseBinding

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration: Long = 60 //Counter for rest time

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration: Long = 60 //Counter for exercise time

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    private var mLevel: String? = null

    private var currentSeries = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mLevel = intent.getStringExtra(Constants.LEVELS)

        //ads initialization
        MobileAds.initialize(this@ExerciseActivity)
        //Banner ad request
        val adReq = AdRequest.Builder().build()

        binding.adViewBMI.loadAd(adReq)
        setSupportActionBar(binding.toolbarExerciseActivity)
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarExerciseActivity.setNavigationOnClickListener {
            customDialogForBackButton()
        }



        tts = TextToSpeech(this, this)

        exerciseList = defaultExerciseList()

            setupRestView()

            setupExerciseStatusRecyclerView()

    }

    override fun onDestroy() {
        if (restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if (tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player !=null ){
            player!!.stop()
        }

        super.onDestroy()
    }

    private fun setRestProgressBar(){
        binding.progressBar.progress = restProgress

        restTimer = object: CountDownTimer(restTimerDuration * 1000, 1000){

            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding.progressBar.progress = (restTimerDuration - restProgress).toInt()
                binding.tvTimer.text = (restTimerDuration - restProgress).toString()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setupExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar(){
        binding.progressBarExercise.progress = exerciseProgress

        exerciseTimer = object: CountDownTimer(exerciseTimerDuration * 1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding.progressBarExercise.progress = (exerciseTimerDuration - exerciseProgress).toInt()
                binding.tvExerciseTimer.text = (exerciseTimerDuration-exerciseProgress).toString()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {

                if(mLevel == "1"){
                    if (currentExercisePosition < exerciseList?.size!! - 1){
                        exerciseList!![currentExercisePosition].setIsSelected(false)
                        exerciseList!![currentExercisePosition].setIsCompleted(true)
                        exerciseAdapter!!.notifyDataSetChanged()
                        setupRestView()
                    } else{
                        finish()
                        val intent = Intent(this@ExerciseActivity,
                            FinishActivity::class.java)
                        startActivity(intent)
                    }
                } else if(mLevel == "2"){
                    //Code to reset the recyclerview for the exercise number
                    if (currentExercisePosition == (exerciseList?.size!! - 1)){
                        for (i in 0 until exerciseList?.size!!){
                            exerciseList!![i].setIsCompleted(false)
                            exerciseList!![i].setIsSelected(false)
                        }
                    }
                    //Code to go through all exercises in exerciseList
                    if (currentExercisePosition < exerciseList?.size!! - 1) {
                        exerciseList!![currentExercisePosition].setIsSelected(false)
                        exerciseList!![currentExercisePosition].setIsCompleted(true)
                        exerciseAdapter!!.notifyDataSetChanged()

                        setupRestView()
                    } else{
                        currentSeries++
                        if (currentSeries == 2){
                            finish()
                            val intent = Intent(this@ExerciseActivity,
                            FinishActivity::class.java)
                            startActivity(intent)
                        }else{
                            currentExercisePosition = -1
                            setupRestView()
                        }
                    }
                } else if(mLevel == "3"){
                    //Code to reset the recyclerview for the exercise number
                    if (currentExercisePosition == (exerciseList?.size!! - 1)){
                        for (i in 0 until exerciseList?.size!!){
                            exerciseList!![i].setIsCompleted(false)
                            exerciseList!![i].setIsSelected(false)
                        }
                    }
                    //Code to go through all exercises in exerciseList
                    if (currentExercisePosition < exerciseList?.size!! - 1) {
                        exerciseList!![currentExercisePosition].setIsSelected(false)
                        exerciseList!![currentExercisePosition].setIsCompleted(true)
                        exerciseAdapter!!.notifyDataSetChanged()
                        setupRestView()
                    } else{
                        currentSeries++
                        if (currentSeries == 3){
                            finish()
                            val intent = Intent(this@ExerciseActivity,
                                FinishActivity::class.java)
                            startActivity(intent)
                        }else{
                            currentExercisePosition = -1
                            setupRestView()
                        }
                    }
                }

            }
        }.start()
    }

    private fun setupRestView(){

        try {
            player = MediaPlayer.create(applicationContext, R.raw.whistle)
            player!!.isLooping = false
            player!!.start()

        } catch (e: Exception){
            e.printStackTrace()
        }

        binding.llRestView.visibility = View.VISIBLE
        binding.llExerciseView.visibility = View.GONE

        if (restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }

        setRestProgressBar()

        binding.tvUpcomingExerciseName.text = exerciseList!![currentExercisePosition + 1].getName()
    }

    private fun setupExerciseView(){

        binding.llRestView.visibility = View.GONE
        binding.llExerciseView.visibility = View.VISIBLE

        if (exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        setExerciseProgressBar()

        binding.ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding.tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this@ExerciseActivity,
                    "Text to speech failed due to language is not supported or " +
                            "installed in your phone",
                    Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@ExerciseActivity, "TTS initialization failed!",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun setupExerciseStatusRecyclerView(){
        binding.rvExerciseStatus.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this)
        binding.rvExerciseStatus.adapter = exerciseAdapter
    }

    private fun customDialogForBackButton(){

        val customDialog = Dialog(this)
        val binding: DialogCustomBackConfirmationBinding =
            DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(binding.root)

        binding.btnYES.setOnClickListener {
            finish()
            customDialog.dismiss()
        }

        binding.btnNO.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()

    }

}