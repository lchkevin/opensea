package com.opensea.autoTest.util

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "auto.config")
@Component
class Config {

    lateinit var useragent: String

    lateinit var chromeDriverPath: String

    lateinit var firefoxDriverPath: String

    lateinit var sqlitePath: String

}
