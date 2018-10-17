package com.juanarroyes.apichat.repository;

import com.juanarroyes.apichat.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>{

    @Query(value = "SELECT m.id, m.from, m.to_user_id, m.to_room_id, m.message_text, m.message_type, m.attach_url, m.created FROM message m WHERE ((m.from = ?1 AND m.to_user_id = ?2) OR (m.from = ?2 AND m.to_user_id = ?1)) ORDER BY m.created ASC", nativeQuery = true)
    List<Message> findAllByUserIdAndFriendId(Long userId, Long friendId);

}
