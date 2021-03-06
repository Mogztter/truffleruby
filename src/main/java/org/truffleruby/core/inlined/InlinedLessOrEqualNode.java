/*
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 */
package org.truffleruby.core.inlined;

import org.truffleruby.RubyContext;
import org.truffleruby.language.dispatch.RubyCallNodeParameters;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class InlinedLessOrEqualNode extends BinaryInlinedOperationNode {

    public InlinedLessOrEqualNode(RubyContext context, RubyCallNodeParameters callNodeParameters) {
        super(
                callNodeParameters,
                context.getCoreMethods().integerLessOrEqualAssumption);
    }

    @Specialization(assumptions = "assumptions")
    protected boolean doInt(int a, int b) {
        return a <= b;
    }

    @Specialization(assumptions = "assumptions")
    protected boolean doLong(long a, long b) {
        return a <= b;
    }

    @Specialization
    protected Object fallback(VirtualFrame frame, Object a, Object b) {
        return rewriteAndCall(frame, a, b);
    }

}
