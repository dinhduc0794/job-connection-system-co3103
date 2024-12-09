
package com.javaweb.jobconnectionsystem.repository;

import com.javaweb.jobconnectionsystem.entity.ApplicantJobtypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantJobTypeRepository extends JpaRepository<ApplicantJobtypeEntity,Long> {

}
