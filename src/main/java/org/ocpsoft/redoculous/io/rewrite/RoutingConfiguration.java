package org.ocpsoft.redoculous.io.rewrite;

import javax.servlet.ServletContext;

import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.servlet.config.DispatchType;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.Resource;
import org.ocpsoft.rewrite.servlet.config.SendStatus;
import org.ocpsoft.rewrite.servlet.config.ServletMapping;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public class RoutingConfiguration extends HttpConfigurationProvider
{

   @Override
   public Configuration getConfiguration(ServletContext context)
   {
      return ConfigurationBuilder.begin()

               /*
                * Block direct file access.
                */
               .addRule()
               .when(DispatchType.isRequest().and(Direction.isInbound())
                        .and(Path.matches("/{p}.xhtml"))
                        .and(Resource.exists("/{p}.xhtml"))
                        .andNot(ServletMapping.includes("/{p}")))
               .perform(SendStatus.error(404))
               .where("p").matches(".*")

               /*
                * Application Routes
                */
               .addRule(Join.path("/{p}/").to("/faces/{p}/index.xhtml"))
               .when(Resource.exists("/{p}/index.xhtml"))
               .where("p").matches(".*")

               .addRule(Join.path("/{p}").to("/faces/{p}.xhtml"))
               .when(Resource.exists("/{p}.xhtml"))
               .where("p").matches(".*")

               /*
                * Resources routes
                */
               .addRule(Join.path("/img/{p}").to("/resources/img/{p}"))
               .addRule(Join.path("/favicon.ico").to("/resources/img/redoculous-favicon-64x64.ico"));
   }

   @Override
   public int priority()
   {
      return Integer.MAX_VALUE;
   }
}
