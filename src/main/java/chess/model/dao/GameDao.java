package chess.model.dao;

import chess.entity.GameEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class GameDao {
    private static final String INIT_TURN = "WHITE";
    private final JdbcTemplate jdbcTemplate;

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long initGame(String name, String password) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into games (name, password, turn) values (?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"game_id"});
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, INIT_TURN);
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<GameEntity> findAll() {
        String sql = "select game_id, name, password, turn from games";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    return new GameEntity(
                            resultSet.getLong("game_id"),
                            resultSet.getString("name"),
                            resultSet.getString("password"),
                            resultSet.getString("turn")
                    );
                });
    }

    public String findTurnById(Long gameId) {
        String query = "select turn from games where game_id = ?";
        return jdbcTemplate.queryForObject(query, String.class, gameId);
    }

    public String findPwdById(Long gameId) {
        String query = "select password from games where game_id = ?";
        return jdbcTemplate.queryForObject(query, String.class, gameId);
    }

    public void update(String nextTurn, Long gameId) {
        String query = "UPDATE games SET turn = (?) where game_id = ?";
        jdbcTemplate.update(query, nextTurn, gameId);
    }

    public void deleteByGameId(Long id) {
        String sql = "DELETE FROM games where game_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteAll() {
        String sql = "DELETE FROM games";
        jdbcTemplate.update(sql);
    }
}
