package org.ocpsoft.redoculous.io.security;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.ocpsoft.urlbuilder.AddressBuilder;
import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;

@Named
public class LoginController
{
   public static String RETURN_TO_PARAM = "returnTo";

   @Inject
   private Identity identity;

   @Inject
   private FacesContext context;

   public String login()
   {
      AuthenticationResult result = identity.login();
      if (AuthenticationResult.FAILED.equals(result))
      {
         context.addMessage(
                  null,
                  new FacesMessage("Authentication was unsuccessful.  Please check your username and password "
                           + "before trying again."));
      }

      String target = context.getExternalContext().getRequestParameterMap().get(RETURN_TO_PARAM);
      if (target != null && !target.isEmpty())
      {
         return AddressBuilder.begin().path(target).query("faces-redirect", "true").toString();
      }

      return "/account/dashboard.xhtml?faces-redirect=true";
   }

   public void logout()
   {
      identity.logout();
   }
}