/*
 * Copyright (c) 2016, 2017 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 */
package org.truffleruby.core.string;

import static org.truffleruby.core.string.StringOperations.rope;

import org.truffleruby.builtins.CoreModule;
import org.truffleruby.builtins.CoreMethod;
import org.truffleruby.builtins.CoreMethodArrayArgumentsNode;
import org.truffleruby.core.rope.Rope;
import org.truffleruby.core.rope.RopeNodes;
import org.truffleruby.language.RubyGuards;
import org.truffleruby.language.control.RaiseException;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.object.DynamicObject;

@CoreModule("Truffle::StringOperations")
public class TruffleStringNodes {

    @CoreMethod(names = "truncate", onSingleton = true, required = 2, lowerFixnum = 2)
    public abstract static class TruncateNode extends CoreMethodArrayArgumentsNode {

        @Specialization(guards = { "newByteLength < 0" })
        @TruffleBoundary
        protected DynamicObject truncateLengthNegative(DynamicObject string, int newByteLength) {
            throw new RaiseException(
                    getContext(),
                    getContext().getCoreExceptions().argumentError(formatNegativeError(newByteLength), this));
        }

        @Specialization(
                guards = { "newByteLength >= 0", "isRubyString(string)", "isNewLengthTooLarge(string, newByteLength)" })
        @TruffleBoundary
        protected DynamicObject truncateLengthTooLong(DynamicObject string, int newByteLength) {
            throw new RaiseException(
                    getContext(),
                    getContext()
                            .getCoreExceptions()
                            .argumentError(formatTooLongError(newByteLength, rope(string)), this));
        }

        @Specialization(
                guards = {
                        "newByteLength >= 0",
                        "isRubyString(string)",
                        "!isNewLengthTooLarge(string, newByteLength)" })
        protected DynamicObject stealStorage(DynamicObject string, int newByteLength,
                @Cached RopeNodes.SubstringNode substringNode) {

            StringOperations.setRope(string, substringNode.executeSubstring(rope(string), 0, newByteLength));

            return string;
        }

        protected static boolean isNewLengthTooLarge(DynamicObject string, int newByteLength) {
            assert RubyGuards.isRubyString(string);

            return newByteLength > rope(string).byteLength();
        }

        @TruffleBoundary
        private String formatNegativeError(int count) {
            return StringUtils.format("Invalid byte count: %d is negative", count);
        }

        @TruffleBoundary
        private String formatTooLongError(int count, final Rope rope) {
            return StringUtils
                    .format("Invalid byte count: %d exceeds string size of %d bytes", count, rope.byteLength());
        }

    }

}
