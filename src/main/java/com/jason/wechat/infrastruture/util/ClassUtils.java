package com.jason.wechat.infrastruture.util;


import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import com.jason.wechat.domain.message.Message;

/**
 * Class 帮助类
 * @author Jason
 * @data 2014-3-30 下午10:17:12
 */
public class ClassUtils {

    /**
     * 将map集合转换成Message对象
     * @param messageMap  消息map集合
     * @param messageClazz 消息类型
     * @param <T>
     * @return
     */
    public static <T extends Message> T map2Message(Map<String,Object> messageMap,Class<T>  messageClazz){

        T message;
        Iterator<String> it =  messageMap.keySet().iterator();
        //给Message赋值
        try {
            message = messageClazz.newInstance();
            while (it.hasNext()){
                String key  = it.next();
                Field field = getDeclaredField(message,key);

                if(field==null){
                    continue;
                }

                Object value = messageMap.get(key);
                if(field.getType()==Long.class){
                    value = new Long(value+"");
                }

                ClassUtils.parSetVal(key,message,value);
            }
        }catch (Exception e){
            throw new RuntimeException("请求对象给Message赋值出错!",e);
        }
        return message;
    }

    /**
     * 迭代获取该类或者夫类的属性字段
     * @param object
     * @param fieldName
     * @return
     */
    private static Field getDeclaredField(Object object, String fieldName){
        Field field = null ;
        Class<?> clazz = object.getClass() ;

        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName) ;
                return field ;
            } catch (Exception e) {
                //这里什么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return null;
    }

    /**
     * 通过set方法给对象赋值
     * @param fieldName 需要赋值的字段名称
     * @param obj  需要赋值的对象
     * @param value 值
     * @return
     * @throws Exception
     */
    public static void parSetVal(String fieldName,Object obj,Object value)throws Exception{
        PropertyDescriptor pd = new PropertyDescriptor(fieldName,obj.getClass());
        Method method = pd.getWriteMethod();
        method.invoke(obj,value);
    }

    /**
     * 通过get方法获取属性值
     * @param fieldName 需要赋值的字段名称
     * @param obj  需要赋值的对象
     * @return
     * @throws Exception
     */
    public static Object parGetVal(String fieldName,Object obj) throws Exception{
        Object fieldVal = null;
        PropertyDescriptor pd = new PropertyDescriptor(fieldName,obj.getClass());
        Method method = pd.getReadMethod();
        fieldVal = method.invoke(obj);
        return  fieldVal;
    }
}
