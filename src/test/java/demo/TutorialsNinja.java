package demo;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TutorialsNinja {

	WebDriver driver;

	// Test Data
	String currencyOption = "Euro";
	String expectedErrorSelectOption = "Select required!";
	String expectedSuccessMsg = "Success: You have added MacBook to your shopping cart!";
	String expectedWarining = "Warning: No Payment options are available. Please contact us for assistance!";
	String couponCode = "ABCD123", voucherCode = "AXDFGH123";
	String ninjaUrl = "http://tutorialsninja.com/demo/index.php";

	Wait<WebDriver> wait;

	/** 1. Launch the application **/
	@BeforeTest
	public void setup() {

		String driverPath = System.getProperty("user.dir") + "/src/test/resources/Drivers/chromedriver.exe";

		// Set the chrome driver path
		System.setProperty("webdriver.chrome.driver", driverPath);

		// Create instance of chrome driver
		//driver = new ChromeDriver();

		//Set chrome options 
		ChromeOptions options = new ChromeOptions();
		options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		options.setAcceptInsecureCerts(true);
		options.setScriptTimeout(Duration.ofSeconds(30));
		options.setPageLoadTimeout(Duration.ofSeconds(30));
		options.setImplicitWaitTimeout(Duration.ofSeconds(10));

		// Create instance of chrome driver
		driver = new ChromeDriver(options);
				
		System.out.println("\n1.Launch the application");

		driver.get(ninjaUrl);
		driver.manage().window().maximize();

		wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(5)).pollingEvery(Duration.ofMillis(500))
				.ignoring(Exception.class);

		System.out.println("Application successfully launched");
	}

	@Test(priority = 1)
	public void SelectCurrency() {
		System.out.println("\n2. Select the currency in the left top corner to Euro");

		driver.findElement(By.cssSelector(".fa.fa-caret-down")).click();
		WebElement currency = driver.findElement(
				By.xpath("//ul[@class='dropdown-menu']//button[contains(text(),'" + currencyOption + "')]"));
		currency.click();

		System.out.println("Currency successfully changed");
	}

	@Test(priority = 2)
	public void OrderCanon() {
		String model = "Canon EOS";
		System.out.println(
				"\n3. Order a canon EOS 5 D camera and collect the error message occurred due to a bug in select option.");

		// Click on cannon link
		WebElement product = driver
				.findElement(By.xpath("//div[contains(@class,'product-layout')]//a[contains(text(),'" + model + "')]"));
		product.click();

		// Click on Add to Cart
		driver.findElement(By.id("button-cart")).click();

		// Wait until the error message is displayed
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("text-danger")));

		// Capture error message and verify with expected
		String actualError = driver.findElement(By.className("text-danger")).getText();
		Assert.assertEquals(actualError, expectedErrorSelectOption);

		System.out.println("Message displayed on Screen ---> " + actualError);
	}

	@Test(priority = 3)
	public void OrderIPhone() {

		System.out.println(
				"\n4. Move to the home screen, Click on iphone and go to details screen, change the quantity to two then add to cart.");

		String model = "iPhone";
		String iPhoneLocator = "//div[contains(@class,'product-layout')]//a[contains(text(),'" + model + "')]";

		// Move to the home screen
		/* driver.findElement(By.cssSelector(".fa.fa-home")).click(); */
		driver.navigate().to(ninjaUrl);

		// Wait until the home page is loaded
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(iPhoneLocator)));

		// Click on iphone link
		WebElement product = driver.findElement(By.xpath(iPhoneLocator));
		product.click();

		// Change the quantity to 2
		WebElement quantity = driver.findElement(By.name("quantity"));
		quantity.clear();
		quantity.sendKeys("2");

		System.out.println("Quanity successfully changed");

		// Click on Add to Cart
		driver.findElement(By.id("button-cart")).click();

		System.out.println("\n5. Print the success message in the console");

		// Print the success message in the console
		System.out.println("2 IPhones succesfully added to the cart");
	}

	@Test(dependsOnMethods = "OrderIPhone")
	public void CheckoutIphone() throws UnsupportedEncodingException, InterruptedException {

		String strlocator;
		strlocator = "//ul[contains(@class,'pull-right')]//i[contains(@class,'fa-shopping-cart')]";

		System.out.println("\n6. Click on the cart icon (black color) in the right side top then click view cart");

		// Click on the cart icon in the right side top then click view cart
		driver.findElement(By.id("cart-total")).click();

		// wait for the view cart panel to load
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(strlocator)));

		// Click View Cart
		driver.findElement(By.xpath(strlocator)).click();

		// wait until the View Cart page is loaded
		strlocator = "//input[@class='form-control' and contains(@name,'quantity')]";
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(strlocator)));

		System.out.println("View Cart page successfully loaded");

		System.out.println("\n7. Change the quantity of iPhone to 3 and click update button.");

		// Change the quantity to 3 & click update button
		WebElement quantity = driver.findElement(By.xpath(strlocator));
		quantity.clear();
		quantity.sendKeys("3");

		strlocator = "//button[@data-original-title='Update']";
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(strlocator)));

		WebElement updateBtn = driver.findElement(By.xpath(strlocator));
		updateBtn.click();

		System.out.println("Quantity successfully changed to 3");

		System.out.println("\n8. Print Eco tax & VAT Amount in console & click Checkout");

		strlocator = "(//table//strong[contains(text(),'Eco')]/parent::td/following-sibling::td)[2]";
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(strlocator)));

		WebElement ecoTax = driver.findElement(By.xpath(strlocator));
		WebElement vat = driver
				.findElement(By.xpath("(//table//strong[contains(text(),'VAT')]/parent::td/following-sibling::td)[2]"));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ecoTax);

		// Printstream to display the currency symobols
		PrintStream out = new PrintStream(System.out, true, "UTF-8");
		out.println("Eco Tax: " + ecoTax.getText());
		out.println("VAT amount: " + vat.getText());

		System.out.println("\n9. Print the error message and remove the product from the cart");

		// checkout
		driver.findElement(By.xpath("//a[text()='Checkout']")).click();
		WebElement alert = driver.findElement(By.cssSelector(".alert.alert-danger.alert-dismissible"));
		System.out.println("Error message on checking out 3 iPhones --> " + GetMessage(alert.getText()));

		// Remove the product from the cart
		WebElement deleteBtn = driver.findElement(By.xpath("//button[@data-original-title='Remove']"));
		deleteBtn.click();

		System.out.println("Products successfully removed from the cart.");

		// Move to the home screen
		System.out.println(
				"\n10. Move to the home screen and click on Mac book, check the default quantity is 1 and click add to cart then verify success message");
		Thread.sleep(500);
		WebElement homeBtn = driver.findElement(By.cssSelector(".fa.fa-home"));
		homeBtn.click();

	}

	@Test(priority = 4)
	public void OrderMacBook() throws InterruptedException {

		String model = "MacBook";
		String strlocator;

		strlocator = "//div[contains(@class,'product-layout')]//a[contains(text(),'" + model + "')]";

		// Wait until the home page is loaded
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(strlocator)));

		// Click on MacBook link
		WebElement product = driver.findElement(By.xpath(strlocator));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", product);
		product.click();

		// Wait until the product page is loaded
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("quantity")));

		// Check the default quantity is 1
		WebElement quantity = driver.findElement(By.name("quantity"));
		Assert.assertEquals(quantity.getAttribute("value"), "1");

		// Click on Add to Cart
		driver.findElement(By.id("button-cart")).click();

		strlocator = ".alert.alert-success.alert-dismissible";
		// Wait until the alert is available
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(strlocator)));

		// verify success message
		WebElement alert = driver.findElement(By.cssSelector(".alert.alert-success.alert-dismissible"));
		String actualMsg = GetMessage(alert.getText());
		Assert.assertEquals(actualMsg, expectedSuccessMsg);

		System.out.println("Success Message displayed ---> " + actualMsg);
	}

	@Test(dependsOnMethods = "OrderMacBook")
	public void CheckoutMacBook() throws InterruptedException {

		String strlocator;
		System.out.println(
				"\n11. Click on the shopping cart link in the top and apply ABCD123 as coupon code to check, print error message");

		// Click on the shopping cart link
		strlocator = "//span[text()='Shopping Cart']";

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(strlocator)));
		WebElement shoppingCartLink = driver.findElement(By.xpath(strlocator));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", shoppingCartLink);
		shoppingCartLink.click();
		Thread.sleep(3000);
		// Apply ABCD123 as coupon code
		strlocator = "//div[@class='panel-group']//a[contains(text(),'Coupon')]";

		WebElement couponPanel = driver.findElement(By.xpath(strlocator));

		wait.until(ExpectedConditions.visibilityOfAllElements(couponPanel));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", couponPanel);

		couponPanel.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-coupon")));
		WebElement txtCoupon = driver.findElement(By.id("input-coupon"));
		txtCoupon.sendKeys(couponCode);

		driver.findElement(By.id("button-coupon")).click();

		strlocator = ".alert.alert-danger.alert-dismissible";

		// Wait until the alert is available
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(strlocator)));

		// print error message
		WebElement alert = driver.findElement(By.cssSelector(".alert.alert-danger.alert-dismissible"));
		System.out.println("Error Message displayed ---> " + GetMessage(alert.getText()));

		// clear the coupon code
		txtCoupon.clear();

		System.out.println("\n12. Enter AXDFGH123 as gift certificate and perform apply to check, print error message");

		// Enter AXDFGH123 as gift certificate
		strlocator = "//div[@class='panel-group']//a[contains(text(),'Gift')]";
		WebElement giftCertPanel = driver.findElement(By.xpath(strlocator));
		wait.until(ExpectedConditions.visibilityOfAllElements(giftCertPanel));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", giftCertPanel);

		// Thread.sleep to wait till the scroll is complete, to avoid "Element Is Not
		// Clickable at Point" exception
		Thread.sleep(500);
		giftCertPanel.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-voucher")));
		WebElement txtVoucher = driver.findElement(By.id("input-voucher"));
		txtVoucher.sendKeys(voucherCode);

		driver.findElement(By.id("button-voucher")).click();

		// Thread is unavoidablde since the same div is updated with different error
		// message at runtime through javascript
		//Thread.sleep(1000);

		strlocator = ".alert.alert-danger.alert-dismissible";

		// Wait until the alert is available
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(strlocator)));
		wait.until(ExpectedConditions.textMatches(By.cssSelector(strlocator), Pattern.compile(".*Gift.*")));
		// print error message
		alert = driver.findElement(By.cssSelector(".alert.alert-danger.alert-dismissible"));
		System.out.println("Error Message displayed ---> " + GetMessage(alert.getText()));

		// clear voucher code
		txtVoucher.clear();
		System.out.println("\n13. Clear both textbox values and proceed to checkout");

		// checkout
		WebElement checkout = driver.findElement(By.xpath("//a[text()='Checkout']"));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkout);
		wait.until(ExpectedConditions.elementToBeClickable(checkout));

		checkout.click();
		System.out.println("Cleared the values & successfully landed on check out page");
	}

	@Test(dependsOnMethods = "CheckoutMacBook")
	public void NewCustomerDetails() {

		System.out.println(
				"\n14.Select register account option and enter all account and billing details, click continue");

		String parentlocator = "//div//p[contains(text(),'Checkout Options')]";
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
				By.xpath(parentlocator + "/following-sibling::input[@value='Continue']")));
		WebElement btnContinue = driver
				.findElement(By.xpath(parentlocator + "/following-sibling::input[@value='Continue']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnContinue);

		wait.until(ExpectedConditions.elementToBeClickable(btnContinue));
		btnContinue.click();
	}

	@Test(dependsOnMethods = "NewCustomerDetails")
	public void AccountBillingDetails() throws InterruptedException {

		String parentlocator = "//div[@id='collapse-payment-address']";

		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.name("firstname")));

		// User Details
		driver.findElement(By.name("firstname")).sendKeys("Naveena");
		driver.findElement(By.name("lastname")).sendKeys("Pai");
		driver.findElement(By.xpath("//div[@class='form-group required']//input[@name='email']"))
				.sendKeys(GenerateRandomEmail());
		driver.findElement(By.name("telephone")).sendKeys("123456786");
		driver.findElement(By.xpath(parentlocator + "//input[@name='password']")).sendKeys("Password");
		driver.findElement(By.xpath(parentlocator + "//input[@name='confirm']")).sendKeys("Password");
		driver.findElement(By.name("address_1")).sendKeys("House No. 1234");
		driver.findElement(By.name("city")).sendKeys("Plainsboro");
		driver.findElement(By.name("postcode")).sendKeys("08546");

		// Country Dropdown
		Select country = new Select(driver.findElement(By.name("country_id")));
		country.selectByVisibleText("United States");

		// Thread.sleep to wait till the state dropdown refresh is complete
		Thread.sleep(1000);

		// Region / State Dropdown
		wait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(By.xpath("//select[@name='zone_id']/option[text()='New Jersey']")));
		Select state = new Select(driver.findElement(By.name("zone_id")));
		state.selectByVisibleText("New Jersey");

		driver.findElement(By.xpath(parentlocator + "//input[@name='agree']")).click();

		driver.findElement(By.xpath(parentlocator + "//input[@value='Continue']")).click();

		// Thread.sleep(1000);
		System.out.println("Successfully added all account and billing details");
	}

	@Test(dependsOnMethods = "AccountBillingDetails")
	public void PaymentMethodDetails() {

		System.out.println("\n15. Add comments, click continue and check the error message related to payment method");

		String parentlocator = "//div[@id='collapse-payment-method']";
		wait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(By.xpath(parentlocator + "//textarea[@name='comment']")));
		driver.findElement(By.xpath("//textarea[@name='comment']")).sendKeys("Test Comment");
		driver.findElement(By.xpath(parentlocator + "//input[@name='agree']")).click();

		// Wait until the error message is displayed
		String strLocator = parentlocator + "//div[contains(@class,'alert-warning')]";
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(strLocator)));

		// Capture error message and verify with expected
		String actualError = driver.findElement(By.xpath(strLocator)).getText();
		Assert.assertEquals(actualError, expectedWarining);

		System.out.println("Message displayed on screen ---> " + actualError);

		driver.findElement(By.xpath("//a[text()='contact us']")).click();

	}

	@Test(dependsOnMethods = "PaymentMethodDetails")
	public void ContactUsForm() {
		System.out.println("\n16. Click on contact us hyperlink and submit a contact request and click continue");

		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div/h1[text()='Contact Us']")));
		driver.findElement(By.id("input-enquiry")).sendKeys("Contact us details for the user");
		driver.findElement(By.cssSelector(".btn.btn-primary")).click();

		System.out.println("Successfully submitted the contact request\n\n");

	}

	// Method to generate random email
	private static String GenerateRandomEmail() {
		String email = "";
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		while (email.length() < 5) {
			int character = (int) (Math.random() * 26);
			email += alphabet.substring(character, character + 1);
			email += Integer.valueOf((int) (Math.random() * 99)).toString();
			email += "@gmail.com";
		}

		return email;
	}

	// Method to remove special characters appearing at the end of the message
	private String GetMessage(String text) {
		return text.substring(0, text.lastIndexOf('!') + 1);
	}

	@AfterTest()
	public void teardown() throws InterruptedException {
		driver.quit();
	}

}