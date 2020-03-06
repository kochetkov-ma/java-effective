package ru.iopump.qa.proxy2;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import ru.iopump.qa.support.http.LocalSimpleHtmlServer;
import ru.iopump.qa.support.selenium.listener.HighlighterListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class SelenideTest {

    private static final String HTML;

    static {
        try {
            HTML = IOUtils.resourceToString("/index.html", StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    @Rule
    public final LocalSimpleHtmlServer.TestHtmlServer htmlServer = LocalSimpleHtmlServer.of(8080, "/")
        .asTestRule(HTML);

    @Test
    public void test() {
        WebDriverRunner.addListener(HighlighterListener.newSingleThreadHighlighterListener());

        Selenide.open(htmlServer.getUrl());
        Page page = Selenide.page(Page.class);




        // display
        page.elementsCollection.shouldBe(CollectionCondition.size(5));
        assert page.webElementList.size() == 5;
        assert page.selenideElementList.size() == 5;


        // hide
        page.hide.click();

        assertThat(page.webElementList).hasSize(4);
        assert page.selenideElementList.size() == 4;

        page.elementsCollection.shouldBe(CollectionCondition.size(4));



        // display
        page.hide.click();

        page.elementsCollection.shouldBe(CollectionCondition.size(5));
        assert page.webElementList.size() == 5;
        assert page.selenideElementList.size() == 5;

        Selenide.sleep(1000);
    }

    @Test
    public void testCache() {
        WebDriverRunner.addListener(HighlighterListener.newSingleThreadHighlighterListener());

        Selenide.open(htmlServer.getUrl());
        Page page = Selenide.page(Page.class);


        // display
        assert page.cachedWebElementList.size() == 5;

        // hide
        page.hide.click();
        assertThat(page.cachedWebElementList).hasSize(4);
    }


    @Test
    public void single() {
        WebDriverRunner.addListener(HighlighterListener.newSingleThreadHighlighterListener());

        Selenide.open(htmlServer.getUrl());
        Page page = Selenide.page(Page.class);

        // display
        SelenideElement selenideElement = page.elementsCollection.get(2);
        SelenideElement lastSelenideElement = page.elementsCollection.get(4);
        WebElement lastWebElement = page.elementsCollection.get(4);
        assertThat(selenideElement.exists()).isTrue();
        assertThat(lastSelenideElement.exists()).isTrue();
        assertThat(lastWebElement.isDisplayed()).isTrue();

        ElementsCollection oldCollection = page.elementsCollection.snapshot();

        // hide
        page.hide.click();
        Selenide.sleep(600);

        assertThat(selenideElement.exists()).isTrue(); // пропавший элемент другой и он существует
        assertThat(oldCollection.get(2).exists()).isTrue(); // не существует от старой коллекции

        selenideElement.should(Condition.exist);
        oldCollection.get(2).shouldNot(Condition.exist); // только тут все правильно
        SelenideElement old = oldCollection.get(2);
        old.shouldNot(Condition.exist); // только тут все правильно

        // проблема пропадания элемента
        selenideElement.click();
        assertThat(lastSelenideElement.exists()).isFalse(); // false
        assertThat(lastWebElement.isDisplayed()).isFalse(); // false



        // для последнего элемента используется отдельный прокси в Selenide
        lastSelenideElement = page.elementsCollection.last();
        // Selenium использует только индекс
        lastWebElement = page.elementsCollection.get(3);

        assertThat(lastSelenideElement.text()).isEqualTo("Button-5");
        assertThat(lastWebElement.getText()).isEqualTo("Button-5");

        // display
        page.hide.click();
        Selenide.sleep(600);
        assertThat(lastSelenideElement.text()).isEqualTo("Button-5");
        assertThat(lastWebElement.getText()).isEqualTo("Button-4"); // последний элемент
    }

    @Getter
    @NoArgsConstructor
    private static class Page {
        @FindBy(tagName = "button")
        private List<WebElement> webElementList;
        @FindBy(tagName = "button")
        @CacheLookup
        private List<WebElement> cachedWebElementList;
        @FindBy(tagName = "button")
        private List<SelenideElement> selenideElementList;
        @FindBy(tagName = "button")
        private ElementsCollection elementsCollection;
        @FindBy(id = "hide")
        private SelenideElement hide;
    }
}