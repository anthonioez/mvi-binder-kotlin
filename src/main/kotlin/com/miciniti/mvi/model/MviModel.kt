package com.miciniti.mvi.model

import com.miciniti.mvi.binder.BinderEvent

import com.miciniti.mvi.binder.MviExecutor
import com.miciniti.mvi.binder.interfaces.BinderConsumer

abstract class MviModel<I, E> : BinderConsumer,
    BinderEvent<E>() {

    override fun consume(event: Any) {
    }

    protected fun serialExecute(run: () -> Unit) {
        if (executor.isTerminated || executor.isShutdown) {
            return
        }

        executor.execute(run)
    }

}