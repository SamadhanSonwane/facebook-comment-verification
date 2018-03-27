package pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TargetPost {
	
	static XSSFWorkbook workbook;
	// method to get input data from the file FacebookCommentCheck.xlsx
	public XSSFSheet getSheet(String sheetName) throws IOException{
		File source = new File(System.getProperty("user.dir") + "\\input-data\\FacebookCommentCheck.xlsx");
		FileInputStream fis = new FileInputStream(source);
		
		workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheet(sheetName);
		
		return sheet;
	}
	
	// method to navigate to target post and read the comments
	public void getPostComments(WebDriver driver) throws IOException, InterruptedException{
		
		// creating output workbook
		XSSFWorkbook outputWorkbook = new XSSFWorkbook();
		XSSFSheet outputSheet = outputWorkbook.createSheet("AllComments");
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		driver.get((getSheet("TargetPost").getRow(1).getCell(1).getStringCellValue()));	// navigate to the target post
		
		// wait for the photo to appear in snowlift mode and hit the cancel button
		WebElement cancelPhotoSnowlift = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath((getSheet("TargetPost").getRow(1).getCell(4).getStringCellValue()))));
		cancelPhotoSnowlift.click();
		Thread.sleep(1000);
		
		// write output file column headers
		writeColumnHeaders(outputSheet);
		
		// read all the users and comments and write in the output file
		List<WebElement> users = wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath((getSheet("TargetPost").getRow(2).getCell(4).getStringCellValue())))));
		List<WebElement> comments = wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath((getSheet("TargetPost").getRow(3).getCell(4).getStringCellValue())))));
		for(int i=0; i<comments.size(); i++){	// i=1 because row 0 will contain column headers
			
			Row row = outputSheet.getRow(i + 1);
			if (row == null){
				row = outputSheet.createRow(i + 1);
			}
			
			Cell cell = row.getCell(0);
			if (cell == null){
				cell = row.createCell(0);
			}
			cell.setCellValue(users.get(i).getText());	// writing users in the output file
			
			cell = row.getCell(1);
			if (cell == null){
				cell = row.createCell(1);
			}
			cell.setCellValue(comments.get(i).getText());	// writing comments in the output file
		}
		
		FileOutputStream fos = new FileOutputStream(new File(System.getProperty("user.dir") + "\\output-data\\FacebookPostComments.xlsx"));
		outputWorkbook.write(fos);
		fos.close();
		outputWorkbook.close();
	}
	
	// method to write column headers in the output file 
	public void writeColumnHeaders(XSSFSheet outputSheet) throws IOException{
		
		// writing column headers
		Row hRow = outputSheet.getRow(0);
		if (hRow == null){
			hRow = outputSheet.createRow(0);
		}
		Cell hCell = hRow.getCell(0);
		if (hCell == null){
			hCell = hRow.createCell(0);
		}
		hCell.setCellValue("User");	// column header: User
		hCell = hRow.getCell(1);
		if (hCell == null){
			hCell = hRow.createCell(1);
		}
		hCell.setCellValue("Comment");	// column header: Comment
	}
	
	// method to log out from the Facebook account
	public void logout(WebDriver driver) throws InterruptedException, IOException{
		
		driver.findElement(By.xpath(getSheet("TargetPost").getRow(4).getCell(4).getStringCellValue())).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath(getSheet("TargetPost").getRow(5).getCell(4).getStringCellValue())).click();	// click the log out button
		
		workbook.close();
	}
	
	// calculate total number of blocked words
	public int getBlockedWordCount() throws IOException{
		
		int i = 0;
		while(!getSheet("BlockedWords").getRow(i + 1).getCell(0).getStringCellValue().equals("")){
			i++;
		}
		
		return i;
	}
	
	// method to navigate to target post, read all the comments and record comments with blocked content
	public void getPostWithBlockedWords(WebDriver driver) throws IOException, InterruptedException{
		
		// creating output workbook
		XSSFWorkbook outputWorkbook = new XSSFWorkbook();
		XSSFSheet outputSheet = outputWorkbook.createSheet("CommentsWithBlockedContent");
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		driver.get((getSheet("TargetPost").getRow(1).getCell(1).getStringCellValue()));	// navigate to the target post
		
		// wait for the photo to appear in snowlift mode and hit the cancel button
		WebElement cancelPhotoSnowlift = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath((getSheet("TargetPost").getRow(1).getCell(4).getStringCellValue()))));
		cancelPhotoSnowlift.click();
		Thread.sleep(1000);
		
		// write output file column headers
		writeColumnHeaders(outputSheet);
		
		// read all the users and comments and write in the output file
		List<WebElement> users = wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath((getSheet("TargetPost").getRow(2).getCell(4).getStringCellValue())))));
		List<WebElement> comments = wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath((getSheet("TargetPost").getRow(3).getCell(4).getStringCellValue())))));
		
		int commentsWithBlockedWord = 0;
		
		for(int i=0; i<comments.size(); i++){
			
			// record comment only if it contains a blocked word
			if(searchBlockedWord(comments.get(i).getText())){
				Row row = outputSheet.getRow(commentsWithBlockedWord + 1);
				if (row == null){
					row = outputSheet.createRow(commentsWithBlockedWord + 1);
				}
				
				Cell cell = row.getCell(0);
				if (cell == null){
					cell = row.createCell(0);
				}
				cell.setCellValue(users.get(i).getText());	// writing users in the output file
				
				cell = row.getCell(1);
				if (cell == null){
					cell = row.createCell(1);
				}
				cell.setCellValue(comments.get(i).getText());	// writing comments in the output file
				
				commentsWithBlockedWord++;
			}

		}
		
		FileOutputStream fos = new FileOutputStream(new File(System.getProperty("user.dir") + "\\output-data\\CommentsWithBlockedContent.xlsx"));
		outputWorkbook.write(fos);
		fos.close();
		outputWorkbook.close();
	}
	
	// method to search blocked words in a comment
	public boolean searchBlockedWord(String comment) throws IOException{
		
		int blockedWordCount = getBlockedWordCount();	// count the number of words in blocked content list
		
		String blockedWord = "";
		for(int i=1; i<=blockedWordCount; i++){
			
			blockedWord = getSheet("BlockedWords").getRow(i).getCell(0).getStringCellValue().toLowerCase();	// reading a blocked word from the list
			if(comment.toLowerCase().contains(blockedWord)){
				return true;
			}
		}
		
		return false;
	}
}
