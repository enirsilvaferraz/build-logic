plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "com.eferraz.buildlogic"
version = "1.0.0-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

dependencies {
    compileOnly(libs.detekt.api)
    testImplementation(libs.detekt.test)
    testImplementation(kotlin("test"))
}
