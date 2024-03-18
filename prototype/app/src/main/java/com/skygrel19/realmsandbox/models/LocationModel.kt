package com.skygrel19.realmsandbox.models

import com.skygrel19.realmsandbox.IRealmModel
import com.skygrel19.realmsandbox.exceptions.WrongFormatException
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.Date

open class LocationModel : RealmObject, IRealmModel<LocationModel> {
    @PrimaryKey
    var locationId: ObjectId = ObjectId()
    var sessionId: ObjectId = ObjectId()
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var timestamp: Long = 0

    override fun toString(): String {
        return "LocationModel(locationId=$locationId, sessionId=$sessionId, latitude=$latitude, longitude=$longitude, timestamp=$timestamp)"
    }
    override fun hint(): String {
        return "latitude: Double, longitude: Double, timestamp: Long"
    }

    override fun create_from_text(text: String): LocationModel {
        try {
            val parts = text.split(",").map { it.trim() }
            val location = LocationModel().apply {
                latitude = parts[0].toDouble()
                longitude = parts[1].toDouble()
                timestamp = parts[2].toLong()
            }
            return location
        } catch (e: Exception) {
            throw WrongFormatException(this.hint())
        }
    }
}
