package com.ryan.router.compiler;

import com.squareup.javapoet.ClassName;

/**
 * @author Ryan
 * @Date 17/8/26 下午8:59
 */
class InterfaceHelper {

    static final String PACKAGE_NAME = "com.ryan.router";
    static final String CLASS_NAME = "RouteRegister";
    static final ClassName INTERFACE_ROUTER_REGISTER = ClassName.get(PACKAGE_NAME, "IRouterRegister");
    static final ClassName CLASS_ROUTER_MANAGER = ClassName.get(PACKAGE_NAME, "RouteManager");

}
