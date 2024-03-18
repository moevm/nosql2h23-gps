package com.skygrel19.realmsandbox.models

import com.skygrel19.realmsandbox.IRealmModel
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class DistanceRecord : RealmObject, IRealmModel<DistanceRecord> {
    @PrimaryKey
    var distanceRecordId: ObjectId = ObjectId()
    var userId: ObjectId = ObjectId()
    var distance: Double = 0.0
    var bestTime: Long = 0L
    var bestSessionId: ObjectId = ObjectId()

    override fun toString(): String {
        return "DistanceRecord(distanceRecordId=$distanceRecordId, userId=$userId, distance=$distance, bestTime=$bestTime, bestSessionId=$bestSessionId)"
    }

    override fun create_from_text(text: String): DistanceRecord {
        val parts = text.split(",")
        val distanceRecord = DistanceRecord().apply {
            userId = ObjectId(parts[0])
            distance = parts[1].toDouble()
            bestTime = parts[2].toLong()
            bestSessionId = ObjectId(parts[3])
        }
        return distanceRecord
    }
}