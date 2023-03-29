package com.miciniti.mvi.binder

import com.miciniti.mvi.binder.interfaces.BinderConsumer
import com.miciniti.mvi.binder.interfaces.BinderProducer
import com.miciniti.mvi.binder.interfaces.BinderTransformer

infix fun <Out> Pair<BinderProducer<Out>, BinderConsumer>.using(transformer: BinderTransformer?): BinderConnection<Out> =
    BinderConnection(
        from = first,
        to = second,
        transformer = transformer
    )

infix fun <Out> Pair<BinderProducer<Out>, BinderConsumer>.intercept(dummy: Any?): BinderConnection<Out> =
    BinderConnection(
        from = first,
        to = second,
        intercept = true
    )

infix fun <Out> BinderConnection<Out>.intercept(dummy: Any?): BinderConnection<Out> {
    intercept = true
    return this
}
