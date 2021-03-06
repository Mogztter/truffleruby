/*
 * Copyright (c) 2015, 2017 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 *
 * Contains code modified from JRuby's org.jruby.runtime.Helpers and org.jruby.runtime.ArgumentType.
 */
package org.truffleruby.language.arguments;

import org.truffleruby.RubyContext;
import org.truffleruby.core.array.ArrayHelpers;
import org.truffleruby.parser.ArgumentDescriptor;
import org.truffleruby.parser.ArgumentType;

import com.oracle.truffle.api.object.DynamicObject;

public class ArgumentDescriptorUtils {

    public static DynamicObject argumentDescriptorsToParameters(RubyContext context,
            ArgumentDescriptor[] argsDesc,
            boolean isLambda) {
        final Object[] params = new Object[argsDesc.length];

        for (int i = 0; i < argsDesc.length; i++) {
            params[i] = toArray(context, argsDesc[i], isLambda);
        }

        return ArrayHelpers.createArray(context, params);
    }

    public static DynamicObject toArray(RubyContext context,
            ArgumentDescriptor argDesc,
            boolean isLambda) {
        if ((argDesc.type == ArgumentType.req) && !isLambda) {
            return toArray(context, ArgumentType.opt, argDesc.name);
        }

        return toArray(context, argDesc.type, argDesc.name);
    }

    public static DynamicObject toArray(RubyContext context,
            ArgumentType argType,
            String name) {
        final Object[] store;

        if (argType.anonymous || name == null) {
            store = new Object[]{
                    context.getSymbolTable().getSymbol(argType.symbolicName)
            };
        } else {
            store = new Object[]{
                    context.getSymbolTable().getSymbol(argType.symbolicName),
                    context.getSymbolTable().getSymbol(name)
            };
        }

        return ArrayHelpers.createArray(context, store);
    }
}
