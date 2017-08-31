package com.sc.framework.compiler.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * @author ShamsChu
 * @Date 17/8/24 下午2:30
 */
public abstract class AbsAnnotationProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Messager mMessager;
    private Map<String, AbsSingleAnnotationHandler> mHandlers;
    private Elements mElementUtils;
    private Types mTypeUtils;
    private boolean mProcessEnd = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        mTypeUtils = processingEnvironment.getTypeUtils();
        initMaps();
    }

    // 初始化单个注解处理器映射关系
    private void initMaps() {
        mHandlers = new HashMap<>();
        for (AbsSingleAnnotationHandler handler : getHandlers()) {
            String annotationClassName = handler.getAnnotationClass().getCanonicalName();
            mHandlers.put(annotationClassName, handler);
        }
    }

    @Override
    public final boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (mProcessEnd) {
            return true;
        }
        try {
            onProcessStart();
            annotationProcess(set, roundEnvironment);
            onProcessEnd();
            mProcessEnd = true;
            return true;
        } catch (Exception e) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return false;
    }

    protected void annotationProcess(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) throws AnnotationException {
        if (set.isEmpty()) {
            return;
        }
        List<AbsSingleAnnotationHandler> tasks = new ArrayList<>();
        for (TypeElement element : set) {
            String aClass = element.toString();
            if (mHandlers.containsKey(aClass)) {
                tasks.add(mHandlers.get(aClass));
            }
        }
        if (tasks.isEmpty()) {
            return;
        }
        Collections.sort(tasks);
        for (AbsSingleAnnotationHandler handler : tasks) {
            handler.process(roundEnvironment);
        }
    }

    // 返回支持注解的类
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        for (AbsSingleAnnotationHandler handler : getHandlers()) {
            set.add(handler.getAnnotationClass().getCanonicalName());
        }
        return set;
    }

    public Filer getFiler() {
        return mFiler;
    }

    public Elements getElementUtils() {
        return mElementUtils;
    }

    public Types getTypeUtils() {
        return mTypeUtils;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    // 获取单个注解处理器列表
    protected abstract List<AbsSingleAnnotationHandler> getHandlers();

    // 开始处理注解
    protected void onProcessStart() {

    }

    // 处理注解结束
    protected void onProcessEnd() throws IOException {

    }

    protected void error(String errMsg) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, errMsg);
    }

}
