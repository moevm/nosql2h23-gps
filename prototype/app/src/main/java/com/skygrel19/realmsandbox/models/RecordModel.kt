package com.skygrel19.realmsandbox.models

import com.skygrel19.realmsandbox.IRealmModel
import com.skygrel19.realmsandbox.exceptions.WrongFormatException
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.Date


open class RecordModel : RealmObject, IRealmModel<RecordModel> {
    @PrimaryKey
    var recordId: ObjectId = ObjectId()
    var distance: Double = 0.0
    var time: Long = 0L
    var sessionId: ObjectId = ObjectId()
    var createdAt: Long = 0

    override fun toString(): String {
        return "RecordModel(recordId=$recordId, distance=$distance, time=$time, sessionId=$sessionId, createdAt=$createdAt)"
    }

    override fun hint(): String {
        return "distance: Double, time: Long, sessionId: ObjectId, createdAt: Long"
    }
    override fun create_from_text(text: String): RecordModel {
        try {
            val parts = text.split(",").map { it.trim() }
            val record = RecordModel().apply {
                distance = parts[0].toDouble()
                time = parts[1].toLong()
                sessionId = ObjectId(parts[2])
                createdAt = parts[3].toLong()
            }
            return record
        } catch (e: Exception) {
            throw WrongFormatException(this.hint())
        }
    }
}