package com.sahriar.springPagination.Config;



import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionConfig {



    @Bean
    public Config hazelConfig(){
        return new Config()
                .addMapConfig(
                        new MapConfig()
                        .setName("hazel-cache")
                        .setEvictionPolicy(EvictionPolicy.LRU)
                        .setTimeToLiveSeconds(2400))
                .setProperty("hazelcast.logging.type","slf4j");
    }




//    @Bean
//    public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer() {
//        return new CacheManagerCustomizer<ConcurrentMapCacheManager>() {
//            @Override
//            public void customize(ConcurrentMapCacheManager cacheManager) {
//                cacheManager.setAllowNullValues(false);
//            }
//        };
//    }

//    @Bean
//    public Config hazelCastConfig(){
//
//
//        return new Config();


//        return new Config().addMapConfig(
//                new MapConfig()
//                        .setName("accepted-messages")
//                        .setEvictionPolicy(EvictionPolicy.LRU)
//                        .setTimeToLiveSeconds(2400))
//                .setProperty("hazelcast.logging.type","slf4j");


//        return new Config()
//                .setInstanceName("hazelcast-instance")
//                .addMapConfig(
//                        new MapConfig()
//                                .setName("map")
//                             //   .setName("users")
//                                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
//                                .setEvictionPolicy(EvictionPolicy.LRU)
//                                .setTimeToLiveSeconds(20));

  //  }


//    @Bean
//    public HazelcastInstance hazelcastInstance() {
//        MapAttributeConfig attributeConfig = new MapAttributeConfig()
//                .setName(HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
//                .setExtractor(PrincipalNameExtractor.class.getName());
//
//        Config config = new Config();
//
//        config.getMapConfig(HazelcastSessionRepository.DEFAULT_SESSION_MAP_NAME)
//                .addMapAttributeConfig(attributeConfig)
//                .addMapIndexConfig(new MapIndexConfig(
//                        HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE, false));
//
//        return Hazelcast.newHazelcastInstance(config);
//    }


//    @Bean
//    public HazelcastInstance hazelcastInstance() {
//        Config config = new Config();
//        config.setProperty("hazelcast.jmx", "true");
//        return Hazelcast.newHazelcastInstance(config);
//    }
//
//    @Bean
//    public CacheManager cacheManager() {
//        // The Stormpath SDK knows to use the Spring CacheManager automatically
//        return new HazelcastCacheManager(hazelcastInstance());
//    }

}
