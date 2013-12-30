package it.crinali.www.archprot.domain;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

/**
 * 
 * @author ignazio
 * tipologia comunicazioni (entità) convocazioni,  
 * lista destinatari 
 * in ricerca inserire OR Logico
 */
@RooJavaBean
@RooJpaActiveRecord
@DynamicUpdate
public class Document {
    /**
     */
    @NotNull
    @Size(max = 100)
    private String oggetto;

    /**
     */
    @NotNull
    private String numeroProtocollo;

    /**
     */
    @NotNull
    @OneToOne
    private TipoProtocollo tipoProtocollo;
    
    /**
     */
    @OneToOne
    private TipoComunicazione tipoComunicazione;
    
    
    /**
     */
    @OneToOne
    private Note note;

    /**
     */
    @NotNull
    @OneToOne
    private Progetto progetto;

    /**
     */
    @NotNull
    @OneToOne
    private Struttura struttura;

    /**
     */
    @NotNull
    @OneToOne
    private Contatto contatto;
    
    /**
     */
    @ManyToMany(cascade = { javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.REFRESH })
    private Set<Destinatario> destinatari = new HashSet<Destinatario>();

    /**
     */
    private String nomeFile;

    /**
     */
    private Long size;
    /**
     */
    @NotNull
    @NotEmpty
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;
    /**
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] thumbnail;

    /**
     */
    private String contentType;

    @Transient
    @Size(max = 100)
    private String url;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    @NotNull
    private Date dataProtocollo;
    
    
    @Size(max = 255)
    private String path;
    
    
   @Override
    public String toString() {
    	     return (new ReflectionToStringBuilder(this) {
    	         protected boolean accept(Field f) {
    	             return super.accept(f) && !f.getName().equals("content") &&  !f.getName().equals("thumbnail");
    	         }
    	     }).toString();
    	 }
    	
    
}
