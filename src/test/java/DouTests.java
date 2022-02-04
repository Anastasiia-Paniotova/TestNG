package test.java;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.time.Duration;

public class DouTests {
    WebDriver driver;

    @DataProvider(name = "DataProvider")
    public Object[][] dataProviderMethod() {
        return new Object[][] {{"TestNg"},{"JUnit"}};
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpPrerequisites(){
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Professional\\Desktop\\auto_hw\\chrome_webdriver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://dou.ua/");
    }

    // получаем цвет после наведения курсора на меню Форум
    @Test(groups = {"positive"})
    public void hoverMenu() {
        WebElement forumElement = driver.findElement(By.xpath("//*[text()='Форум']"));

        Actions moveMouse = new Actions(driver);
        moveMouse.moveToElement(forumElement).perform();

        String actual = forumElement.getCssValue("color");

        Assert.assertEquals(actual,"rgba(255, 0, 0, 1)", "Error: color is not red");
    }

    //клик на лого редиректит юзера на главную страницу
    @Test(groups = {"positive"})
    public void redirectByClickingLogo() {
        WebElement robotaElement = driver.findElement(By.xpath("//*[text()='Робота']"));
        robotaElement.click();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        WebElement logo = driver.findElement(By.className("logo"));
        logo.click();

        String URL = driver.getCurrentUrl();

        assertThat(URL,equalTo("https://dou.ua/"));

    }

    //получить автора и название первой статьи в разделе Технические статьи
//    @Test(groups = {"positive"})
//    public void getAuthorAndTitle() {
//        WebElement firstArticle = driver.findElement(By.xpath("//div[@class='b-articles b-articles_tech']/ul/li/a[1]"));
//        firstArticle.click();
//
//        WebElement author = driver.findElement(By.className("name"));
//        WebElement title = driver.findElement(By.xpath("//*[@class='b-typo b-typo_post']/h1"));
//
//        //возможно они зафейляться, так как статьи меняются иногда
//        assertThat(author.getText(), equalToIgnoringCase("Taras Ustyianovych, Operational Intelligence Engineer в EPAM"));
//        assertThat(title.getText(),equalToIgnoringCase("MLOps: універсальний гайд з моніторингу моделей на проді"));
//
//    }

    // тест с DataProvider, где проверяем, что наш поисковый запрос содержит ключевое слово
    @Test(dataProvider = "DataProvider", groups = {"negative"})
    public void searchWithDataProvider(String data) {
        WebElement searchField = driver.findElement(By.id("txtGlobalSearch"));
        searchField.sendKeys(data);
        searchField.sendKeys(Keys.ENTER);

        WebElement result = driver.findElement(By.xpath("//*[@class='gs-bidi-start-align gs-snippet'][1]"));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        Boolean actualResult = result.getText().contains(data);

        Assert.assertTrue(actualResult,"Text doesn't contain keyword");

    }

    //проверка что есть страница "Новий топік"
    @Test(groups = {"negative"})
    public void addNewTopic() {
        WebElement forumElement = driver.findElement(By.xpath("//*[text()='Форум']"));
        forumElement.click();

        WebElement addTopic = (new WebDriverWait(driver,Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='wrap add-content-link']/a"))));
        addTopic.click();

        Boolean actualResult = driver.findElement(By.className("page-head")).getText().contains("Новий топік");

        Assert.assertTrue(actualResult,"Wrong title");

    }

    @AfterMethod(alwaysRun = true)
    public void closeBrowser() {
        driver.quit();
    }


}
