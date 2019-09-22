package com.wan.asctool.controller

import cc.binmt.signature.ASCTool
import tornadofx.*
import java.io.File
import java.util.*

/**
 *
 * @author StarsOne
 * @date Create in  2019/9/18 0018 11:33
 * @description
 *
 */
class MainController : Controller() {


    fun test() {
        val file = File(javaClass.classLoader.getResource("").toURI())
        println(file.path)
    }
    /**
     * 开始签名破解
     */
    fun startTask(srcApkPath: String, outApkPath: String, option: Int) {
        val resourceAsStream = javaClass.classLoader.getResourceAsStream("config.properties")

        val properties = Properties()
        properties.load(resourceAsStream)

        properties.setProperty("option", option.toString())//保存自动签名的选择option

        val lists = arrayListOf("my-keyFilePath", "my-password", "my-alias", "my-aliasPassword")

        val map = properties.toMutableMap()
        when (option) {
        //使用默认签名
            0 -> {
                //删除map中不需要的配置数据
                map.remove("option")
                if (map.containsKey("my-keyFilePath")) {
                    for (key in lists) {
                        map.remove(key)
                    }
                }
                //转为Map<String,String>类型
                val infoMap = hashMapOf<String, String>()
                infoMap.apply {
                    val entries = map.entries
                    for (entry in entries) {
                        this["${entry.key}"] = "${entry.value}"
                    }
                }
                //签名验证破解
                val keyFileInputSteam = javaClass.classLoader.getResourceAsStream(infoMap["keyFilePath"])
                ASCTool(srcApkPath, outApkPath,keyFileInputSteam ,infoMap).startTask()
            }
        //使用自定义签名文件进行签名
            1 -> {
                val infoMap = hashMapOf<String, String>()
                infoMap.apply {
                    for (key in lists) {
                        this[key.substringAfter("my-")] = "${map[key]}"
                    }
                }
                ASCTool(srcApkPath, outApkPath, infoMap).startTask()
            }
        //不自动签名
            2 -> {
                ASCTool(srcApkPath, outApkPath).startTask()
            }
        }

    }
}

