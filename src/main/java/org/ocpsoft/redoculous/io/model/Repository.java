package org.ocpsoft.redoculous.io.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import org.ocpsoft.redoculous.io.model.Ref;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.OneToMany;
import java.lang.Override;

@Entity
public class Repository implements Serializable
{
   private static final long serialVersionUID = -4430760114318545927L;

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id = null;

   @Version
   @Column(name = "version")
   private int version = 0;

   @Column
   private String name;

   @Column
   private String url;

   @ManyToOne
   private Profile owner;

   @OneToMany
   private Set<Ref> refs = new HashSet<Ref>();

   @Column
   private String allowedRefPattern;

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   @Override
   public boolean equals(Object that)
   {
      if (this == that)
      {
         return true;
      }
      if (that == null)
      {
         return false;
      }
      if (getClass() != that.getClass())
      {
         return false;
      }
      if (id != null)
      {
         return id.equals(((Repository) that).id);
      }
      return super.equals(that);
   }

   @Override
   public int hashCode()
   {
      if (id != null)
      {
         return id.hashCode();
      }
      return super.hashCode();
   }

   public String getName()
   {
      return this.name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }

   public String getUrl()
   {
      return this.url;
   }

   public void setUrl(final String url)
   {
      this.url = url;
   }

   public Profile getOwners()
   {
      return this.owner;
   }

   public void setOwners(final Profile owners)
   {
      this.owner = owners;
   }

   public Set<Ref> getRefs()
   {
      return this.refs;
   }

   public void setRefs(final Set<Ref> refs)
   {
      this.refs = refs;
   }

   public String getAllowedRefPattern()
   {
      return this.allowedRefPattern;
   }

   public void setAllowedRefPattern(final String allowedRefPattern)
   {
      this.allowedRefPattern = allowedRefPattern;
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      result += "serialVersionUID: " + serialVersionUID;
      if (name != null && !name.trim().isEmpty())
         result += ", name: " + name;
      if (url != null && !url.trim().isEmpty())
         result += ", url: " + url;
      if (allowedRefPattern != null && !allowedRefPattern.trim().isEmpty())
         result += ", allowedRefPattern: " + allowedRefPattern;
      return result;
   }
}