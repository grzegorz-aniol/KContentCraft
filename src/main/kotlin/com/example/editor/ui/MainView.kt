package com.example.editor.ui

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

open class MainView(
    private val textChecker: (String) -> String,
    private val textEnricher: (String) -> String,
) {

    lateinit var root: BorderPane

    fun initializeMainView(): BorderPane {
        root = BorderPane()
        val textArea = TextArea()
        val checkButton = Button("Check")
        val enrichButton = Button("Enrich")

        // Set up the text area
        textArea.font = Font.font("Segoe UI Emoji", FontWeight.BOLD, 16.0)
        textArea.isWrapText = true
        root.center = textArea

        // Set up the bottom panel with buttons
        val bottomPanel = HBox(10.0)
        bottomPanel.padding = Insets(10.0)
        bottomPanel.alignment = Pos.CENTER

        // Set button sizes
        checkButton.prefWidth = 100.0
        checkButton.prefHeight = 40.0
        enrichButton.prefWidth = 100.0
        enrichButton.prefHeight = 40.0

        bottomPanel.children.addAll(checkButton, enrichButton)
        root.bottom = bottomPanel

        // Define actions for the buttons
        checkButton.setOnAction {
            val text = textArea.text
            val modifiedText = textChecker.invoke(text)
            textArea.text = modifiedText
        }

        enrichButton.setOnAction {
            val text = textArea.text
            val modifiedText = textEnricher.invoke(text)
            textArea.text = modifiedText
        }

        return root
    }

}