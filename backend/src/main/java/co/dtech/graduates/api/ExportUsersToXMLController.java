package co.dtech.graduates.api;

import co.dtech.graduates.model.PostInterest;
import co.dtech.graduates.repositories.PostCommentRepository;
import co.dtech.graduates.repositories.PostInterestRepository;
import co.dtech.graduates.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import co.dtech.graduates.dto.UserIdentifiers;
import co.dtech.graduates.model.Post;
import co.dtech.graduates.model.PostComment;
import co.dtech.graduates.model.User;
import com.linkdin.app.services.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.StringWriter;
import java.util.*;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


@RestController
public class ExportUsersToXMLController {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    PostCommentRepository postCommentRepository;
    @Autowired
    PostInterestRepository postInterestRepository;
    @Autowired
    UserNetworkService userNetworkService;
    @Autowired
    AuthRequestService authRequestService;
    @Autowired
    AdminAuthRequestService adminAuthRequestService;

    @PostMapping(path = "/exportusers")
    public ResponseEntity<Object> exportUsers(@RequestBody String jsonUsers, HttpSession session) {

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject obj = new JSONObject(jsonUsers);
        try {
            JSONObject userObj = obj.getJSONObject("userIdentifiers");
            JSONObject listObj = obj.getJSONObject("userListRequest");

            UserIdentifiers userIdentifiers = objectMapper.readValue(userObj.toString(), UserIdentifiers.class);

            //Authenticate request
            if (!adminAuthRequestService.authenticateRequest(userIdentifiers, session)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            JSONArray userList = listObj.getJSONArray("usersToExport");
            ArrayList<User> users = new ArrayList<User>(); // an arraylist to store the users to be exported
            for (int i = 0; i < userList.length(); i++) {
                String userID = userList.get(i).toString();
                User user = userService.returnUserByID(Integer.parseInt(userID));
                users.add(user);
            }

            String xml = exportUsersToXML(users);
            return new ResponseEntity<>(xml, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String exportUsersToXML(ArrayList<User> users) {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();

            Element mainRootElement = doc.createElement("users");
            doc.appendChild(mainRootElement);

            for (User user: users) {
                Element userElement = doc.createElement("user");
                userElement.setAttribute("id", Integer.toString(user.getId()));
                // Name
                Element nameElement = doc.createElement("name");
                userElement.appendChild(nameElement);
                nameElement.appendChild(doc.createTextNode(user.getName()));
                // Surname
                Element surnameElement = doc.createElement("surname");
                userElement.appendChild(surnameElement);
                surnameElement.appendChild(doc.createTextNode(user.getSurname()));
                // Company
                Element companyElement = doc.createElement("company");
                userElement.appendChild(companyElement);
                companyElement.appendChild(doc.createTextNode(user.getCompany()));
                // Education
                Element educationElement = doc.createElement("education");
                userElement.appendChild(educationElement);
                educationElement.appendChild(doc.createTextNode(user.getEducation()));
                // Email
                Element emailElement = doc.createElement("email");
                userElement.appendChild(emailElement);
                emailElement.appendChild(doc.createTextNode(user.getEmail()));
                // PhoneNumber
                Element PhoneNumberElement = doc.createElement("phonenumber");
                userElement.appendChild(PhoneNumberElement);
                PhoneNumberElement.appendChild(doc.createTextNode(user.getPhoneNumber()));
                // City
                Element cityElement = doc.createElement("city");
                userElement.appendChild(cityElement);
                cityElement.appendChild(doc.createTextNode(user.getCity()));
                // Profession
                Element professionElement = doc.createElement("profession");
                userElement.appendChild(professionElement);
                professionElement.appendChild(doc.createTextNode(user.getProfession()));

                // Posts
                List<Post> posts = postService.getAllUserPosts(user.getId());
                Element postsElement = doc.createElement("posts");
                userElement.appendChild(postsElement);
                for (Post post: posts) {
                    // Post
                    Element postElement = doc.createElement("post");
                    postsElement.appendChild(postElement);
                    // Post attributes
                    postElement.setAttribute("id", Integer.toString(post.getId()));
                    postElement.setAttribute("isAd", Integer.toString(post.getIsAdvertisment()));
                    postElement.setAttribute("isPublic", Integer.toString(post.getIsPublic()));
                    // Post Content
                    Element postContentElement = doc.createElement("content");
                    postElement.appendChild(postContentElement);
                    postContentElement.appendChild(doc.createTextNode(post.getContent()));
                    // Post Timestamp
                    Element postTimestampElement = doc.createElement("timestamp");
                    postElement.appendChild(postTimestampElement);
                    postTimestampElement.appendChild(doc.createTextNode(post.getTimestamp().toString()));
                }

                // Comments
                List<PostComment> comments = postCommentRepository.findByUserId(user.getId());

                Element commentsElement = doc.createElement("comments");
                userElement.appendChild(commentsElement);

                for (PostComment comment: comments) {
                    // Comment
                    Element commentElement = doc.createElement("comment");
                    commentsElement.appendChild(commentElement);
                    // Comment attributes
                    commentElement.setAttribute("id", Integer.toString(comment.getId()));
                    commentElement.setAttribute("postId", Integer.toString(comment.getPostId()));
                    // Comment Content
                    Element commentContentElement = doc.createElement("content");
                    commentElement.appendChild(commentContentElement);
                    commentContentElement.appendChild(doc.createTextNode(comment.getContent()));
                    // Post Timestamp
                    Element commentTimestampElement = doc.createElement("timestamp");
                    commentElement.appendChild(commentTimestampElement);
                    commentTimestampElement.appendChild(doc.createTextNode(comment.getCommentTimestamp().toString()));
                }

                // Interests
                List<PostInterest> interests = postInterestRepository.findAllByUserId(user.getId());

                Element interestsElement = doc.createElement("interests");
                userElement.appendChild(interestsElement);

                for (PostInterest interest: interests) {
                    // Interest
                    Element interestElement = doc.createElement("interest");
                    interestsElement.appendChild(interestElement);
                    // Interest attributes
                    interestElement.setAttribute("id", Integer.toString(interest.getId()));
                    interestElement.setAttribute("postId", Integer.toString(interest.getPostId()));
                }

                mainRootElement.appendChild(userElement);
            }

            // output DOM XML to file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            String outputDir = "./outputXML/";
            String outputFileName = "output.xml";
            String finalPath = outputDir + outputFileName;

            // Create output directory if it does not exist
            new File(outputDir).mkdirs();

            StreamResult sr = new StreamResult(new File(finalPath));
            StringWriter outWriter = new StringWriter();

            StreamResult resultString = new StreamResult( outWriter );

            transformer.transform(source, sr); // to filesystem
            transformer.transform(source, resultString); // to string

            StringBuffer sb = outWriter.getBuffer();
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
