package com.brozek.socialnetwork.service.impl;

import com.brozek.socialnetwork.dos.IUserDO;
import com.brozek.socialnetwork.repository.IUserRepository;
import com.brozek.socialnetwork.service.IUserService;
import com.brozek.socialnetwork.vos.IUserVO;
import com.brozek.socialnetwork.vos.impl.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    @Override
    public Set<IUserVO> getAllUsers() {
        var usersDB = userRepository.getAllUsers();
        if (usersDB.size() == 0){
            log.info("Neexistuje zadny uzivatel.");
            //TODO Throw exception ? return null -> zkouska logovani
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
}
