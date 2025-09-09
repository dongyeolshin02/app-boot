package it.korea.app_boot.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.korea.app_boot.board.dto.BoardDTO;
import it.korea.app_boot.board.entity.BoardEntity;
import it.korea.app_boot.board.entity.BoardFileEntity;
import it.korea.app_boot.board.repository.BoardRepository;
import it.korea.app_boot.common.files.FileUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardJPAService {

    private final BoardRepository boardRepository;
    private final FileUtils fileUtils;

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
        resultMap.put("content", list);

        return resultMap;
    }

     @Transactional
    public Map<String, Object> getBoard(int brdId) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();

        BoardEntity entity = 
            boardRepository.getBoard(brdId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        BoardDTO.Detail  detail = BoardDTO.Detail.of(entity);

        resultMap.put("vo", detail);

        return resultMap;
    }


     @Transactional
     public Map<String, Object> writeBoard(BoardDTO.Request request) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();

        //물리적으로 저장
        Map<String, Object> fileMap = fileUtils.uploadFiles(request.getFile(), "board");
        BoardEntity entity = new BoardEntity();
        entity.setTitle(request.getTitle());
        entity.setContents(request.getContents());
        entity.setWriter("admin");
        
        if(fileMap != null) {

          BoardFileEntity fileEntity = new BoardFileEntity();
          fileEntity.setFileName(fileMap.get("fileName").toString());
          fileEntity.setStoredName(fileMap.get("storedFileName").toString());
          fileEntity.setFilePath(fileMap.get("filePath").toString());
          fileEntity.setFileSize(request.getFile().getSize());
          entity.addFiles(fileEntity);
        }

        boardRepository.save(entity);

        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "OK");   
        
        return resultMap;
    }
}