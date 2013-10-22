package org.ocpsoft.redoculous.io.model.repositories;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.ocpsoft.redoculous.io.model.DeletableObject;
import org.ocpsoft.redoculous.io.model.account.UserProfile;

@Entity
public class SourceRepository extends DeletableObject<SourceRepository>
{
   private static final long serialVersionUID = -5073498985294369095L;

   @Column(length = 128)
   private String url;

   @Column
   @Enumerated(EnumType.STRING)
   private SourceRepositoryType type;

   @Column
   private String name;

   @Column
   private String apiKey;

   @ManyToOne
   private UserProfile owner;

   /**
    * Getters and Setters
    */
   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public UserProfile getOwner()
   {
      return this.owner;
   }

   public void setOwner(final UserProfile account)
   {
      this.owner = account;
   }

   public String getUrl()
   {
      return this.url;
   }

   public void setUrl(final String url)
   {
      this.url = url;
   }

   public String getApiKey()
   {
      return this.apiKey;
   }

   public void setApiKey(final String apiKey)
   {
      this.apiKey = apiKey;
   }

   public SourceRepositoryType getType()
   {
      return type;
   }

   public void setType(SourceRepositoryType type)
   {
      this.type = type;
   }

   @Override
   public String toString()
   {
      return url + " {key: " + apiKey + "}";
   }

}