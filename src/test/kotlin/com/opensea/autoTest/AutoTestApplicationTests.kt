package com.opensea.autoTest


import com.opensea.autoTest.service.ItemService
import com.opensea.autoTest.util.Config
import com.opensea.autoTest.util.DataUtil
import com.sun.org.apache.bcel.internal.generic.NEW
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AutoTestApplicationTests {


	@Test
	fun contextLoads() {
		val dir = System.getProperty("user.dir")
		DataUtil.initSQL(url = "D:\\workspace\\mysqlite\\mysqlite.sqlite")
		System.setProperty("webdriver.chrome.driver", "${dir}/chromedriver.exe")
		val itemService: ItemService = ItemService()
		itemService.getItem()
		itemService.clickItem()
	}

}
