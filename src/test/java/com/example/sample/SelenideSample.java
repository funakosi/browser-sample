package com.example.sample;

import static com.codeborne.selenide.Selenide.*;
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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.ElementNotFound;
import com.example.common.DateTools;
import com.example.common.PlatformUtils;

public class SelenideSample {

	//slf4j使用
	private final static Logger log = LoggerFactory.getLogger("TestLog");
	private  WebDriver driver = null;

	@Before
	public void setUp() {
		Configuration.browser = WebDriverRunner.CHROME;
		String driverPath = "";
		if (PlatformUtils.isWindows()) {
			driverPath = "driver/chromedriver.exe";
		} else if (PlatformUtils.isLinux()) {
			driverPath = "driver/chromedriver";
		}
		System.setProperty("webdriver.chrome.driver", driverPath);
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--headless");
		//chromeOptions.addArguments("headless","disable-gpu");
		Map<String, Object> chromePrefs = new HashMap<>();
        //PopUp表示を抑制
        chromePrefs.put("profile.default_content_settings.popups", 0);
        //ダウンロードフォルダ指定(テストメソッドごとに格納パスを切り替える等任意で設定)
        chromePrefs.put("download.default_directory", "/User/Temp/result/DownloadFile");
        //ダウンロード先指定ダイアログ表示抑制
        chromePrefs.put("download.prompt_for_download", false);
        chromeOptions.setExperimentalOption("prefs", chromePrefs);
	    driver = new ChromeDriver(chromeOptions);
	    WebDriverRunner.setWebDriver(driver);
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void selnideChromeTest() {
		String method = new Object(){}.getClass().getEnclosingMethod().getName();
		try {
			open("http://example.selenium.jp/reserveApp/");
			LocalDate nextDay = DateTools.getNextDay($("#reserve_year").val(), $("#reserve_month").val(), $("#reserve_day").val());
			$("#reserve_year").val(String.valueOf(nextDay.getYear()));
			$("#reserve_month").val(String.valueOf(nextDay.getMonthValue()));
			$("#reserve_day").val(String.valueOf(nextDay.getDayOfMonth()));
			$("#guestname").val("山田　太郎");
			$("#goto_next").click();
			$("#commit").click();
			assertThat($(By.tagName("h1")).getText(),is("予約を完了しました。"));
			log.info("{}-{}..PASSED",method,Configuration.browser);
		} catch (ElementNotFound e) {
			log.info("{}-{}..FAILED",method,Configuration.browser);
			log.error(e.getLocalizedMessage());
			fail(e.getMessage());
		} finally {
			close();
		}
	}
}
