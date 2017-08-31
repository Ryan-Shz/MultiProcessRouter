package com.sc.framework.compiler;

import com.squareup.javapoet.ClassName;

/**
 * @author ShamsChu
 * @Date 17/8/26 下午8:59
 */
class InterfaceHelper {

    static final String PACKAGE_NAME = "com.sc.framework.router";
    static final String CLASS_NAME = "RouterRegister";
    static final ClassName INTERFACE_ROUTER_REGISTER = ClassName.get("com.sc.framework.router", "IRouterRegister");
    static final ClassName CLASS_ROUTER_MANAGER = ClassName.get("com.sc.framework.router", "RouterManager");

}
