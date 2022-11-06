package dev.slasher.smartplugins.reflection;

import dev.slasher.smartplugins.reflection.acessors.ConstructorAccessor;
import dev.slasher.smartplugins.reflection.acessors.FieldAccessor;
import dev.slasher.smartplugins.reflection.acessors.MethodAccessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * A class created in order to access<br/>
 * methods, fields and class constructors through<br/>
 * Reflection from Java.
 */
@SuppressWarnings({"rawtypes"})
public class Accessors {

  private Accessors() {
  }

  /**
   * Method used to remove the {@link Modifier#FINAL} from the field if it has<br>
   * and set the field to accessible if it is not accessible.
   *
   * @param field The field to modify.
   */
  public static void setAccessible(Field field) {
    if (!field.isAccessible()) {
      field.setAccessible(true);
    }

    if (field.getModifiers() != (field.getModifiers() & ~Modifier.FINAL)) {
      getField(Field.class, "modifiers").set(field, field.getModifiers() & ~Modifier.FINAL);
    }
  }

  /**
   * Sets the value of a field through a {@link FieldAccessor}
   *
   * @param field The field to modify.
   * @param target The target that contains the field to modify.
   * @param value The new value of the field.
   */
  public static void setFieldValue(Field field, Object target, Object value) {
    new FieldAccessor<>(field, true).set(target, value);
  }

  /**
   * Returns the value of a field through a {@link FieldAccessor}
   *
   * @param field The field to get the value.
   * @param target The target that contains the field to catch.
   * @return The value of the field.
   */
  public static Object getFieldValue(Field field, Object target) {
    return new FieldAccessor<>(field, true).get(target);
  }

  /**
   * An untyped shortener for {@link Accessors#getField(Class, int, Class)}
   */
  public static FieldAccessor<Object> getField(Class clazz, int index) {
    return getField(clazz, index, null);
  }

  /**
   * An untyped shortener for {@link Accessors#getField(Class, String, Class)}
   */
  public static FieldAccessor<Object> getField(Class clazz, String fieldName) {
    return getField(clazz, fieldName, null);
  }

  /**
   * A correct unnamed shortener for {@link Accessors#getField(Class, String, int, Class)}
   */
  public static <T> FieldAccessor<T> getField(Class clazz, int index, Class<T> fieldType) {
    return getField(clazz, null, index, fieldType);
  }

  /**
   * A shortener with index 0 for {@link Accessors#getField(Class, String, int, Class)}
   */
  public static <T> FieldAccessor<T> getField(Class clazz, String fieldName, Class<T> fieldType) {
    return getField(clazz, fieldName, 0, fieldType);
  }

  /**
   * Method used to get a {@link Field} per index.
   *
   * @param clazz The class to fetch the fields.
   * @param fieldName The name of the field, if null it will ignore the name.
   * @param index The index to get the field, if it reaches 0 it will return the field.
   * @param fieldType The type of the field, if null it will ignore the type.
   * @return The {@link Field} represented by a {@link FieldAccessor}
   */
  public static <T> FieldAccessor<T> getField(Class clazz, String fieldName, int index, Class<T> fieldType) {
    int indexCopy = index;
    for (final Field field : clazz.getDeclaredFields()) {
      if ((fieldName == null || fieldName.equals(field.getName())) && (fieldType == null || fieldType.equals(field.getType())) && index-- == 0) {
        return new FieldAccessor<>(field, true);
      }
    }

    String message = " with index " + indexCopy;
    if (fieldName != null) {
      message += " and name " + fieldName;
    }
    if (fieldType != null) {
      message += " and type " + fieldType;
    }

    throw new IllegalArgumentException("Cannot find field " + message);
  }

  /**
   * A parameterless shortener for {@link Accessors#getMethod(Class, String, Class...)}
   */
  public static MethodAccessor getMethod(Class clazz, String methodName) {
    return getMethod(clazz, null, methodName, (Class[]) null);
  }

