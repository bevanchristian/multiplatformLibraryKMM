import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode.BITCODE
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary )
//    id("com.chromaticnoise.multiplatform-swiftpackage") version "2.0.3"
//    alias(libs.plugins.kotlinSerialization)
}

var androidTarget: String = ""

kotlin {
    val ktorVersion = "2.3.7"
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

//    multiplatformSwiftPackage {
//        packageName("sharedBevanKmm")
//        swiftToolsVersion("5.3")
//        targetPlatforms {
//            iOS { v("13") }
//        }
//    }
    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.1"
        ios.deploymentTarget = "13.0"
        source = "https://github.com/bevanchristian/multiplatformLibraryKMM.git"
        framework {
            baseName = "sharedBevanLib2"
            isStatic = false
            transitiveExport = true
            embedBitcode(BITCODE)
        }
        specRepos {
            url("https://github.com/bevanchristian/multiplatformLibraryKMM.git") //use your repo here
        }

        sourceSets {
            val commonMain by getting {
                dependencies {
//                    api(commonlibs.kotlin.stdlib)
                }
            }
            val commonTest by getting {
                dependencies {
                    implementation(kotlin("test-common"))
                    implementation(kotlin("test-annotations-common"))
                }
            }
            val androidMain by getting {
                dependencies {
                    //Add your specific android dependencies here
                }
            }
            val androidUnitTest by getting {
                dependsOn(androidMain)
                dependsOn(commonMain)
                dependencies {
                    implementation(kotlin("test-junit"))
                    //you should add the android junit here
                }
            }
            val iosX64Main by getting
            val iosArm64Main by getting
            val iosSimulatorArm64Main by getting
            val iosMain by creating {
                dependsOn(commonMain)
                iosX64Main.dependsOn(this)
                iosArm64Main.dependsOn(this)
                iosSimulatorArm64Main.dependsOn(this)
                dependencies {
                    //Add any ios specific dependencies here, remember to also add them to the export block
                }
            }
            val iosX64Test by getting
            val iosArm64Test by getting
            val iosSimulatorArm64Test by getting
            val iosTest by creating {
                dependsOn(commonTest)
                iosX64Test.dependsOn(this)
                iosArm64Test.dependsOn(this)
                iosSimulatorArm64Test.dependsOn(this)
            }
        }
        publishDir = rootProject.file("./")
    }

//    cocoapods {
//        val iosDefinitions = commonlibs.versions.ios
//        name = iosDefinitions.basename.get()
//        summary = iosDefinitions.summary.get()
//        homepage = iosDefinitions.homepage.get()
//        authors = iosDefinitions.authors.get()
//        version = commonlibs.versions.library.version.get()
//        ios.deploymentTarget = iosDefinitions.deployment.target.get()
//        framework {
//            baseName = iosDefinitions.basename.get()
//            isStatic = false
//            transitiveExport = true
//            embedBitcode(BITCODE)
//        }
//        specRepos {
//            url("https://github.com/user/repo.git") //use your repo here
//        }
//        publishDir = rootProject.file("./")
//    }
    
    sourceSets {
        commonMain.dependencies {
            implementation("io.ktor:ktor-client-core:$ktorVersion")
            implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
        }
        androidMain.dependencies {
            implementation("io.ktor:ktor-client-android:$ktorVersion")
        }
        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:$ktorVersion")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.example.libbevantestingkmm"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}