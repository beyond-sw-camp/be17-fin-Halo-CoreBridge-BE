package com.halo.core_bridge.api.board.controller;

import com.halo.core_bridge.api.board.model.dto.BoardDto;
import com.halo.core_bridge.api.board.model.entity.Board;
import com.halo.core_bridge.api.board.service.BoardService;
import com.halo.core_bridge.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    /*** 전체 게시글 조회
     * GET /api/boards */
    @GetMapping
    public ResponseEntity<BaseResponse<List<Board>>> getBoards() {

        List<Board> boards = boardService.findAll();
        return ResponseEntity.ok(BaseResponse.success(boards));

    }

    /**특정 게시글 조회
     * GET /api/boards/{id}*/
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Board>> getBoard(@PathVariable Long id) {

        Board findBoard = boardService.findById(id);
        return ResponseEntity.ok(BaseResponse.success(findBoard));

    }

    /**게시글 생성
     * POST /api/boards
     * 요청 본문(JSON)을 Board 객체로 받아 저장*/
    @PostMapping
    public Board createBoard(@RequestBody BoardDto.Create createBoard) {
        return boardService.save(createBoard);
    }

    /** 게시글 수정
     * PUT /api/boards/{id}
     * 기존 게시글을 찾아서 제목, 내용 등 변경*/
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> updateBoard(@PathVariable Long id, @RequestBody BoardDto.Update updateBoard) {
        boardService.updateById(id, updateBoard);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    /**
     * 게시글 삭제
     * DELETE /api/boards/{id} */
    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardService.deleteById(id);
    }
}
