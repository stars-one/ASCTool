package com.wan.asctool.controller

import bin.signer.ApkSigner
import bin.signer.key.KeystoreKey
import cc.binmt.signature.ASCTool
import com.android.apksigner.ApkSignerTool
import com.wan.asctool.util.getCurrentJarPath
import com.wan.asctool.util.isWin
import tornadofx.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
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
     * 重新签名
     */
    fun reSignApk(srcApkPath: String, outApkPath: String, option: Int, signVersion: Int = 1) :String{
        //使用自定义签名文件设置签名
        val apkFile = File(srcApkPath)
        val outputApkFile = File(outApkPath)

        //获取签名文件及相关密码信息,使用strList存放
        val strList = arrayListOf<String>()

        if (option == 1) {
            //保存自动签名的选择option
            val lists = arrayListOf("my-keyFilePath", "my-password", "my-alias", "my-aliasPassword")
            lists.forEach {
                strList.add(config.string(it, ""))
            }

        } else {
            //使用默认测试签名文件进行签名
            val resourceAsStream = javaClass.classLoader.getResourceAsStream("config.properties")
            val properties = Properties()
            properties.load(resourceAsStream)
            val lists = arrayListOf("keyFilePath", "password", "alias", "aliasPassword")
            val map = properties.toMutableMap()

            lists.forEach {
                strList.add(map[it].toString())
            }
        }

        val signFilePath = strList[0]
        val signPwd = strList[1]
        val signAlias = strList[2]
        val signAliasPwd = strList[3]

        val keystoreKey = KeystoreKey(signFilePath, signPwd, signAlias, signAliasPwd)

        ApkSigner.signApk(apkFile, outputApkFile, keystoreKey, null)

        if (signVersion == 1) {
            //v2签名
            return v2SignApk(outApkPath, strList)
        }
        return ""

    }

    /**
     * v2签名
     */
    fun v2SignApk(srcApkPath: String, strList: List<String>):String {
        val srcFile = File(srcApkPath)
        val tempFile = File(srcFile.parentFile, srcFile.nameWithoutExtension + "_temp" + ".apk")
        val outputApk = File(srcFile.parentFile, srcFile.nameWithoutExtension + "_v2" + ".apk")


        //todo 需要调整
        val zalipPath = getZalipPath()

        //zip对齐
        //val zalipPath = "D:\\app\\dev\\Android\\SDK\\build-tools\\28.0.3\\zipalign.exe"
        val progress = Runtime.getRuntime().exec("$zalipPath -p -f -v 4 $srcApkPath ${tempFile.path}")

        val br = BufferedReader(InputStreamReader(progress.inputStream))
        //StringBuffer b = new StringBuffer();
        var line: String? = null
        val b = StringBuffer()
        while (br.readLine().also { line = it } != null) {
            b.appendln(line)
        }

        val signFilePath = strList[0]
        val signPwd = strList[1]
        val signAlias = strList[2]
        val signAliasPwd = strList[3]

        val str = """sign --ks $signFilePath --ks-pass pass:${signPwd} --ks-key-alias ${signAlias} --key-pass pass:${signAliasPwd} --out ${outputApk.path} ${tempFile.path}
        """.trimMargin()
        b.appendln()
        b.appendln(str)

        val paramArray = str.split(" ").toTypedArray()
        ApkSignerTool.main(paramArray)

        //校验下签名
        ApkSignerTool.main(arrayOf("verify", "-v", outputApk.path))

        //删除对齐的那个apk文件
        System.gc()
        tempFile.delete()
        srcFile.delete()

        return b.toString()
    }

    private fun getZalipPath():String {
        val zipalignPath = if (isWin()) "/util/zipalign.exe" else "/util/zipalign"
        val fileName = if (isWin()) "zipalign.exe" else "zipalign"

        val fileUrl = resources.url("/img/file.png")

        val result = if (fileUrl.path.contains("!/")) {
            //是jar包打开
            val currentJarPath = getCurrentJarPath(fileUrl)
            val file = File(currentJarPath.parent, fileName)
            if (!file.exists()) {
                file.writeBytes(resources.stream(zipalignPath).readBytes())
            }
            file.path
        } else {
            val filePath = resources.url(zipalignPath).file
            val file = File(filePath)
            file.path
        }
        println("ZalipPath : $result")
        return result
    }

    /**
     * 开始签名破解
     */
    fun startTask(srcApkPath: String, outApkPath: String, option: Int, signVersion: Int = 1) {
        val resourceAsStream = javaClass.classLoader.getResourceAsStream("config.properties")

        val properties = Properties()
        properties.load(resourceAsStream)

        properties.setProperty("option", option.toString())//保存自动签名的选择option

        val lists = arrayListOf("my-keyFilePath", "my-password", "my-alias", "my-aliasPassword")

        val map = properties.toMutableMap()
        //转为Map<String,String>类型 固定顺序
        val infoMap = linkedMapOf<String, String>()

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
                infoMap.apply {
                    val entries = map.entries
                    for (entry in entries) {
                        this["${entry.key}"] = "${entry.value}"
                    }
                }
                //签名验证破解
                val keyFileInputSteam = resources.stream(infoMap["keyFilePath"]?: "")
                ASCTool(srcApkPath, outApkPath, keyFileInputSteam, infoMap).startTask()
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


        if (option == 0 || option == 1) {
            //是否v2签名
            if (signVersion == 2) {
                v2SignApk(srcApkPath,infoMap.values.toList())
            }
        }

    }
}

