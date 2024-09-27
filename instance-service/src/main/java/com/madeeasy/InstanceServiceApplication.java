package com.madeeasy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableCaching
//@EnableDiscoveryClient
@SpringBootApplication
public class InstanceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InstanceServiceApplication.class, args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
//                .build();
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
//
//        // Use String serialization for keys
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//
//        // Use JSON serialization for values
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//        return redisTemplate;
//    }
//
//    @Bean
//    public RedisCacheConfiguration cacheConfiguration() {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10))  // Default TTL of 10 minutes
//                .disableCachingNullValues()         // Avoid caching null values
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//    }
}