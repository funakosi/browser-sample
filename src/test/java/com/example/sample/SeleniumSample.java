package com.example.sample;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.example.common.DateTools;
import com.example.common.PlatformUtils;

public class SeleniumSample {

	private  WebDriver driver = null;

	@Before
	public void setUp() {
		String driverPath = "";
		if (PlatformUtils.isWindows()) {
			driverPath = "driver/chromedriver.exe";
		} else if (PlatformUtils.isLinux()) {
			driverPath = "driver/chromedriver";
		}
		System.setProperty("webdriver.chrome.driver", driverPath);
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--headless");
		Map<String, Object> chromePrefs = new HashMap<>();
        //PopUp表示を抑制
        chromePrefs.put("profile.default_content_settings.popups", 0);
        //ダウンロードフォルダ指定(テストメソッドごとに格納パスを切り替える等任意で設定)
        chromePrefs.put("download.default_directory", "/User/Temp/result/DownloadFile");
        //ダウンロード先指定ダイアログ表示抑制
        chromePrefs.put("download.prompt_for_download", false);
        chromeOptions.setExperimentalOption("prefs", chromePrefs);
	    driver = new ChromeDriver(chromeOptions);
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void seleniumChromeTest() throws InterruptedException {
		driver.get("http://example.selenium.jp/reserveApp/");
	    WebElement reserveYear = driver.findElement(By.id("reserve_year"));
	    WebElement reserveMonth = driver.findElement(By.id("reserve_month"));
	    WebElement reserveDay = driver.findElement(By.id("reserve_day"));
	    LocalDate nextDay = DateTools.getNextDay(
	    		reserveYear.getAttribute("value"),
	    		reserveMonth.getAttribute("value"),
	    		reserveDay.getAttribute("value"));
	    reserveYear.clear();
	    reserveYear.sendKeys(String.valueOf(nextDay.getYear()));
	    reserveMonth.clear();
	    reserveMonth.sendKeys(String.valueOf(nextDay.getMonthValue()));
	    reserveDay.clear();
	    reserveDay.sendKeys(String.valueOf(nextDay.getDayOfMonth()));
	    driver.findElement(By.id("guestname")).sendKeys("山田　太郎");
	    driver.findElement(By.id("goto_next")).click();
	    driver.findElement(By.id("commit")).click();;
	    WebElement comment = driver.findElement(By.tagName("h1"));
	    assertThat(comment.getText(), is("予約を完了しました。"));
	    System.out.println("seleniumChromeTest..passed");
	    Thread.sleep(3000);
	}

}
