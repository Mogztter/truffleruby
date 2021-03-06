/*
 * Copyright (c) 2015, 2017 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 */
package org.truffleruby.core.array;

import org.truffleruby.core.basicobject.BasicObjectLayout;

import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.DynamicObjectFactory;
import com.oracle.truffle.api.object.ObjectType;
import com.oracle.truffle.api.object.dsl.Layout;

@Layout
public interface ArrayLayout extends BasicObjectLayout {

    DynamicObjectFactory createArrayShape(DynamicObject logicalClass,
            DynamicObject metaClass);

    DynamicObject createArray(DynamicObjectFactory factory,
            Object store,
            int size);

    boolean isArray(ObjectType objectType);

    boolean isArray(DynamicObject object);

    boolean isArray(Object object);

    Object getStore(DynamicObject object);

    void setStore(DynamicObject object, Object value);

    int getSize(DynamicObject object);

    void setSize(DynamicObject object, int value);

}
