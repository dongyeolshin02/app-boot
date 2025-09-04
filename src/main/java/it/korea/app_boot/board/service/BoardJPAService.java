package it.korea.app_boot.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.korea.app_boot.board.dto.BoardDTO;
import it.korea.app_boot.board.dto.BoardDTO.Response;
import it.korea.app_boot.board.entity.BoardEntity;
import it.korea.app_boot.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardJPAService {

    private final BoardRepository boardRepository;

    public Map<String, Object> getBoardList(Pageable pageable) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        
        //findAll() -> select * from board;
        Page<BoardEntity> pageObj = boardRepository.findAll(pageable);

        // List of Entity ==> List of DTO 
        List<BoardDTO.Response> list  =
             //pageObj.ge JDK 16부터 가능tContent().stream().map(BoardDTO.Response::of).toList(); //toList() 는 불변 객체 출력  
            pageObj.getContent()
                 .stream()
                 .map(BoardDTO.Response::of)
                 .collect( Collectors.toList() );  //가변 객체 
        
        
        resultMap.put("total", pageObj.getTotalElements());
        resultMap.put("contents", list);

        return resultMap;
    }
}