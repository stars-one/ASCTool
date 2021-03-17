package com.wan.asctool.util

import javafx.beans.property.StringProperty
import javafx.scene.control.Control
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

/**
 *
 * @author StarsOne
 * @date Create in  2019/9/20 0020 18:47
 * @description
 *
 */
/**
 * 验证apk输入是否正确
 */
fun validateApkFile(control: Control, property: StringProperty): Boolean {
    val validationContext = ValidationContext()
    validationContext.addValidator(control, property) {

        when {
            it.isNullOrBlank() -> error("此项是不能为空！！")
            File(it).extension == "apk" -> null
            else -> error("输入错误")
        }
    }
    return validationContext.validate()
}

/**
 * 验证签名文件是否正确
 */
fun validateSignFile(control: Control, property: StringProperty): Boolean {
    val validationContext = ValidationContext()
    validationContext.addValidator(control, property) {

        when {
            it.isNullOrBlank() -> error("此项是不能为空！！")
            File(it).extension == "keystore" || File(it).extension == "jks" -> null
            else -> error("请输入正确的文件路径")
        }
    }

    return validationContext.validate()
}

/**
 * 选择apk文件
 */
fun chooseApkFile(control: Control): String {
    val fileChooser = FileChooser()
    fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("选择文件", "*.apk"))
    fileChooser.title ="选择签名文件"
    return fileChooser.showOpenDialog(control.scene.window)?.path ?: ""
}

/**
 * 选择签名文件
 */
fun chooseSignFile(control: Control): String {
    val fileChooser = FileChooser()
    fileChooser.title ="选择签名文件"
    fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("选择文件", listOf("*.jks","*.keystore")))
    return fileChooser.showOpenDialog(control.scene.window)?.path ?: ""
}

