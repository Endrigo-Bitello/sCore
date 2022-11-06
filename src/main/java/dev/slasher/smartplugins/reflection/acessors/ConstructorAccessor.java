package dev.slasher.smartplugins.reflection.acessors;

import java.lang.reflect.Constructor;

/**
 * This class represents a {@link Constructor} with safe access methods.
 */
public class ConstructorAccessor<T> {

  private Constructor<T> handle;

  public ConstructorAccessor(Constructor<T> constructor) {
    this(constructor, false);
  }

  public ConstructorAccessor(Constructor<T> constructor, boolean forceAccess) {
    this.handle = constructor;
    if (forceAccess) {
      constructor.setAccessible(true);
    }
  }

  /**
   * Method used to create an instance of a {@link Constructor}
   *
   * @param args The parameters to create the instance.
   * @return The created instance.
   */
  public T newInstance(Object... args) {
    try {
      return this.handle.newInstance(args);
    } catch (ReflectiveOperationException ex) {
      throw new RuntimeException("Cannot invoke constructor.", ex);
    }
  }

  /**
   * Method used to check if the Object class has the {@link Constructor}.
   *
   * @param target The target to check.
   * @return TRUE if you have the constructor in the class, FALSE otherwise.
   */
  public boolean hasConstructor(Object target) {
    return target != null && this.handle.getDeclaringClass().equals(target.getClass());
  }

  /**
   * @return The {@link Constructor} represented in this Accessor.
   */
  public Constructor<T> getHandle() {
    return handle;
  }

  @Override
  public String toString() {
    return "ConstructorAccessor[class=" + this.handle.getDeclaringClass().getName() + ", params=" + this.handle.getParameterTypes().toString() + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }

    if (obj instanceof ConstructorAccessor) {
      ConstructorAccessor<?> other = (ConstructorAccessor<?>) obj;
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
