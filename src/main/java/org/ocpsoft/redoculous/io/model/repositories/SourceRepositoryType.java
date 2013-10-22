/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.ocpsoft.redoculous.io.model.repositories;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public enum SourceRepositoryType
{
   GIT("Git"), SVN("Subversion"), HG("Mercurial");

   private String name;

   private SourceRepositoryType(String name)
   {
      this.name = name;
   }

   public String getName()
   {
      return name;
   }
}
