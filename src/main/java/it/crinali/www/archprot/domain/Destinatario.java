package it.crinali.www.archprot.domain;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooJpaActiveRecord
public class Destinatario {
	 /**
     */
	@NotNull
	String ragioneSociale;
	 /**
     */
	String pIvaCodiceFiscale;
	 /**
     */
	String note;
	 /**
     */
    @ManyToMany(cascade = { javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.REFRESH }, mappedBy = "destinatari")
    private Set<Document> documenti = new HashSet<Document>();
    
    @Override
    public String toString() {
    	return this.ragioneSociale;
    	 }
    	 
}
