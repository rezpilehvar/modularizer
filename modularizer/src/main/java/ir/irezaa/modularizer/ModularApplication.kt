package ir.irezaa.modularizer

import android.app.Application
import android.content.Context
import kotlin.reflect.KClass

open class ModularApplication : Application(), ModuleHost {
    private val modules = mutableMapOf<KClass<Module>, Module>()
    private val installingModules = hashSetOf<KClass<Module>>()

    protected fun start() {
        if (modules.isEmpty()) {
            return
        }

        modules.values.forEach { it ->
            it.onStart(this)
        }
    }

    override fun installModule(module : Module) {
        if (installingModules.contains(module::class) || modules.containsKey(module::class)) {
            println("Module::duplicated module ${module::class.simpleName}")
            return
        }

        println("Module::installing ${module::class.simpleName}")
        installingModules.add(module.javaClass.kotlin)

        module.dependencies()?.forEach {
            println("Module::installing ${it::class.simpleName} dependency for ${module::class.simpleName}")
            installModule(it)
        }

        modules[module.javaClass.kotlin] = module
        installingModules.remove(module.javaClass.kotlin)

        println("Module::${module.javaClass.kotlin.simpleName} installed")
    }
}

interface ModuleHost {
    fun installModule(module: Module)
}

interface Module {
    fun dependencies(): List<Module>? {
        return null
    }

    fun onStart(context: Context)
}