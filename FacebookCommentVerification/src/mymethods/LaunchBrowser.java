package mymethods;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class LaunchBrowser {
	
	public WebDriver launchMyBrowser(String browser){
		
		WebDriver driver;
		
		switch(browser){
			case "Chrome":
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
				
				// to disable Chrome notifications
				Map<String, Object> prefs = new HashMap<String, Object>();
				prefs.put("profile.default_content_setting_values.notifications", 2);
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("prefs", prefs);
				
				driver = new ChromeDriver(options);
				break;
			case "Firefox":
				driver = new FirefoxDriver();
				break;
			case "IE":
				System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\drivers\\IEDriverServer32.exe");
				driver =new InternetExplorerDriver();
				break;
			default: driver = new FirefoxDriver();
		}
		
		driver.manage().window().maximize();
		return driver;
		
	}

}
