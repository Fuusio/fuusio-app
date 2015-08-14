package org.fuusio.api.util;

public final class ObjectToolkit {

    private ObjectToolkit() {
        // No instances.
    }

    public static <T> Class<T> getClass(final Object pObject) {
        return (Class<T>) pObject.getClass();
    }

    public static String getHumanReadableClassName(final Object pObject) {
        final Class<Object> objectClass = getClass(pObject);
        String className = objectClass.getSimpleName();

        if (objectClass.isMemberClass()) {
            className = objectClass.getDeclaringClass().getSimpleName() + "." + className;
        }
        return className;
    }
}
