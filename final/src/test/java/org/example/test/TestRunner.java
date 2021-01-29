package org.example.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestRunner {
    public static void main(String[] args) throws Exception {
        Class<?> testClass = Class.forName("org.example.app.IntCalculatorTest");
        Object testInstance = testClass.getDeclaredConstructor().newInstance();

        runTest(testInstance);
    }

    private static void runTest(Object testInstance) throws Exception {
        final String testClassName = testInstance.getClass().getSimpleName();
        System.out.println("RUNNING - " + testClassName);

        for (Method declaredMethod : testInstance.getClass().getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(Test.class)) {
                runTestMethod(testInstance, testClassName, declaredMethod);
            }
        }
    }

    private static void runTestMethod(Object testInstance, String testClassName, Method declaredMethod) throws Exception {
        String testMethodName = declaredMethod.getName();
        try {
            if (!declaredMethod.canAccess(testInstance)) {
                declaredMethod.setAccessible(true);
            }
            declaredMethod.invoke(testInstance);
            System.out.println(String.format("PASSED - %s#%s", testClassName, testMethodName));
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof RuntimeException) {
                System.out.println(String.format("FAILED - %s#%s", testClassName, testMethodName));
            } else {
                throw e;
            }
        }
    }
}