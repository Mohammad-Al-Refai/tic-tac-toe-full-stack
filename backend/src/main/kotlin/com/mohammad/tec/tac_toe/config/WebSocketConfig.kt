package com.mohammad.tec.tac_toe.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.mohammad.tec.tac_toe.WebSocketHandler
import com.mohammad.tec.tac_toe.services.GameService
import com.mohammad.tec.tac_toe.services.PlayerService
import com.mohammad.tec.tac_toe.services.SessionService
import kotlinx.coroutines.CoroutineDispatcher
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor


@Configuration
@EnableWebSocket
class WebSocketConfig(
    val objectMapper: ObjectMapper,
    val dispatcher: CoroutineDispatcher,
    val playerService: PlayerService,
    val gameService: GameService,
    val sessionService: SessionService
) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(
            WebSocketHandler(objectMapper, dispatcher, playerService, gameService, sessionService),
            "/ws"
        )
            .addInterceptors(HttpSessionHandshakeInterceptor()).setAllowedOrigins("*")
    }
}