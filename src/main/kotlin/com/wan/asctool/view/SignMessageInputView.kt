package com.wan.asctool.view

import com.wan.asctool.app.Styles
import com.wan.asctool.util.chooseSignFile
import com.wan.asctool.util.validateSignFile
import javafx.geometry.Pos
import javafx.scene.control.TextField
import kfoenix.jfxbutton
import tornadofx.*
import java.io.File
import java.io.FileWriter
import java.util.*

/**
 *
 * @author StarsOne
 * @date Create in  2019/9/20 0020 14:07
 * @description
 *
 */
class SignMessageInputView : View("输入自定义签名文件信息") {
    var keyFilePathTf by singleAssign<TextField>()
    var passwordTf by singleAssign<TextField>()
    var aliasTf by singleAssign<TextField>()
    var aliasPasswordTf by singleAssign<TextField>()



    override fun onDock() {
        loadProperties()
    }

    override val root = vbox {
        form {
            fieldset {
                field("签名文件路径") {
                    keyFilePathTf = textfield()
                    jfxbutton {
                        graphic = imageview("img/file.png") {
                            fitHeight = 30.0
                            fitWidth = 30.0
                        }
                        action {
                            keyFilePathTf.text = chooseSignFile(keyFilePathTf)
                        }
                    }
                }
                field("密码") {
                    passwordTf = textfield()
                }
                field("别名(alias)") {
                    aliasTf = textfield()
                }
                field("别名密码") {
                    aliasPasswordTf = textfield()
                }
            }
        }
        hbox {
            alignment = Pos.CENTER
            jfxbutton("保存") {
                addClass(Styles.mybutton)
                action {
                    if (validateSignFile(keyFilePathTf, keyFilePathTf.textProperty())) {
                        writeProperties()
                        //关闭对话框
                        close()
                    }
                }
            }
        }
    }


    fun loadProperties() {
        val pro = Properties()
        pro.load(resources.stream("/config.properties"))
        val lists = listOf("my-keyFilePath", "my-password", "my-alias", "my-aliasPassword")
        val controlList = listOf(keyFilePathTf, passwordTf, aliasTf, aliasPasswordTf)
        val map = pro.toMap()
        if (map.containsKey(lists[0])) {
            for (i in 0..3) {
                controlList[i].text = map[lists[i]].toString()
            }
        }

    }

    fun writeProperties() {
        val pro = Properties()
        val stream = resources.stream("/config.properties")
        pro.load(stream)
        val controlList = listOf(keyFilePathTf, passwordTf, aliasTf, aliasPasswordTf)
        val lists = listOf("my-keyFilePath", "my-password", "my-alias", "my-aliasPassword")

        val hashMap = hashMapOf<String, String>()
        hashMap.apply {
            for (i in 0..3) {
                this[lists[i]] = controlList[i].text
            }
        }
        pro.plusAssign(hashMap)
        //把属性写出到文件
        val writer = FileWriter(File(resources.url("/config.properties").toURI()))
        pro.store(writer,"增加的属性")
    }
}