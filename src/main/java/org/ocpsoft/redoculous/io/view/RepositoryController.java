package org.ocpsoft.redoculous.io.view;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.ocpsoft.common.util.Strings;
import org.ocpsoft.redoculous.io.model.account.UserProfile;
import org.ocpsoft.redoculous.io.model.repositories.SourceRepository;
import org.ocpsoft.redoculous.io.util.jsf.FacesMessages;
import org.ocpsoft.rewrite.faces.navigate.Navigate;

@Named
@Stateful
@RequestScoped
public class RepositoryController implements Serializable
{
   private static final long serialVersionUID = -2583174292087675121L;

   private SourceRepository repository = new SourceRepository();

   @Inject
   private EntityManager em;

   @Inject
   private UserProfile profile;

   public Object add()
   {
      repository.setApiKey(UUID.randomUUID().toString());
      repository.setOwner(null);
      profile.getRepositories().add(repository);
      repository.setOwner(profile);

      em.persist(repository);
      em.merge(profile);
      return Navigate.to("/account/repositories");
   }

   public void validateName(FacesContext context, UIComponent component, Object value)
   {
      if (value instanceof String)
      {
         if (((String) value).length() > 32)
         {
            throw new ValidatorException(FacesMessages.error("Name must be shorter than 32 characters."));
         }
      }
   }

   public void validateUrl(FacesContext context, UIComponent component, Object value)
   {
      if (value instanceof String)
      {
         try
         {
            new URL((String) value);
         }
         catch (MalformedURLException e)
         {
            throw new ValidatorException(FacesMessages.error("Invalid URL. " + Strings.capitalize(e.getMessage())));
         }
      }
   }

   public SourceRepository getRepository()
   {
      return repository;
   }

   public void setRepository(SourceRepository repository)
   {
      this.repository = repository;
   }
}