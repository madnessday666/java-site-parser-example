package config;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;

@Setter
@NoArgsConstructor
public class WebDriverConfiguration {

    private int timeout;

    public WebDriver getWebDriver() {
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.setPageLoadTimeout(Duration.of(timeout, ChronoUnit.SECONDS));
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        return driver;
    }

}
