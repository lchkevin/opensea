package com.opensea.autoTest.service.util


import org.openqa.selenium.*
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration

class WebDriverWaitUtil(
    val chrome: RemoteWebDriver
) {

    companion object {
        val log: Logger = LoggerFactory.getLogger(WebDriverWaitUtil::class.java)
    }

    private var successful = true
    private val _timeoutSecond = 60L // 测试的时候超时为60s

    fun start(url: String): WebDriverWaitUtil {
        this.chrome.get(url)
        return this
    }

    // 默认超时为10s
    fun next(
        msg: String,
        timeoutSecond: Long = _timeoutSecond,
        function: (chrome: RemoteWebDriver) -> Any
    ): WebDriverWaitUtil {
        if (!successful) {
            log.info("In next action successful is false")
            return this
        }
        try {
            WebDriverWait(chrome, Duration.ofSeconds(timeoutSecond), Duration.ofMillis(500)).until {
                try {
                    val any = function(it as RemoteWebDriver)

                    if (any is Boolean) {
                        any
                    } else {
                        true
                    }
                } catch (e: Exception) {
                    log.info("加载中。。。")
                    Thread.sleep(100)
                    false
                }
            }
        } catch (e: Exception) {
            successful = false
        }
        return this
    }

    fun clickByXPath(msg: String, xpath: String): WebDriverWaitUtil {

        this.next(msg = msg) {
            it.findElementByXPath(xpath).click()
        }
        return this
    }


    fun verifyByXpath(xpathSelected: String): Boolean {
        var flag = false

        try {
            WebDriverWait(chrome, Duration.ofSeconds(3), Duration.ofMillis(200)).until {
                if (chrome.findElementByXPath(xpathSelected).isDisplayed)
                    flag = true
            }
        } catch (e: TimeoutException) {
            flag = false
        }

        return flag
    }
}