pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "KoordXtract"
include(":library")
include(":composeApp")
project(":composeApp").projectDir = File(rootDir, "koordxtractkmp/composeApp")
