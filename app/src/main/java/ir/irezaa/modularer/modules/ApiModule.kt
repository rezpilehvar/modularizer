package ir.irezaa.modularer.modules

import android.content.Context
import ir.irezaa.modularizer.Module
import ir.irezaa.modularizer.ModuleHost

object ApiModule : Module {
    override fun dependencies(): List<Module>? {
        return mutableListOf(
            RetrofitModule
        )
    }

    override fun onStart(context: Context) {
        println("Module::api onCreate")
    }
}