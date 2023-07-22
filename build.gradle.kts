@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `maven-publish`
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        val main by compilations.getting {
            kotlinOptions {
                // Setup the Kotlin compiler options for the 'main' compilation:
                jvmTarget = "1.8"
            }
        }
        val test by compilations.getting {
            kotlinOptions {
                // Setup the Kotlin compiler options for the 'main' compilation:
                jvmTarget = "1.8"
            }
        }
    }
    js(IR) {
        nodejs {
            testTask {
                useMocha {
                    // javascript is a lot slower than Java, we hit the default timeout of 2000
                    timeout = "20000"
                }
            }
        }
    }

    sourceSets {

        val commonMain by getting {
                dependencies {
                }
            }

        val commonTest by getting {
                dependencies {
                    implementation(kotlin("test-common"))
                    implementation(kotlin("test-annotations-common"))
                    implementation("io.kotest:kotest-assertions-core:_")
                }
            }

        val jvmMain by getting {
            dependencies {
            }
        }
        val jvmTest by getting {
            dependencies {
                runtimeOnly("org.junit.jupiter:junit-jupiter:_")
                implementation(kotlin("test-junit"))

                // kotlintest runner needs this to enable logging
                implementation("org.slf4j:slf4j-api:_")
                implementation("org.slf4j:jcl-over-slf4j:_")
                implementation("org.slf4j:log4j-over-slf4j:_")
                implementation("org.slf4j:jul-to-slf4j:_")
                implementation("ch.qos.logback:logback-classic:_")
            }
        }

        val jsMain by getting {
                dependencies {
                }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
        }
    }
}


publishing {
    repositories {
        maven {
            url = uri("file://$projectDir/localRepo")
        }
    }
}
