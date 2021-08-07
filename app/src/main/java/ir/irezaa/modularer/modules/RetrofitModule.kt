package ir.irezaa.modularer.modules

import android.content.Context
import ir.irezaa.modularizer.Module

object RetrofitModule : Module {

    override fun dependencies(): List<Module>? {
        return listOf(
            ConfigsModule
        )
    }

    override fun onStart(context: Context) {
        println("Module::retrofit onCreate ${ConfigsModule.serverAddress}")
    }
}
