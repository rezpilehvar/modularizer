package ir.irezaa.modularer.modules

import android.content.Context
import ir.irezaa.modularer.di.DaggerRetrofitComponent
import ir.irezaa.modularer.di.RetrofitComponent
import ir.irezaa.modularizer.Module

object RetrofitModule : Module {
    lateinit var component : RetrofitComponent

    override fun dependencies(): List<Module> {
        return listOf(ConfigsModule)
    }

    override fun onStart(context: Context) {
        println("Module::retrofit onCreate")
        component = DaggerRetrofitComponent.builder()
            .build()
    }
}
