// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package it.crinali.www.archprot.domain;

import it.crinali.www.archprot.domain.TipoProtocollo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect TipoProtocollo_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager TipoProtocollo.entityManager;
    
    public static final EntityManager TipoProtocollo.entityManager() {
        EntityManager em = new TipoProtocollo().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long TipoProtocollo.countTipoProtocolloes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TipoProtocollo o", Long.class).getSingleResult();
    }
    
    public static List<TipoProtocollo> TipoProtocollo.findAllTipoProtocolloes() {
        return entityManager().createQuery("SELECT o FROM TipoProtocollo o", TipoProtocollo.class).getResultList();
    }
    
    public static TipoProtocollo TipoProtocollo.findTipoProtocollo(Long id) {
        if (id == null) return null;
        return entityManager().find(TipoProtocollo.class, id);
    }
    
    public static List<TipoProtocollo> TipoProtocollo.findTipoProtocolloEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TipoProtocollo o", TipoProtocollo.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void TipoProtocollo.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void TipoProtocollo.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            TipoProtocollo attached = TipoProtocollo.findTipoProtocollo(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void TipoProtocollo.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void TipoProtocollo.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public TipoProtocollo TipoProtocollo.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TipoProtocollo merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
