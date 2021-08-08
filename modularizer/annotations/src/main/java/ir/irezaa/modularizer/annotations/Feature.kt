package ir.irezaa.modularizer.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Feature (vararg val dependencies : KClass<*>)