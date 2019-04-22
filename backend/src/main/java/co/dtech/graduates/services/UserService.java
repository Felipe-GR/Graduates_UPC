package co.dtech.graduates.services;

import co.dtech.graduates.dto.ListUsers;
import co.dtech.graduates.dto.UserBasicInfo;
import co.dtech.graduates.dto.UserInfo;
import co.dtech.graduates.model.User;
import co.dtech.graduates.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageStorageService imageStorageService;

    @Autowired
    private UserNetworkService userNetworkService;

    public boolean emailExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    public User returnUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user;
        }
        return null;
    }

    public User returnUserByID(Integer id) {
        User user = userRepository.findById(id);
        if (user != null) {
            return user;
        }
        return null;
    }

    public boolean authenticate(String email, String password) {
        System.err.println(email);
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) { // TODO hash klp
            return true;
        }
        return false;
    }

    public void storeUser(User user) {
        userRepository.save(user);
    }

    public ListUsers searchUsers(String queryName) {
        queryName = queryName.trim();
        String words[] = queryName.split("\\s+");
        List<User> list;
        if (words.length == 2) {
            list = userRepository.findByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(words[0], words[1]);
        } else {
            list = userRepository.findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(queryName, queryName);
        }
        ArrayList<UserBasicInfo> resultList = new ArrayList<UserBasicInfo>();
        for (User element : list) {
            UserBasicInfo userSearchResult = new UserBasicInfo();
            userSearchResult.id = Integer.toString(element.getId());
            userSearchResult.name = element.getName();
            userSearchResult.surname = element.getSurname();
            userSearchResult.image = imageStorageService.getImage(element.getProfilePicture());
            resultList.add(userSearchResult);
        }
        String totalResults = Integer.toString(resultList.size());
        ListUsers listUsers = new ListUsers();
        listUsers.list = resultList;
        listUsers.numberOfResults = totalResults;
        return listUsers;
    }

    // Return all the information of a user (private/public)
    public UserInfo getUserInfo(String userID) {
        UserInfo userInfo = new UserInfo();
        User user = returnUserByID(Integer.parseInt(userID));
        userInfo.phoneNumber = user.getPhoneNumber();
        userInfo.isPhonePublic = user.getPublicPhoneNumber();
        userInfo.city = user.getCity();
        userInfo.isCityPublic = user.getPublicCity();
        userInfo.profession = user.getProfession();
        userInfo.isProfessionPublic = user.getPublicProfession();
        userInfo.company = user.getCompany();
        userInfo.isCompanyPublic = user.getPublicCompany();
        userInfo.education = user.getEducation();
        userInfo.isEducationPublic = user.getPublicEducation();
        return userInfo;
    }

    // Returns only public information
    public UserInfo getPublicUserInfo(String userID) {
        UserInfo userInfo = new UserInfo();
        User user = returnUserByID(Integer.parseInt(userID));
        userInfo.isPhonePublic = user.getPublicPhoneNumber();
        if (userInfo.isPhonePublic == 1) {
            userInfo.phoneNumber = user.getPhoneNumber();
        } else {
            userInfo.phoneNumber = "";
        }
        userInfo.isCityPublic = user.getPublicCity();
        if (userInfo.isCityPublic == 1) {
            userInfo.city = user.getCity();
        } else {
            userInfo.city = "";
        }
        userInfo.isProfessionPublic = user.getPublicProfession();
        if (userInfo.isProfessionPublic == 1) {
            userInfo.profession = user.getProfession();
        } else {
            userInfo.profession = "";
        }
        userInfo.isCompanyPublic = user.getPublicCompany();
        if (userInfo.isCompanyPublic == 1) {
            userInfo.company = user.getCompany();
        } else {
            userInfo.company = "";
        }
        userInfo.isEducationPublic = user.getPublicEducation();
        if (userInfo.isEducationPublic == 1) {
            userInfo.education = user.getEducation();
        } else {
            userInfo.education = "";
        }
        return userInfo;
    }

    public List listAllUsers() {
        List<User> allUsers = userRepository.findAllByIsAdmin((byte) 0);
        ArrayList<UserBasicInfo> tempList = new ArrayList<UserBasicInfo>();
        for (User element : allUsers) {
            UserBasicInfo targetUserInfo = new UserBasicInfo();
            targetUserInfo.id = Integer.toString(element.getId());
            targetUserInfo.name = element.getName();
            targetUserInfo.surname = element.getSurname();
            targetUserInfo.image = imageStorageService.getImage(element.getProfilePicture());
            tempList.add(targetUserInfo);
        }
        return tempList;
    }

    public Set<Integer> getAllNotConnectedUsers(int userID) {
        List<User> allUsers = userRepository.findAllByIsAdmin((byte) 0);
        Set<Integer> set = new HashSet<Integer>();
        for (User user : allUsers) {
            set.add(user.getId());
        }
        List<Integer> connectedUsers = userNetworkService.getConnectedUsersIDsOnly(userID);
        for (Integer id : connectedUsers) {
            set.remove(id);
        }
        set.remove(userID);
        return set;
    }
}
