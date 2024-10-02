plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.7.10"
}

android {
    namespace = "com.michael.kompanion"
    compileSdk = 34

    // Make sure you specify your source directories properly here
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
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

publishing {
    publications {
        create("release", MavenPublication::class) {
            groupId = "com.mikeisesele"
            artifactId = "kompanion"
            version = "1.2.0"

//            // Add the sources and javadoc artifacts
//            artifact(tasks.named("sourcesJar").get())
//            artifact(tasks.named("javadocJar").get())

            afterEvaluate {
                from(components["release"])
            }
        }


    }
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    outputDirectory.set(buildDir.resolve("dokka"))
}
//
//tasks.register<Jar>("sourcesJar") {
//    from(android.sourceSets["main"].java.srcDirs)
//    archiveClassifier.set("sources")
//}
//
//tasks.register<Jar>("javadocJar") {
//    from(tasks["dokkaHtml"])
//    archiveClassifier.set("javadoc")
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
    implementation(kotlin("reflect"))
}