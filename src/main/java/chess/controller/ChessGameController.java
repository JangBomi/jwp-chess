package chess.controller;

import chess.model.GameResult;
import chess.model.dto.MoveDto;
import chess.model.dto.WebBoardDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.Map;

@RestController
@RequestMapping("/game")
public class ChessGameController {

    private final ChessService chessService;

    public ChessGameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getGame(@PathVariable Long id) {
        WebBoardDto board = chessService.getBoardByGameId(id);
        return ResponseEntity.ok(board.getWebBoard());
    }

    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable Long id, @RequestBody String confirmPwd) {
        chessService.deleteByGameId(confirmPwd, id);
    }

    @PatchMapping("/{id}/move")
    public ResponseEntity<Map<String, String>> move(@PathVariable Long id, @RequestBody MoveDto moveCommand) {
        WebBoardDto board = chessService.move(moveCommand, id);
        return ResponseEntity.ok(board.getWebBoard());
    }

    @GetMapping("/{id}/turn")
    public ResponseEntity<String> turn(@PathVariable Long id) {
        return ResponseEntity.ok(chessService.getTurn(id));
    }

    @GetMapping("/{id}/dead")
    public ResponseEntity<Boolean> isKingDead(@PathVariable Long id) {
        return ResponseEntity.ok(chessService.isKingDead(id));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<GameResult> status(@PathVariable Long id) {
        return ResponseEntity.ok(chessService.getResult(id));
    }
}
