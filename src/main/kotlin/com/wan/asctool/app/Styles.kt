package com.wan.asctool.app

import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val mybutton by cssclass()
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }
        button {
            and(mybutton) {
                backgroundColor += c("#66bef1")
                textFill = c("white")
                and(hover) {
                    backgroundColor += c("#435aee")
                }

            }

        }
    }
}