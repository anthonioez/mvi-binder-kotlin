package com.miciniti.mvi.binder.interfaces

interface BinderObserver<EventType> {

    fun onEvent(event: EventType)

}