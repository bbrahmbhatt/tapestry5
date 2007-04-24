// Copyright 2006, 2007 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.ioc.services;

import java.lang.reflect.Method;

/**
 * Service used when dynamically creating new classes.
 */
public interface ClassFactory
{
    /**
     * Simplified version of {@link #newClass(String, Class)} that generates a name based on the
     * service interface name, extends from java.lang.Object, and automatically adds the
     * serviceInterface to the returned ClassFab. This is the most common use when creating the
     * kinds of proxies used throughout Tapestry IoC.
     * 
     * @param serviceInterface
     */
    ClassFab newClass(Class serviceInterface);

    /**
     * Creates a {@link ClassFab} object for the given name; the new class is a subclass of the
     * indicated class. The new class is always public and concrete.
     * 
     * @param name
     *            the full qualified name of the class to create (note that it is common to place
     *            created classes in the default package)
     * @param superClass
     *            the parent class, which is often java.lang.Object
     */

    ClassFab newClass(String name, Class superClass);

    /**
     * Returns the number of classes (and interfaces) actually created.
     */

    int getCreatedClassCount();

    /**
     * Returns the class loader used when creating new classes; this is generally the same as the
     * current thread's context class loader (except perhaps during testing).
     */
    ClassLoader getClassLoader();

    /**
     * Converts a method to a {@link MethodLocation}, which includes information about the source
     * file name and line number.
     * 
     * @param method
     *            to look up
     * @return the location, or null if the necessary information is not available
     */
    MethodLocation getMethodLocation(Method method);
}