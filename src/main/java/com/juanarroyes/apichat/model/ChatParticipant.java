package com.juanarroyes.apichat.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "chat_participant")
@EntityListeners(AuditingEntityListener.class)
public class ChatParticipant implements Serializable {

    @EmbeddedId ChatParticipantKey id;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @UpdateTimestamp
    @Column(insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @JoinColumns({
            @JoinColumn(name = "chat_id", referencedColumnName = "id"),
            @JoinColumn(name = "user_id", referencedColumnName = "id")
    })

    public ChatParticipantKey getId() {
        return id;
    }

    public void setId(ChatParticipantKey id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
