package basesetup;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LaunchBrowser {
	static WebDriver driver;
	
	static String url;
	
	public static WebDriver OpenBrowser(String Browser){
	
	if(Browser.equalsIgnoreCase("chrome")){
	System.setProperty("webdriver.chrome.driver", "D:\\Selenium Jars\\chromedriver.exe");
	//driver = new ChromeDriver();
	url = "http://10.159.34.199:4444/wd/hub";
	 
    try {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setPlatform(Platform.WINDOWS);
        driver = new RemoteWebDriver(new URL(url), capabilities);
    }catch(Exception e){
        e.printStackTrace();
    }
	driver.manage().window().maximize();
	driver.get("http://10.207.182.108:81/opencart/");
	driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
	}
	return driver;
	}
	/*
	
	
	}*/
}
