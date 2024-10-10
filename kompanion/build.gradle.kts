import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")
    id("org.jetbrains.dokka")
}


android {
    namespace = "com.michael.kompanion"
    compileSdk = 34

    sourceSets["main"].java.srcDirs("src/main/kotlin")

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
    }
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            sourceRoots.from(file("src/main/kotlin"))
        }
    }
}
tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE // Exclude duplicates
}

tasks.register("cleanDokkaModuleDocs") {
    doLast {
        delete(file("../kompanionDocumentation/html"))
    }
}

tasks.register<Exec>("createDokkaModuleDocs") {
    commandLine("./../gradlew", "dokkaHtml")
}

tasks.dokkaHtml {
    outputDirectory.set(file("../kompanionDocumentation/html"))
//    val dPrefsLogo = file("../DPrefs/src/main/java/com/dsofttech/dprefs/d_prefs_logo.svg")
    val dokkaBaseConfiguration = """
    {
      "footerMessage": "Made with ❤️ by Michael Isesele, (c) 2024",
      "separateInheritedMembers": false,
      "mergeImplicitExpectActualDeclarations": false
    }
    """
    pluginsMapConfiguration.set(
        mapOf("org.jetbrains.dokka.base.DokkaBase" to dokkaBaseConfiguration)
    )
}

tasks.register("androidKDocJar", Jar::class) {
    archiveClassifier.set("kdoc")
    from(tasks["dokkaHtml"])
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
}

//
//publishing {
//    publications {
//        create<MavenPublication>("release") {
//            groupId = "com.mikeisesele"
//            artifactId = "kompanion"
//            version = "1.4.0"
//            artifact(tasks["androidKDocJar"])
//
//            afterEvaluate {
//                from(components["release"])
//            }
//        }
//    }
//}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    api(libs.easylog)
    implementation(libs.kotlin.reflect)

    // Is applied universally
//    dokkaPlugin("org.jetbrains.dokka:mathjax-plugin:1.9.20")
    dokkaPlugin("org.jetbrains.dokka:android-documentation-plugin:1.9.20")

}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.mikeisesele"
                artifactId = "kompanion"
                version = "1.4.5"
            }
        }
    }
}
