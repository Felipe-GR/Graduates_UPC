package co.dtech.graduates.services;

import co.dtech.graduates.model.Notifications;
import co.dtech.graduates.repositories.NotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class NotificationsService {

    @Autowired
    NotificationsRepository notificationsRepository;

    public void createNewNotification(Integer fromUserID, Integer toUserID, Integer postID, Integer notificationType) {
        Notifications notification = new Notifications();
        notification.setNotifiedByUser(fromUserID);
        notification.setUserId(toUserID);
        notification.setPostId(postID);
        // Notification type == 1 => INTEREST
        if (notificationType == 1) {
            notification.setInterest((byte) 1);
            notification.setComment((byte) 0);
            notification.setJobapply((byte) 0);
        }
        // Notification type == 2 => COMMENT
        else if (notificationType == 2) {
            notification.setInterest((byte) 0);
            notification.setComment((byte) 1);
            notification.setJobapply((byte) 0);
        }
        // Notification type == 3 => JOB APPLY
        else if (notificationType == 3) {
            notification.setInterest((byte) 0);
            notification.setComment((byte) 0);
            notification.setJobapply((byte) 1);
        }
        Date date = new java.util.Date();
        Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
        notification.setTimestamp(sqlDate);
        notificationsRepository.save(notification);
    }

    public List<Notifications> getNotifications(Integer userID) {
        // Bring the 20 most recent notifications
        return notificationsRepository.getAllByUserIdOrderByTimestampDesc(new PageRequest(0, 20), userID);
    }

}
