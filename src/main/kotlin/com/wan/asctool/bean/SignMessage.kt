package com.wan.asctool.bean

/**
 *存放签名文件和签名信息
 *  sign.file=test.keystore
 *  sign.password=123456
 *  sign.alias=user
 *  sign.aliasPassword=654321
 * @author StarsOne
 * @date Create in  2019/9/17 0017 10:56
 * @description
 *
 */

data class SignMessage(var keyFilePath: String,var password: String,var alias: String,var aliasPassword: String) {
    fun toMap() : Map<String,String> {
        val map = hashMapOf<String, String>()
        map["keyFilePath"] = keyFilePath
        map["password"] = password
        map["alias"] = alias
        map["aliasPassword"] = aliasPassword
        return map
    }
}