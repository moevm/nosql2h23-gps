package com.skygrel19.realmsandbox.models

import com.skygrel19.realmsandbox.IRealmModel
import com.skygrel19.realmsandbox.exceptions.WrongFormatException
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

    override fun hint(): String {
        return "averageSpeed: Double, maxSpeed: Double, averagePace: Double, caloriesBurned: Int"
    }

    override fun create_from_text(text: String): TrainingStatsModel {
        try {
            val parts = text.split(",").map { it.trim() }
            val stats = TrainingStatsModel().apply {
                sessionId = ObjectId(parts[0])
                averageSpeed = parts[1].toDouble()
                maxSpeed = parts[2].toDouble()
                averagePace = parts[3].toDouble()
                caloriesBurned = parts[4].toInt()
            }
            return stats
        } catch (e: Exception) {
            throw WrongFormatException(this.hint())
        }
    }
}