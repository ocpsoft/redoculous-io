package org.ocpsoft.redoculous.io.model.repositories;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.ocpsoft.redoculous.io.model.DeletableObject;

@Entity
public class SourceRepository extends DeletableObject<SourceRepository>
{
   private static final long serialVersionUID = -5073498985294369095L;

   @Column(length = 128)
   private String url;

   @Column
   @Enumerated(EnumType.STRING)
   private VCSType type;

   @Column
   private String apiKey;

   @ManyToOne
   private Account account;

   /**
    * Getters and Setters
    */
   public Account getAccount()
   {
      return this.account;
   }

   public void setAccount(final Account account)
   {
      this.account = account;
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

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      result += "serialVersionUID: " + serialVersionUID;
      if (url != null && !url.trim().isEmpty())
         result += ", url: " + url;
      if (apiKey != null && !apiKey.trim().isEmpty())
         result += ", apiKey: " + apiKey;
      return result;
   }

}