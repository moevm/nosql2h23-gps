package com.skygrel19.realmsandbox.models

import com.skygrel19.realmsandbox.IRealmModel
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.Date
import org.mongodb.kbson.ObjectId

open class UserModel : RealmObject, IRealmModel<UserModel> {
    @PrimaryKey
    var userId: ObjectId = ObjectId()
    var name: String = ""
    var color: String = ""
    var createdAt: Long = 0

    override fun toString(): String {
        return "UserModel(userId=$userId, name='$name', color='$color', createdAt=$createdAt)"
    }

    override fun create_from_text(text: String): UserModel {
        val parts = text.split(",")
        val user = UserModel()
        user.name = parts[0]
        user.color = parts[1]
        user.createdAt = parts[2].toLong()
        return user
    }
}