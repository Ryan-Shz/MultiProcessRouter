package com.ryan.router.compiler.base;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;

/**
 * @author Ryan
 * @Date 17/8/24 上午9:50
 */
public abstract class AbsSingleAnnotationHandler implements Comparable<AbsSingleAnnotationHandler> {

    private Class<? extends Annotation> mAnnotationClass;
    private AbsAnnotationProcessor mProcessor;
    private int mPriority;

    protected AbsSingleAnnotationHandler(AbsAnnotationProcessor processor, Class<? extends Annotation> annotationClass, int priority) {
        mAnnotationClass = annotationClass;
        mProcessor = processor;
        mPriority = priority;
    }

    final void process(RoundEnvironment roundEnvironment) throws AnnotationException {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(mAnnotationClass);
        if (elements == null || elements.isEmpty()) {
            return;
        }
        for (Element element : elements) {
            if (isAvailable(element)) {
                processElement(element);
            }
        }
    }

    // 验证是否可用
    protected abstract boolean isAvailable(Element element) throws AnnotationException;

    // 处理单个注解元素
    protected abstract void processElement(Element element);

    protected AbsAnnotationProcessor getProcessor() {
        return mProcessor;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return mAnnotationClass;
    }

    public int getPriority() {
        return mPriority;
    }

    @Override
    public int compareTo(AbsSingleAnnotationHandler handler) {
        int priority = handler.getPriority();
        if (mPriority < priority) {
            return 1;
        } else if (mPriority > priority) {
            return -1;
        }
        return 0;
    }
}
