package com.yds.iocdemo.java;

import android.content.Context;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectUtils {
    public static void inject(Context context) {
        //注入布局
        injectLayout(context);
        //注入控件
        injectView(context);
        //注入点击事件
//        injectClick1(context);
        injectClick2(context);
    }

    private static void injectClick2(Context context) {
        Class<?> clazz = context.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            //不能把代码写死了 OnClick onClick = method.getAnnotation(OnClick.class);
            Annotation[] annotations  = method.getDeclaredAnnotations();
            for (Annotation annotation:annotations){
                //annotation 如果是事件就去取对应的注解
                Class<?> annotationClass = annotation.annotationType();
                EvenBase evenBase = annotationClass.getAnnotation(EvenBase.class);
                if(evenBase == null)
                    continue;
                //开始获取事件处理相关信息，拿到三要素用于确定是哪种事件
                /**
                 * btn1?.setOnClickListener(object : View.OnClickListener{
                 *             override fun onClick(v: View?) {
                 *
                 *             }
                 *
                 *         })
                 */
                String listenerSetter = evenBase.listenerSetter();
                Class<?> listernerType = evenBase.listenerType();
                String callBackMethod = evenBase.callbackMethod();
                //得到三要素执行代码
                Method valueMethod = null;
                try {
                    //反射得到id，根据id得到对应的view，
                    valueMethod = annotation.getClass().getDeclaredMethod("value");
                    int[] viewId = (int[]) valueMethod.invoke(annotation);
                    for(int id:viewId){
                        //为了得到view对象，使用findViewById
                        Method findViewById = clazz.getMethod("findViewById",int.class);
                        View view = (View) findViewById.invoke(context,id);
                        //拿到
                        if(view == null) continue;
                        //activity就是传进来的context click就是method
                        ListenerInvocationHandler listenerInvocationHandler =
                                new ListenerInvocationHandler(context,method);
                        //做代理 new View.OnClickListener()
                        Object proxy = Proxy.newProxyInstance(listernerType.getClassLoader(),
                                new Class[]{listernerType},listenerInvocationHandler);
                        //执行 proxy执行onClick()
                        Method onclick = view.getClass().getMethod(listenerSetter,listernerType);
                        onclick.invoke(view,proxy);
                        //这时候点击按钮时就会去执行代理类中的invoke方法
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private static void injectClick1(Context context) {
        //一次性处理安卓23种事件 使用注解的多态
        Class<?> clazz = context.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            try {
                if (onClick != null) {
                    int[] ids = onClick.value();
                    for (int id : ids) {
                        Method injectMethod = clazz.getMethod("findViewById", int.class);
                        View view = (View) injectMethod.invoke(context, id);
                        view.setOnClickListener(v -> {
                            try {
                                method.invoke(context);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static void injectView(Context context) {
        Class<?> clazz = context.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                int id = viewInject.value();
                //反射执行 findViewById
                try {
                    //反射去执行setContentView
                    Method method = clazz.getMethod("findViewById", int.class);
                    View view = (View) method.invoke(context, id);
                    field.setAccessible(true);
                    field.set(context, view);
                    field.setAccessible(false);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void injectLayout(Context context) {
        ContentView contentView = context.getClass().getAnnotation(ContentView.class);
        if (contentView != null) {
            //取到注解
            int layout = contentView.value();
            try {
                //反射去执行setContentView
                Method method = context.getClass().getMethod("setContentView", int.class);
                method.invoke(context, layout);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }
}
