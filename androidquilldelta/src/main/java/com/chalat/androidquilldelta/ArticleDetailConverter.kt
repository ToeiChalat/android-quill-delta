package com.chalat.androidquilldelta

import java.util.*
import kotlin.collections.HashMap

/**
 *
 * Created by Chalat Chansima on 3/22/18.
 *
 */
object ArticleDetailConverter {

    private const val NEW_LINE = "\n"

    fun convert(content: List<DeltaItem>)
            : List<DeltaViewItem> {
        val viewList = ArrayList<DeltaViewItem>()
        val stringBuilder = StringBuilder()
        for (item in content) {
            val attributes = item.attributes
            val text = item.insert
            processQuillData(text, attributes, viewList, stringBuilder)
        }
        return viewList
    }

    private fun processQuillData(text: String, attributes: HashMap<String, Any>?, componentList: ArrayList<DeltaViewItem>, stringBuilder: StringBuilder) {
        if (text.contains(NEW_LINE)) {
            processNewLine(attributes, componentList, stringBuilder, text)
        } else {
            processPlainText(attributes, text, stringBuilder)
        }
    }

    private fun processPlainText(attributes: HashMap<String, Any>?, text: String, stringBuilder: StringBuilder) {
        var lineText = text
        when {
            attributes?.get("bold") != null -> {
                lineText = "<b>$text</b>"
            }
            attributes?.get("underline") != null -> {
                lineText = "<u>$text</u>"
            }
            attributes?.get("italic") != null -> {
                lineText = "<i>$text</i>"
            }
            attributes?.get("link") != null -> {
                lineText = "<a href=\"${attributes["link"]}\">$text</a>"
            }
        }
        stringBuilder.append(lineText)
    }

    private fun processNewLine(attributes: HashMap<String, Any>?,
                               componentList: ArrayList<DeltaViewItem>,
                               stringBuilder: StringBuilder,
                               currentText: String) {
        val currentTextList = ArrayList(currentText.split(NEW_LINE))
        if (currentTextList.size > 2) {
            // Recursive case
            stringBuilder.append(currentTextList[0] + NEW_LINE)
            currentTextList.removeAt(0)
            addTextComponent(componentList, stringBuilder)
            processQuillData(currentTextList.joinToString(NEW_LINE), attributes, componentList, stringBuilder)
        } else {
            // Base case
            when {
                attributes?.get("header") != null -> {
                    when (attributes["header"]) {
                        1 -> addHeading1Component(componentList, stringBuilder)
                        2 -> addHeading2Component(componentList, stringBuilder)
                    }
                }
                attributes?.get("quote") != null -> {
                    addQuoteComponent(componentList, stringBuilder)
                }
                else -> {
                    val testSurroundNewLine = currentText.split(NEW_LINE)
                    val textBeforeNewLine = testSurroundNewLine[0]
                    val textAfterNewLine = testSurroundNewLine[1]
                    stringBuilder.append(textBeforeNewLine)
                    addTextComponent(componentList, stringBuilder)
                    stringBuilder.append(textAfterNewLine)
                }
            }
        }
    }

    private fun addHeading1Component(componentList: ArrayList<DeltaViewItem>,
                                     stringBuilder: StringBuilder) {
        componentList.add(DeltaViewItem(DeltaViewType.HEADER1, stringBuilder.toString()))
        stringBuilder.clear()
    }

    private fun addHeading2Component(componentList: ArrayList<DeltaViewItem>,
                                     stringBuilder: StringBuilder) {
        componentList.add(DeltaViewItem(DeltaViewType.HEADER2, stringBuilder.toString()))
        stringBuilder.clear()
    }

    private fun addQuoteComponent(componentList: ArrayList<DeltaViewItem>,
                                  stringBuilder: StringBuilder) {
        componentList.add(DeltaViewItem(DeltaViewType.QUOTE, stringBuilder.toString()))
        stringBuilder.clear()
    }

    private fun addTextComponent(componentList: ArrayList<DeltaViewItem>, stringBuilder: StringBuilder) {
        componentList.add(DeltaViewItem(DeltaViewType.TEXT, stringBuilder.toString()))
        stringBuilder.clear()
    }

}

private fun java.lang.StringBuilder.clear() {
    this.setLength(0)
}
