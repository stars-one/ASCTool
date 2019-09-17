package com.wan.asctool.view

import javafx.scene.control.TextField
import tornadofx.*

class MainView : View("apk签名破解工具") {
    var srcApkFile: TextField by singleAssign()
    var outApkFile: TextField by singleAssign()
    override val root = vbox {
        setPrefSize(500.0,400.0)
        form{
            fieldset {

                field ("需要处理的apk"){
                    hbox {
                        srcApkFile = textfield {  }
                        button("选择文件") {
                            setOnAction {

//                                println(File("/").path)

                            }
                        }
                    }
                }
                field("输出apk"){
                    hbox {

                    outApkFile = textfield {  }
                    }
                }


            }
        }

    }
}