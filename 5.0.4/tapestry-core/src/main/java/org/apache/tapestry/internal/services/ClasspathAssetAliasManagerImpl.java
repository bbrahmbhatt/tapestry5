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

package org.apache.tapestry.internal.services;

import static org.apache.tapestry.ioc.internal.util.CollectionFactory.newList;
import static org.apache.tapestry.ioc.internal.util.CollectionFactory.newMap;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.TapestryConstants;
import org.apache.tapestry.ioc.annotations.InjectService;
import org.apache.tapestry.services.ClasspathAssetAliasManager;

public class ClasspathAssetAliasManagerImpl implements ClasspathAssetAliasManager
{
    private final ContextPathSource _contextPathSource;

    /** Map from alias to path. */
    private final Map<String, String> _aliasToPathPrefix;

    /** Map from path to alias. */
    private final Map<String, String> _pathPrefixToAlias = newMap();

    private final List<String> _sortedAliases;

    private final List<String> _sortedPathPrefixes;

    /**
     * Configuration is a map of aliases (short names) to complete names. Keys and values should not
     * start with a slash, but should end with one. Example: "tapestry/" --> "org/apache/tapestry/".
     */
    public ClasspathAssetAliasManagerImpl(@InjectService("ContextPathSource")
    ContextPathSource contextPathSource,

    final Map<String, String> configuration)
    {
        _contextPathSource = contextPathSource;

        _aliasToPathPrefix = configuration;

        for (Map.Entry<String, String> e : _aliasToPathPrefix.entrySet())
        {
            _pathPrefixToAlias.put(e.getValue(), e.getKey());
        }

        Comparator<String> sortDescendingByLength = new Comparator<String>()
        {
            public int compare(String o1, String o2)
            {
                return o2.length() - o1.length();
            }
        };

        _sortedAliases = newList(_aliasToPathPrefix.keySet());
        Collections.sort(_sortedAliases, sortDescendingByLength);

        _sortedPathPrefixes = newList(_aliasToPathPrefix.values());
        Collections.sort(_sortedPathPrefixes, sortDescendingByLength);
    }

    public String toClientURL(String resourcePath)
    {
        StringBuilder builder = new StringBuilder(_contextPathSource.getContextPath());
        builder.append(TapestryConstants.ASSET_PATH_PREFIX);

        for (String pathPrefix : _sortedPathPrefixes)
        {
            if (resourcePath.startsWith(pathPrefix))
            {
                String alias = _pathPrefixToAlias.get(pathPrefix);
                builder.append(alias);
                builder.append(resourcePath.substring(pathPrefix.length()));

                return builder.toString();
            }
        }

        // No alias available as a prefix (kind of unlikely, but whatever).

        builder.append(resourcePath);

        return builder.toString();
    }

    public String toResourcePath(String clientURL)
    {
        String basePath = clientURL.substring(TapestryConstants.ASSET_PATH_PREFIX.length());

        for (String alias : _sortedAliases)
        {
            if (basePath.startsWith(alias)) { return _aliasToPathPrefix.get(alias)
                    + basePath.substring(alias.length()); }
        }

        return basePath;
    }

}