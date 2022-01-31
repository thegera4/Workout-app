package com.app.kot_workout_app.constants

import com.app.kot_workout_app.models.ExerciseModel
import com.app.kot_workout_app.R

class Constants {
    companion object {

        var LEVELS: String = "level"

        fun defaultExerciseList(): ArrayList<ExerciseModel>{
            val exerciseList = ArrayList<ExerciseModel>()

            val jumpingJacks = ExerciseModel(1, "Jumping Jacks",
                R.drawable.jumpingjacks, false, false )
            exerciseList.add(jumpingJacks)

            val squats = ExerciseModel(2, "Squats",
                R.drawable.squats, false, false )
            exerciseList.add(squats)

            val highKnees = ExerciseModel(3, "High Knees",
                R.drawable.highknees, false, false )
            exerciseList.add(highKnees)

           val lunges = ExerciseModel(4, "Lunges",
                R.drawable.lunges, false, false )
            exerciseList.add(lunges)

            val pushUps = ExerciseModel(5, "Push Ups",
                R.drawable.pushups, false, false )
            exerciseList.add(pushUps)

            val climbers = ExerciseModel(6, "Climbers",
                R.drawable.climbers, false, false )
            exerciseList.add(climbers)

            val plankLegRises = ExerciseModel(7, "Plank Leg Raises",
                R.drawable.planklegrises, false, false )
            exerciseList.add(plankLegRises)

            val legRises = ExerciseModel(8, "Leg Raises",
                R.drawable.legraises, false, false )
            exerciseList.add(legRises)

            val kneePullIns = ExerciseModel(9, "Knee Pull-Ins",
                R.drawable.kneepullins, false, false )
            exerciseList.add(kneePullIns)

            val bicycleCrunches = ExerciseModel(10, "Bicycle Crunches",
                R.drawable.bicyclecrunches, false, false )
            exerciseList.add(bicycleCrunches)

            return exerciseList
        }
    }
}