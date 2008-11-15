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

package org.apache.tapestry.internal.test;

import static org.apache.tapestry.ioc.internal.util.CollectionFactory.newMap;

import java.util.Map;

import org.apache.tapestry.Link;
import org.apache.tapestry.dom.Element;
import org.apache.tapestry.internal.services.ComponentInvocation;
import org.apache.tapestry.internal.services.ComponentInvocationMap;
import org.apache.tapestry.internal.services.NoOpComponentInvocationMap;
import org.apache.tapestry.test.PageTester;

/**
 * This is the real implementation, used by {@link PageTester}. The typical implementation,
 * {@link NoOpComponentInvocationMap}, is used in production as a place holder.
 */
public class PageTesterComponentInvocationMap implements ComponentInvocationMap
{
    private final Map<Element, Link> _elementToLink = newMap();

    private final Map<Link, ComponentInvocation> _linkToInvocation = newMap();

    public void store(Element element, Link link)
    {
        _elementToLink.put(element, link);
    }

    public void store(Link link, ComponentInvocation invocation)
    {
        _linkToInvocation.put(link, invocation);
    }

    public void clear()
    {
        _elementToLink.clear();
        _linkToInvocation.clear();
    }

    public ComponentInvocation get(Element element)
    {
        Link link = _elementToLink.get(element);

        return get(link);
    }

    public ComponentInvocation get(Link link)
    {
        return _linkToInvocation.get(link);
    }

}