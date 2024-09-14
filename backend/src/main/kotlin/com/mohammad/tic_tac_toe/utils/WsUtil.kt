package com.mohammad.tic_tac_toe.utils

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import com.mohammad.tic_tac_toe.models.WsCommand
import com.mohammad.tic_tac_toe.responses.IGameResponse
import java.io.IOException

fun messageToWsCommand(message: String, objectMapper: ObjectMapper): WsCommand? {
    return try {
        val jsonObject = objectMapper.readValue(message, WsCommand::class.java)
        jsonObject

    } catch (e: JsonParseException) {
        null
    }
}

fun dtoToByteArray(dto: IGameResponse, objectMapper: ObjectMapper): ByteArray {
    return try {
        objectMapper.writeValueAsBytes(dto)
    } catch (e: IOException) {
        byteArrayOf()
    }
}