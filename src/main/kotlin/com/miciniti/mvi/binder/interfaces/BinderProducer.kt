package com.miciniti.mvi.binder.interfaces

interface BinderProducer<IntentType> {

    fun subscribe(observer: BinderObserver<IntentType>)
    fun unsubscribe(observer: BinderObserver<IntentType>)

}
