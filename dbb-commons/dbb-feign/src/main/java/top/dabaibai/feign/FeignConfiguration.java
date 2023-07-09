package top.dabaibai.feign;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Primary;
import top.dabaibai.feign.intercepter.FeignRequestInterceptor;
import top.dabaibai.feign.intercepter.FeignResponseInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * @description: feign客户端配置
 * @author: 白剑民
 * @dateTime: 2022-10-24 11:59:28
 */
@Configuration
public class FeignConfiguration {

    /**
     * @description: 设置默认的全局feign日志级别为NONE
     * @author: 白剑民
     * @date: 2023-06-09 20:54:32
     * @return: feign.Logger.Level
     * @version: 1.0
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        // 设置feign的日志不输出任何信息
        // 若需要输出某个服务的日志，请指定@FeignClient的configuration属性为包含了Logger.Level的Spring Bean
        return Logger.Level.NONE;
    }

    /**
     * @description: 使用fastjson处理feign的编码
     * @author: 白剑民
     * @date: 2023-06-09 20:40:22
     * @return: feign.codec.Encoder
     * @version: 1.0
     */
    @Bean
    public Encoder feignEncoder() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.ALL));
        FastJsonConfig config = converter.getFastJsonConfig();
        // 输出结果不带双引号
        config.setSerializerFeatures(SerializerFeature.QuoteFieldNames);
        // 设置HttpMessageConverters为false不再初始化加载默认的转换器以提升转码链速度
        // 如有额外转码需求，请在@FeignClient中指定configuration属性并自定义编解码器
        HttpMessageConverters converters =
                new HttpMessageConverters(false, Collections.singleton(converter));
        return new SpringEncoder(() -> converters);
    }

    /**
     * @description: 使用fastjson处理feign的解码
     * @author: 白剑民
     * @date: 2023-06-09 20:40:22
     * @return: feign.codec.Encoder
     * @version: 1.0
     */
    @Bean
    public Decoder feignDecoder(ObjectProvider<HttpMessageConverterCustomizer> customizers) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.ALL));
        FastJsonConfig config = converter.getFastJsonConfig();
        // 允许不带引号的字段名
        config.setFeatures(Feature.AllowUnQuotedFieldNames);
        // 设置HttpMessageConverters为false不再初始化加载默认的转换器以提升转码链速度
        // 如有额外转码需求，请在@FeignClient中指定configuration属性并自定义编解码器
        HttpMessageConverters converters =
                new HttpMessageConverters(false, Collections.singleton(converter));
        return new ResponseEntityDecoder(new SpringDecoder(() -> converters, customizers));
    }

    /**
     * @description: 默认的全局feign请求拦截器，传递用户信息
     * @author: 白剑民
     * @date: 2023-06-05 18:52:49
     * @return: feign.RequestInterceptor
     * @version: 1.0
     */
    @Bean
    public RequestInterceptor getFeignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }

    /**
     * @param interceptor 全局feign响应拦截器
     * @description: 注册全局feign响应拦截器
     * @author: 白剑民
     * @date: 2023-06-09 21:07:46
     * @return: okhttp3.OkHttpClient.Builder
     * @version: 1.0
     */
    @Bean
    @Primary
    @ConditionalOnBean(FeignResponseInterceptor.class)
    public OkHttpClient.Builder customOkHttpClientBuilder(FeignResponseInterceptor interceptor,
                                                          OkHttpClient.Builder builder) {
        return builder.addInterceptor(new OkHttpClientResponseInterceptor(interceptor));
    }

    /**
     * @description: okHttp响应拦截器
     * @author: 白剑民
     * @dateTime: 2022/10/12 15:59
     */
    private static class OkHttpClientResponseInterceptor implements Interceptor {

        private final FeignResponseInterceptor responseInterceptor;

        private OkHttpClientResponseInterceptor(FeignResponseInterceptor responseInterceptor) {
            this.responseInterceptor = responseInterceptor;
        }

        @NonNull
        @Override
        public Response intercept(Chain chain) throws IOException {
            okhttp3.Request request = chain.request();
            Response response = chain.proceed(request);
            // 调用拦截方法
            this.responseInterceptor.handle(request, response);
            //生成新的response返回，网络请求的response取出之后，直接返回将会抛出异常
            assert response.body() != null;
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        }
    }
}
