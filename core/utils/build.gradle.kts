import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinMultiplatformAndroidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    android {
        namespace = "com.pixnpunk.utils"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.documentfile)

            implementation(libs.storage)

            implementation(libs.kotlinx.coroutines.android)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)

            implementation("com.github.omicronapps:7-Zip-JBinding-4Android:Release-16.02-2.03")
        }
        commonMain.dependencies {
            implementation(project(":core:dto"))

            implementation(libs.androidx.lifecycle.viewmodelCompose)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)

            implementation(libs.ktor.client.core)

        }
        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)

            implementation("net.sf.sevenzipjbinding:sevenzipjbinding:16.02-2.01")
            implementation("net.sf.sevenzipjbinding:sevenzipjbinding-all-platforms:16.02-2.01")
        }
    }
}