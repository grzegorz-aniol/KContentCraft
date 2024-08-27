package com.example.editor

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

class MainView {

    val root: BorderPane = BorderPane()
    private val textArea: TextArea = TextArea()
    private val checkButton: Button = Button("Check")
    private val enrichButton: Button = Button("Enrich")
    private val textChecker: TextChecker = TextChecker()

    init {
        // Set up the text area
        textArea.font = Font.font("Arial", FontWeight.BOLD, 16.0)
        textArea.isWrapText = true
        root.center = textArea

        // Set up the bottom panel with buttons
        val bottomPanel = HBox(10.0)
        bottomPanel.padding = Insets(10.0)
        bottomPanel.alignment = Pos.CENTER
        bottomPanel.children.addAll(checkButton, enrichButton)
        root.bottom = bottomPanel

        // Define actions for the buttons
        checkButton.setOnAction {
            val text = textArea.text
            textChecker.checkText(text) { result ->
                textArea.text = result.joinToString(separator = "\n\n")
            }
        }

        enrichButton.setOnAction {
            val text = textArea.text
            textArea.appendText("\n\n[Enriched Text]")
        }
    }
}