package org.waterwood.waterfunservice.service.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.waterwood.waterfunservice.DTO.common.ResponseCode;
import org.waterwood.waterfunservice.entity.permission.Permission;
import org.waterwood.waterfunservice.entity.permission.UserPermission;
import org.waterwood.waterfunservice.entity.user.User;
import org.waterwood.waterfunservice.repository.UserPermRepo;
import org.waterwood.waterfunservice.service.dto.OpResult;

import java.util.List;

@Service
public class UserPermissionService {
    @Autowired
    private UserPermRepo userPermRepo;
    @Autowired
    private PermissionService permService;
    @Autowired
    private UserService userService;

    public List<Permission> getUserPermissions(long userId){
        return userPermRepo.findByUserId(userId).stream().map(
                UserPermission::getPermission
        ).toList();
    }

    public List<User> getPermissionUsers(int permId){
        return userPermRepo.findByPermissionId(permId).stream().map(
                UserPermission::getUser
        ).toList();
    }

    @Transactional
    public OpResult<Void> addUserPermission(long userId,int permId){
        return userService.getUserById(userId).map(user->
                permService.getPermission(permId).map(perm->{
                    UserPermission userPermission = new UserPermission();
                    userPermission.setUser(user);
                    userPermission.setPermission(perm);
                    userPermRepo.save(userPermission);
                    return OpResult.success();
                }).orElse(OpResult.failure(ResponseCode.PERMISSION_NOT_FOUND)))
                .orElse(OpResult.failure(ResponseCode.USER_NOT_FOUND));
    }

    @Transactional
    public OpResult<Void> removeUserPermission(long userId,int permId){
        return userService.getUserById(userId).map(user->
                        permService.getPermission(permId).map(perm->{
                            if(!userPermRepo.existsByUserIdAndPermissionId(userId,permId)){
                                return OpResult.failure(ResponseCode.PERMISSION_NOT_FOUND,
                                        "User ID "+userId +"doesn't have permission ID" + permId);
                            }
                            userPermRepo.deleteByUserIdAndPermissionId(userId,permId);
                            return OpResult.success();
                        }).orElse(OpResult.failure(ResponseCode.PERMISSION_NOT_FOUND)))
                .orElse(OpResult.failure(ResponseCode.USER_NOT_FOUND));
    }
}
