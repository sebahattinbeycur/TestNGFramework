package com.exelenter.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.exelenter.utils.CommonMethods;
import com.exelenter.utils.Constants;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.Duration;
import java.time.Instant;

import static com.exelenter.utils.CommonMethods.*;

public class Listener implements ITestListener {
    ExtentSparkReporter reporter;
    ExtentReports reports;
    ExtentTest test;
    Instant startTime;

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getName());

        test = reports.createTest(result.getName());
        test.pass("Test Case Passed: " + result.getName());

        // Optionally, you can capture screenshot here, for each success test case. (Not recommended!).
//        test.addScreenCaptureFromPath(CommonMethods.takeScreenshot("PASS/" + result.getName()));

        test.log(Status.PASS, "Test Passed. This is coming from the log status");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test Failed: " + result.getName());

        test = reports.createTest(result.getName());
        test.fail("Test Case Failed: " + result.getName());
        test.addScreenCaptureFromPath(CommonMethods.takeScreenshot("FAIL/" + result.getName())); // + "_" + getTimeStamp()
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test Skipped: " + result.getName());

        test = reports.createTest(result.getName());
        test.skip("Test Case Skipped: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("=== Test Started ===> " + context.getName() + " | " + context.getStartDate());
        // Adding Reports (start tracking & recording when test starts). ExtentReports library is required.
        // ExtentHTMLreporter <== deprecated, no longer supported after 5.0.6 or above.
        reporter = new ExtentSparkReporter(Constants.REPORT_FILEPATH);
        reporter.config().setTheme(Theme.DARK);
        reporter.config().setDocumentTitle("Exelenter Test Report");      // This will show on the browser Tab, just like page title (driver.getTitle()).
        reporter.config().setReportName("My Test Report");                // This will show on the report-page Dashboard.
        reports = new ExtentReports();
        reports.attachReporter(reporter);

        startTime = Instant.now();

    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("\n**********************************\n=== End of Test ===> " + context.getName() + " | " + context.getEndDate());

        reports.flush();   // Erases previous (old) data and creates new one.

        Instant endTime = Instant.now();
        Duration totalTime = Duration.between(startTime, endTime);
        int milliseconds = totalTime.getNano() / 1_000_000;

//        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime.toMillis());
//        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime.toMillis());
//        long hours = TimeUnit.MILLISECONDS.toHours(totalTime.toMillis());
//        long days = TimeUnit.MILLISECONDS.toDays(totalTime.toMillis());

        long seconds = totalTime.toSeconds();
        long minutes = totalTime.toMinutes();
        long hours = totalTime.toHours();
        long days = totalTime.toDays();

        System.out.println("\nTotal Test Completion Time: \nDays: " + days +
                "\nHours: " + (hours % 24) +
                "\nMinutes: " + (minutes % 60) +
                "\nSeconds: " + (seconds % 60) +
                "\nMilliseconds: " + milliseconds);

        System.out.printf("Total Test Completion Time: %d Minutes %d Seconds, and %3d Milliseconds", minutes, seconds, milliseconds);
    }
}