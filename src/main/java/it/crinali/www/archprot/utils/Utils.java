package it.crinali.www.archprot.utils;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

  /**
   * Split a CamelCase string into a separator separated string. Original version written by PŒl
   * Brattberg.
   *
   * @param inputString The string you wish to split.
   * @return A string where each upper case letter is separated by a single space
   */
  public static String unCamelize(final String inputString,final String separator) {
    Pattern p = Pattern.compile("\\p{Lu}");
    Matcher m = p.matcher(inputString);
    StringBuffer sb = new StringBuffer();
    while (m.find()) {
      m.appendReplacement(sb, separator + m.group());
    }
    m.appendTail(sb);
    return sb.toString().trim();
  }

  public static String getPropName(Method getMethod) {
    String propName;
    final String methodName = getMethod.getName();
    if (methodName.startsWith("get")) {
      propName = methodName.substring(3);
    } else if (methodName.startsWith("is")) {
      propName = methodName.substring(2);
    } else {
      throw new RuntimeException("Non-property sent to function");
    }
    return propName;
  }

  static String niceName(String targetClassName,String separator) {
    final String s = unCamelize(targetClassName,separator);
    return s.substring(0, 1).toUpperCase() + s.substring(1);
  }

   static Method setMethodFromGetMethod(Method getMethod) {
    Method calcSetMethod2;
    try {
      calcSetMethod2 = getMethod.getDeclaringClass()
         .getMethod("set" + Utils.getPropName(getMethod), getMethod.getReturnType());
      calcSetMethod2.setAccessible(true); // FORCE accessible
    } catch (NoSuchMethodException e) {
      calcSetMethod2 = null;
    }
    return calcSetMethod2;
  }

  static public String getNiceName(Class targetContainedType,String separator) {
    return unCamelize(targetContainedType.getName().substring(
          targetContainedType.getName().lastIndexOf('.') + 1),separator);
  }
}