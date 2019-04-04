package com.ryan.router.compiler;

import com.ryan.router.annotation.Provider;
import com.ryan.router.compiler.base.AbsAnnotationProcessor;
import com.ryan.router.compiler.base.AbsSingleAnnotationHandler;
import com.ryan.router.compiler.base.AnnotationException;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @author Ryan
 * @Date 17/8/24 上午11:33
 */
public class ProviderSingleHandler extends AbsSingleAnnotationHandler {

    protected ProviderSingleHandler(AbsAnnotationProcessor processor, Class<? extends Annotation> annotationClass, int priority) {
        super(processor, annotationClass, priority);
    }

    @Override
    protected boolean isAvailable(Element element) throws AnnotationException {
        if (element.getKind() != ElementKind.CLASS) {
            throw new AnnotationException("@Provider annotation can only be used in class!");
        }
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.ABSTRACT)) {
            throw new AnnotationException("@Provider annotation can not be used in abstract class!");
        }
        if (modifiers.contains(Modifier.STATIC)) {
            throw new AnnotationException("@Provider annotation can not be used in static class!");
        }
        return true;
    }

    @Override
    protected void processElement(Element element) {
        TypeElement typeElement = (TypeElement) element;
        Provider provider = typeElement.getAnnotation(Provider.class);
        String providerProcess = provider.process();
        String simpleName = typeElement.getSimpleName().toString().toLowerCase();
        RouterProcessor processor = (RouterProcessor) getProcessor();
        MethodSpec.Builder methodBuilder = processor.getMethodBuilder();
        methodBuilder.addStatement("$T $N = null", ClassName.get(typeElement.asType()), simpleName);
        methodBuilder.beginControlFlow("if(process.equals($S))", providerProcess);
        methodBuilder.addStatement("$N = new $T()", simpleName, ClassName.get(typeElement.asType()));
        methodBuilder.addStatement("$T.getInstance().registerProvider($N)", InterfaceHelper.CLASS_ROUTER_MANAGER, simpleName);
        methodBuilder.endControlFlow();
    }

}
