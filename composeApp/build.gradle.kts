import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosArm64("iosArm64") {
        binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts("-framework", "MapLibre")
        }

        compilations.getByName("main") {
            cinterops {
                val tkkmpSampleInterop by creating {
                    defFile = file("src/iosMain/c_interop/TKKMPSample.def")
                    val path = "-I${projectDir}/src/iosMain/c_interop/iosDerivedSources"
                    compilerOpts(path)
                }
            }
        }
    }

    iosSimulatorArm64("iosSimulatorArm64") {
        binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts("-framework", "MapLibre")
        }

        compilations.getByName("main") {
            cinterops {
                val tkkmpSampleInterop by creating {
                    defFile = file("src/iosMain/c_interop/TKKMPSample.def")
                    val path = "-I${projectDir}/src/iosMain/c_interop/iosDerivedSources"
                    compilerOpts(path)
                }
            }
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.gl.android.sdk.v1151) // MapLibre GL Native for Android

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.ktor.client.core)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)

        }
    }
}

tasks.named("linkDebugFrameworkIosArm64") {
    dependsOn("cinteropTkkmpSampleInteropIosArm64")
}

tasks.named("linkDebugFrameworkIosSimulatorArm64") {
    dependsOn("cinteropTkkmpSampleInteropIosSimulatorArm64")
}

android {
    namespace = "com.lwwo.tkkmp_sample"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.lwwo.tkkmp_sample"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.androidx.ui.android)
    debugImplementation(compose.uiTooling)
}

tasks.register("printProjectDir") {
    doLast {
        println("DEBUG: Project directory: $projectDir")
    }
}
