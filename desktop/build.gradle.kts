plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
}

sourceSets["main"].java.setSrcDirs(listOf("src"))

dependencies {
    implementation(project(":core"))
    implementation(libs.gdx.backend.lwjgl3)
    implementation(variantOf(libs.gdx.platform) { classifier("natives-desktop") })
}

val launcherMainClass = "ru.ibelykh.sickdopeskills.desktop.DesktopLauncher"
val assetsDir = rootProject.file("android/assets")

tasks.register<JavaExec>("run") {
    dependsOn(tasks.classes)
    mainClass.set(launcherMainClass)
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
    workingDir = assetsDir
    isIgnoreExitValue = true
}

tasks.register<JavaExec>("debug") {
    dependsOn(tasks.classes)
    mainClass.set(launcherMainClass)
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
    workingDir = assetsDir
    isIgnoreExitValue = true
    debugOptions {
        enabled.set(true)
    }
}

tasks.register<Jar>("dist") {
    dependsOn(tasks.classes)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(files(sourceSets["main"].output.classesDirs))
    from(files(sourceSets["main"].output.resourcesDir))
    from({ configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) } })
    from(files(assetsDir))

    manifest {
        attributes("Main-Class" to launcherMainClass)
    }
}
