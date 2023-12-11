package com.tobeict.intergamma

import org.springframework.boot.fromApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.with

@TestConfiguration(proxyBeanMethods = false)
class TestIntergammaApplication

fun main(args: Array<String>) {
	fromApplication<IntergammaApplication>().with(TestIntergammaApplication::class).run(*args)
}
