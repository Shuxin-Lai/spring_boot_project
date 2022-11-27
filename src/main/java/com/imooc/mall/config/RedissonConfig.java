package com.imooc.mall.config;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonConfig {
  private static final Config config = new Config();

  static {
    config.useSingleServer()
        .setAddress("redis://127.0.0.1:6379")
        .setPassword("redis")
        .setConnectionMinimumIdleSize(3)
        .setTimeout(6 * 1000);
  }

  public static Config getConfig() {
    return config;
  }

  public static void main(String[] args) {
    RedissonClient client = Redisson.create(config);
    RBucket<Object> test = client.getBucket("test");
//    test.set("test1");
    Object o = test.get();
    System.out.println(o);
  }
}
