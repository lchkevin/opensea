package com.opensea.autoTest.service

import com.opensea.autoTest.util.ActiveConfig
import com.opensea.autoTest.util.Config
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
open class ServiceGateway(
//    private val activeConfig: ActiveConfig,
    private val config: Config
) {

    companion object {
        val log: Logger = LoggerFactory.getLogger(ServiceGateway::class.java)
    }


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

        options.addArguments("user-agent=${config.useragent}")
        options.addArguments("--disable-gpu")
        options.addArguments("--no-sandbox")
        options.addArguments("--disable-dev-shm-usage")

        val chrome = ChromeDriver(options)
        this.executeCdp(chrome = chrome)

        return chrome
    }
}