plugins {
    `java-library`
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

sourceSets["main"].java.setSrcDirs(listOf("src"))

// Путь тестов задаётся симметрично главному и обязательно СНАРУЖИ "src". Дефолт у test —
// "core/src/test/java", то есть внутри srcDir главного сорс-сета: тесты попали бы в compileJava
// главного сорс-сета и через зависимость android -> core уехали в APK.
sourceSets["test"].java.setSrcDirs(listOf("test"))
sourceSets["test"].resources.setSrcDirs(listOf("testResources"))

tasks.test {
    useJUnitPlatform()
}

dependencies {
    // api, а не implementation: libGDX должен протекать в android и desktop.
    api(libs.gdx)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    // Объявлен явно: иначе Gradle 8.x иногда не находит launcher при --configuration-cache.
    testRuntimeOnly(libs.junit.platform.launcher)
}
