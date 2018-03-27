package pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FacebookLogin {
	
	static XSSFWorkbook workbook;
	// method to get input data from the file FacebookCommentCheck.xlsx
	public XSSFSheet getSheet(String sheetName) throws IOException{
		File source = new File(System.getProperty("user.dir") + "\\input-data\\FacebookCommentCheck.xlsx");
		FileInputStream fis = new FileInputStream(source);
		
		workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheet(sheetName);
		
		return sheet;
	}
	
	// method to launch the Facebook login page
	public void launchPage(WebDriver driver) throws IOException{
		
		String url = getSheet("FacebookLogin").getRow(1).getCell(1).getStringCellValue();
		driver.get(url);

		workbook.close();
	}
	
	// method to login to Facebook account
	public void performLogin(WebDriver driver) throws IOException{
		
		WebDriverWait wait = new WebDriverWait(driver, 15);
		WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath((getSheet("FacebookLogin").getRow(1).getCell(4).getStringCellValue()))));
		WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath((getSheet("FacebookLogin").getRow(2).getCell(4).getStringCellValue()))));
		WebElement login = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath((getSheet("FacebookLogin").getRow(3).getCell(4).getStringCellValue()))));
		
		email.sendKeys(getSheet("FacebookLogin").getRow(2).getCell(1).getStringCellValue());
		password.sendKeys(getSheet("FacebookLogin").getRow(3).getCell(1).getStringCellValue());
		
		login.click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath((getSheet("FacebookLogin").getRow(4).getCell(4).getStringCellValue()))));
	}
}
