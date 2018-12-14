package com.sc.framework.compiler;

import com.sc.framework.annotation.Action;
import com.sc.framework.compiler.base.AbsAnnotationProcessor;
import com.sc.framework.compiler.base.AbsSingleAnnotationHandler;
import com.sc.framework.compiler.base.AnnotationException;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

/**
 * @author shamschu
 * @Date 17/8/24 上午11:33
 */
public class ActionSingleHandler extends AbsSingleAnnotationHandler {

    protected ActionSingleHandler(AbsAnnotationProcessor processor, Class<? extends Annotation> annotationClass, int priority) {
        super(processor, annotationClass, priority);
    }

    @Override
    protected boolean isAvailable(Element element) throws AnnotationException {
        if (element.getKind() != ElementKind.CLASS) {
            throw new AnnotationException("@Action annotation can only be used in class!");
        }
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.ABSTRACT)) {
            throw new AnnotationException("@Action annotation can not be used in abstract class!");
        }
        if (modifiers.contains(Modifier.STATIC)) {
            throw new AnnotationException("@Action annotation can not be used in static class!");
        }
        return true;
    }

    @Override
    protected void processElement(Element element) {
        TypeElement typeElement = (TypeElement) element;
        Action action = element.getAnnotation(Action.class);
        TypeMirror classType = null;
        try {
            action.provider();
        } catch (MirroredTypeException e) {
            classType = e.getTypeMirror();
        }
        Element provider = getProcessor().getTypeUtils().asElement(classType);
        String simpleName = provider.getSimpleName().toString().toLowerCase();
        RouterProcessor processor = (RouterProcessor) getProcessor();
        MethodSpec.Builder methodBuilder = processor.getMethodBuilder();
        methodBuilder.beginControlFlow("if ($N != null)", simpleName);
        methodBuilder.addStatement("$N.addAction(new $T())", simpleName, ClassName.get(typeElement.asType()));
        methodBuilder.endControlFlow();
    }
}
