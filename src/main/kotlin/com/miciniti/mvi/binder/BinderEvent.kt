package com.miciniti.mvi.binder

import com.miciniti.mvi.binder.interfaces.BinderObserver
import com.miciniti.mvi.binder.interfaces.BinderProducer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

open class BinderEvent<IntentType> : BinderProducer<IntentType> {

    protected val executor: ExecutorService by lazy { Executors.newSingleThreadExecutor() }

    private var listeners: MutableList<BinderObserver<IntentType>> = ArrayList()

    open fun produce(intent: IntentType) {
        try {
            executor.execute {
                callListeners(intent)
            }
        } catch (_: Exception) {
        }
    }

    open fun produceInline(intent: IntentType) {
        try {
            callListeners(intent)
        } catch (_: Exception) {
        }
    }

    private fun callListeners(event: IntentType) {
        val list = synchronized(listeners) {
             ArrayList(listeners)
        }
        for (listener in list) {
            listener.onEvent(event)
        }
    }

    override fun subscribe(observer: BinderObserver<IntentType>) {
        synchronized(listeners) {
            if (!listeners.contains(observer)) {
                listeners.add(observer)
            }
        }
    }

    override fun unsubscribe(observer: BinderObserver<IntentType>) {
        synchronized(listeners) {
            if (listeners.contains(observer)) {
                listeners.remove(observer)
            }
        }
    }

    open fun destroy() {
        // executor.shutdown() // Shared executor, do not shutdown
        synchronized(listeners) {
            listeners.clear()
        }
    }
}