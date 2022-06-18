package com.opensea.autoTest.service

import com.opensea.autoTest.service.util.WebDriverWaitUtil
import com.opensea.autoTest.util.DataUtil
import com.opensea.autoTest.util.Item
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime


@Component
class ItemService(
//    private val config: Config
) {
    companion object {
        val log = LoggerFactory.getLogger(ItemService::class.java)
    }

    //    private val chrome = serviceGateway.chromeProfile()
    private fun executeCdp(chrome: ChromeDriver) {

        val data = mapOf<String, Any>(
            "source" to "Object.defineProperty(navigator, 'webdriver', {webdriver:undefined});"
        )
        chrome.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", data)
    }

    /**
     * chrome配置
     */
    fun chromeProfile(): RemoteWebDriver {

        val options = ChromeOptions()

//        options.addArguments("user-agent=${config.useragent}")
        options.addArguments("--disable-gpu")
        options.addArguments("--no-sandbox")
        options.addArguments("--disable-dev-shm-usage")
        options.addArguments("--start-maximized")

        val chrome = ChromeDriver(options)
        this.executeCdp(chrome = chrome)

        return chrome
    }

    val chrome = chromeProfile()

    fun getItem() {

        var times = 1
        var loopTimesF = 4
        var loopTimesS = 1
        var itemUrl = ""

        WebDriverWaitUtil(chrome = chrome).start("https://opensea.io/collection/perdidos-no-tempo")
        val itemsTotal =
            if (WebDriverWaitUtil(chrome = chrome).verifyByXpath("/html/body/div[1]/div/main/div/div/div[5]/div/div[3]/div[3]/div[3]/div[2]/div/p")) {
                chrome.findElementByXPath("/html/body/div[1]/div/main/div/div/div[5]/div/div[3]/div[3]/div[3]/div[2]/div/p").text.split(
                    " "
                )[0].replace(",", "").toInt()
            } else
                2100
        moveSc(chrome = chrome, times = 1)

        while (times < itemsTotal) {
            if (times == 1) {
                while (loopTimesF < 7) {
                    itemUrl =
                        if (WebDriverWaitUtil(chrome = chrome).verifyByXpath("/html/body/div[1]/div/main/div/div/div[5]/div/div[3]/div[3]/div[3]/div[3]/div[2]/div/div/div[${loopTimesF}]/div/article/a")) {
                            chrome.findElementByXPath("/html/body/div[1]/div/main/div/div/div[5]/div/div[3]/div[3]/div[3]/div[3]/div[2]/div/div/div[${loopTimesF}]/div/article/a")
                                .getAttribute("href")
                        } else
                            "error"
                    val item = Item(
                        item_id = times,
                        item_url = "https://opensea.io/$itemUrl",
                        status_click = false,
                        status_queued = false,
                        status_error = false,
                        createdTime = LocalDateTime.now()
                    )
                    DataUtil.insert(item = item)
                    log.info("DataUtil.insert:${times}")
                    loopTimesF += 1
                    times += 1
                }
                loopTimesF = 4
            } else {
                while (loopTimesS < 4) {
                    itemUrl =
                        if (WebDriverWaitUtil(chrome = chrome).verifyByXpath("/html/body/div[1]/div/main/div/div/div[5]/div/div[3]/div[3]/div[3]/div[3]/div[2]/div/div/div[${loopTimesS}]/div/article/a")) {
                            chrome.findElementByXPath("/html/body/div[1]/div/main/div/div/div[5]/div/div[3]/div[3]/div[3]/div[3]/div[2]/div/div/div[${loopTimesS}]/div/article/a")
                                .getAttribute("href")
                        } else
                            "error"
                    val item = Item(
                        item_id = times,
                        item_url = "https://opensea.io/$itemUrl",
                        status_click = false,
                        status_queued = false,
                        status_error = false,
                        createdTime = LocalDateTime.now()
                    )
                    DataUtil.insert(item = item)
                    log.info("DataUtil.insert:${times}")
                    loopTimesS += 1
                    times += 1
                }
                loopTimesS = 1
            }

            moveSc(chrome = chrome, times = 100)
            val verifyUrl =
                chrome.findElementByXPath("/html/body/div[1]/div/main/div/div/div[5]/div/div[3]/div[3]/div[3]/div[3]/div[2]/div/div/div[${loopTimesS}]/div/article/a")
                    .getAttribute("href")
            while (DataUtil.get("https://opensea.io/$verifyUrl").item_url.contains(verifyUrl)) {
                moveSc(chrome = chrome, times = 100)
            }
        }
    }

    private fun moveSc(chrome: RemoteWebDriver, times: Int) {
        if (times == 1) {
            val js = chrome as JavascriptExecutor
            js.executeScript("window.scrollBy(0, 500)")
            Thread.sleep(1000)
        } else {
            val js = chrome as JavascriptExecutor
            js.executeScript("window.scrollBy(0, 300)")
            Thread.sleep(1000)
        }
    }

    fun clickItem() {
        var itemSum = try {
            DataUtil.getSum()
        } catch (e: Exception) {
            0
        }

        while (itemSum > 0) {
            chrome.get(DataUtil.getById(itemSum).item_url)
            itemSum -= 1
            WebDriverWaitUtil(chrome = chrome)
                .clickByXPath(
                    msg = "进入item详情页面",
                    xpath = "/html/body/div[1]/div/main/div/div/div/div[1]/div/div[1]/div[2]/section[1]/div/div[2]/div/button[1]"
                )

            val stateQueued =
                WebDriverWaitUtil(chrome = chrome).verifyByXpath("//*[contains(text(), 'this item for an updat')]")

            DataUtil.update(
                itemID = itemSum,
                status_click = true,
                status_queued = stateQueued,
                status_error = !stateQueued
            )
        }
    }
}