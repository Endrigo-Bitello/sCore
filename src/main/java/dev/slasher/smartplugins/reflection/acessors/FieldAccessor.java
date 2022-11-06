package dev.slasher.smartplugins.reflection.acessors;

import java.lang.reflect.Field;

import dev.slasher.smartplugins.reflection.Accessors;

/**
 * This class represents a {@link Field} with safe access methods.
 */
@SuppressWarnings("unchecked")
public class FieldAccessor<TField> {

  private Field handle;

  public FieldAccessor(Field field) {
    this(field, false);
  }

  public FieldAccessor(Field field, boolean forceAccess) {
    this.handle = field;
    if (forceAccess) {
      Accessors.setAccessible(field);
    }
  }

  /**
   * Method used to get the value of a {@link Field}
   *
   * @param target The target to get the value of the field.
   * @return The value of the field.
   */
  public TField get(Object target) {
    try {
      return (TField) handle.get(target);
    } catch (ReflectiveOperationException ex) {
      throw new RuntimeException("Cannot access field.", ex);
    }
  }

  /**
   * Method used to set the value of a {@link Field}
   *
   * @param target The target to set the value of the field.
   * @param value The new value of the field.
   */
  public void set(Object target, TField value) {
    try {
      handle.set(target, value);
    } catch (ReflectiveOperationException ex) {
      throw new RuntimeException("Cannot access field.", ex);
    }
  }

  /**
   * Method used to check if the Object class has the {@link Field}.
   *
   * @param target The target to check.
   * @return TRUE if you have the field in the class, FALSE if not.
   */
  public boolean hasField(Object target) {
    return target != null && this.handle.getDeclaringClass().equals(target.getClass());
  }

  /**
   * @return The {@link Field} represented in this Accessor.
   */
  public Field getHandle() {
    return handle;
  }

  @Override
  public String toString() {
    return "FieldAccessor[class=" + this.handle.getDeclaringClass().getName() + ", name=" + this.handle.getName() + ", type=" + this.handle.getType() + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }

    if (obj instanceof FieldAccessor) {
      FieldAccessor<?> other = (FieldAccessor<?>) obj;
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
