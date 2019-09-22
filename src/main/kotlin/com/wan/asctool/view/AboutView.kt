package com.wan.asctool.view

import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.*
import java.awt.Desktop
import java.net.URI

/**
 *
 * @author StarsOne
 * @date Create in  2019/9/20 0020 14:07
 * @description
 *
 */
class AboutView : View("关于") {


    override val root = vbox {
        setPrefSize(500.0, 600.0)
        form {
            fieldset {
                alignment = Pos.CENTER
                vbox(spacing = 5.0) {
                    hbox {
                        alignment = Pos.CENTER
                        imageview("img/icon.png") {
                            alignment = Pos.CENTER
                            fitWidth = 50.0
                            fitHeight = 50.0
                        }
                    }
                    hbox {
                        alignment = Pos.CENTER
                        label("APK签名验证破解工具")
                    }

                }

                vbox {
                    padding = tornadofx.insets(left = 50,top = 15)
                    label("本工具只对那些仅通过 PackageManager.getPackageInfo().signatures来校验签名的应用有效"){
                        prefHeight = 50.0
                        isWrapText = true
                        style {
                            fontWeight = FontWeight.BOLD
                            fontSize = Dimension(16.0, Dimension.LinearUnits.px)
                        }
                    }
                }
                vbox{
                    padding = tornadofx.insets(left = 50.0)

                    field("软件作者") {
                        label("stars-one")
                    }
                    field("项目地址") {
                        hyperlink("https://github.com/Stars-One/ASCTool") {
                            action {
                                Desktop.getDesktop().browse(URI(this.text))
                            }
                        }
                    }
                    field("我的博客") {
                        hyperlink("https://www.cnblogs.com/kexing") {
                            action {
                                Desktop.getDesktop().browse(URI(this.text))
                            }
                        }
                    }
                    field("联系QQ") {
                        hyperlink("1053894518") {

                        }
                    }
                }

                hbox {
                    padding = tornadofx.insets(top = 10)
                    alignment = Pos.CENTER
                    label("对你有帮助的话，不妨打赏一波！") {
                        style {
                            fontWeight = FontWeight.BOLD
                            fontSize = Dimension(20.0, Dimension.LinearUnits.px)
                        }
                    }
                }

                hbox {
                    alignment = Pos.CENTER
                    spacing = 20.0
                    vbox {
                        alignment = Pos.CENTER
                        label("微信")
                        imageview("img/weixin.jpg") {
                            alignment = Pos.CENTER
                            fitWidth = 180.0
                            fitHeight = 180.0
                        }
                    }
                    vbox {
                        alignment = Pos.CENTER
                        label("支付宝")
                        imageview("img/zhifubao.jpg") {
                            alignment = Pos.CENTER
                            fitWidth = 180.0
                            fitHeight = 180.0
                        }
                    }

                }
            }
        }

    }


}