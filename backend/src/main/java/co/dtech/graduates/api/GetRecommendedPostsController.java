package co.dtech.graduates.api;

import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.model.Post;
import co.dtech.graduates.model.PostComment;
import co.dtech.graduates.model.PostInterest;
import co.dtech.graduates.repositories.PostCommentRepository;
import co.dtech.graduates.repositories.PostInterestRepository;
import co.dtech.graduates.repositories.PostRepository;
import co.dtech.graduates.services.AuthRequestService;
import co.dtech.graduates.services.PostService;
import co.dtech.graduates.services.UserNetworkService;
import co.dtech.graduates.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
public class GetRecommendedPostsController {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostCommentRepository postCommentRepository;
    @Autowired
    PostInterestRepository postInterestRepository;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;

    // Inner class
    public class UserActivityContainer implements Comparable<UserActivityContainer> {
        int id;
        byte interests[];
        byte comments[];
        double distance;

        public UserActivityContainer(int id, int size) {
            this.id = id;
            interests = new byte[size];
            comments = new byte[size];
            for (int i = 0; i < size; i++) {
                interests[i] = 0;
                comments[i] = 0;
            }
        }

        @Override
        public int compareTo(UserActivityContainer o) {
            return Double.compare(distance, o.distance);
        }
    }

    @PostMapping(path = "/recommendedposts")
    public ResponseEntity<Object> exportUsers(@RequestBody String jsonUsers, HttpSession session) {

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonUsers);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");

            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);

            //Authenticate request
            if (!authRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Set<Integer> set = userService.getAllNotConnectedUsers(Integer.parseInt(userIdentifiers.id));

            List<Post> posts = postService.getAllPublicPosts();
            Map<Integer, Integer> interestsDict = new HashMap<Integer, Integer>();
            Map<Integer, Integer> commentsDict = new HashMap<Integer, Integer>();
            int counter = 0;
            for (Post post : posts) {
                interestsDict.put(post.getId(), counter);
                commentsDict.put(post.getId(), counter);
                counter++;
            }

            byte[] userInterests = new byte[posts.size()];
            byte[] userComments = new byte[posts.size()];
            for (int i = 0; i < posts.size(); i++) {
                userInterests[i] = 0;
                userComments[i] = 0;
            }

            // First update for the user who sent the query
            updateZerosToOnes(Integer.parseInt(userIdentifiers.id), userInterests, interestsDict, userComments, commentsDict);

            UserActivityContainer users[] = new UserActivityContainer[set.size()];
            counter = 0;
            // For every user
            for (Integer id : set) {
                users[counter] = new UserActivityContainer(id, posts.size());
                System.err.println(id);
                // Then, update for every other user (who is not connected with the current user)
                updateZerosToOnes(id, users[counter].interests, interestsDict, users[counter].comments, commentsDict);
                // Calculate distance
                users[counter].distance = distance(userInterests, userComments, users[counter].interests, users[counter].comments);
                counter++;
            }

            // Sort by distance
            Arrays.sort(users);

            List<Integer> userList = new ArrayList<>();
            for (UserActivityContainer userActivityContainer : users) {
                userList.add(userActivityContainer.id);
            }
            List<Post> returnValue = new ArrayList<>();
            for (Integer id : userList) {
                List<Post> list = postRepository.findTop3ByUserIdAndIsPublicOrderByTimestampDesc(id, (byte) 1);
                returnValue.addAll(list);
            }
            return new ResponseEntity<>(returnValue, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private void updateZerosToOnes(int userId, byte userInterests[], Map<Integer, Integer> interestsDict, byte userComments[], Map<Integer, Integer> commentsDict) {
        // Interests (user)
        List<PostInterest> interests = postInterestRepository.findAllByUserId(userId);
        for (PostInterest postInterest : interests) {
            int postId = postInterest.getPostId();
            Integer position = interestsDict.get(postId);
            if (position != null) {
                userInterests[position] = 1;
            }
        }

        // Comments (user)
        List<PostComment> comments = postCommentRepository.findByUserId(userId);
        for (PostComment postComment : comments) {
            int postId = postComment.getPostId();
            Integer position = commentsDict.get(postId);
            if (position != null) {
                userComments[position] = 1;
            }
        }
    }

    private double distance(byte user1Interests[], byte user1Comments[], byte user2Interests[], byte user2Comments[]) {
        int interestsDistance = 0;
        double interestsWeight = 1;
        int commentsDistance = 0;
        double commentsWeight = 1;
        for (int i = 0; i < user1Comments.length; i++) {
            interestsDistance += user1Interests[i] ^ user2Interests[i];
            commentsDistance += user1Comments[i] ^ user2Comments[i];
        }
        return interestsDistance * interestsWeight + commentsDistance * commentsWeight;
    }
}
