
# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:

-keep class com.zee5.hipi.presentation.browser.FunctionCallInterceptor { *; }


# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#native mothods
-keepclasseswithmembernames class * { native <methods>; }

#Data Binding
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}


#custom view
#-keep public class * extends android.view.View {
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#    public void set*(...);
#}


# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

#-keepclassmembers class * extends java.lang.Enum {
#    <fields>;
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}

# Ignore JSR 305 annotations for embedding nullability information.

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# moshi-kotlin
-keepattributes Signature
-keepattributes EnclosingMethod
# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable


# Moshi
-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-keep @com.squareup.moshi.JsonQualifier interface *
-dontwarn org.jetbrains.annotations.**
-keep class kotlin.Metadata { *; }
-keep,allowobfuscation,allowshrinking class com.squareup.moshi.JsonAdapter
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
# Keep helper method to avoid R8 optimisation that would keep all Kotlin Metadata when unwanted
-keepclassmembers class com.squareup.moshi.internal.Util {
    private static java.lang.String getKotlinMetadataClassName();
}

# Keep ToJson/FromJson-annotated methods
-keepclassmembers class * {
  @com.squareup.moshi.FromJson <methods>;
  @com.squareup.moshi.ToJson <methods>;
}

-keep class com.google.android.exoplayer2.** { *; }
-keep class com.google.obf.** { *; }
-keep interface com.google.obf.** { *; }
-keep class com.google.ads.interactivemedia.** { *; }
-keep interface com.google.ads.interactivemedia.** { *; }

-keep class com.newrelic.** { *; } -dontwarn com.newrelic.** -keepattributes Exceptions, Signature, InnerClasses, LineNumberTable
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation interface com.facebook.common.internal.DoNotStrip
# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.facebook.infer.**


-keep class com.zee5.hipi.domain.model.videoedit.model.** { *; }
-keep class com.zee5.hipi.domain.model.videocreate.caption.dataInfo.** { *; }
-keep class com.zee5.hipi.domain.model.videocreate.model.** { *; }
-keep class com.zee5.hipi.presentation.videocreate.effect.** { *; }
-keep class com.zee5.hipi.presentation.videocreate.filter.** { *; }
-keep class com.zee5.hipi.presentation.videocreate.vcinterface.** { *; }
-keep class com.zee5.hipi.domain.model.videocreate.data.** { *; }
-keep class com.hipi.model.** { *; }
-keep class com.hipi.model.comments.** { *; }
-keep class com.hipi.model.** { *; }
-keep class com.hipi.analytics.remoteconfig.** {*; }
-keep class com.hipi.analytics.** {*; }
#//Charmboard plugin
-keep class com.zee5.hipi.domain.model.** { *; }


