package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.PostDateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDateRepository extends JpaRepository<PostDateEntity,Long> {
}
