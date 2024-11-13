package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	private void doMockSignUp(String firstName, String lastName, String userName, String password) {
		driver.get("http://localhost:" + this.port + "/login");

		// Try logging in first to avoid duplicate signups
		try {
			doLogIn(userName, password);
		} catch (Exception e) {
			// Proceed with signup if login fails
			WebDriverWait wait = new WebDriverWait(driver, 2);
			driver.get("http://localhost:" + this.port + "/signup");

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName"))).sendKeys(firstName);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName"))).sendKeys(lastName);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername"))).sendKeys(userName);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword"))).sendKeys(password);

			WebElement buttonSignUp = driver.findElement(By.id("submit-button"));
			buttonSignUp.click();

			Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
			// Click on continue link
			driver.findElement(By.id("login-link")).click();
		}
	}

	private void doLogIn(String userName, String password) {
		driver.get("http://localhost:" + this.port + "/login");

		WebDriverWait wait = new WebDriverWait(driver, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername"))).sendKeys(userName);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword"))).sendKeys(password);

		driver.findElement(By.id("submit-button")).click();
		wait.until(ExpectedConditions.titleContains("Home"));
	}

	@Test
	public void testUnauthorizedRestrictions() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void testUserSignupLoginLogout() {
		doMockSignUp("Test", "User", "testuser", "password");
		doLogIn("testuser", "password");

		Assertions.assertEquals("Home", driver.getTitle());

		driver.findElement(By.id("logoutDiv")).findElement(By.tagName("button")).click();
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}
	// Note Management Tests (Create, Edit, Delete)
	// Credential Management Tests (Create, Edit, Delete)
	// (All previously defined methods)

	// Newly Added Tests:

	/**
	 * Test redirection after successful sign-up.
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection", "Test", "RT", "123");

		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * Test that accessing a non-existent page doesn't show a whitelabel error.
	 */
	@Test
 	public void testBadUrl() {
		// Create a test account and log in
		doMockSignUp("URL", "Test", "UT", "123");
		doLogIn("UT", "123");

		// Try to access a random made-up URL
		driver.get("http://localhost:" + this.port + "/some-random-page");

		// Check that the page does not contain the default Whitelabel error text, but instead shows custom error content
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"), "Default error page was displayed.");
		Assertions.assertTrue(driver.getPageSource().contains("Oops! Something went wrong."), "Custom error page was not displayed.");
	}


	/**
	 * Test large file uploads to ensure the app handles them gracefully.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Test
	public void testCreateNote() {
		doMockSignUp("Note", "Test", "notetest", "password");
		doLogIn("notetest", "password");

		WebDriverWait wait = new WebDriverWait(driver, 5);
		WebElement navNotesTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		navNotesTab.click();

		WebElement addNewNoteButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn-info")));
		addNewNoteButton.click();

		WebElement noteTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		noteTitle.sendKeys("Test Note");

		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.sendKeys("This is a test note.");

		WebElement saveNoteButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn-primary")));
		saveNoteButton.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
		driver.findElement(By.cssSelector(".alert-success a")).click();

		navNotesTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		navNotesTab.click();

		Assertions.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='Test Note']"))).isDisplayed());
	}

	@Test
	public void testEditNote() {
		// Reuse the creation logic as editing requires a note to exist
		testCreateNote();

		WebDriverWait wait = new WebDriverWait(driver, 5);

		// Click the "Edit" button for the existing note
		WebElement editButton = driver.findElement(By.xpath("//button[contains(text(),'Edit')]"));
		wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();

		// Edit the note title and description
		WebElement noteTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		noteTitle.clear();
		noteTitle.sendKeys("Edited Note");

		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.clear();
		noteDescription.sendKeys("This is an edited note.");

		// Ensure that 'Save changes' button is visible and clickable
		WebElement saveChangesButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn-primary")));
		saveChangesButton.click();

		// Wait for success message and click the continue link
		WebElement successAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
		WebElement continueLink = successAlert.findElement(By.tagName("a"));
		continueLink.click();

		// Re-click the Notes tab to refresh the view
		WebElement navNotesTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		navNotesTab.click();

		// Verify that the edited note is displayed in the table
		Assertions.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='Edited Note']"))).isDisplayed());
	}

	@Test
	public void testDeleteNote() {
		testCreateNote(); // Ensure a note is created before trying to delete

		WebDriverWait wait = new WebDriverWait(driver, 10); // Adjusted wait duration for consistency

		// Navigate to the Notes tab
		WebElement navNotesTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		navNotesTab.click();

		// Wait for the notes table to be visible
		WebElement notesTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));

		// Identify the last row in the notes table and locate the delete button for that row
		List<WebElement> deleteButtons = notesTable.findElements(By.xpath("//a[contains(@class, 'btn-danger')]"));

		if (deleteButtons.isEmpty()) {
			Assertions.fail("No delete button found in the notes table.");
		} else {
			// Click the delete button on the last note (or modify as needed)
			WebElement lastDeleteButton = deleteButtons.get(deleteButtons.size() - 1);
			wait.until(ExpectedConditions.elementToBeClickable(lastDeleteButton)).click();
		}

		// Wait for success message and click the continue link
		WebElement successAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
		WebElement continueLink = successAlert.findElement(By.tagName("a"));
		continueLink.click();

		// Re-click the Notes tab to refresh the view
		navNotesTab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		navNotesTab.click();

		// Verify that the deleted note is no longer present in the table
		String deletedNoteXPath = "//th[text()='Test Note']";
		Assertions.assertTrue(wait.withTimeout(java.time.Duration.ofSeconds(10))
						.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(deletedNoteXPath))),
				"Note was not deleted successfully.");
	}


	@Test
	public void testCreateCredential() {
		// Sign up and log in as the test user
		doMockSignUp("Credential", "Test", "credentialtest", "password");
		doLogIn("credentialtest", "password");

		// Navigate to the HomePage and open the Credentials tab
		HomePage homePage = new HomePage(driver);
		homePage.goToCredentialsTab();

		WebDriverWait wait = new WebDriverWait(driver, 15); // Adjusted wait duration for potentially slow interactions

		// Click "Add a New Credential" button
		homePage.clickAddNewButton();

		// Wait for credential form fields to be visible and fill them in
		WebElement credentialUrl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		credentialUrl.sendKeys("http://test.com");

		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.sendKeys("testuser");

		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.sendKeys("password");

		// Save the credential using the page's method
		homePage.saveCredential();

		// Wait for the modal to close and the success alert to appear
		homePage.waitForSuccessAlert();

		// Debugging - Print the current page source
		System.out.println(driver.getPageSource());

		// Re-click the Credentials tab to refresh the view
		homePage.goToCredentialsTab(); // Re-navigate to the Credentials tab to refresh the view

		// Verify that the new credential is displayed in the table
		String xpathExpression = "//th[text()='http://test.com']/following-sibling::td[text()='testuser']";
		Assertions.assertTrue(wait.withTimeout(java.time.Duration.ofSeconds(15))
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathExpression))).isDisplayed());
	}

	@Test
	public void testEditCredential() {
		testCreateCredential(); // Ensure a credential is created before editing

		WebDriverWait wait = new WebDriverWait(driver, 10); // Same wait duration as in testCreateCredential

		// Navigate to the Credentials tab
		HomePage homePage = new HomePage(driver);
		homePage.goToCredentialsTab();

		// Click the "Edit" button for the existing credential
		WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Edit')]")));
		editButton.click();

		// Wait for credential form fields to be visible and edit them
		WebElement credentialUrl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		credentialUrl.clear();
		credentialUrl.sendKeys("http://edited.com");

		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.clear();
		credentialUsername.sendKeys("editeduser");

		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.clear();
		credentialPassword.sendKeys("newpassword");

		// Save the credential using the HomePage method (consistent with testCreateCredential)
		homePage.saveCredential();

		// Wait for the success message and click the continue link (consistent with testCreateCredential)
		homePage.waitForSuccessAlert();

		// Debugging - Print the current page source to ensure the changes were saved correctly (optional)
		System.out.println(driver.getPageSource());

		// Re-click the Credentials tab to refresh the view
		homePage.goToCredentialsTab();

		// Verify that the edited credential is displayed in the table using the same verification approach as testCreateCredential
		String editedCredentialXPath = "//th[text()='http://edited.com']/following-sibling::td[text()='editeduser']";
		Assertions.assertTrue(wait.withTimeout(java.time.Duration.ofSeconds(10))
						.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(editedCredentialXPath))).isDisplayed(),
				"The edited credential was not found in the table after saving.");
	}

	@Test
	public void testDeleteCredential() {
		testCreateCredential(); // Ensure a credential is created before trying to delete

		WebDriverWait wait = new WebDriverWait(driver, 10); // Adjusted wait duration for consistency with other tests

		// Navigate to the Credentials tab
		HomePage homePage = new HomePage(driver);
		homePage.goToCredentialsTab();

		// Wait for the credentials table to be visible
		WebElement credentialsTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

		// Identify the delete button within the credentials table for the first credential
		// Adjust XPath to target `<a>` elements with class `btn-danger`
		List<WebElement> deleteButtons = credentialsTable.findElements(By.xpath("//a[contains(@class, 'btn-danger')]"));

		if (deleteButtons.isEmpty()) {
			Assertions.fail("No delete button found in the credentials table.");
		} else {
			// Click the delete button for the first credential (or last if preferred)
			WebElement firstDeleteButton = deleteButtons.get(0); // Change index as needed
			wait.until(ExpectedConditions.elementToBeClickable(firstDeleteButton)).click();
		}

		// Wait for the success message and click the continue link
		homePage.waitForSuccessAlert(); // Wait for the success alert and handle it

		// Re-click the Credentials tab to refresh the view
		homePage.goToCredentialsTab();

		// Verify that the credential is no longer present in the table using an appropriate method
		String deletedCredentialXPath = "//th[text()='http://test.com']";
		Assertions.assertTrue(wait.withTimeout(java.time.Duration.ofSeconds(10))
						.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(deletedCredentialXPath))),
				"Credential was not deleted successfully.");
	}



}
