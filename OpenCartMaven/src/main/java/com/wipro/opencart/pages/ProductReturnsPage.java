package com.wipro.opencart.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;

public class ProductReturnsPage {
	
	WebDriver driver;
	
	@FindBy(how=How.ID,using="return-reason-id3")
	public WebElement sfdsf;
	
	public ProductReturnsPage(WebDriver driver){
		
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}

}
