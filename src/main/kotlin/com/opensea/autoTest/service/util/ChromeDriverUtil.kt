package com.opensea.autoTest.service.util

import org.openqa.selenium.chrome.ChromeDriver


object ChromeDriverUtil {


    val cache = hashMapOf<String, ChromeDriver>()

    fun getChrome(orderId: String): ChromeDriver {
        val chrome = cache[orderId] ?: ChromeDriver()
        cache[orderId] = chrome
        return chrome
    }

    fun quit(orderId: String) {
        cache[orderId]?.quit()
    }

}