// Copyright 2007, 2008 The Apache Software Foundation
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

package org.apache.tapestry.corelib.components;

import org.apache.tapestry.*;
import org.apache.tapestry.annotations.Environmental;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.internal.TapestryInternalUtils;
import org.apache.tapestry.ioc.annotations.Inject;
import org.apache.tapestry.services.*;

public class RadioGroup implements Field
{
    /**
     * The property read and updated by the group as a whole.
     */
    @Parameter(required = true, principal = true)
    private Object _value;

    /**
     * If true, then the field will render out with a disabled attribute (to turn off client-side behavior). Further, a
     * disabled field ignores any value in the request when the form is submitted.
     */
    @Parameter("false")
    private boolean _disabled;

    /**
     * Allows a specific implementation of {@link ValueEncoder} to be supplied. This is used to create client-side
     * string values for the different radio button values.
     *
     * @see ValueEncoderSource
     */
    @Parameter(required = true)
    private ValueEncoder _encoder;

    @Inject
    private ComponentDefaultProvider _defaultProvider;

    @Inject
    private ComponentResources _resources;

    @Environmental
    private FormSupport _formSupport;

    @Inject
    private Environment _environment;

    @Inject
    private ValueEncoderSource _valueEncoderSource;

    @Inject
    private Request _request;

    @Environmental
    private ValidationTracker _tracker;

    private String _elementName;

    final Binding defaultValue()
    {
        return _defaultProvider.defaultBinding("value", _resources);
    }

    final ValueEncoder defaultEncoder()
    {
        return _valueEncoderSource.createEncoder("value", _resources);
    }

    private static class Setup implements ComponentAction<RadioGroup>
    {
        private static final long serialVersionUID = -7984673040135949374L;

        private final String _elementName;

        Setup(String elementName)
        {
            _elementName = elementName;
        }

        public void execute(RadioGroup component)
        {
            component.setup(_elementName);
        }
    }

    private static final ComponentAction<RadioGroup> PROCESS_SUBMISSION = new ComponentAction<RadioGroup>()
    {
        private static final long serialVersionUID = -3857110108918776386L;

        public void execute(RadioGroup component)
        {
            component.processSubmission();
        }
    };

    private void setup(String elementName)
    {
        _elementName = elementName;
    }

    private void processSubmission()
    {
        String clientValue = _request.getParameter(_elementName);

        _tracker.recordInput(this, clientValue);

        _value = _encoder.toValue(clientValue);
    }

    /**
     * Obtains the element name for the group, and stores a {@link RadioContainer} into the {@link Environment} (so that
     * the {@link Radio} components can find it).
     */
    final void setupRender()
    {
        String name = _formSupport.allocateElementName(_resources.getId());

        ComponentAction<RadioGroup> action = new Setup(name);

        _formSupport.storeAndExecute(this, action);

        String submittedValue = _tracker.getInput(this);

        final String selectedValue = submittedValue != null ? submittedValue : _encoder.toClient(_value);


        _environment.push(RadioContainer.class, new RadioContainer()
        {
            public String getElementName()
            {
                return _elementName;
            }

            public boolean isDisabled()
            {
                return _disabled;
            }

            @SuppressWarnings("unchecked")
            public String toClient(Object value)
            {
                // TODO: Ensure that value is of the expected type?

                return _encoder.toClient(value);
            }

            public boolean isSelected(Object value)
            {
                return TapestryInternalUtils.isEqual(_encoder.toClient(value), selectedValue);
            }

        });

        _formSupport.store(this, PROCESS_SUBMISSION);
    }

    /**
     * Pops the {@link RadioContainer}.
     */
    final void afterRender()
    {
        _environment.pop(RadioContainer.class);
    }

    public String getElementName()
    {
        return _elementName;
    }

    /**
     * Always returns null; individual {@link org.apache.tapestry.corelib.components.Radio} components may have their
     * own label.
     */
    public String getLabel()
    {
        return null;
    }

    public boolean isDisabled()
    {
        return _disabled;
    }

    /**
     * Returns null; the radio group does not render as a tag and so doesn't have an id to share.  RadioGroup implements
     * {@link org.apache.tapestry.Field} only so it can interact with the {@link org.apache.tapestry.ValidationTracker}.
     *
     * @return null
     */
    public String getClientId()
    {
        return null;
    }
}