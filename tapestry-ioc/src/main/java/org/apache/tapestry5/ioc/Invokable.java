// Copyright 2006, 2009, 2011 The Apache Software Foundation
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

package org.apache.tapestry5.ioc;


/**
 * Similiar to {@link Runnable} execept that it returns a value. Used to represent any operation which can return a
 * value. This is often used in situations where the code is to be executed in another threads.
 *
 * @param <T> the return value type
 * @see org.apache.tapestry5.ioc.OperationTracker#invoke(String, Invokable)
 * @see org.apache.tapestry5.ioc.services.ParallelExecutor#invoke(Invokable)
 */
public interface Invokable<T>
{
    /**
     * Called to produce a value.
     */
    T invoke();
}
