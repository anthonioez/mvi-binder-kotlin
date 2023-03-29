package com.miciniti.mvi.binder

import com.miciniti.mvi.binder.interfaces.BinderObserver
import com.miciniti.mvi.binder.interfaces.BinderProducer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

open class BinderEvent<IntentType> : BinderProducer<IntentType> {

    private val executor: ExecutorService by lazy { Executors.newSingleThreadExecutor() }

    private var listeners: MutableList<BinderObserver<IntentType>> = ArrayList()

    open fun produce(intent: IntentType) {
        try {
            executor.execute {
                callListeners(intent)
            }
        } catch (e: Exception) {
        }
    }

    private fun callListeners(event: IntentType) {
        val list = ArrayList(listeners)
        for (listener in list) {
            listener.onEvent(event)
        }
    }

    override fun subscribe(observer: BinderObserver<IntentType>) {
        if (!listeners.contains(observer)) {
            listeners.add(observer)
        }
    }

    override fun unsubscribe(observer: BinderObserver<IntentType>) {
        if (listeners.contains(observer)) {
            listeners.remove(observer)
        }
    }

    open fun destroy() {
        executor.shutdown()
        listeners.clear()
    }
}