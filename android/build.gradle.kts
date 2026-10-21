import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "ru.ibelykh.sickdopeskills"
    compileSdk = libs.versions.compileSdk.get().toInt()

    sourceSets {
        named("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.setSrcDirs(listOf("src"))
            aidl.setSrcDirs(listOf("src"))
            res.setSrcDirs(listOf("res"))
            assets.setSrcDirs(listOf("assets"))
            jniLibs.setSrcDirs(listOf("libs"))
        }
    }

    defaultConfig {
        applicationId = "ru.ibelykh.sickdopeskills"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

val natives: Configuration by configurations.creating

dependencies {
    implementation(project(":core"))
    implementation(libs.gdx.backend.android)
    natives(variantOf(libs.gdx.platform) { classifier("natives-arm64-v8a") })
    natives(variantOf(libs.gdx.platform) { classifier("natives-armeabi-v7a") })
    natives(variantOf(libs.gdx.platform) { classifier("natives-x86") })
    natives(variantOf(libs.gdx.platform) { classifier("natives-x86_64") })
}

// Распаковывает нативные библиотеки libGDX из конфигурации natives в libs/<abi>,
// откуда их забирает jniLibs и кладёт в APK.
val copyAndroidNatives by tasks.registering {
    doFirst {
        // libs/ чистится целиком: иначе при смене версии libGDX или набора ABI
        // в APK уезжают .so от предыдущей сборки.
        delete(file("libs"))

        natives.files.forEach { jar ->
            val outputDir = when {
                jar.name.endsWith("natives-arm64-v8a.jar") -> file("libs/arm64-v8a")
                jar.name.endsWith("natives-armeabi-v7a.jar") -> file("libs/armeabi-v7a")
                jar.name.endsWith("natives-x86_64.jar") -> file("libs/x86_64")
                jar.name.endsWith("natives-x86.jar") -> file("libs/x86")
                else -> null
            }
            if (outputDir != null) {
                copy {
                    from(zipTree(jar))
                    into(outputDir)
                    include("*.so")
                }
            }
        }
    }
}

tasks.named("preBuild") {
    dependsOn(copyAndroidNatives)
}

tasks.register<Exec>("run") {
    val localProperties = project.file("../local.properties")
    val sdkPath = if (localProperties.exists()) {
        val properties = Properties()
        localProperties.inputStream().use { properties.load(it) }
        properties.getProperty("sdk.dir") ?: System.getenv("ANDROID_HOME")
    } else {
        System.getenv("ANDROID_HOME")
    }

    commandLine(
        "$sdkPath/platform-tools/adb",
        "shell", "am", "start",
        "-n", "ru.ibelykh.sickdopeskills/ru.ibelykh.sickdopeskills.AndroidLauncher"
    )
}
