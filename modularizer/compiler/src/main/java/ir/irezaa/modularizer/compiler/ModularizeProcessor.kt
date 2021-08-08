package ir.irezaa.modularizer.compiler

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import ir.irezaa.modularizer.annotations.Feature
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.*
import javax.tools.Diagnostic
import javax.lang.model.element.TypeElement

import javax.lang.model.type.TypeMirror


@AutoService(Processor::class)
class ModularizerProcessor : AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Feature::class.java.name)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        val fileName = "ModularizedApplication"
        val fileBuilder = FileSpec.builder("", fileName)
        val classBuilder = TypeSpec.classBuilder(fileName)

        val features = mutableSetOf<ClassName>()

        roundEnv?.getElementsAnnotatedWith(Feature::class.java)?.forEach {
            if (it.kind != ElementKind.CLASS) {
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Only classes can be annotated"
                )
                return true
            }

            if (!processingEnv.typeUtils.isAssignable(
                    it.asType(),
                    processingEnv.getTypeUtils().erasure(
                        processingEnv.getElementUtils()
                            .getTypeElement("ir.irezaa.modularizer.Module").asType()
                    )
                )
            ) {
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "$it must be extended from Module interface"
                )
                return true
            }

            val feature = processAnnotation(it)
            features.add(feature)

            processingEnv.messager.printMessage(
                Diagnostic.Kind.WARNING,
                "processed $it - features size = ${features.size}"
            )
        }

        classBuilder
            .superclass(
                processingEnv.getElementUtils()
                    .getTypeElement("ir.irezaa.modularizer.ModularApplication").asType()
                    .asTypeName()
            )

        val funBuilder = FunSpec.builder("onCreate")
            .addModifiers(KModifier.OVERRIDE)
            .addStatement("super.onCreate()")


        val installFeatureMethods = StringBuilder()
        features.forEach {
            installFeatureMethods.append("installFeature(${it.simpleName()})\n ")
        }

        funBuilder.addStatement("// feature count = ${features.size}")

        processingEnv.messager.printMessage(
            Diagnostic.Kind.WARNING,
            "add statement - features size = ${features.size}"
        )

        funBuilder.addStatement(installFeatureMethods.toString())

        classBuilder.addFunction(funBuilder.build())

        val file = fileBuilder.addType(classBuilder.build()).build()
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        file.writeTo(File(kaptKotlinGeneratedDir!!))
        return false
    }

    private fun processAnnotation(element: Element): ClassName {
        val className = element.simpleName.toString()
        val pack = processingEnv.elementUtils.getPackageOf(element).toString()

        return ClassName(pack, className)
    }


    private fun getAnnotationMirror(typeElement: TypeElement, clazz: Class<*>): AnnotationMirror? {
        val clazzName = clazz.name
        for (m in typeElement.annotationMirrors) {
            if (m.annotationType.toString() == clazzName) {
                return m
            }
        }
        return null
    }

    private fun getAnnotationValue(
        annotationMirror: AnnotationMirror,
        key: String
    ): AnnotationValue? {
        for ((key1, value) in annotationMirror.elementValues) {
            if (key1!!.simpleName.toString() == key) {
                return value
            }
        }
        return null
    }

    private fun asTypeElement(typeMirror: TypeMirror): TypeElement? {
        val typeUtils = processingEnv.typeUtils
        return typeUtils.asElement(typeMirror) as TypeElement
    }
}