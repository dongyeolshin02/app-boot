package it.korea.app_boot.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.korea.app_boot.admin.dto.AdminUserDTO;
import it.korea.app_boot.common.dto.PageVO;
import it.korea.app_boot.user.entity.UserEntity;
import it.korea.app_boot.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

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

}
