package com.javaweb.jobconnectionsystem.repository.custom.impl;

import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.entity.JobPostingEntity;
import com.javaweb.jobconnectionsystem.model.request.CompanySearchRequest;
import com.javaweb.jobconnectionsystem.model.request.JobPostingSearchRequest;
import com.javaweb.jobconnectionsystem.repository.custom.CompanyRepositoryCustom;
import com.javaweb.jobconnectionsystem.utils.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CompanyRepositoryImpl implements CompanyRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CompanyEntity> findAll(CompanySearchRequest params) {
        StringBuilder sql = new StringBuilder("SELECT co.* FROM company co ");

        handleJoinTable(params, sql);
        handleWhereCondition(params, sql);

        sql.append(" GROUP BY co.id");	//handle duplicate

        Query query = entityManager.createNativeQuery(sql.toString(), CompanyEntity.class);
        return query.getResultList();
    }

    public void handleJoinTable(CompanySearchRequest params, StringBuilder sql) {
        /* join to get job type */
        List<String> fields = params.getFields();
        if (fields != null && !fields.isEmpty()) {
            sql.append(" JOIN company_field cf ON co.id = cf.company_id")
                    .append(" JOIN field fi ON cf.field_id = fi.id");
        }

        // join to get company
        String province = (String) params.getProvince();
        String city = (String) params.getCity();
        String ward = (String) params.getWard();

        if (StringUtils.notEmptyData(province) || StringUtils.notEmptyData(city) || StringUtils.notEmptyData(ward)) {
            sql.append(" JOIN user_ward uw ON co.id = uw.user_id")
                    .append(" JOIN ward wa ON uw.ward_id = wa.id")
                    .append(" JOIN city ci ON wa.city_id = ci.id")
                    .append(" JOIN province pr ON ci.province_id = pr.id");
        }
    }

    public void handleWhereCondition(CompanySearchRequest params, StringBuilder sql) {
        sql.append(" WHERE 1=1");

        List<String> fields = params.getFields();
        if (fields != null && !fields.isEmpty()) {
            String fi = fields.stream().map(i -> "'" + i + "'").collect(Collectors.joining(", "));
            sql.append(" AND sk.name IN (" + fi + ")");
        }

        // duyet map handle cac attribute con lai, tru typecode
        // update: duyet object bang java reflection
        try {
            Field[] flds = JobPostingSearchRequest.class.getDeclaredFields();	// ham nay get ra nhu
            for (Field fi : flds) {
                fi.setAccessible(true);
                String key = fi.getName();
                Object value = fi.get(params);
                if (value != null) {
                    switch (key) {
                        case "field":
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
                        default:
                            if (value instanceof Number) {
                                sql.append(" AND co." + key + " = " + value);
                            }
                            else if (value instanceof String){
                                sql.append(" AND co." + key + " LIKE '%" + value.toString() + "%'");
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
