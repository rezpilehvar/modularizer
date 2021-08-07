package ir.irezaa.modularer.modules

import android.content.Context
import ir.irezaa.modularizer.Module

object ConfigsModule : Module {
    override fun dependencies(): List<Module>? {
        return listOf(
            ApiModule
        )
    }
    lateinit var serverAddress : String

    override fun onStart(context: Context) {
        serverAddress = "http://google.com"
        println("Module::configs onCreate")
    }
}