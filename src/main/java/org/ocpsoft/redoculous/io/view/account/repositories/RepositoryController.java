package org.ocpsoft.redoculous.io.view.account.repositories;

import java.io.Serializable;
import java.util.UUID;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.ocpsoft.redoculous.io.model.account.UserProfile;
import org.ocpsoft.redoculous.io.model.repositories.SourceRepository;
import org.ocpsoft.redoculous.io.util.jsf.FacesMessages;
import org.ocpsoft.redoculous.io.util.jsf.FacesUtils;
import org.ocpsoft.rewrite.annotation.Parameter;
import org.ocpsoft.rewrite.annotation.PathPattern;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.navigate.Navigate;

@Named
@Stateful
@RequestScoped
@PathPattern("/account/repositories/view")
public class RepositoryController implements Serializable
{
   private static final long serialVersionUID = -2583174292087675121L;

   @Inject
   private EntityManager em;

   @Inject
   private UserProfile profile;

   @Deferred
   @Parameter("repo")
   private String repoUrl;

   private SourceRepository repository = new SourceRepository();

   @Deferred
   @RequestAction
   public void loadRepo()
   {
      for (SourceRepository repo : profile.getRepositories()) {
         if (!repo.isDeleted() && repo.getUrl().equals(repoUrl))
         {
            repository = repo;
            break;
         }
      }

      if (!repository.isPersistent())
         FacesUtils.send404();
   }

   public void regenerateApiKey()
   {
      repository.setApiKey(UUID.randomUUID().toString());
      em.merge(repository);
   }

   public Object add()
   {
      repository.setApiKey(UUID.randomUUID().toString());
      repository.setOwner(null);
      profile.getRepositories().add(repository);
      repository.setOwner(profile);

      em.persist(repository);
      return Navigate.to("/account/repositories");
   }

   public Object save()
   {
      em.merge(repository);
      return Navigate.to("/account/repositories/view").with("repo", repoUrl);
   }

   public void validateNewName(FacesContext context, UIComponent component, Object value)
   {
      if (value instanceof String)
      {
         String repoUrl = (String) value;
         if (repoUrl.length() > 32)
         {
            throw new ValidatorException(FacesMessages.error("Name must be shorter than 32 characters."));
         }

         for (SourceRepository repo : profile.getRepositories()) {
            if (repo.getUrl().equals(repoUrl))
            {
               throw new ValidatorException(
                        FacesMessages.error("A repository with the given URL is already registered."));
            }
         }
      }
   }

   public void validateNewUrl(FacesContext context, UIComponent component, Object value)
   {
      if (value instanceof String)
      {
         String repoUrl = (String) value;

         for (SourceRepository repo : profile.getRepositories()) {
            if (repo.getUrl().equals(repoUrl))
            {
               throw new ValidatorException(
                        FacesMessages.error("A repository with the given URL is already registered."));
            }
         }
      }
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
   {}

   public SourceRepository getRepository()
   {
      return repository;
   }

   public void setRepository(SourceRepository repository)
   {
      this.repository = repository;
   }

   public String getRepoUrl()
   {
      return repoUrl;
   }

   public void setRepoUrl(String repoUrl)
   {
      this.repoUrl = repoUrl;
   }
}