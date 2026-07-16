import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinMultiplatformAndroidLibrary)
}

kotlin {
    android {
        namespace = "com.pixnpunk.stock.data"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.work)

            implementation(libs.kotlinx.coroutines.android)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
            implementation(libs.koin.workmanager)

            implementation(libs.storage)
        }
        commonMain.dependencies {
            implementation(project(":core:utils"))
            implementation(project(":core:database"))
            implementation(project(":core:dto"))
            implementation(project(":core:network"))

            implementation(project(":features:stock:domain"))

            implementation(libs.bundles.roomEcosystem)
            implementation(libs.bundles.ktorClientEcosystem)

            implementation(libs.androidx.paging.common)

            implementation(libs.kotlinx.coroutines.core)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)

            implementation(libs.jsoup)
        }
        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}