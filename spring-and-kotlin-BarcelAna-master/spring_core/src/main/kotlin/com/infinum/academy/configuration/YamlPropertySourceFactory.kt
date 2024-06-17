package com.infinum.academy.configuration

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory
import java.io.IOException
import java.util.*

class YamlPropertySourceFactory : PropertySourceFactory {
    @Throws(IOException::class)
    override fun createPropertySource(name: String?, resource: EncodedResource): PropertySource<*> {
        val factory = YamlPropertiesFactoryBean()
        factory.setResources(resource.resource)
        val properties: Properties? = factory.getObject()
        return PropertiesPropertySource(resource.resource.filename!!, properties!!)
    }
}
