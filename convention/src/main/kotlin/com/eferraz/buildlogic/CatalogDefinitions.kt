package com.eferraz.buildlogic

internal interface CatalogDefinitions {

    enum class Plugins(val alias: String) {
        MULTIPLATFORM_LIBRARY("multiplatform.library"),
        COMPOSE_COMPILER("compose-compiler"),
        COMPOSE_HOT_RELOAD("compose-hot-reload"),
        COMPOSE_MULTIPLATFORM("compose-multiplatform"),
        KSP("ksp"),
        KOTLIN_MULTIPLATFORM("kotlin-multiplatform"),
        ROOM("room"),
        KOTLIN_SERIALIZATION("kotlin-serialization")
    }

    enum class Libraries(val alias: String) {
        KOIN_BOM("koin-bom")
    }

    enum class Versions(val alias: String) {
        COMPILE_SDK("android.compileSdk"),
        MIN_SDK("android.minSdk"),
        TARGET_SDK("android.targetSdk"),
    }

    enum class Bundles(val alias: String) {

        COMPOSE_COMMON("compose-common"),

        KOTLIN_COMMON("kotlin-common"),
        KOTLIN_COMMON_TEST("kotlin-common-test"),
        KOTLIN_ANDROID_TEST("kotlin-android-test"),

        KTOR_COMMON("ktor-common"),
        KTOR_ANDROID("ktor-android"),
        KTOR_IOS("ktor-ios"),
        KTOR_DESKTOP("ktor-desktop"),

        KOIN_COMMON("koin-common"),
        KOIN_COMMON_COMPOSE("koin-common-compose"),
        KOIN_COMMON_COMPILER("koin-common-compiler"),
        KOIN_COMMON_TEST("koin-common-test"),

        ROOM_COMMON("room-common"),
        ROOM_COMMON_COMPILER("room-common-compiler")
    }
}
