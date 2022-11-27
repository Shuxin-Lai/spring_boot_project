package com.imooc.mall.service.impl;

import com.imooc.mall.config.RedissonConfig;
import com.imooc.mall.service.RedissonService;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedissonServiceImpl implements RedissonService {
  private final RedissonClient client = Redisson.create(RedissonConfig.getConfig());

  @Override
  public Object get(String key) {
    RBucket<Object> bucket = client.getBucket(key);

    boolean exists = bucket.isExists();
    if (exists) {
      return bucket.get();
    }

    return null;
  }

  @Override
  public boolean set(String key, Object value, Long timeout, TimeUnit unit) {
    RBucket<Object> bucket = client.getBucket(key);
    boolean exists = bucket.isExists();
    if (exists) {
      return false;
    }


    if (timeout != null && unit != null) {
      bucket.set(value, timeout, unit);
    } else {
      bucket.set(value);
    }
    return true;
  }

  public boolean set(String key, Object value) {
    return set(key, value, null, null);
  }
}
