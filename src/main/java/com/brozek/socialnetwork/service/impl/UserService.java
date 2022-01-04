package com.brozek.socialnetwork.service.impl;

import com.brozek.socialnetwork.dos.IUserDO;
import com.brozek.socialnetwork.dos.impl.UserDO;
import com.brozek.socialnetwork.repository.IUserRepository;
import com.brozek.socialnetwork.service.IAuthenticationService;
import com.brozek.socialnetwork.service.IUserService;
import com.brozek.socialnetwork.validation.exception.TakenEmailException;
import com.brozek.socialnetwork.vos.IUserVO;
import com.brozek.socialnetwork.vos.impl.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final PasswordEncoder passwordEncoder;

    private final IUserRepository userRepository;

    private final IAuthenticationService authenticationService;

    @Override
    public Set<IUserVO> getAllUsers() {
        String userEmail = authenticationService.getUserEmail();

        var usersDB = userRepository.getAllUsers();
        if (usersDB.size() == 0){
            log.info("Neexistuje zadny uzivatel.");
        }

        Set<IUserVO> users = new HashSet<>();
        for (IUserDO userDO: usersDB){
            IUserVO user = new UserVO(
                    userDO.getEmail(),
                    userDO.getPassword(),
                    userDO.getFirstName(),
                    userDO.getSurname(),
                    IUserVO.EnumUserRole.user
            );
            users.add(user);
        }

        return users;
    }

    @Override
    public IUserVO findUserByEmail(String email) {
        var userDO = userRepository.findUserWithRoleByEmail(email);
        if (userDO == null){
            log.info("User email was not found {}.", email);
            return null;
        }

        return new UserVO(
                userDO.getEmail(),
                userDO.getPassword(),
                userDO.getFirstName(),
                userDO.getSurname(),
                IUserVO.EnumUserRole.valueOf(userDO.getRole().name())
        );
    }

    @Override
    @Transactional
    public boolean createUser(String email, String password, String firstName, String surname) throws TakenEmailException {
        //TODO VALIDATE
        IUserDO userDO = new UserDO(email,passwordEncoder.encode(password),firstName,surname);

        if (userRepository.isEmailTaken(userDO.getEmail())){
            throw new TakenEmailException("Email is taken");
        }

        if (userRepository.registerUser(userDO) != 1){
            throw  new RuntimeException("Error while creating new user");
        }

        return true;
    }
}
