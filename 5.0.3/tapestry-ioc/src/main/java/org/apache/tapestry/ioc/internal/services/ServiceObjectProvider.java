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

package org.apache.tapestry.ioc.internal.services;

import org.apache.tapestry.ioc.ObjectProvider;
import org.apache.tapestry.ioc.ServiceLocator;

/**
 * Simple implementation of {@link org.apache.tapestry.ioc.ObjectProvider} that is mapped to the
 * "service" provider prefix.
 */
public class ServiceObjectProvider implements ObjectProvider
{
    /**
     * Interprets the expression as a service id.
     * 
     * @see ServiceLocator#getService(String)
     */
    public <T> T provide(String expression, Class<T> objectType, ServiceLocator locator)
    {
        return locator.getService(expression, objectType);
    }

}
