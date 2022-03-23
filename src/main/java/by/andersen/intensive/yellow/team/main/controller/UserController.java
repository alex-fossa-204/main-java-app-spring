package by.andersen.intensive.yellow.team.main.controller;

import by.andersen.intensive.yellow.team.main.controller.dto.ResponseUser;
import by.andersen.intensive.yellow.team.main.controller.http.HttpResponse;
import by.andersen.intensive.yellow.team.main.model.entity.User;
import by.andersen.intensive.yellow.team.main.security.details.UserPrincipal;
import by.andersen.intensive.yellow.team.main.security.token.JWTTokenProvider;
import by.andersen.intensive.yellow.team.main.service.exception.ServiceException;
import by.andersen.intensive.yellow.team.main.service.exception.impl.user.EmailExistException;
import by.andersen.intensive.yellow.team.main.service.exception.impl.user.NotAnImageFileException;
import by.andersen.intensive.yellow.team.main.service.exception.impl.user.UserNotFoundException;
import by.andersen.intensive.yellow.team.main.service.exception.impl.user.UsernameExistException;
import by.andersen.intensive.yellow.team.main.service.impl.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static by.andersen.intensive.yellow.team.main.security.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static by.andersen.intensive.yellow.team.main.service.exception.message.MessageConstant.PASSWORD_EMAIL_SENT;
import static by.andersen.intensive.yellow.team.main.service.exception.message.MessageConstant.USER_DELETED_SUCCESSFULLY;
import static by.andersen.intensive.yellow.team.main.service.impl.UserService.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping(path = {"/", "/user"})
public class UserController extends ErrorControllerHandler {

    private UserService userDetailsService;

    private AuthenticationManager authenticationManager;

    private JWTTokenProvider jwtTokenProvider;

    public UserController(UserService userDetailsService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseUser> login(@RequestBody User user) throws UserNotFoundException {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userDetailsService.findByUserName(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(new ResponseUser(loginUser), jwtHeader, OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseUser> register(@RequestBody User user) throws UserNotFoundException, EmailExistException, UsernameExistException {
        User newUser = userDetailsService.register(user.getUsername(), user.getLastName(), user.getFirstName(), user.getEmailAddress(), user.getPassword());
        return new ResponseEntity<>(new ResponseUser(newUser), OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseUser> addNewUser(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("username") String username, @RequestParam("emailAddress") String email, @RequestParam("role") String role, @RequestParam("active") String isActive, @RequestParam("nonLocked") String isNonLocked, @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException, MessagingException, NotAnImageFileException {

        User newUser = userDetailsService.addNewUser(username, firstName, lastName, email, Boolean.getBoolean(isNonLocked), Boolean.getBoolean(isActive), role, profileImage);
        return new ResponseEntity<>(new ResponseUser(newUser), OK);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseUser> updateUser(@RequestParam("currentUserName") String currentUserName,
                                                   @RequestParam("firstName") String firstName,
                                                   @RequestParam("lastName") String lastName,
                                                   @RequestParam("username") String username,
                                                   @RequestParam("emailAddress") String email,
                                                   @RequestParam("role") String role,
                                                   @RequestParam("active") String isActive,
                                                   @RequestParam("nonLocked") String isNonLocked,
                                                   @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException, NotAnImageFileException {
        User updateUser = userDetailsService.updateUser(currentUserName, firstName, lastName, username, email, role, Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
        return new ResponseEntity<>(new ResponseUser(updateUser), OK);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("username") String username) throws UserNotFoundException {
        User user = userDetailsService.findByUserName(username);
        ResponseUser responseUser = new ResponseUser(user);
        return new ResponseEntity<>(responseUser, OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ResponseUser>> getAllUsers() throws ServiceException {
        List<User> users = userDetailsService.getUsers();
        return new ResponseEntity<>(convertToResponseUserList(users), OK);
    }

    @GetMapping("/reset/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) throws MessagingException, UserNotFoundException, EmailExistException {
        System.out.println(email);
        userDetailsService.resetPassword(email);
        return response(OK, String.format("%s : %s", PASSWORD_EMAIL_SENT, email));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) throws IOException, UserNotFoundException, UsernameExistException {
        userDetailsService.deleteUser(username);
        return response(OK, String.format("%s : %s", USER_DELETED_SUCCESSFULLY, username));
    }

    @PostMapping("/update-profile-image")
    public ResponseEntity<ResponseUser> updateProfileImage(@RequestParam("userName") String userName, @RequestParam(value = "profileImage") MultipartFile profileImage) throws UserNotFoundException, EmailExistException, IOException, UsernameExistException, NotAnImageFileException {
        User updateUserImage = userDetailsService.updateProfileImage(userName, profileImage);
        return new ResponseEntity<>(new ResponseUser(updateUserImage), OK);
    }

    @GetMapping(path = "/image/{userName}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("userName") String userName, @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + userName + FORWARD_SLASH + fileName));
    }

    @GetMapping(path = "/image/profile/{userName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("userName") String userName) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + userName);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while ((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private List<ResponseUser> convertToResponseUserList(List<User> users) {
        List<ResponseUser> responseUsers = new ArrayList<>();
        users.forEach(user -> responseUsers.add(new ResponseUser(user)));
        return responseUsers;
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase()), httpStatus);
    }

}