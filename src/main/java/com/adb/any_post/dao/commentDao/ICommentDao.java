package com.adb.any_post.dao.commentDao;

import com.adb.any_post.dto.CommentDto;
import com.adb.any_post.exceptions.DAOException;
import com.adb.any_post.models.Comment;

import java.util.List;
import java.util.Optional;

public interface ICommentDao {
    List<CommentDto> getPostComments(int postId) throws DAOException;
    Optional<CommentDto> saveComment(Comment comment) throws DAOException;
    int deleteComment(int commentId, int ownerId) throws DAOException;
}
