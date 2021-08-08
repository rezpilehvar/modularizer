package ir.irezaa.modularer.modules

import android.content.Context
import ir.irezaa.modularer.di.ApiComponent
import ir.irezaa.modularer.di.DaggerApiComponent
import ir.irezaa.modularizer.Module
import ir.irezaa.modularizer.annotations.Feature

@Feature()
object ApiModule : Module {
    lateinit var component: ApiComponent

    override fun dependencies(): List<Module>? {
        return listOf(
            RetrofitModule
        )
    }

    override fun onStart(context: Context) {
        println("Module::api onCreate")
        component = DaggerApiComponent.builder()
            .retrofitComponent(RetrofitModule.component)
            .build()

//        println("Dagger::api someString: ${component.getSomeString()}")
    }
}