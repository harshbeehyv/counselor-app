package com.counselor_app.npp.dao;

import com.counselor_app.npp.model.Counselor;
import com.counselor_app.npp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CounselorDao extends JpaRepository <Counselor,Integer> {
    boolean existsByEmail(String email);

    Counselor findByEmail(String email);

    Optional<Counselor> findOneByEmailAndPassword(String email, String encodedPassword);

}
