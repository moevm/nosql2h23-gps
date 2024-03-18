package com.skygrel19.realmsandbox

import io.realm.kotlin.types.RealmObject
import kotlin.reflect.KClass

interface IRealmModel <T: RealmObject> {
    fun name(): String {
        return this::class.simpleName ?: "Unknown"
    }
    fun create_from_text(text: String): T

    fun as_class(): KClass<T> {
        return this::class as KClass<T>
    }
}