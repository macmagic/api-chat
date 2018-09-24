package com.juanarroyes.apichat.model;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name= "contact_list_status")
@EntityListeners(AuditingEntityListener.class)
public class ContactListStatus implements Serializable {

    public static final int CONTACT_IS_PENDING = 1;
    public static final int CONTACT_IS_FRIEND = 2;
    public static final int CONTACT_IS_BLOCKED = 3;

    @Id
    private Integer id;

    private String name;

    @CreationTimestamp
    @Column(nullable =  false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
