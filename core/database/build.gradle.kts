import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinMultiplatformAndroidLibrary)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.ksp)
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    androidLibrary {
        namespace = "com.pixnpunk.database"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(project(":core:dto"))
            
            implementation(libs.bundles.roomEcosystem)

            implementation(libs.androidx.paging.common)

            implementation(libs.kotlinx.coroutines.core)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
        }
        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
}