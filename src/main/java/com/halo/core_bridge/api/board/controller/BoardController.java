package com.halo.core_bridge.api.board.controller;

import com.halo.core_bridge.api.board.model.dto.BoardDto;
import com.halo.core_bridge.api.board.model.entity.Board;
import com.halo.core_bridge.api.board.service.BoardService;
import com.halo.core_bridge.common.model.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "board-controller", description = "게시판 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    /** 전체 게시글 조회: GET /api/board */
    @Operation(summary = "전체 게시글 조회")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping
    public ResponseEntity<BaseResponse<List<Board>>> getBoards() {
        List<Board> boards = boardService.findAll();
        return ResponseEntity.ok(BaseResponse.success(boards));
    }

    /** 특정 게시글 조회: GET /api/board/{id} */
    @Operation(summary = "특정 게시글 조회")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<BoardDto.Read>> getBoard(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long id) {
        BoardDto.Read findBoard = boardService.findById(id);
        return ResponseEntity.ok(BaseResponse.success(findBoard));
    }

    /** 게시글 생성: POST /api/board */
    @Operation(
            summary = "게시글 생성",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BoardDto.Create.class),
                            examples = @ExampleObject(
                                    name = "생성 요청 예시",
                                    value = """
                                            {
                                              "title": "첫 번째 공지",
                                              "contents": "CoreBridge 공지사항 본문입니다.",
                                              "userId": 1
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "생성 성공",
            content = @Content(schema = @Schema(implementation = Board.class)))
    @PostMapping
    public Board createBoard(@RequestBody BoardDto.Create createBoard) {
        return boardService.save(createBoard);
    }

    /** 게시글 수정: PUT /api/board/{id} */
    @Operation(
            summary = "게시글 수정",
            parameters = @Parameter(name = "id", description = "수정할 게시글 ID", required = true),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BoardDto.Update.class),
                            examples = @ExampleObject(
                                    name = "수정 요청 예시",
                                    value = """
                                            {
                                              "title": "수정된 제목",
                                              "contents": "수정된 본문 내용입니다."
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Object>> updateBoard(
            @PathVariable Long id,
            @RequestBody BoardDto.Update updateBoard) {
        boardService.updateById(id, updateBoard);
        return ResponseEntity.ok(BaseResponse.success(null));
    }

    /** 게시글 삭제: DELETE /api/board/{id} */
    @Operation(summary = "게시글 삭제")
    @ApiResponse(responseCode = "200", description = "삭제 성공")
    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardService.deleteById(id);
    }
}
