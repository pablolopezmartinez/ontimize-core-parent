package com.ontimize.util;

import java.util.ArrayList;
import java.util.List;

public final class ReflectionUtils {

    /**
     * Check all interfaces of <code>theClass</code> that implements <code>theInterface</code>.
     * @param theClass the the class
     * @param theInterface the the interface
     * @return the interfaces extending
     */
    public static Class<?>[] getInterfacesExtending(Class<?> theClass, Class<?> theInterface) {
        if (theClass == null) {
            return null;
        }
        List<Class<?>> interfaces = ReflectionUtils.getAllInterfaces(theClass);
        if (theInterface == null) {
            return interfaces.toArray(new Class<?>[0]);
        }
        List<Class<?>> res = new ArrayList<Class<?>>();
        for (Class<?> interfaceToCheck : interfaces) {
            if (theInterface.isAssignableFrom(interfaceToCheck)) {
                res.add(interfaceToCheck);
            }
        }
        return res.toArray(new Class<?>[0]);
    }

    /**
     * <p>
     * Gets a <code>List</code> of all interfaces implemented by the given class and its superclasses.
     * </p>
     *
     * <p>
     * The order is determined by looking through each interface in turn as declared in the source file
     * and following its hierarchy up. Then each superclass is considered in the same way. Later
     * duplicates are ignored, so the order is maintained.
     * </p>
     * @param cls the class to look up, may be <code>null</code>
     * @return the <code>List</code> of interfaces in order, <code>null</code> if null input
     */
    public static List<Class<?>> getAllInterfaces(Class<?> cls) {
        if (cls == null) {
            return null;
        }

        List<Class<?>> interfacesFound = new ArrayList<Class<?>>();
        ReflectionUtils.getAllInterfaces(cls, interfacesFound);

        return interfacesFound;
    }

    /**
     * Get the interfaces for the specified class.
     * @param cls the class to look up, may be <code>null</code>
     * @param interfacesFound the <code>Set</code> of interfaces for the class
     * @return the all interfaces
     */
    private static void getAllInterfaces(Class<?> cls, List<Class<?>> interfacesFound) {
        while (cls != null) {
            Class<?>[] interfaces = cls.getInterfaces();

            for (int i = 0; i < interfaces.length; i++) {
                if (!interfacesFound.contains(interfaces[i])) {
                    interfacesFound.add(interfaces[i]);
                    ReflectionUtils.getAllInterfaces(interfaces[i], interfacesFound);
                }
            }

            cls = cls.getSuperclass();
        }
    }

}
