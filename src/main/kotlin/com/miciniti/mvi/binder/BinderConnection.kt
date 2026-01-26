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
                if (event == null) {
                    return
                }

                val processedEvent: Any = if (intercept) {
                    Binder.intercept(name, event as Any)
                } else {
                    event as Any
                }

                val finalEvent = transformer?.invoke(processedEvent) ?: processedEvent
                to.consume(finalEvent)
            }
        }
        listener?.let { from.subscribe(it) }
    }

    fun disconnect() {
        listener?.let { from.unsubscribe(it) }
    }

}
