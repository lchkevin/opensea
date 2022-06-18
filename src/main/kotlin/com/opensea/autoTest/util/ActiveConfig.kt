package com.opensea.autoTest.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component


@Configuration
@Component
open class ActiveConfig {

    @Value("\${spring.profiles.active}")
    open lateinit var profile: String
}
