package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "logoutDiv")
    private WebElement logoutButton;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(css = "#nav-credentials button.btn-info")
    private WebElement addNewButton;

    @FindBy(css = ".modal-footer .btn-primary")
    private WebElement saveChangesButton;

    @FindBy(css = ".alert-success")
    private WebElement successAlert;

    @FindBy(css = "#credentialModal .modal-footer .btn-primary")
    private WebElement saveCredentialButton;


    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10); // Increased wait time for slower loading elements
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        logoutButton.findElement(By.tagName("button")).click();
    }

    public void goToCredentialsTab() {
        wait.until(ExpectedConditions.elementToBeClickable(credentialsTab)).click();
    }

    public void clickAddNewButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addNewButton)).click();
    }
    public void saveCredential() {
        WebDriverWait wait = new WebDriverWait(driver, 20); // Increased timeout for visibility
        try {
            // Wait for the credentials modal to be fully visible
            WebElement credentialModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
            System.out.println("Credentials modal is visible.");

            // Wait for the button to be visible within the modal
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(saveCredentialButton));
            System.out.println("Save button in credentials modal is visible and clickable.");

            // Attempt to click the save button
            saveButton.click();
            System.out.println("Save button in credentials modal clicked successfully.");
        } catch (TimeoutException e) {
            System.out.println("Timeout while waiting for the credentials Save button to become visible.");
            throw e;
        } catch (ElementClickInterceptedException ex) {
            System.out.println("Element click intercepted, falling back to JavaScript click.");
            // Fall back to JavaScript click if regular click fails
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveCredentialButton);
            System.out.println("Save button in credentials modal clicked using JavaScript.");
        }
    }

    public void waitForSuccessAlert() {
        // Wait for the success alert to be visible and click the continue link
        wait.until(ExpectedConditions.visibilityOf(successAlert));
        WebElement continueLink = successAlert.findElement(By.tagName("a"));
        continueLink.click();
    }
}
