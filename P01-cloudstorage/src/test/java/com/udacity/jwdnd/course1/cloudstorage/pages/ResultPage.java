package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {
    @FindBy(xpath = "//div[@class='alert alert-success']")
    private WebElement successMessage;

    @FindBy(xpath = "//div[@class='alert alert-danger']")
    private WebElement errorMessage;

    public ResultPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public boolean isSuccess() {
        return successMessage != null && successMessage.isDisplayed();
    }

    public boolean isError() {
        return errorMessage != null && errorMessage.isDisplayed();
    }
}
