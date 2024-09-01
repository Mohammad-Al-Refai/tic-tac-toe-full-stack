package com.mohammad.tec.tac_toe.config

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class CoroutineScopeConfig {

    @Bean
    fun coroutineScope(): CoroutineScope {
        // Using SupervisorJob to ensure child coroutines don't cancel the whole scope on failure
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}