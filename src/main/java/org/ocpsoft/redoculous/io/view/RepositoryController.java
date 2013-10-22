package org.ocpsoft.redoculous.io.view;

import java.io.Serializable;
import java.util.UUID;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.ocpsoft.redoculous.io.model.account.UserProfile;
import org.ocpsoft.redoculous.io.model.repositories.SourceRepository;
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

   public SourceRepository getRepository()
   {
      return repository;
   }

   public void setRepository(SourceRepository repository)
   {
      this.repository = repository;
   }
}