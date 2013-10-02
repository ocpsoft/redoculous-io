/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.ocpsoft.redoculous.io.util;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class Threads
{
   public static void sleep(int millis)
   {
      try {
         Thread.sleep(millis);
      }
      catch (InterruptedException e) {
         throw new RuntimeException(e);
      }
   }

}
