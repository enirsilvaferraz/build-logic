package com.eferraz.buildlogic

internal interface CatalogDefinitions {

    enum class Plugins(val alias: String) {
        ANDROID_APPLICATION("android.application"),
        COMPOSE_COMPILER("compose-compiler"),
        COMPOSE_MULTIPLATFORM("compose-multiplatform"),
        KSP("ksp"),
        KOTLIN_MULTIPLATFORM("kotlin-multiplatform"),
        KOTLIN_MULTIPLATFORM_LIBRARY("android-kotlin-multiplatform-library"),
        ROOM("room"),
        KOTLIN_SERIALIZATION("kotlin-serialization")
    }

    enum class Libraries(val alias: String) {
        KOTLIN_STDLIB("kotlin-stdlib"),
        KOTLIN_TEST("kotlin-test"),
        KOIN_BOM("koin-bom"),
        ACTIVITY_COMPOSE("androidx-activity-compose")
    }

    enum class Versions(val alias: String) {
        COMPILE_SDK("android.compileSdk"),
        MIN_SDK("android.minSdk"),
        TARGET_SDK("android.targetSdk"),
    }

    enum class Bundles(val alias: String) {

        KTOR_COMMON("ktor-common"),
        KTOR_ANDROID("ktor-android"),
        KTOR_IOS("ktor-ios"),

        KOIN_COMMON("koin-common"),
        KOIN_COMMON_COMPOSE("koin-common-compose"),
        KOIN_COMMON_COMPILER("koin-common-compiler"),
        KOIN_COMMON_TEST("koin-common-test"),

        ROOM_COMMON("room-common"),
        ROOM_COMMON_COMPILER("room-common-compiler"),

        NAVIGATION_COMMON("navigation-common")
    }
}
