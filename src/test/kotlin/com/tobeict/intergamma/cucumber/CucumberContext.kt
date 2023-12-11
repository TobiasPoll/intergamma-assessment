package com.tobeict.intergamma.cucumber

import com.tobeict.intergamma.IntergammaApplication
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [IntergammaApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
class CucumberContext {
}