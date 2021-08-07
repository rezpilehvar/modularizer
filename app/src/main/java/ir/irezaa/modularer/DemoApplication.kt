package ir.irezaa.modularer

import ir.irezaa.modularer.modules.ApiModule
import ir.irezaa.modularer.modules.RetrofitModule
import ir.irezaa.modularizer.ModularApplication

class DemoApplication : ModularApplication() {
    override fun onCreate() {
        super.onCreate()
        installModule(ApiModule)
        start()
    }
}



