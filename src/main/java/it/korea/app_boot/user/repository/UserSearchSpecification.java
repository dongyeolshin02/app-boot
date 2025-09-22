package it.korea.app_boot.user.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import it.korea.app_boot.admin.dto.AdminUserSearchDTO;
import it.korea.app_boot.user.entity.UserEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UserSearchSpecification implements Specification<UserEntity>{

    private AdminUserSearchDTO searchDTO;

    public UserSearchSpecification(AdminUserSearchDTO searchDTO) {
        this.searchDTO = searchDTO;
    }

    /*
     * root:  비교대상 > entity >> jpa 가 만들어서 넣어줌 
     * query:  sql 조작 
     * cb  : where 조건 
     */
    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
         List<Predicate> predicates = new ArrayList<>();

         if(StringUtils.isNotBlank(searchDTO.getSearchText())) {
            String likeText = "%" + searchDTO.getSearchText() +"%";

            Predicate userIdrPredicate = cb.like(root.get("userId"), likeText);
            Predicate userNamePredicate = cb.like(root.get("userName"), likeText);
            predicates.add(cb.or(userIdrPredicate, userNamePredicate));
         }


        if(StringUtils.isNotBlank(searchDTO.getDelYn()) &&
             !searchDTO.getDelYn().equalsIgnoreCase("ALL")) {
                
            predicates.add(cb.equal(root.get("delYn"), searchDTO.getDelYn()));
        }
         
        return andTogether(predicates, cb);
    }

    private Predicate andTogether(List<Predicate> predicates,  CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    
}
