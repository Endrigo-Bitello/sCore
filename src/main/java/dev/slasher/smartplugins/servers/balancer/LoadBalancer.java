package dev.slasher.smartplugins.servers.balancer;

import dev.slasher.smartplugins.servers.balancer.elements.LoadBalancerObject;

public interface LoadBalancer<T extends LoadBalancerObject> {

  public T next();
}
