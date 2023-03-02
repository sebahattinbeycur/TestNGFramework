package com.exelenter.base;

import com.exelenter.utils.CommonMethods;
import com.exelenter.utils.ConfigsReader;
import com.exelenter.utils.Constants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

// NOTE: THIS CLASS IS USED TO LAUNCH AND QUIT THE BROWSER

public class BaseClass extends CommonMethods {
    public static WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public static void setUp() {
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
        //System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "true");
        ConfigsReader.loadProperties(Constants.CONFIGURATION_FILEPATH);
        String headless = ConfigsReader.getProperties("headless");

        switch (ConfigsReader.getProperties("browser").toLowerCase()) {
            case "chrome" -> {
                System.setProperty("webdriver.chrome.driver", Constants.CHROME_DRIVER_PATH);
                if (headless.equalsIgnoreCase("true")) {
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--headless", "--log-level=3");          // <== Run in headless mode
                    driver = new ChromeDriver(options);
                } else {
                    driver = new ChromeDriver();               // <== if headless=false this line will run
                }
            }
            case "firefox" -> {
                System.setProperty("web.driver.gecko.driver", Constants.GECKO_DRIVER_PATH);
                if (headless.equalsIgnoreCase("true")) {
                    FirefoxOptions options = new FirefoxOptions();
                    options.addArguments("--headless");
                    driver = new FirefoxDriver(options);
                } else {
                    driver = new FirefoxDriver();              // <== if headless=false this line will run
                }
            }
            default -> throw new RuntimeException("Browser is not supported");
        }

        driver.get(ConfigsReader.getProperties("url"));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.IMPLICIT_WAIT_TIME));
        initialize();
    }

    @AfterMethod(alwaysRun = true)
    public static void tearDown() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
        if (driver != null) {     // This line is optional. We only use it to prevent NullPointerException.
            driver.quit();
        }
    }

}