#Live hipi rules
-allowaccessmodification
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes Exceptions
-keepattributes JavascriptInterface
-keepattributes ElementList, Root, Attribute, Element
-ignorewarnings
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.zee5.data.**$$serializer { *; }
-keepclassmembers class com.zee5.data.network.dto.** {
    *** Companion;
}
-keepclasseswithmembers class com.zee5.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context,android.util.AttributeSet);
    public <init>(android.content.Context,android.util.AttributeSet,int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers class * extends android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

-keep class com.google.gson.** { *; }

-keep public class com.cdv.** {*;}
-keep public class com.meicam.** {*;}


-keep public class com.google.android.gms.* {
   <fields>;
  <methods>;
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
   public static final *** NULL;
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers class * {
   native <methods>;
}

-keep public class com.google.android.gms.tasks.OnSuccessListener {
   <fields>;
   <methods>;
}


-keep,allowshrinking @com.google.android.gms.common.annotation.KeepName class *

#Proguard rules template helper
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient { public *; }
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info { public *;}

-keep class com.google.android.gms.internal.** { *; }

-dontwarn com.google.**
-dontwarn com.google.android.gms.**
-optimizations !class/unboxing/enum

-allowaccessmodification
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes Exceptions
-keepattributes JavascriptInterface
-keepattributes ElementList, Root, Attribute, Element
-ignorewarnings
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.zee5.data.**$$serializer { *; }
-keepclassmembers class com.zee5.data.network.dto.** {
    *** Companion;
}
-keepclasseswithmembers class com.zee5.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep,allowobfuscation @interface com.facebook.soloader.DoNotOptimize

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Do not strip any method/class that is annotated with @DoNotOptimize
-keep @com.facebook.soloader.DoNotOptimize class *
-keepclassmembers class * {
    @com.facebook.soloader.DoNotOptimize *;
}

# Keep native methods
-keepclassmembers class com.facebook.** {
    native <methods>;
}

# Do not strip SoLoader class and init method
-keep public class com.facebook.soloader.SoLoader {
    public static void init(android.content.Context, int);
}
-dontwarn com.android.volley.toolbox.**

-dontwarn com.android.installreferrer

-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context,android.util.AttributeSet);
    public <init>(android.content.Context,android.util.AttributeSet,int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers class * extends android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

-keep class * implements android.os.Parcelable {
   public static final android.os.Parcelable$Creator *;
}

-keep class com.google.gson.** { *; }

-keep public class com.cdv.** {*;}
-keep public class com.meicam.** {*;}
-keep public class com.amazonaws.** {*;}

#------------------- Vmax -------------------------------#
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


-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient { public *; }
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info { public *;}
-keepattributes *Annotation*,JavascriptInterface,Exceptions,InnerClasses,Signature,*Annotation*,EnclosingMethod,*Annotation*,Signature

-dontwarn com.google.**
-dontwarn com.google.android.gms.**


-keep public class com.google.android.gms.* {
  <fields>;
<methods>;
}

-keep public class com.google.ads.* {
  public <fields>;
  public <methods>;
}



-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
  public static final *** NULL;
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers class * {
  native <methods>;
}
-keep public class com.google.android.gms.location.FusedLocationProviderClient {
  public <fields>;
  public <methods>;
}
-keep public class com.google.android.gms.tasks.OnSuccessListener {
  <fields>;
  <methods>;
}


#Chrome Custom Tab
-keep public class android.support.customtabs.CustomTabsIntent {
  public <fields>;
  public <methods>;
}

#Volley Progurad
-keep public class com.android.volley.** {
  <fields>;
  <methods>;
}
-dontwarn com.android.volley.**

-keep,allowobfuscation,allowshrinking interface retrofit2.Call

-keep,allowobfuscation,allowshrinking class retrofit2.Response

# Don't note a bunch of dynamically referenced classes
-dontnote com.google.**
-dontnote com.facebook.**
-dontnote com.squareup.okhttp.**
-dontnote okhttp3.internal.**

# Recommended flags for Firebase Auth
-keepattributes Signature
-keepattributes *Annotation*

# Retrofit config
-dontnote retrofit2.Platform
-dontwarn retrofit2.**
-keepattributes Exceptions

# TODO remove https://github.com/google/gson/issues/1174
-dontwarn com.google.gson.Gson$6

-keep class io.jsonwebtoken.** { *; }
-keepnames class io.jsonwebtoken.* { *; }
-keepnames interface io.jsonwebtoken.* { *; }

-keep class org.bouncycastle.** { *; }
-keepnames class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**

-dontwarn com.razorpay.**
-keep class com.razorpay.** {*;}

-keepclasseswithmembers class * {
  public void onPayment*(...);
}

# Google Ad Manager SDK
-keep class com.google.android.gms.ads.** { *; }
-keep interface com.google.android.gms.ads.** { *; }

# Facebook Audience Network SDK
-keep class com.facebook.ads.** { *; }
-keep interface com.facebook.ads.** { *; }

# Facebook Mediation Adapter
-keep class com.google.ads.mediation.facebook.** { *; }

-keep class com.app.adstertimes.data.** { *; }
-keep class res.** { *;}
