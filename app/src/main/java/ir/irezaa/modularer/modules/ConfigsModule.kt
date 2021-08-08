package ir.irezaa.modularer.modules

import android.content.Context
import ir.irezaa.modularizer.Module

object ConfigsModule : Module {

    override fun onStart(context: Context) {
        println("Module::configs onCreate")
    }
}