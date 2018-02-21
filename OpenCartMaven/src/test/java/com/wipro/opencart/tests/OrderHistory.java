package com.wipro.opencart.tests;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wipro.opencart.pages.AccountLoginPage;
import com.wipro.opencart.pages.CheckOutPage;
import com.wipro.opencart.pages.CommonActions;
import com.wipro.opencart.pages.HomePage;
import com.wipro.opencart.pages.MyAccountPage;
import com.wipro.opencart.pages.OrderHistoryPage;
import com.wipro.opencart.pages.ProductPage;
import com.wipro.opencart.pages.RegistrationPage;
import com.wipro.opencart.pages.ShoppingCartPage;
import com.wipro.opencart.pages.YourOrderPage;
import com.wipro.opencart.utilities.ExcelData;
import com.wipro.opencart.utilities.ExcelWriteData;

import basesetup.LaunchBrowser;

public class OrderHistory{
	

		// TODO Auto-generated constructor stub

	WebDriver driver;
	AccountLoginPage accountLoginPage;
	MyAccountPage myAccountPage;
	RegistrationPage registrationPage;
	HomePage homePage;
	ProductPage productPage;
	ShoppingCartPage shoppingCartPage;
	CheckOutPage checkOutPage;
	YourOrderPage yourOrderPage;
	OrderHistoryPage orderHistoryPage;
	
	@BeforeTest
	public void launchBrowser(){
		
		driver=LaunchBrowser.OpenBrowser("chrome");
		
	}
	
	@Test(dataProvider = "Authentication")
	public void login(String firstname, String email, String password) throws Exception{
		
		accountLoginPage = new AccountLoginPage(driver);
		registrationPage = new RegistrationPage(driver);
		accountLoginPage.clickOnLoginLink();
		myAccountPage=accountLoginPage.giveCredentialsAndSignin(email, password);
		
		//Verify firstname of signed in user is shown in the 'You are signed in as...' text after signed in
		Assert.assertEquals(firstname, myAccountPage.getSignedInUserName(), "User is not signed in or incorrect username is shown");
		System.out.println("Firstname of signed in user is shown in the page");
		
		//Calling method to navigate to Home page
		homePage = registrationPage.navigateToHome();
		
		//Calling method to view any product
		productPage = homePage.clickOnProduct();
		
		//Calling method to add product to cart
		productPage.clickOnAddToCart();
		Assert.assertTrue(productPage.isSuccessToastDisplayed(), "Success toast is not displayed");
		System.out.println("Success: You have added MacBook to your shopping cart!");
		
		//Calling method to click on Shopping cart link
		shoppingCartPage = productPage.clickOnShoppingCart();
		
		//Writing the items present in Shopping cart to the excel sheet
		ExcelWriteData excelWriteData = new ExcelWriteData("Products");
		excelWriteData.createRows(0);	
		excelWriteData.createCellsAndWriteData(0, "Shopping Cart");
		excelWriteData.createCellsAndWriteData(1, "Check Out");
		excelWriteData.createCellsAndWriteData(2, "Status");
		
		List<String> items=shoppingCartPage.getItemsInCart();
		
		for(int rows=1;rows<=shoppingCartPage.numOfItems();rows++){
			
			excelWriteData.createRows(rows);			
			excelWriteData.createCellsAndWriteData(0, items.get(rows-1));
			
		}
		
		//excelWriteData.writeTo("D:/Test Data/Items.xlsx");
		//Calling methods to update the quantity of product and navigate to checkout page
		shoppingCartPage.quantity("4");
		shoppingCartPage.clickOnUpdateBtn();
		checkOutPage = shoppingCartPage.navigateToCheckOutPage();
		
		//
		Thread.sleep(1500);
		checkOutPage.continueWithBillingDetails();
		Thread.sleep(1500);
		checkOutPage.continueWithDeliveryDetails();
		Thread.sleep(1500);
		checkOutPage.continueWithDeliveryMethod();
		Thread.sleep(3000);
		checkOutPage.continueWithPaymentMethod();
		
		//Writing the items present in CheckOut Page to the excel sheet
		List<String> itemsOfCheckOut= checkOutPage.getItemsFromCheckoutPage();
		
		for(int rows=1;rows<=checkOutPage.numOfItems();rows++){
			
			//excelWriteData.createRows(rows);		
			System.out.println(itemsOfCheckOut.get(rows-1));
			excelWriteData.createCellsAndWriteData(1, itemsOfCheckOut.get(rows-1));
			
		}
		excelWriteData.writeTo("D:/Items.xlsx");
		Thread.sleep(1500);
		yourOrderPage = checkOutPage.confirmOrder();
		Thread.sleep(3000);
		Assert.assertEquals("Your Order Has Been Processed!", yourOrderPage.getPageHeader());
		System.out.println("Your Order Has Been Processed!");
		
		orderHistoryPage = yourOrderPage.navigateToOrderHistoryPage();
		
		String orderId= orderHistoryPage.getPreviousOrderId();
		orderHistoryPage.viewOrderDetails();
		String orderId_InfoPage=orderHistoryPage.getOrderId_InfoPage();
		try{
			Assert.assertEquals(orderId_InfoPage.substring(0,15),orderId);
			System.out.println("Previous Order is displayed in the Result view");
		}catch(NoSuchElementException e){
			
			orderHistoryPage.logout();
			System.out.println("Signed Out:Previous Order is not displayed");
		}
		
		//Calling method to navigate to product returns page
		orderHistoryPage.navigateToProductReturnPage();
		
	}
	
	@DataProvider(name="Authentication")
	public Object[][] credentials() throws Exception{
		
		ExcelData exceldata = new ExcelData("D:/OpenCart.xlsx",1);
		int rows = exceldata.numOfRows();
		Object[][] data = new Object[rows][3];
		for(int row=0;row<rows;row++){
			
			data[row][0]=exceldata.getData(row+1, 0);
			data[row][1]= exceldata.getData(row+1, 2);
			data[row][2]= exceldata.getData(row+1, 9);
		}
		return data;
	}

}
