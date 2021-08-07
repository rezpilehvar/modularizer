package ir.irezaa.modularizer

import android.app.Application
import android.content.Context
import kotlin.reflect.KClass

open class ModularApplication : Application(), ModuleHost {
    private val modules = mutableMapOf<KClass<Module>, Module>()
    private val installingModules = hashSetOf<KClass<Module>>()

    final override fun installModule(module: Module) {
        if (installingModules.contains(module.javaClass.kotlin) || modules.containsKey(module.javaClass.kotlin)) {
            println("Module::duplicated module ${module.javaClass.kotlin.simpleName}")
            return
        }

        println("Module::installing ${module.javaClass.kotlin.simpleName}")
        installingModules.add(module.javaClass.kotlin)

        module.dependencies()?.forEach {
            println("Module::installing ${it.javaClass.kotlin.simpleName} dependency for ${module.javaClass.kotlin.simpleName}")
            installModule(it)
        }

        modules[module.javaClass.kotlin] = module
        installingModules.remove(module.javaClass.kotlin)

        println("Module::${module.javaClass.kotlin.simpleName} installed")
    }

    protected fun start() {
        if (modules.isEmpty()) {
            return
        }

        modules.values.forEach { it ->
            it.onStart(this)
        }
    }
}

interface ModuleHost {
    fun installModule(module: Module)
}

interface Module {
    fun dependencies() : List<Module>? {
        return null
    }
    fun onStart(context: Context)
}