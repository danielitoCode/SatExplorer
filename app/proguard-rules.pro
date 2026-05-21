# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepclasseswithmembernames class com.elitec.satexplorer.feature.alerts.domain.entity.*
-keepclasseswithmembernames class com.elitec.satexplorer.feature.analitics.domain.entity.*
-keepclasseswithmembernames class com.elitec.satexplorer.feature.auth.domain.entity.*
-keepclasseswithmembernames class com.elitec.satexplorer.feature.map.domain.entity.*
-keepclasseswithmembernames class com.elitec.satexplorer.feature.prediction.domain.entity.*
-keepclasseswithmembernames class com.elitec.satexplorer.feature.satellite.domain.entity.*
-keepclasseswithmembernames class com.elitec.satexplorer.feature.satellite.data.dto.*
-keepclasseswithmembernames class com.elitec.satexplorer.feature.settings.domain.entity.*
-keepclasseswithmembernames class com.elitec.satexplorer.feature.tracking.domain.entity.*
-keepclasseswithmembernames class com.elitec.satexplorer.feature.visualization.domain.entity.*