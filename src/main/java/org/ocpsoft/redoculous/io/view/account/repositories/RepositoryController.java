package org.ocpsoft.redoculous.io.view.account.repositories;

import java.io.Serializable;
import java.util.UUID;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.ocpsoft.redoculous.io.core.RepositoryOperations;
import org.ocpsoft.redoculous.io.model.account.UserProfile;
import org.ocpsoft.redoculous.io.model.repositories.SourceRepository;
import org.ocpsoft.redoculous.io.util.jsf.FacesMessages;
import org.ocpsoft.redoculous.io.util.jsf.FacesUtils;
import org.ocpsoft.redoculous.rest.model.RepositoryStatus;
import org.ocpsoft.redoculous.rest.model.RepositoryStatus.State;
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

   private RepositoryStatus repositoryStatus = new RepositoryStatus();

   @Inject
   private RepositoryOperations operations;

   @Deferred
   @RequestAction
   public void loadRepo()
   {
      for (SourceRepository repo : profile.getRepositories())
      {
         if (!repo.isDeleted() && repo.getUrl().equals(repoUrl))
         {
            repository = repo;
            break;
         }
      }

      if (!repository.isPersistent())
         FacesUtils.send404();
      else
         repositoryStatus = operations.getStatus(profile, repository.getUrl());
   }

   public void regenerateApiKey()
   {
      repository.setApiKey(UUID.randomUUID().toString());
      em.merge(repository);
   }

   public SourceRepository findRepositoryByKey(String key)
   {
      TypedQuery<SourceRepository> query = em.createQuery("FROM SourceRepository r WHERE r.apiKey=:key",
               SourceRepository.class);
      query.setParameter("key", key);
      SourceRepository result = null;
      try
      {
         result = query.getSingleResult();
      }
      catch (NoResultException e)
      {
         return null;
      }
      return result;
   }

   public Object add()
   {
      repository.setApiKey(UUID.randomUUID().toString());
      repository.setOwner(null);
      profile.getRepositories().add(repository);
      repository.setOwner(profile);
      em.persist(repository);

      initialize(repository.getUrl());

      return Navigate.to("/account/repositories/view").with("repo", repository.getUrl());
   }

   public void initialize(String url)
   {
      operations.initializeRepository(profile, url);
   }

   public boolean isWorking()
   {
      State state = getRepositoryStatus().getState();
      return !State.INITIALIZED.equals(state) && !State.ERROR.equals(state) && !State.MISSING.equals(state);
   }

   public boolean isWorkingOrMissing()
   {
      State state = getRepositoryStatus().getState();
      return !State.INITIALIZED.equals(state) && !State.ERROR.equals(state);
   }

   public Object save()
   {
      em.merge(repository);
      return Navigate.to("/account/repositories/view").with("repo", repository.getUrl());
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

         for (SourceRepository repo : profile.getRepositories())
         {
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

         for (SourceRepository repo : profile.getRepositories())
         {
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
   {
      if (value instanceof String)
      {
         if (!repository.getUrl().trim().equals(((String) value).trim()))
         {
            validateNewUrl(context, component, value);
         }
      }
   }

   public void repositoryUrlChanged(ValueChangeEvent e)
   {
      operations.purgeRepository(profile, (String) e.getOldValue());
      operations.initializeRepository(profile, (String) e.getNewValue());
   }

   public SourceRepository getRepository()
   {
      return repository;
   }

   public void setRepository(SourceRepository repository)
   {
      this.repository = repository;
   }

   public RepositoryStatus getRepositoryStatus()
   {
      return repositoryStatus;
   }

   public void setRepositoryStatus(RepositoryStatus repositoryStatus)
   {
      this.repositoryStatus = repositoryStatus;
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