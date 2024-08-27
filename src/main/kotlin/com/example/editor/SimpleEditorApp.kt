package com.example.editor

import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.stage.Stage

class SimpleEditorApp : Application() {

    override fun start(stage: Stage) {
        val mainView = MainView()
        val scene = Scene(mainView.root, 800.0, 600.0)
        stage.title = "Enhanced Editor"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(SimpleEditorApp::class.java)
}