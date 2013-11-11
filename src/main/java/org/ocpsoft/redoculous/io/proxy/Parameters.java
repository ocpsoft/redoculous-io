/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.ocpsoft.redoculous.io.proxy;

import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.param.ParameterStore;
import org.ocpsoft.rewrite.param.ParameterValueStore;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class Parameters
{
   public static String retrieve(EvaluationContext context, String string)
   {
      return ((ParameterValueStore) context.get(ParameterValueStore.class)).retrieve(((ParameterStore) context
               .get(ParameterStore.class)).get(string));
   }
}
