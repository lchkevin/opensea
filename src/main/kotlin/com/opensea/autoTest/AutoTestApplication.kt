package com.opensea.autoTest

import com.opensea.autoTest.service.ItemService
import com.opensea.autoTest.util.Config
import com.opensea.autoTest.util.DataUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*

@SpringBootApplication
class AutoTestApplication: ApplicationRunner {
	@Autowired
	lateinit var config: Config

	override fun run(args: ApplicationArguments?) {

		// init db
		DataUtil.initSQL(url = config.sqlitePath)
		// init chrome driver
		System.setProperty("webdriver.chrome.driver", config.chromeDriverPath)
		System.setProperty("webdriver.gecko.driver", config.firefoxDriverPath)
	}
}

fun main(args: Array<String>) {
	runApplication<AutoTestApplication>(*args)
	TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"))
}
