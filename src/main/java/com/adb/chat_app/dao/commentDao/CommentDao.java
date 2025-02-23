package com.adb.chat_app.dao.commentDao;

import com.adb.chat_app.dto.CommentDto;
import com.adb.chat_app.exceptions.DAOException;
import com.adb.chat_app.models.Comment;
import com.adb.chat_app.utils.BuildSqlStatement;
import com.adb.chat_app.utils.CreateDbConnection;
import com.adb.chat_app.utils.EntityModelMapper;
import com.adb.chat_app.utils.GetSqlStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentDao implements ICommentDao{
    private static final Logger logger = LoggerFactory.getLogger(CommentDao.class);

    @Override
    public Optional<Comment> get(long ID) throws DAOException {
        return Optional.empty();
    }

    @Override
    public List<Comment> getAll() throws DAOException {
        return null;
    }

    public List<CommentDto> getPostComments(int postId) throws DAOException{
        List<CommentDto> allComments = new ArrayList<>();

        try(Connection connection = CreateDbConnection.getConnection()){
            String sqlScript = CommentSqlScriptsPath.GET_POST_COMMENTS.getPath();
            String getAllPostComment = GetSqlStatement.sqlQueryBuilder(sqlScript);

            try(PreparedStatement preparedStatement = connection.prepareStatement(getAllPostComment)){
                preparedStatement.setInt(1, postId);

                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while(resultSet.next()){
                        CommentDto commentDto = EntityModelMapper.commentMapper(resultSet);
                        allComments.add(commentDto);
                    }
                }

            } catch (SQLException e){
                logger.error("SQL query execution error: " + e.getMessage(), e);
                throw new DAOException("sql execution error", e);
            }

        } catch (SQLException e) {
            logger.error("Fail to connect to database: " + e.getMessage(), e);
            throw new DAOException("Fail to connect to database", e);
        } catch (IOException e){
            logger.error("Failed to fetch get post comment sql query -- " + e.getMessage());
            throw new DAOException("Failed to fetch get post comment sql query", e);
        }

        return allComments;
    }

    @Override
    public int save(Comment comment) throws DAOException {
       return 0;
    }

    public Optional<CommentDto> saveComment(Comment comment) throws DAOException{
        Optional<CommentDto> optionalCommentDto = Optional.empty();

        try(Connection connection = CreateDbConnection.getConnection()){
            String sqlScriptPath = CommentSqlScriptsPath.INSERT_COMMENT.getPath();
            String insertCommentQuery = GetSqlStatement.sqlQueryBuilder(sqlScriptPath);

            try(PreparedStatement preparedStatement = connection.prepareStatement(insertCommentQuery)){
                Integer userId = comment.getUserId();
                userId = (userId != 0) ? userId : null;

                BuildSqlStatement.setParameters(
                        preparedStatement,
                        comment.getPostId(),
                        comment.getComment(),
                        userId
                );

                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                     optionalCommentDto = Optional.ofNullable(EntityModelMapper.commentMapper(resultSet));
                }

                resultSet.close();
            } catch (SQLException e){
                logger.error("SQL query execution error: " + e.getMessage(), e);
                throw new DAOException("sql execution error", e);
            }
        } catch (SQLException e) {
            logger.error("Fail to connect to database: " + e.getMessage(), e);
            throw new DAOException("Fail to connect to database", e);
        } catch (IOException e){
            logger.error("Failed to fetch insert comment sql query -- " + e.getMessage());
            throw new DAOException("Failed to fetch insert comment sql query", e);
        }

        return optionalCommentDto;
    }

    @Override
    public int update(Object... params) throws DAOException {
        return 0;
    }

    @Override
    public int delete(long id) throws DAOException {
        return 0;
    }
}
