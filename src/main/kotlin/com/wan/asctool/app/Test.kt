package com.wan.asctool.app

import com.wan.asctool.bean.SignMessage
import java.util.*

/**
 *
 * @author StarsOne
 * @date Create in  2019/9/17 0017 12:08
 * @description
 *
 */
fun main(args: Array<String>) {

    val toMap = SignMessage("Q:\\AndroidProject\\Mykey.jks", "13556710asd", "key0", "13556710asd").toMap()
    val properties = Properties()
//    properties.load(FileInputStream(File("config.properties")))
//    println(properties.size)
//    ASCTool("Q:\\Android破解\\复仇_1.5.0.apk","Q:\\Android破解\\out1.apk",toMap).startTask()
}