/**
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.colorcc.ddrpc.service.proxy;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class MethodCache {
    private static final ConcurrentHashMap<String, Method> methods = new ConcurrentHashMap<String, Method>();

    public static void registerMethod(Class<?> iface, Object impl) {
        for (Method m : impl.getClass().getMethods()) {
            m.setAccessible(true);
            methods.putIfAbsent(iface.getName() + "." + m.getName(), m);
        }
    }

    public static Method getMethod(String serviceName, String methodName) {
        return methods.get("" + serviceName + "." + methodName);
    }

    public static Method getMethod(Class<?> iface, String methodName) {
        return methods.get("" + iface.getName() + "." + methodName);
    }
}
