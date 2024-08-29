package com.appga.contentcraft

import com.appga.contentcraft.actions.TextChecker
import com.appga.contentcraft.actions.TextEnricher
import com.appga.contentcraft.ui.MainView
import javafx.application.Application
import javafx.stage.Stage
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent

@SpringBootApplication(exclude = [
    ReactiveWebServerFactoryAutoConfiguration::class,
    ServletWebServerFactoryAutoConfiguration::class,
])
class KContentCraftSpringApp : CommandLineRunner, ApplicationListener<ContextRefreshedEvent> {

    companion object {
        lateinit var springContext: ApplicationContext
    }

    override fun run(vararg args: String?) {
        Application.launch(KContentJavaFxApp::class.java, *args)
    }

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        springContext = event.applicationContext
    }
}

class KContentJavaFxApp : Application() {

    override fun init() {
    }

    override fun start(stage: Stage) {
        val textChecker = KContentCraftSpringApp.springContext.getBean(TextChecker::class.java)
        val textEnricher = KContentCraftSpringApp.springContext.getBean(TextEnricher::class.java)
        val mainView = MainView(textChecker = textChecker::checkText, textEnricher = textEnricher::enrichText)
        mainView.initializeMainView()
        val scene = javafx.scene.Scene(mainView.root, 800.0, 600.0)
        stage.title = "KContentCraft"
        stage.scene = scene
        stage.show()
    }

    override fun stop() {
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(KContentCraftSpringApp::class.java, *args)
}