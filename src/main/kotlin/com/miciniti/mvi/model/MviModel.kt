package com.miciniti.mvi.model

import android.os.Handler
import android.os.Looper
import com.miciniti.mvi.binder.BinderEvent
import com.miciniti.mvi.binder.interfaces.BinderConsumer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

abstract class MviModel<I, E> : BinderConsumer,
    BinderEvent<E>() {

    private val executor: ExecutorService by lazy { Executors.newSingleThreadExecutor() }

    private var handler = Handler(Looper.getMainLooper())

    override fun consume(event: Any) {
        handler.post {
            consumeEvent(event)
        }
    }

    open fun consumeEvent(event: Any) {
    }

    override fun destroy() {
        super.destroy()
        executor.shutdown()
    }

    protected fun serialExecute(run: () -> Unit) {
        executor.execute(run)
    }

}