package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * 基于 Token 的身份验证方法

 使用基于 Token 的身份验证方法，在服务端不需要存储用户的登录记录。大概的流程是这样的：

 客户端使用用户名跟密码请求登录
 服务端收到请求，去验证用户名与密码
 验证成功后，服务端会签发一个 Token，再把这个 Token 发送给客户端
 客户端收到 Token 以后可以把它存储起来，比如放在 Cookie 里或者 Local Storage 里
 客户端每次向服务端请求资源的时候需要带着服务端签发的 Token
 服务端收到请求，然后去验证客户端请求里面带着的 Token，如果验证成功，就向客户端返回请求的数据
 */
public class TokenCache {
    //声明日志
   private static org.slf4j.Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";

    //LRU算法 缓冲区 设置最大空间 有效期为12小时
    private static LoadingCache<String,String>localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现，当调用get取值的时候，如果key没有对应的值则调用这个方法进行加载。
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key,String value){
        localCache.put(key, value);
    }

     //缓冲区有值则返回value，无则返回null
    public static String getKey(String key){
        String value = null;
        try{
            value = localCache.get(key);
            if("null".equals(value)){
                  return null;
            }
            return value;
        }catch (Exception e){
                logger.error("localCache get error",e);
        }
        return null;
    }
}
