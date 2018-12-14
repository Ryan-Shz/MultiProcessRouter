package com.sc.framework.compiler;

import com.google.auto.service.AutoService;
import com.sc.framework.annotation.Action;
import com.sc.framework.annotation.Provider;
import com.sc.framework.compiler.base.AbsAnnotationProcessor;
import com.sc.framework.compiler.base.AbsSingleAnnotationHandler;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Processor;
import javax.lang.model.element.Modifier;

/**
 * @author shamschu
 * @Date 17/8/23 下午6:10
 */
@AutoService(Processor.class)
public class RouterProcessor extends AbsAnnotationProcessor {

    private MethodSpec.Builder mMethodBuilder;

    @Override
    protected List<AbsSingleAnnotationHandler> getHandlers() {
        List<AbsSingleAnnotationHandler> handlers = new ArrayList<>();
        handlers.add(new ProviderSingleHandler(this, Provider.class, 1));
        handlers.add(new ActionSingleHandler(this, Action.class, 0));
        return handlers;
    }

    @Override
    protected void onProcessStart() {
        mMethodBuilder = MethodSpec.methodBuilder("register")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(String.class, "process")
            .addAnnotation(Override.class)
            .returns(TypeName.VOID);
    }

    @Override
    protected void onProcessEnd() throws IOException {
        MethodSpec methodSpec = mMethodBuilder.build();
        TypeSpec typeSpec = TypeSpec.classBuilder(InterfaceHelper.CLASS_NAME)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(InterfaceHelper.INTERFACE_ROUTER_REGISTER)
            .addMethod(methodSpec)
            .build();
        JavaFile javaFile = JavaFile.builder(InterfaceHelper.PACKAGE_NAME, typeSpec).build();
        javaFile.writeTo(getFiler());
    }

    MethodSpec.Builder getMethodBuilder() {
        return mMethodBuilder;
    }
}
