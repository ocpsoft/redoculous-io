package org.ocpsoft.redoculous.io.view.admin;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.ocpsoft.redoculous.io.model.admin.Settings;
import org.ocpsoft.redoculous.io.util.jsf.FacesMessages;
import org.ocpsoft.rewrite.faces.navigate.Navigate;

@Named
@Stateful
@RequestScoped
public class AdminController implements Serializable
{
   private static final long serialVersionUID = -2583174292087675121L;

   @Inject
   private EntityManager em;

   @Inject
   private Settings settings;

   public Object save()
   {
      em.merge(settings);
      return Navigate.to("/admin");
   }

   public void validateNewUrl(FacesContext context, UIComponent component, Object value)
   {
      if (value instanceof String)
      {
         throw new ValidatorException(
                  FacesMessages.error("Redoculous server could not be contacted."));
      }
   }

   public Settings getSettings()
   {
      return settings;
   }

   public void setSettings(Settings settings)
   {
      this.settings = settings;
   }
}