rootProject.name = "Quoroscope"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven("https://jitpack.io")
    }
}

include(
    // app
    ":composeApp",
    //
    ":composeApp:share",
    //
    ":composeApp:targets",
    // android
    ":composeApp:targets:androidTarget",
    // desktop
    ":composeApp:targets:desktopTarget",
    //
    ":features",
    // addons
    ":features:addons",
    ":features:addons:data",
    ":features:addons:domain",
    ":features:addons:presentation",
    // dialogs
    ":features:dialogs",
    ":features:dialogs:presentation",
    // details
    ":features:details",
    ":features:details:presentation",
    // news
    ":features:news",
    ":features:news:presentation",
    // settings
    ":features:settings",
    ":features:settings:data",
    ":features:settings:domain",
    ":features:settings:presentation",
    // stock
    ":features:stock",
    ":features:stock:data",
    ":features:stock:domain",
    ":features:stock:presentation",
    //
    ":core",
    // database
    ":core:database",
    // utils
    ":core:utils",
    // dto
    ":core:dto",
    // network
    ":core:network"
)
