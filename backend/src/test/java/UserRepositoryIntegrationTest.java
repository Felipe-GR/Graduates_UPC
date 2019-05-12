import co.dtech.graduates.Application;
import co.dtech.graduates.model.User;
import co.dtech.graduates.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = Application.class)
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    public void whenEmailExist_thenReturnTrue() {
        //When
        boolean emailFound = userService.emailExist("admin@admin");
        //Then
        assertThat(emailFound).isEqualTo(true);
    }

    @Test
    public void whenUserExist_thenReturnUser() {
        //Given
        User testUser = new User();
        testUser.setEmail("test1@test");
        testUser.setName("John");
        //When
        User userFound = userService.returnUser(testUser.getEmail());
        //Then
        assertThat(userFound.getName()).isEqualTo(testUser.getName());
    }

    @Test
    public void whenIdExist_thenReturnUser() {
        //Given
        User testUser = new User();
        testUser.setId(1);
        testUser.setName("Drunk");
        //When
        User userFound = userService.returnUserByID(testUser.getId());
        //Then
        assertThat(userFound.getName()).isEqualTo(testUser.getName());
    }

    @Test
    public void whenUserIsStore_thenReturnUser() {
        //Given
        User testUser = new User();
        testUser.setId(11);
        testUser.setEmail("test9@test");
        testUser.setPassword("123");
        testUser.setName("Wanda");
        testUser.setSurname("Maximoff");
        testUser.setPhoneNumber("123");
        testUser.setProfilePicture("WdebFZEB551qUPDcChtR");
        testUser.setCity("Los Angeles, CA");
        testUser.setProfession("Software application developer");
        testUser.setCompany("Stark Industries");
        testUser.setEducation("Sorcery");
        testUser.setPublicPhoneNumber((byte) 1);
        testUser.setPublicCity((byte) 1);
        testUser.setPublicProfession((byte) 1);
        testUser.setPublicCompany((byte) 1);
        testUser.setPublicEducation((byte) 1);
        testUser.setIsAdmin((byte) 0);
        userService.storeUser(testUser);
        //When
        User foundUser = userService.returnUserByID(11);
        //Then
        assertThat(foundUser.getName()).isEqualTo(testUser.getName());
    }

}