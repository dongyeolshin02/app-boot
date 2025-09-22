package it.korea.app_boot.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.korea.app_boot.admin.dto.AdminUserDTO;
import it.korea.app_boot.admin.dto.AdminUserProjection;
import it.korea.app_boot.admin.dto.AdminUserSearchDTO;
import it.korea.app_boot.common.dto.PageVO;
import it.korea.app_boot.user.entity.UserEntity;
import it.korea.app_boot.user.entity.UserRoleEntity;
import it.korea.app_boot.user.repository.UserRepository;
import it.korea.app_boot.user.repository.UserRoleRepository;
import it.korea.app_boot.user.repository.UserSearchSpecification;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Map<String, Object> getUserList(Pageable pageable) throws Exception {
         Map<String, Object> resultMap = new HashMap<>();

         Page<UserEntity> pageList = 
                userRepository.findAll(pageable);

         
         List<AdminUserDTO> list = pageList.getContent()
                    .stream().map(AdminUserDTO::of).toList();

        PageVO pageVO = new PageVO();
        pageVO.setData(pageList.getNumber(), (int)pageList.getTotalElements());

        resultMap.put("total", pageList.getTotalElements());
        resultMap.put("content", list);
        resultMap.put("pageHTML", pageVO.pageHTML());
        resultMap.put("page", pageList.getNumber());

         return resultMap;
    }

    @Transactional
    public Map<String, Object> getUserList(Pageable pageable, AdminUserSearchDTO searchDTO) throws Exception {
         Map<String, Object> resultMap = new HashMap<>();

         Page<UserEntity> pageList =  null;

         UserSearchSpecification userSearchSpecification = new UserSearchSpecification(searchDTO);
       
          pageList = userRepository.findAll(userSearchSpecification, pageable);
         

            
         List<AdminUserDTO> list = pageList.getContent()
                    .stream().map(AdminUserDTO::of).toList();

        PageVO pageVO = new PageVO();
        pageVO.setData(pageList.getNumber(), (int)pageList.getTotalElements());

        resultMap.put("total", pageList.getTotalElements());
        resultMap.put("content", list);
        resultMap.put("pageHTML", pageVO.pageHTML());
        resultMap.put("page", pageList.getNumber());

         return resultMap;
    }


    @Transactional
     public  AdminUserDTO getUser(String userId) throws Exception {
         AdminUserProjection user = userRepository
                .getUserById(userId).orElseThrow(() -> new RuntimeException("사용자없음"));
      
         return AdminUserDTO.of(user);
     }


     @Transactional
     public Map<String, Object> createUser(AdminUserDTO dto) throws Exception {

          UserEntity checkUser =  userRepository.findById(dto.getUserId()).orElse(null);

          if(checkUser != null) {
               throw new RuntimeException("해당 사용자가 존재함");
          }

          UserEntity user =  AdminUserDTO.to(dto);
          String encodePasswd =  bCryptPasswordEncoder.encode(dto.getPasswd());
          user.setPasswd(encodePasswd);

          //사용자 권한 가져오기
          UserRoleEntity roleEntity =  
               userRoleRepository
                    .findById(dto.getUserRole()).orElseThrow(() -> new ClassNotFoundException("권한없음"));

          user.setRole(roleEntity);
          //사용자 등록 
          userRepository.save(user);

          Map<String, Object> resultMap = new HashMap<>();
          resultMap.put("resultCode", 200);
          resultMap.put("resultMsg", "OK");

          return resultMap;
     }



      @Transactional
     public Map<String, Object> updateUser(AdminUserDTO dto) throws Exception {

          UserEntity user =  
              userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ClassNotFoundException("사용자 없음"));


          //변경할 패스워드가 있다며 암호화 처리 
          if(StringUtils.isNotBlank(dto.getPasswd())){
               String encodePasswd =  bCryptPasswordEncoder.encode(dto.getPasswd());
               user.setPasswd(encodePasswd);
          }

          
          user.setUserName(dto.getUserName());
          user.setPhone(dto.getPhone());
          user.setEmail((dto.getEmail()));
          user.setAddr(dto.getAddr());
          user.setAddrDetail(dto.getAddrDetail() != null? 
                             dto.getAddrDetail() : user.getAddrDetail() );

          //사용자 권한 가져오기
          UserRoleEntity roleEntity =  
               userRoleRepository
                    .findById(dto.getUserRole())
                    .orElseThrow(() -> new ClassNotFoundException("권한없음"));

          user.setRole(roleEntity);
          //사용자 수정  
          userRepository.save(user);

          Map<String, Object> resultMap = new HashMap<>();
          resultMap.put("resultCode", 200);
          resultMap.put("resultMsg", "OK");

          return resultMap;
     }


     /**
      * 회원 삭제
      * @param userId
      * @return
      * @throws Exception
      */
      public Map<String, Object> deleteUser(String userId) throws Exception {

          UserEntity user =  
              userRepository.findById(userId)
                .orElseThrow(() -> new ClassNotFoundException("사용자 없음"));

          user.setDelYn("Y");
          userRepository.save(user);

          Map<String, Object> resultMap = new HashMap<>();
          resultMap.put("resultCode", 200);
          resultMap.put("resultMsg", "OK");
          return resultMap;
      }

}
