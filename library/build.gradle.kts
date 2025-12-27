import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
}

kotlin {
    androidLibrary {
        namespace = "io.github.shashlikmap.koordxtract"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withHostTestBuilder {}.configure {}
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            api(libs.arrow.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.kermit)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlin.coroutines.test)
            implementation(libs.ktor.client.mock)
        }
    }
}

group = "io.github.shashlikmap"
version = "0.3.0"

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "koordxtract", version.toString())

    pom {
        name = "KoordXTract"
        description = "Converts GoogleMaps links to latitude/longitude"
        inceptionYear = "2025"
        url = "https://github.com/ShashlikMap/KoordXtract"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "ShashlikMap"
                name = "ShashlikMap"
                url = "https://github.com/ShashlikMap"
                email = "olenyov.kirill@me.com"
                organization = "ShashlikMap"
                organizationUrl = "https://github.com/ShashlikMap"
            }
        }
        scm {
            url = "https://github.com/ShashlikMap/KoordXtract"
            connection = "scm:git:git://github.com/ShashlikMap/KoordXtract.git"
            developerConnection = "scm:git:ssh://git@github.com/ShashlikMap/KoordXtract.git"
        }
    }
}