/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */

package org.teiid.query.function.aggregate;

import java.util.List;

import org.teiid.api.exception.query.ExpressionEvaluationException;
import org.teiid.api.exception.query.FunctionExecutionException;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.TeiidProcessingException;
import org.teiid.core.types.ArrayImpl;
import org.teiid.query.util.CommandContext;

/**
 * We store up to three values related to the lead/lag per row
 */
public class LeadLagValue extends AggregateFunction {
    
    private Object[] vals = null;
    private int partition = 0;
    
    @Override
    public void addInputDirect(List<?> tuple, CommandContext commandContext)
            throws TeiidComponentException, TeiidProcessingException {
        vals = new Object[argIndexes.length + 1];
        for (int i = 0; i < argIndexes.length; i++) {
            vals[i] = tuple.get(argIndexes[i]);
        }
        vals[argIndexes.length] = partition;
    }
    
    @Override
    public Object getResult(CommandContext commandContext)
            throws FunctionExecutionException, ExpressionEvaluationException,
            TeiidComponentException, TeiidProcessingException {
        return new ArrayImpl(vals);
    }
    
    @Override
    public void reset() {
        vals = null;
        partition++;
    }
    
    @Override
    public boolean respectsNull() {
        return true;
    }

}
