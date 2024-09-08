package com.eastwoo.socketkeyapi.api.login.repository;

import com.eastwoo.socketkeyapi.api.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.eastwoo.socketkeyapi.api.repository
 * fileName       : UserRepository
 * author         : dongwoo
 * date           : 2024-09-05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-05        dongwoo       최초 생성
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
}