package dev.slasher.smartplugins.reflection.acessors;

import java.lang.reflect.Method;

/**
 * This class represents a {@link Method} with safe access methods.
 */
public class MethodAccessor {

  private Method handle;

  public MethodAccessor(Method method) {
    this(method, false);
  }

  public MethodAccessor(Method method, boolean forceAccess) {
    this.handle = method;
    if (forceAccess) {
      method.setAccessible(true);
    }
  }

  /**
   * Method used to invoke a {@link Method}
   *
   * @param target The instance to use the method.
   * @param args The parameters for invoking the method.
   * @return Result of the method.
   */
  public Object invoke(Object target, Object... args) {
    try {
      return handle.invoke(target, args);
    } catch (ReflectiveOperationException ex) {
      throw new RuntimeException("Cannot invoke method.", ex);
    }
  }

  /**
   * Method used to check if the Object class has the {@link Method}.
   *
   * @param target The target to check.
   * @return TRUE if you have the method in the class, FALSE otherwise.
   */
  public boolean hasMethod(Object target) {
    return target != null && this.handle.getDeclaringClass().equals(target.getClass());
  }

  /**
   * @return The {@link Method} represented in this Accessor.
   */
  public Method getHandle() {
    return handle;
  }

  @Override
  public String toString() {
    return "MethodAccessor[class=" + this.handle.getDeclaringClass().getName() + ", name=" + this.handle.getName() + ", params=" + this.handle.getParameterTypes().toString() + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }

    if (obj instanceof MethodAccessor) {
      MethodAccessor other = (MethodAccessor) obj;
      if (other.handle.equals(handle)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public int hashCode() {
    return this.handle.hashCode();
  }
}
