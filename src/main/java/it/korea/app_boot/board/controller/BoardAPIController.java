package it.korea.app_boot.board.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.korea.app_boot.board.dto.BoardSearchDTO;
import it.korea.app_boot.board.service.BoardService;
import lombok.RequiredArgsConstructor;


//return 이 view 아닌 data 
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardAPIController {

    private final BoardService service;

    @GetMapping("/board/list")
    public ResponseEntity<Map<String, Object>> getBoardListData(BoardSearchDTO searchDTo) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        try{

            resultMap = service.getBoardList(searchDTo);

        }catch(Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        }

        // HttpServletResponse + HttpStatus 결합 객체 
        return new ResponseEntity<>(resultMap, status);
    }
}
