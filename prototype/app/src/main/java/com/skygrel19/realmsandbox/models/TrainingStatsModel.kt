package com.skygrel19.realmsandbox.models

import com.skygrel19.realmsandbox.IRealmModel
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class TrainingStatsModel : RealmObject, IRealmModel<TrainingStatsModel> {
    @PrimaryKey
    var statsId: ObjectId = ObjectId()
    var sessionId: ObjectId = ObjectId()
    var averageSpeed: Double = 0.0
    var maxSpeed: Double = 0.0
    var averagePace: Double = 0.0
    var caloriesBurned: Int = 0

    override fun toString(): String {
        return "TrainingStatsModel(statsId=$statsId, sessionId=$sessionId, averageSpeed=$averageSpeed, maxSpeed=$maxSpeed, averagePace=$averagePace, caloriesBurned=$caloriesBurned)"
    }

    override fun create_from_text(text: String): TrainingStatsModel {
        val parts = text.split(",")
        val stats = TrainingStatsModel().apply {
            averageSpeed = parts[0].toDouble()
            maxSpeed = parts[1].toDouble()
            averagePace = parts[2].toDouble()
            caloriesBurned = parts[3].toInt()
        }
        return stats
    }
}