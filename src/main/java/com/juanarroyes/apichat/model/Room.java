package com.juanarroyes.apichat.model;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name= "room")
@EntityListeners(AuditingEntityListener.class)
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "room_message_broadcast")
    private String roomMessageBroadcast;

    @CreationTimestamp
    @Column(nullable =  false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public Room() {}

    public Room(String roomName, String roomMessageBroadcast) {
        this.roomName = roomName;
        this.roomMessageBroadcast = roomMessageBroadcast;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomMessageBroadcast() {
        return roomMessageBroadcast;
    }

    public void setRoomMessageBroadcast(String roomMessageBroadcast) {
        this.roomMessageBroadcast = roomMessageBroadcast;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
