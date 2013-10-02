package org.ocpsoft.redoculous.io.view;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.ocpsoft.redoculous.io.util.jsf.FacesMessages;
import org.ocpsoft.urlbuilder.AddressBuilder;
import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;

@Named
public class LoginController implements Serializable
{
   private static final long serialVersionUID = 4164173751827686256L;

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
         FacesMessages.addError(context,
                  "Login was unsuccessful. Please check your username and password before trying again.");
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