package com.imooc.mall.service;

import java.util.concurrent.TimeUnit;

public interface RedissonService {

  Object get(String key);

  boolean set(String key, Object value, Long timeout, TimeUnit unit);
}
