import com.android.build.api.dsl.ApplicationExtension
import com.eferraz.buildlogic.AbstractKmpProjectPlugin
import com.eferraz.buildlogic.CatalogDefinitions.Plugins.ANDROID_APPLICATION
import com.eferraz.buildlogic.CatalogDefinitions.Versions.COMPILE_SDK
import com.eferraz.buildlogic.CatalogDefinitions.Versions.MIN_SDK
import com.eferraz.buildlogic.CatalogDefinitions.Versions.TARGET_SDK
import com.eferraz.buildlogic.libs
import com.eferraz.buildlogic.plugin
import com.eferraz.buildlogic.version
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KmpApplicationPlugin : AbstractKmpProjectPlugin() {

    override fun apply(target: Project) {
        super.apply(target)

        with(target) {

            apply(plugin = libs.plugin(ANDROID_APPLICATION))

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    @OptIn(ExperimentalKotlinGradlePluginApi::class)
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_11)
                    }
                }
            }

            extensions.configure<ApplicationExtension> {

                // namespace = // aplicado posteriomente
                compileSdk = libs.version(COMPILE_SDK)

                defaultConfig {
                    // applicationId = // aplicado posteriomente
                    minSdk = libs.version(MIN_SDK)
                    targetSdk = libs.version(TARGET_SDK)
                    // versionCode = 1
                    // versionName = "1.0"
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
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
            }
        }
    }
}