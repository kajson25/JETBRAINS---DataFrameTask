import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
//    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "paket"
version = "1.0"

repositories {
    google()
    mavenCentral()
//    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:dataframe:0.11.0")
    implementation("org.jetbrains.kotlinx:dataframe-excel:0.10.0")
    implementation("org.xerial:sqlite-jdbc:3.41.2.2")
    implementation("com.itextpdf:html2pdf:4.0.4")
    implementation("com.zaxxer:HikariCP:5.0.1")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dataframe"
            packageVersion = "1.0.0"
        }
    }
}
