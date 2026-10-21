plugins {
    alias(libs.plugins.android.application) apply false
}

allprojects {
    // От version зависит имя дистрибутива desktop-1.0.jar.
    version = "1.0"
}
