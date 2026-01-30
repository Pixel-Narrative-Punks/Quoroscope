import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinMultiplatformAndroidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidLibrary {
        namespace = "com.pixnpunk.quoroscope.share"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:utils"))
            implementation(project(":core:database"))
            implementation(project(":core:dto"))
            implementation(project(":core:network"))

            implementation(project(":features:stock:domain"))

            implementation(project(":features:addons:presentation"))
            implementation(project(":features:dialogs:presentation"))
            implementation(project(":features:details:presentation"))
            implementation(project(":features:settings:presentation"))
            implementation(project(":features:stock:presentation"))

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material3.windowSizeClass)
            implementation(libs.compose.icons.ext)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.util)
            implementation(libs.compose.ui.preview)
            implementation(libs.compose.resources)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.kotlinx.serialization.json)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)

            implementation(libs.essenty.lifecycle)
            implementation(libs.decompose.core)
            implementation(libs.decompose.ext)
            implementation(libs.decompose.ext.exp)
            implementation(libs.mvikotlin.core)
            implementation(libs.mvikotlin.main)
            implementation(libs.mvikotlin.coro.ext)

            implementation(libs.preference)
        }
    }
}
