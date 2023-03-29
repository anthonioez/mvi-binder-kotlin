package com.miciniti.mvi.binder

import com.miciniti.mvi.binder.interfaces.BinderConsumer
import com.miciniti.mvi.binder.interfaces.BinderObserver
import com.miciniti.mvi.binder.interfaces.BinderProducer
import com.miciniti.mvi.binder.interfaces.BinderTransformer

data class BinderConnection<Out>(
    val from: BinderProducer<Out>,
    val to: BinderConsumer,
    val transformer: BinderTransformer? = null,
    var name: String = "",
    var intercept: Boolean = false
) {

    private var listener: BinderObserver<Out>? = null

    fun connect() {
        listener = object : BinderObserver<Out> {
            override fun onEvent(event: Out) {
                val newEvent = event as Any?
                if (newEvent != null) {
                    val interceptedEvent = if (intercept) {
                        Binder.intercept(name, newEvent)
                    } else {
                        newEvent
                    }

                    val finalEvent = transformer?.invoke(interceptedEvent) ?: interceptedEvent
                    finalEvent.let { to.consume(it) }
                }
            }
        }
        listener?.let { from.subscribe(it) }
    }

    fun disconnect() {
        listener?.let { from.unsubscribe(it) }
    }

}
