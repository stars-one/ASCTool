package com.wan.asctool.view

import com.jfoenix.controls.JFXButton
import com.wan.asctool.app.Styles
import com.wan.asctool.controller.MainController
import com.wan.asctool.util.chooseApkFile
import com.wan.asctool.util.validateApkFile
import javafx.geometry.Pos
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.image.Image
import kfoenix.jfxbutton
import kfoenix.jfxradiobutton
import tornadofx.*
import java.io.*

class MainView : View() {

    val maincontroller: MainController by inject()
    var srcApkFile: TextField by singleAssign()
    var outApkFile: TextField by singleAssign()

    var setSignButton: JFXButton by singleAssign()
    var settingSign: Field by singleAssign()

    var textarea by singleAssign<TextArea>()

    var option = 0


    init {
        title = "APK签名验证破解工具 by star-sone"
        setStageIcon(Image("img/icon.png"))
    }

    override val root = vbox {
        setPrefSize(500.0, 400.0)
        menubar {
            menu("帮助") {
                item("关于") {
                    action {
                        find(AboutView::class).openModal()
//                        openInternalWindow(AboutView::class)
                    }
                }
            }
        }
        form {
            fieldset {
                field("需要处理的apk") {
                    srcApkFile = textfield {

                    }

                    jfxbutton {
                        graphic = imageview("img/file.png") {
                            fitHeight = 30.0
                            fitWidth = 30.0
                        }
                        action {
                            srcApkFile.text = chooseApkFile(srcApkFile)
                        }
                    }

                }

                field("输出apk名字") {
                    outApkFile = textfield {
                        promptText = "输出路径与输入路径相同，默认为out.apk"
                    }
                }

                field("自动签名设置") {
                    togglegroup {
                        jfxradiobutton("使用默认签名") {
                            isSelected = true
                            setOnAction {
                                settingSign.isDisable = true
                                option = 0
                            }
                        }
                        jfxradiobutton("使用自定义签名") {
                            setOnAction {
                                settingSign.isDisable = false
                                option = 1
                            }
                        }
                        jfxradiobutton("不自动签名") {
                            setOnAction {
                                settingSign.isDisable = true
                                option = 2
                            }
                        }
                    }
                }
                settingSign = field("设置签名文件") {
                    isDisable = true
                    setSignButton = jfxbutton("设置签名文件") {
                        addClass(Styles.mybutton)
                        action {
                            openInternalWindow(SignMessageInputView::class)
                        }

                    }
                }
                field {
                    hbox {
                        alignment = Pos.CENTER
                        jfxbutton("破解签名验证") {
                            addClass(Styles.mybutton)
                            setOnAction {
                                textarea.clear()

                                /* val stream = ByteArrayOutputStream()
                                 System.setOut(PrintStream(stream))
                                 var text: String
                                 runAsync {
                                     for (i in 1..10) {
                                         Thread.sleep(200)
                                         println("$i")
                                         text = String(stream.toByteArray())
                                         stream.reset()
                                         if (text.isNotEmpty()) {
                                             textarea.appendText(text)
                                         }
                                     }
                                 }*/

                                //验证通过则进行解密
                                if (validateApkFile(srcApkFile, srcApkFile.textProperty())) {
                                    val srcPath = srcApkFile.text
                                    val parentPath = File(srcPath).parent
                                    var outPath = File(parentPath, "out.apk").path
                                    val outPathText = outApkFile.text
                                    if (!outPathText.isNullOrBlank()) {
                                        outPath = if (outPathText.endsWith(".apk")) {
                                            File(parentPath, outPathText).path
                                        } else {
                                            File(parentPath, "$outPathText.apk").path
                                        }
                                    }
                                    val stream = ByteArrayOutputStream()
                                    System.setOut(PrintStream(stream))

//                                    val queue = ArrayBlockingQueue<String>(10)
                                    var lines: List<String>
                                    runAsync {
                                        //进行解密
                                        textarea.appendText("正在处理，请稍候...\n")
                                        maincontroller.startTask(srcPath, outPath, option)
                                        val bf = BufferedReader(InputStreamReader(ByteArrayInputStream(stream.toByteArray())))
                                        lines = bf.readLines()

                                        for (line in lines) {
                                            textarea.appendText(line + "\n")
                                            Thread.sleep(200)
                                        }
                                    } ui {

                                        textarea.appendText("签名破解已成功！")
                                    }

                                }
                            }
                        }
                    }
                }
                field {
                    textarea = textarea {
                        isEditable = false
                        isWrapText = true
                    }
                }

            }
        }

    }

}