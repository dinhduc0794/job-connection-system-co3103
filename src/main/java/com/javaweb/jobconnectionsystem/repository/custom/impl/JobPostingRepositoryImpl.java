package com.javaweb.jobconnectionsystem.repository.custom.impl;

import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.request.JobPostingSearchRequest;
import com.javaweb.jobconnectionsystem.repository.custom.JobPostingRepositoryCustom;
import com.javaweb.jobconnectionsystem.utils.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JobPostingRepositoryImpl implements JobPostingRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<JobPostingEntity> findAll(JobPostingSearchRequest params) {
        StringBuilder sql = new StringBuilder("SELECT jp.* FROM jobposting jp ");

        handleJoinTable(params, sql);
        handleWhereCondition(params, sql);

        sql.append(" GROUP BY jp.id");	//handle duplicate

        Query query = entityManager.createNativeQuery(sql.toString(), JobPostingEntity.class);
        return query.getResultList();
    }

    public void handleJoinTable(JobPostingSearchRequest params, StringBuilder sql) {
        /* join to get job type */
        String jobType = (String) params.getJobType();
        if (StringUtils.notEmptyData(jobType)) {
            sql.append(" JOIN jobtype jt ON jp.jobtype_id = jt.id");
        }

        // join to get skill
        List<String> skills = params.getSkills();
        if (skills != null && !skills.isEmpty()) {
            sql.append(" JOIN jobtype jt ON jp.jobtype_id = jt.id")
                    .append(" JOIN skill sk ON sk.jobtype_id = jt.id");
        }

        // join to get company
        String companyName = (String) params.getCompanyName();
        Integer companyRating = params.getComanyRating();
        String province = (String) params.getProvince();
        String city = (String) params.getCity();
        String ward = (String) params.getWard();

        if (StringUtils.notEmptyData(province) || StringUtils.notEmptyData(city) || StringUtils.notEmptyData(ward)) {
            sql.append(" JOIN company co ON jp.company_id = co.id")
                    .append(" JOIN user_ward uw ON co.id = uw.user_id")
                    .append(" JOIN ward wa ON uw.ward_id = wa.id")
                    .append(" JOIN city ci ON wa.city_id = ci.id")
                    .append(" JOIN province pr ON ci.province_id = pr.id");
            return;
        }

        if (StringUtils.notEmptyData(companyName) || companyRating != null) {
            sql.append(" JOIN company co ON jp.company_id = co.id");

        }
    }

    public void handleWhereCondition(JobPostingSearchRequest params, StringBuilder sql) {
        sql.append(" WHERE 1=1");

        List<String> skills = params.getSkills();
        if (skills != null && !skills.isEmpty()) {
            String sk = skills.stream().map(i -> "'" + i + "'").collect(Collectors.joining(", "));
            sql.append(" AND sk.name IN (" + sk + ")");
        }

        // duyet map handle cac attribute con lai, tru typecode
        // update: duyet object bang java reflection
        try {
            Field[] fields = JobPostingSearchRequest.class.getDeclaredFields();	// ham nay get ra nhu
            for (Field fi : fields) {
                fi.setAccessible(true);
                String key = fi.getName();
                Object value = fi.get(params);
                if (value != null) {
                    switch (key) {
                        case "skill":
                            break;
                        case "salary":
                            sql.append(" AND jp.min_salary <= " + value + " AND jp.max_salary >= " + value);
                            break;
                        case "companyName":
                            sql.append(" AND co.name " + " LIKE '%" + value.toString() + "%'");
                            break;
                        case "jobType":
                            sql.append(" AND jt.name " + " LIKE '%" + value.toString() + "%'");
                            break;
                        case "ward":
                            sql.append(" AND wa.name " + " LIKE '%" + value.toString() + "%'");
                            break;
                        case "city":
                            sql.append(" AND ci.name " + " LIKE '%" + value.toString() + "%'");
                            break;
                        case "province":
                            sql.append(" AND pr.name " + " LIKE '%" + value.toString() + "%'");
                            break;
                        case "minOfApplicants":
                            sql.append(" AND jp.number_of_applicants >= " + value);
                            break;
                        case "level":
                            sql.append(" AND jp.level = " + value);
                            break;
                        default:
                            if (value instanceof Number) {
                                sql.append(" AND jp." + key + " = " + value);
                            }
                            else if (value instanceof String){
                                sql.append(" AND jp." + key + " LIKE '%" + value.toString() + "%'");
                            }
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
