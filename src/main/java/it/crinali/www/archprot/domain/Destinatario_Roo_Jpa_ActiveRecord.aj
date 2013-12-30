// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package it.crinali.www.archprot.domain;

import it.crinali.www.archprot.domain.Destinatario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Destinatario_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Destinatario.entityManager;
    
    public static final EntityManager Destinatario.entityManager() {
        EntityManager em = new Destinatario().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Destinatario.countDestinatarios() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Destinatario o", Long.class).getSingleResult();
    }
    
    public static List<Destinatario> Destinatario.findAllDestinatarios() {
        return entityManager().createQuery("SELECT o FROM Destinatario o", Destinatario.class).getResultList();
    }
    
    public static Destinatario Destinatario.findDestinatario(Long id) {
        if (id == null) return null;
        return entityManager().find(Destinatario.class, id);
    }
    
    public static List<Destinatario> Destinatario.findDestinatarioEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Destinatario o", Destinatario.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Destinatario.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Destinatario.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Destinatario attached = Destinatario.findDestinatario(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Destinatario.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Destinatario.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Destinatario Destinatario.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Destinatario merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
