package it.crinali.www.archprot.domain;
import javax.persistence.Column;

import org.hibernate.validator.constraints.Email;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Contatto {

    /**
     */
    @Column(unique = true)
    @Email
    private String email;
    
}
