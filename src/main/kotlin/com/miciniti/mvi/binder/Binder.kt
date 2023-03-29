package com.miciniti.mvi.binder

import com.miciniti.mvi.binder.interfaces.BinderConsumer
import com.miciniti.mvi.binder.interfaces.BinderInterceptor
import com.miciniti.mvi.binder.interfaces.BinderProducer

class Binder(val name: String = "") {

    private val connections = mutableListOf<BinderConnection<*>>()

    fun <Out> bind(pair: Pair<BinderProducer<Out>, BinderConsumer>): BinderConnection<Out> {
        val connection = BinderConnection(
            from = pair.first,
            to = pair.second,
        )
        return bind(connection)
    }

    fun <Out> bind(connection: BinderConnection<Out>): BinderConnection<Out> {
        connection.name = name
        connection.connect()

        connections.add(connection)
        return connection
    }

    fun destroy() {
        for (connection in connections) {
            connection.disconnect()
        }
    }

    companion object {
        private var middlewares = mutableListOf<BinderInterceptor>()

        fun registerMiddleware(middleware: BinderInterceptor) {
            middlewares.add(middleware)
        }

        fun intercept(name: String, event: Any): Any {
            var interceptedEvent = event
            for (middleware in middlewares) {
                val newEvent = middleware.invoke(name, event)
                if (newEvent != null) {
                    interceptedEvent = newEvent
                }
            }
            return interceptedEvent
        }
    }

}

