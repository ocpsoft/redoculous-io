package org.ocpsoft.redoculous.io.view.admin;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.ProxyFactory;
import org.ocpsoft.redoculous.io.model.account.UserProfile;
import org.ocpsoft.redoculous.io.model.admin.Settings;
import org.ocpsoft.redoculous.io.util.jsf.FacesMessages;
import org.ocpsoft.redoculous.rest.StatusService;
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
   @Named
   private Settings settings;

   @Inject
   private UserProfile profile;

   private List<Settings> settingsHistory;

   public Object save()
   {
      Settings updated = new Settings();
      updated.setLastUpdatedBy(profile);
      updated.setRedoculousURL(settings.getRedoculousURL());
      em.persist(updated);
      return Navigate.to("/admin");
   }

   public List<Settings> getSettingsHistory()
   {
      if (settingsHistory == null)
      {
         settingsHistory = em.createQuery("FROM Settings ORDER BY id DESC", Settings.class).getResultList();
      }
      return settingsHistory;
   }

   public void validateNewUrl(FacesContext context, UIComponent component, Object value)
   {
      if (value instanceof String)
      {
         StatusService client = ProxyFactory.create(StatusService.class, (String) value);
         Response response = client.status();
         if (response.getStatus() != 200)
         {
            throw new ValidatorException(
                     FacesMessages.error("Redoculous server could not be contacted. Status: " + response.getStatus()));
         }
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