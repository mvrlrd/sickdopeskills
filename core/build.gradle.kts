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

dependencies {
    // api, а не implementation: libGDX должен протекать в android и desktop.
    api(libs.gdx)
}