  /**
   * A parameterless shortener {@link Accessors#getMethod(Class, int, Class...)}
   */
  public static MethodAccessor getMethod(Class clazz, int index) {
    return getMethod(clazz, null, index, (Class[]) null);
  }

  /**
   * A shortener with no return type for {@link Accessors#getMethod(Class, Class, String, Class...)}
   */
  public static MethodAccessor getMethod(Class clazz, String methodName, Class... parameters) {
    return getMethod(clazz, null, methodName, parameters);
  }

  /**
   * A shortener with no return type for {@link Accessors#getMethod(Class, Class, int, Class...)}
   */
  public static MethodAccessor getMethod(Class clazz, int index, Class... parameters) {
    return getMethod(clazz, null, index, parameters);
  }

  /**
   * An index-less shortener for {@link Accessors#getMethod(Class, int, Class, String, Class...)}
   */
  public static MethodAccessor getMethod(Class clazz, Class returnType, String methodName, Class... parameters) {
    return getMethod(clazz, 0, returnType, methodName, parameters);
  }

  /**
   * A correct unnamed shortener for {@link Accessors#getMethod(Class, int, Class, String, Class...)}
   */
  public static MethodAccessor getMethod(Class clazz, Class returnType, int index, Class... parameters) {
    return getMethod(clazz, index, returnType, null, parameters);
  }

  /**
   * Method used to get a {@link Method} per index.
   *
   * @param clazz The class to fetch the methods.
   * @param index The index to get the Method, if it reaches 0 returns the Method.
   * @param returnType The method's return type, if null will be ignored.
   * @param methodName The name of the method, if null it will be ignored.
   * @param parameters Method parameters, if null it will be ignored.
   * @return The {@link Method} represented by a {@link MethodAccessor}
   */
  public static MethodAccessor getMethod(Class clazz, int index, Class returnType, String methodName, Class... parameters) {
    int indexCopy = index;
    for (final Method method : clazz.getMethods()) {
      if ((methodName == null || method.getName().equals(methodName)) && (returnType == null || method.getReturnType().equals(returnType)) && (parameters == null || Arrays
        .equals(method.getParameterTypes(), parameters)) && index-- == 0) {
        return new MethodAccessor(method, true);
      }
    }

    String message = " with index " + indexCopy;
    if (methodName != null) {
      message += " and name " + methodName;
    }
    if (returnType != null) {
      message += " and returntype " + returnType;
    }
    if (parameters != null && parameters.length > 0) {
      message += " and parameters " + Arrays.asList(parameters);
    }
    throw new IllegalArgumentException("Cannot find method " + message);
  }

  /**
   * A parameterless type shortener for {@link Accessors#getConstructor(Class, int, Class...)}
   */
  public static <T> ConstructorAccessor<T> getConstructor(Class<T> clazz, int index) {
    return getConstructor(clazz, index, (Class[]) null);
  }

  /**
   * A shortener with index 0 for {@link Accessors#getConstructor(Class, int, Class...)}
   */
  public static <T> ConstructorAccessor<T> getConstructor(Class<T> clazz, Class... parameters) {
    return getConstructor(clazz, 0, parameters);
  }

  /**
   * Method used to get a {@link Constructor} by an index.
   *
   * @param clazz Class to get the constructor.
   * @param index The index to get the constructor, if it reaches 0 it will return the constructor.
   * @param parameters Classes of constructor parameters, if null will ignore parameters.
   * @return The {@link Constructor} represented by a {@link ConstructorAccessor}.
   * @throws IllegalArgumentException If no constructor is found.
   */
  @SuppressWarnings("unchecked")
  public static <T> ConstructorAccessor<T> getConstructor(Class<T> clazz, int index, Class... parameters) {
    int indexCopy = index;
    for (final Constructor<?> constructor : clazz.getDeclaredConstructors()) {
      if ((parameters == null || Arrays.equals(constructor.getParameterTypes(), parameters)) && index-- == 0) {
        return new ConstructorAccessor<>((Constructor<T>) constructor, true);
      }
    }

    throw new IllegalArgumentException("Cannot find constructor for class " + clazz + " with index " + indexCopy);
  }
}
