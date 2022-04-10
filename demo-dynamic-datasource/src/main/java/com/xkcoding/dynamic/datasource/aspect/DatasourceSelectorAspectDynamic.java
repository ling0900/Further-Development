package com.xkcoding.dynamic.datasource.aspect;/**
 * @Author lhMeng
 * @Create 2022-04-10-11:33
 * @Describe
 */

import com.xkcoding.dynamic.datasource.annotation.DatasourceDynamic;
import com.xkcoding.dynamic.datasource.datasource.DatasourceConfigContextHolder;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 *@ClassName DatasourceSelectorAspectDynamic
 *@Description TODO
 *@Author 99261
 *@Datr 2022/4/10 11:33
 *Version 1.0
 **/
@Aspect
@Component
@Log4j2
public class DatasourceSelectorAspectDynamic {
    /**
     * @author： lhmeng
     * @date： 2022/4/10 11:38
     * @description： Pointcut 切入点
     * @modified By：
     * @version: 99261
     */
    @Pointcut("@within(com.xkcoding.dynamic.datasource.annotation.DatasourceDynamic) ||" +
        "@annotation(com.xkcoding.dynamic.datasource.annotation.DatasourceDynamic)")
    public void pointCutDatasource(){
    }

    /**
     * @author： lhmeng
     * @date： 2022/4/10 11:44
     * @description： jointput，用于将pointcut连接起来使用。ProceedingJoinPoint这个只能用around。
     * @modified By：
     * @version: 99261
     */
    @Before("pointCutDatasource()")
    public void beforeAround(JoinPoint point) throws Throwable {
        log.info("切面执行的位置");
        Class<?> targetClass = point.getTarget().getClass();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        //默认目标类型的接口实现
        log.info("targetClass.getInterfaces()" + targetClass.getInterfaces().toString());
        for (Class<?> clazz : targetClass.getInterfaces()){
            log.info("进入目标--");
            selectClazzMethod(clazz, method);
        }
        selectClazzMethod(targetClass, method);
    }

    @After("pointCutDatasource()")
    public void afterAround(JoinPoint point) {
        log.info("设置为原默认数据库");
        // DatasourceConfigContextHolder.setDefaultDatasource();
    }

    /**
     * @author： lhmeng
     * @date： 2022/4/10 16:23
     * @description： 判断类是否被注解、方法是否被注解，然后依次进行切面操作，内部会覆盖外部（方法会将类的覆盖）。
     * @modified By：
     * @version: 99261
     */
    private void selectClazzMethod(Class<?> clazz, Method method) throws NoSuchMethodException {
        Class<DatasourceDynamic> aClass = DatasourceDynamic.class;
        log.info("进来了，开始配置数据源");
        // 再配置方法，意思是每次执行前都要切面一下
        Method m = clazz.getMethod(method.getName(), method.getParameterTypes());
        // 优先配置类
        if (clazz.isAnnotationPresent(aClass)) {
            log.info("检查是否可以配置数据库");
            DatasourceDynamic targetSource = clazz.getAnnotation(aClass);
            Optional.ofNullable(targetSource).ifPresent((t)->configDataSource(t));
        }

        if (m.isAnnotationPresent(aClass)) {
            DatasourceDynamic annotation = m.getAnnotation(aClass);
            Optional.ofNullable(annotation).ifPresent((t)->configDataSource(t));
        }
    }

    /**
     * @author： lhmeng
     * @date： 2022/4/10 16:24
     * @description： 这里真正指定是哪个数据源。
     * @modified By：
     * @version: 99261
     */
    private void configDataSource(DatasourceDynamic targetSource) {
        log.info("开始配置数据库！");
        //参数传来了指定的数据源
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        String configIdInHeader = request.getHeader("Datasource-Config-Id");
        if (StringUtils.hasText(configIdInHeader)) {
            long configId = Long.parseLong(configIdInHeader);
            DatasourceConfigContextHolder.setCurrentDatasourceConfig(configId);
        } else {
            //参数未指定数据源
            log.info("配置注解数据库！");
            DatasourceConfigContextHolder.setCurrentDatasourceConfig(targetSource.configId());
        }
    }
}
