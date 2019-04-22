package co.dtech.graduates.services;

import co.dtech.graduates.dto.CommentData;
import co.dtech.graduates.model.PostComment;
import co.dtech.graduates.model.User;
import co.dtech.graduates.repositories.PostCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostCommentService {

    @Autowired
    PostCommentRepository postCommentRepository;
    @Autowired
    UserService userService;
    @Autowired
    ImageStorageService imageStorageService;

    public void addComment(String comment, int postID, int userID) {
        PostComment postComment = new PostComment();
        Date date = new java.util.Date();
        Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
        postComment.setCommentTimestamp(sqlDate);
        postComment.setContent(comment);
        postComment.setPostId(postID);
        postComment.setUserId(userID);
        postCommentRepository.save(postComment);
    }

    public List getComments(int postID) {
        List<PostComment> comments = postCommentRepository.findAllByPostIdOrderByCommentTimestamp(postID);
        ArrayList<CommentData> commentList = new ArrayList<CommentData>();
        for (PostComment element : comments) {
            int targetUserID = element.getUserId();
            User targetUser = userService.returnUserByID(targetUserID);
            CommentData commentData = new CommentData();
            commentData.id = Integer.toString(targetUserID);
            commentData.name = targetUser.getName();
            commentData.surname = targetUser.getSurname();
            commentData.image = imageStorageService.getImage(targetUser.getProfilePicture());
            commentData.comment = element.getContent();
            commentData.timestamp = element.getCommentTimestamp();
            commentList.add(commentData);
        }
        return commentList;
    }

    public int getCommentsNumber(int postID) {
        return postCommentRepository.countAllByPostId(postID);
    }

    public List<PostComment> getAllUserComments(int userID) {
        return postCommentRepository.findByUserId(userID);
    }
}
