// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package it.crinali.www.archprot.domain;

import it.crinali.www.archprot.domain.TipoProtocollo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect TipoProtocollo_Roo_Jpa_Entity {
    
    declare @type: TipoProtocollo: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long TipoProtocollo.id;
    
    @Version
    @Column(name = "version")
    private Integer TipoProtocollo.version;
    
    public Long TipoProtocollo.getId() {
        return this.id;
    }
    
    public void TipoProtocollo.setId(Long id) {
        this.id = id;
    }
    
    public Integer TipoProtocollo.getVersion() {
        return this.version;
    }
    
    public void TipoProtocollo.setVersion(Integer version) {
        this.version = version;
    }
    
}
