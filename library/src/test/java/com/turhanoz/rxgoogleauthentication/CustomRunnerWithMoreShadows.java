package com.turhanoz.rxgoogleauthentication;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.turhanoz.rxgoogleauthentication.shadows.ShadowGoogleAuthUtil;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.internal.bytecode.InstrumentationConfiguration;

//https://github.com/nenick/android-gradle-template/blob/master/appCt/src/test/java/com/example/project/robolectric/CostomRobolectricTestRunner.java
public class CustomRunnerWithMoreShadows extends RobolectricGradleTestRunner {
    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
     * and res directory by default. Use the {@link } annotation to configure.
     *
     * @param testClass the test class to be run
     * @throws InitializationError if junit says so
     */
    public CustomRunnerWithMoreShadows(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    public InstrumentationConfiguration createClassLoaderConfig() {
        InstrumentationConfiguration.Builder builder = InstrumentationConfiguration.newBuilder();
        builder.addInstrumentedClass(GoogleAuthUtil.class.getName());
        return builder.build();
    }
}
