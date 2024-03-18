package com.skygrel19.realmsandbox.models

import com.skygrel19.realmsandbox.IRealmModel
import com.skygrel19.realmsandbox.exceptions.WrongFormatException
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.Date

open class TrainingSessionModel : RealmObject, IRealmModel<TrainingSessionModel> {
    @PrimaryKey
    var sessionId: ObjectId = ObjectId()
    var userId: ObjectId = ObjectId()
    var startTime: Long = 0
    var endTime: Long = 0
    var totalDistance: Double = 0.0
    var route: RealmList<LocationModel> = realmListOf()
    var records: RealmList<RecordModel> = realmListOf()
    var pausedTime: Long = 0L
//    var stats: TrainingStatsModel? = null

    override fun toString(): String {
        return "TrainingSessionModel(sessionId=$sessionId, userId=$userId, startTime=$startTime, endTime=$endTime, totalDistance=$totalDistance, route=$route, records=$records, pausedTime=$pausedTime)"
    }

    override fun hint(): String {
        return "userId: ObjectId, startTime: Long, endTime: Long, totalDistance: Double, pausedTime: Long"
    }

    override fun create_from_text(text: String): TrainingSessionModel {
        try {
            val parts = text.split(",").map { it.trim() }
            val session = TrainingSessionModel().apply {
                userId = ObjectId(parts[0])
                startTime = parts[1].toLong()
                endTime = parts[2].toLong()
                totalDistance = parts[3].toDouble()
                pausedTime = parts[4].toLong()
            }
            return session
        } catch (e: Exception) {
            throw WrongFormatException(this.hint())
        }
    }
}