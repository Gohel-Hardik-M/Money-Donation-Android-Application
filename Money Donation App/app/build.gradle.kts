plugins {
	id("com.android.application")
	kotlin("android")
}

android {
	namespace = "com.example.moneydonation"
	compileSdk = 35

	defaultConfig {
		applicationId = "com.example.moneydonation"
		minSdk = 26
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"
		vectorDrawables.useSupportLibrary = true
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	buildFeatures { viewBinding = true }
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlinOptions { jvmTarget = "17" }
}

// Workaround: ensure copy configurations are not consumable or resolvable
configurations.configureEach {
	if (name.endsWith("RuntimeClasspathCopy") || name.endsWith("CompileClasspathCopy")) {
		isCanBeConsumed = false
		isCanBeResolved = false
	}
}

dependencies {
	implementation("androidx.core:core-ktx:1.13.1")
	implementation("androidx.appcompat:appcompat:1.7.0")
	implementation("com.google.android.material:material:1.12.0")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	implementation("androidx.activity:activity-ktx:1.9.2")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")
}
