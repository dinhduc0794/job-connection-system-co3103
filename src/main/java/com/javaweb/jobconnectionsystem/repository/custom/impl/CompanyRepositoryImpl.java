package com.javaweb.jobconnectionsystem.repository.custom.impl;

import com.javaweb.jobconnectionsystem.entity.CompanyEntity;
import com.javaweb.jobconnectionsystem.model.request.CompanySearchRequest;
import com.javaweb.jobconnectionsystem.repository.custom.CompanyRepositoryCustom;
import com.javaweb.jobconnectionsystem.utils.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
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
        // Start the JPQL query with the base entity
        StringBuilder jpql = new StringBuilder("SELECT co FROM CompanyEntity co");

        // Handle join tables (company_field, ward, city, etc.)
        handleJoinTable(params, jpql);

        // Handle where conditions based on parameters
        handleWhereCondition(params, jpql);

        // Create the TypedQuery using the JPQL string
        TypedQuery<CompanyEntity> query = entityManager.createQuery(jpql.toString(), CompanyEntity.class);

        // Execute and return the results
        return query.getResultList();
    }

    // Method to handle JOIN conditions in JPQL based on the parameters
    public void handleJoinTable(CompanySearchRequest params, StringBuilder jpql) {
        // Join for company fields if specified
        List<String> fields = params.getFields();
        if (fields != null && !fields.isEmpty()) {
            jpql.append(" JOIN co.fields f");
        }

        // Join for location-related data if any location parameters are provided
        String province = (String) params.getProvince();
        String city = (String) params.getCity();
        String ward = (String) params.getWard();

        if (StringUtils.notEmptyData(province) || StringUtils.notEmptyData(city) || StringUtils.notEmptyData(ward)) {
            jpql.append(" JOIN co.userWards uw")
                    .append(" JOIN uw.ward wa")
                    .append(" JOIN wa.city ci")
                    .append(" JOIN ci.province pr");
        }
    }

    // Method to handle WHERE conditions based on the parameters
    public void handleWhereCondition(CompanySearchRequest params, StringBuilder jpql) {
        jpql.append(" WHERE 1=1");

        // Handle filtering by job fields (company_field)
        List<String> fields = params.getFields();
        if (fields != null && !fields.isEmpty()) {
            String fi = fields.stream().map(i -> "'" + i + "'").collect(Collectors.joining(", "));
            jpql.append(" AND f.name IN (" + fi + ")");
        }

        // Reflectively process other fields from the CompanySearchRequest
        try {
            Field[] flds = CompanySearchRequest.class.getDeclaredFields(); // Get fields of the search request
            for (Field fi : flds) {
                fi.setAccessible(true);
                String key = fi.getName();
                Object value = fi.get(params);
                if (value != null) {
                    switch (key) {
                        case "fields":
                            break; // Already handled in fields filtering
                        case "ward":
                            jpql.append(" AND wa.name LIKE '%" + value.toString() + "%'");
                            break;
                        case "city":
                            jpql.append(" AND ci.name LIKE '%" + value.toString() + "%'");
                            break;
                        case "province":
                            jpql.append(" AND pr.name LIKE '%" + value.toString() + "%'");
                            break;
                        case "minRating":
                            jpql.append(" AND co.rating >= " + value);
                            break;
                        default:
                            if (value instanceof Number) {
                                jpql.append(" AND co." + key + " = " + value);
                            } else if (value instanceof String) {
                                jpql.append(" AND LOWER(co." + key + ") LIKE '%" + value.toString().toLowerCase() + "%'");
                            }
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int countTotalItems(CompanySearchRequest params) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT co.id) FROM company co ");

        handleJoinTable(params, sql);
        handleWhereCondition(params, sql);

        Query query = entityManager.createNativeQuery(sql.toString());
        return ((Number) query.getSingleResult()).intValue();
    }
}
