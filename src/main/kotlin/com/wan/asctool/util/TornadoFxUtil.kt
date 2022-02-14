package com.wan.asctool.util

import java.io.File
import java.net.URI
import java.net.URL

/**
 * @author starsone
 * @date 2022/02/14 15:35
 */

/**
 * 获取当前jar包的文件路径
 *
 * @param url 在View中使用resources.url("")获取的参数
 * @return
 */
fun getCurrentJarPath(url: URL): File {
    val filePath = url.path.substringBeforeLast("!/")
    return File(URI.create(filePath))
}

/**
 * 当前系统是否为window系统
 */
fun isWin(): Boolean {
    val prop = System.getProperties()

    val os = prop.getProperty("os.name")
    return os.contains("win", true)
}
