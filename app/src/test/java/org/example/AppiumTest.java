package org.example;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.options.UiAutomator2Options;

public class AppiumTest {

    private AndroidDriver driver;

    @BeforeEach
    public void setUp() throws MalformedURLException{
        UiAutomator2Options options = new UiAutomator2Options()
            .setDeviceName("emulator-5554")
            .setPlatformName("Android")
            .setAutomationName("UiAutomator2");
        
        driver = new AndroidDriver(new URL("http://host.docker.internal:4723"), options);
    }

    @Test
    public void sampleTest() throws InterruptedException {

    System.out.println("START TEST");

    Thread.sleep(3000);

    driver.openNotifications();
    System.out.println("OPEN NOTIFICATION");

    Thread.sleep(5000);

    System.out.println("END TEST");
}

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}