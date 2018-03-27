package tests;

import java.io.IOException;

import mymethods.LaunchBrowser;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import pages.FacebookLogin;
import pages.TargetPost;

public class CommentCheck{
	
	@Test(enabled = false)
	public void openFacebookLoginPage() throws IOException{
		
		LaunchBrowser launcher = new LaunchBrowser();
		FacebookLogin loginRunner = new FacebookLogin();
		
		WebDriver driver = launcher.launchMyBrowser("Chrome");
		loginRunner.launchPage(driver);
		
		driver.quit();
	}
	
	// this test records all comments (excluding replies on comments) for a photo posted on Facebook
	@Test(enabled = false)
	public void recordPostComments() throws IOException, InterruptedException{
		
		LaunchBrowser launcher = new LaunchBrowser();
		FacebookLogin loginRunner = new FacebookLogin();
		TargetPost postRunner = new TargetPost();
		WebDriver driver = launcher.launchMyBrowser("Chrome");
		
		loginRunner.launchPage(driver);
		loginRunner.performLogin(driver);
		postRunner.getPostComments(driver);
		
		postRunner.logout(driver);
		driver.quit();
	}

	// this test records all the comments (excluding replies on comments) containing blocked words, for a photo posted on Facebook
	@Test
	public void recordCommentsWithBlockedWords() throws IOException, InterruptedException{
		
		LaunchBrowser launcher = new LaunchBrowser();
		FacebookLogin loginRunner = new FacebookLogin();
		TargetPost postRunner = new TargetPost();
		
		WebDriver driver = launcher.launchMyBrowser("Chrome");
		loginRunner.launchPage(driver);
		loginRunner.performLogin(driver);
		postRunner.getPostWithBlockedWords(driver);
		
		postRunner.logout(driver);
		driver.quit();
	}
}
