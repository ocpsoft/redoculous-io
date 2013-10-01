/*
 * Copyright 2011 <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ocpsoft.redoculous.io.rewrite;

import javax.servlet.ServletContext;

import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.DispatchType;
import org.ocpsoft.rewrite.servlet.config.Header;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.RequestParameter;
import org.ocpsoft.rewrite.servlet.config.Response;

public class GZipConfiguration extends HttpConfigurationProvider
{
   @Override
   public Configuration getConfiguration(ServletContext context)
   {
      return ConfigurationBuilder.begin()
               /*
                * Set up compression.
                */
               .addRule()
               .when(DispatchType.isRequest().
                        and(Header.matches("{Accept-Encoding}", "{gzip}")).
                        andNot(RequestParameter.exists("nogzip")).
                        andNot(Path.matches("{*}/javax.faces.resource/{*}")))
               .perform(Response.gzipStreamCompression())

               .where("Accept-Encoding")
               .matches("(?i)Accept-Encoding")
               .where("gzip")
               .matches("(?i).*\\bgzip\\b.*");
   }

   @Override
   public int priority()
   {
      /*
       * Very high priority.
       */
      return -100000;
   }
}
