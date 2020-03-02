package ru.iopump.qa.testcontainers;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import ru.iopump.qa.support.http.LocalSimpleHtmlServer;
import ru.iopump.qa.support.http.LocalSimpleHtmlServer.TestHtmlServer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.testcontainers.containers.GenericContainer.INTERNAL_HOST_HOSTNAME;

public class ContainersTest {
    private static final String HTML;

    static {
        try {
            HTML = IOUtils.resourceToString("/index.html", StandardCharsets.UTF_8);
            Testcontainers.exposeHostPorts(8080);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    @Rule
    public final TestHtmlServer htmlServer = LocalSimpleHtmlServer.of(8080, "/")
            .asTestRule(HTML);

    @Rule
    public final BrowserWebDriverContainer<?> browserWebDriverContainer = new BrowserWebDriverContainer<>()
            .withCapabilities(new ChromeOptions())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL,
                    new File("./build/"));

    @Test
    public void testWebDriver() throws URISyntaxException, IOException, InterruptedException {
        final WebDriver driver = browserWebDriverContainer.getWebDriver();
        final String url = "http://" + INTERNAL_HOST_HOSTNAME + ":" + htmlServer.getPort();

        final HttpClient httpClient = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(new URL(htmlServer.getUrl()).toURI())
                .GET()
                .build();
        final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        System.out.println("\nPage source via native Java Http Client: " + response.body());

        driver.get(url);
        System.out.println("\nPage source via TestContainers WebDriver: " + driver.getPageSource());

        final List<WebElement> elementList = driver.findElements(By.tagName("button"));
        System.out.println("\nButtons via TestContainers WebDriver: " + elementList);

        Assertions.assertThat(elementList).hasSize(5);
    }
}