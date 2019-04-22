package co.dtech.graduates.services;

import co.dtech.graduates.dto.ListUsers;
import co.dtech.graduates.dto.UserBasicInfo;
import co.dtech.graduates.model.User;
import co.dtech.graduates.model.UserNetwork;
import co.dtech.graduates.repositories.UserNetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserNetworkService {

    @Autowired
    UserNetworkRepository userNetworkRepository;
    @Autowired
    UserService userService;
    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    ChatService chatService;

    public ListUsers getConnectedUsers(int userID) {
        List<UserNetwork> allConnections = userNetworkRepository.findByUser1AndIsAcceptedOrUser2AndIsAccepted(userID, (byte) 1, userID, (byte) 1);
        ArrayList<UserBasicInfo> tempList = new ArrayList<UserBasicInfo>();
        for (UserNetwork element : allConnections) {
            String targetUserID;
            if (element.getUser1() == userID) {
                targetUserID = Integer.toString(element.getUser2());
            } else {
                targetUserID = Integer.toString(element.getUser1());
            }
            User targetUser = userService.returnUserByID(Integer.parseInt(targetUserID));
            UserBasicInfo targetUserInfo = new UserBasicInfo();
            targetUserInfo.id = targetUserID;
            targetUserInfo.name = targetUser.getName();
            targetUserInfo.surname = targetUser.getSurname();
            targetUserInfo.image = imageStorageService.getImage(targetUser.getProfilePicture());
            tempList.add(targetUserInfo);
        }
        String totalResults = Integer.toString(tempList.size());
        ListUsers results = new ListUsers();
        results.list = tempList;
        results.numberOfResults = totalResults;
        return results;
    }

    public List<Integer> getConnectedUsersIDsOnly(int userID) {
        List<UserNetwork> allConnections = userNetworkRepository.findByUser1AndIsAcceptedOrUser2AndIsAccepted(userID, (byte) 1, userID, (byte) 1);
        ArrayList<Integer> resultList = new ArrayList<Integer>();
        for (UserNetwork element : allConnections) {
            if (element.getUser1() == userID) {
                resultList.add(element.getUser2());
            } else {
                resultList.add(element.getUser1());
            }
        }
        return resultList;
    }

    public ListUsers getPendingRequests(int userID) {
        List<UserNetwork> pendingConnections = userNetworkRepository.findByUser2AndIsAccepted(userID, (byte) 0);
        ArrayList<UserBasicInfo> tempList = new ArrayList<UserBasicInfo>();
        for (UserNetwork element : pendingConnections) {
            String targetUserID;
            targetUserID = Integer.toString(element.getUser1());
            User targetUser = userService.returnUserByID(Integer.parseInt(targetUserID));
            UserBasicInfo targetUserInfo = new UserBasicInfo();
            targetUserInfo.id = targetUserID;
            targetUserInfo.name = targetUser.getName();
            targetUserInfo.surname = targetUser.getSurname();
            targetUserInfo.image = imageStorageService.getImage(targetUser.getProfilePicture());
            tempList.add(targetUserInfo);
        }
        String totalResults = Integer.toString(tempList.size());
        ListUsers results = new ListUsers();
        results.list = tempList;
        results.numberOfResults = totalResults;
        return results;
    }

    public boolean checkIfConnected(int userID1, int userID2) {
        UserNetwork userNetwork = userNetworkRepository.findByUser1AndUser2AndIsAccepted(userID1, userID2, (byte) 1);
        if (userNetwork != null) {
            return true;
        }
        userNetwork = userNetworkRepository.findByUser1AndUser2AndIsAccepted(userID2, userID1, (byte) 1);
        if (userNetwork != null) {
            return true;
        }
        return false;
    }

    public UserNetwork returnConnection(int userID1, int userID2) {
        UserNetwork userNetwork = userNetworkRepository.findByUser1AndUser2(userID1, userID2);
        if (userNetwork != null) {
            return userNetwork;
        }
        userNetwork = userNetworkRepository.findByUser1AndUser2(userID2, userID1);
        return userNetwork;
    }

    public void sendConnectRequest(int senderID, int receiverID) {
        UserNetwork newConnection = new UserNetwork();
        newConnection.setUser1(senderID);
        newConnection.setUser2(receiverID);
        newConnection.setIsAccepted((byte) 0);
        userNetworkRepository.save(newConnection);
    }

    public boolean acceptConnectRequest(int senderID, int receiverID) {
        UserNetwork connection = userNetworkRepository.findByUser1AndUser2AndIsAccepted(senderID, receiverID, (byte) 0);
        if (connection != null) {
            connection.setIsAccepted((byte) 1);
            userNetworkRepository.save(connection);
            chatService.createNewChat(senderID, receiverID);
            return true;
        } else {
            return false;
        }
    }

    public boolean declineConnectRequest(int senderID, int receiverID) {
        UserNetwork connection = userNetworkRepository.findByUser1AndUser2AndIsAccepted(senderID, receiverID, (byte) 0);
        if (connection != null) {
            userNetworkRepository.delete(connection);
            return true;
        } else {
            return false;
        }
    }

}
