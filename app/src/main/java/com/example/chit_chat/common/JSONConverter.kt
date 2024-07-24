package com.example.chit_chat.common

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JSONConverter {
    inline fun <reified T> unpackObjectFromJSON(json: String): T? {
        return if (json.isEmpty()) {
            null
        } else {
            val type = object : TypeToken<T>() {}.type
            return Gson().fromJson(json, type)
        }
    }

    inline fun <reified T> packObjectToJSON(packingObject: T): String {
        return Gson().toJson(packingObject)
    }

    inline fun <reified T> unpackListFromJSON(json: String): List<T> {
        return if (json.isEmpty()) {
            mutableListOf()
        } else {
            val listType = object : TypeToken<List<T>>() {}.type
            return Gson().fromJson(json, listType)
        }
    }

    inline fun <reified T> packListToJSON(list: List<T>): String {
        return Gson().toJson(list)
    }
}