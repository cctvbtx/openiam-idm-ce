package org.openiam.idm.srvc.role.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openiam.exception.data.ObjectNotFoundException;
import org.openiam.idm.srvc.grp.domain.GroupEntity;
import org.openiam.idm.srvc.grp.dto.Group;
import org.openiam.idm.srvc.grp.service.UserGroupDAO;
import org.openiam.idm.srvc.res.service.ResourceRoleDAO;
import org.openiam.idm.srvc.role.domain.RoleAttributeEntity;
import org.openiam.idm.srvc.role.domain.RoleEmbeddableId;
import org.openiam.idm.srvc.role.domain.RoleEntity;
import org.openiam.idm.srvc.role.domain.RolePolicyEntity;
import org.openiam.idm.srvc.role.domain.UserRoleEntity;
import org.openiam.idm.srvc.role.dto.Role;
import org.openiam.idm.srvc.role.dto.RoleAttribute;
import org.openiam.idm.srvc.role.dto.RoleConstant;
import org.openiam.idm.srvc.role.dto.RoleId;
import org.openiam.idm.srvc.role.dto.RolePolicy;
import org.openiam.idm.srvc.role.dto.RoleSearch;
import org.openiam.idm.srvc.role.dto.UserRole;
import org.openiam.idm.srvc.service.dto.Service;
import org.openiam.idm.srvc.service.service.ServiceDAO;
import org.openiam.idm.srvc.user.domain.UserEntity;
import org.openiam.idm.srvc.user.dto.User;

import org.openiam.idm.srvc.user.dto.UserConstant;
import org.openiam.idm.srvc.user.service.UserDataService;

import java.util.*;

//Note: as per spec serviceName goes in impl class and name goes in interface


public class RoleDataServiceImpl implements RoleDataService {

    private RoleDAO roleDao;
    private RoleAttributeDAO roleAttributeDAO;
    private UserDataService userManager;
    private UserRoleDAO userRoleDao;
    private UserGroupDAO userGroupDao;
    private RolePolicyDAO rolePolicyDao;
    private ResourceRoleDAO resRoleDao;
    private ServiceDAO serviceDao;

    private static final Log log = LogFactory.getLog(RoleDataServiceImpl.class);

    public RoleDAO getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDAO roleDao) {
        this.roleDao = roleDao;
    }

    public Role addRole(Role role) {
        if (role == null)
            throw new IllegalArgumentException("role object is null");
        RoleEntity entity = new RoleEntity(role);
        roleDao.add(entity);

        return new Role(entity);
    }

    public Role getRole(String serviceId, String roleId) {
        if (roleId == null)
            throw new IllegalArgumentException("roleId is null");

        RoleEntity rl = roleDao.findById(new RoleEmbeddableId(serviceId, roleId));

        //if (!org.hibernate.Hibernate.isInitialized(rl.getUsers())) {
//		if (rl != null) {
//			org.hibernate.Hibernate.initialize(rl.getUsers());
//			org.hibernate.Hibernate.initialize(rl.getGroups());	
//		}
        return rl != null ? new Role(rl) : null;

    }


    public void updateRole(Role role) {
        if (role == null)
            throw new IllegalArgumentException("role object is null");
        RoleEntity entity = new RoleEntity(role);
        roleDao.update(entity);

    }

    /**
     * Returns a list of all Roles regardless of service The list is sorted by
     * ServiceId, Role
     *
     * @return
     */
    public List<Role> getAllRoles() {

        List<RoleEntity> rolesEntities = roleDao.findAllRoles();
        List<Role> roles = null;
        if (rolesEntities != null) {
            roles = new LinkedList<Role>();
            for (RoleEntity roleEntity : rolesEntities) {
                roles.add(new Role(roleEntity));
            }
        }
        return roles;
    }

    public int removeRole(String domainId, String roleId) {
        if (roleId == null)
            throw new IllegalArgumentException("roleId is null");
        if (domainId == null)
            throw new IllegalArgumentException("serviceId object is null");

        Role rl = new Role(new RoleId(domainId, roleId));

        try {
            this.roleAttributeDAO.deleteRoleAttributes(domainId, roleId);
            this.userRoleDao.removeAllUsersInRole(domainId, roleId);
            this.resRoleDao.removeResourceRole(domainId, roleId);
            RoleEntity entity = new RoleEntity(rl);
            this.roleDao.remove(entity);
        } catch (Exception e) {
            log.error(e.toString());
            return 0;
        }
        return 1;
    }

    public List<Role> getRolesInDomain(String domainId) {
        long start = System.currentTimeMillis();

        List<RoleEntity> rlList = roleDao.findRolesInService(domainId);

        long end = System.currentTimeMillis();
        log.debug("findRolesInService: " + (end - start));

        if (rlList == null || rlList.size() == 0)
            return null;
        List<Role> roles = new LinkedList<Role>();
        for (RoleEntity entity : rlList) {
            roles.add(new Role(entity));
        }
        return roles;
    }

    /* ---------------------- RoleAttribute Methods --------------- */

    public RoleAttribute addAttribute(RoleAttribute attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("Attribute can not be null");

        if (attribute.getRoleId() == null) {
            throw new IllegalArgumentException(
                    "Role has not been associated with this attribute.");
        }
        RoleAttributeEntity attributeEntity = new RoleAttributeEntity(attribute);
        roleAttributeDAO.add(attributeEntity);
        attribute.setRoleAttrId(attributeEntity.getRoleAttrId());
        return attribute;
    }

    public RoleAttribute[] getAllAttributes(String serviceId, String roleId) {

        if (roleId == null) {
            throw new IllegalArgumentException("groupId is null");
        }

        RoleEntity role = roleDao.findById(new RoleEmbeddableId(serviceId, roleId));
        Set attrSet = role.getRoleAttributes();
        if (attrSet != null && attrSet.isEmpty())
            return null;
        return this.roleAttrSetToArray(attrSet);
    }

    public RoleAttribute getAttribute(String attrId) {
        if (attrId == null) {
            throw new IllegalArgumentException("attrId is null");
        }
        RoleAttributeEntity roleAttributeEntity = roleAttributeDAO.findById(attrId);

        return new RoleAttribute(roleAttributeEntity);
    }

    public void removeAllAttributes(String serviceId, String roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException("roleId is null");
        }
        this.roleAttributeDAO.deleteRoleAttributes(serviceId, roleId);

    }

    public void removeAttribute(RoleAttribute attr) {
        if (attr == null) {
            throw new IllegalArgumentException("attr is null");
        }
        if (attr.getRoleAttrId() == null) {
            throw new IllegalArgumentException("attrId is null");
        }

        roleAttributeDAO.remove(new RoleAttributeEntity(attr));

    }

    public void updateAttribute(RoleAttribute attribute) {
        if (attribute == null)
            throw new IllegalArgumentException("Attribute can not be null");
        if (attribute.getRoleAttrId() == null) {
            throw new IllegalArgumentException("Attribute id is null");
        }
        if (attribute.getRoleId() == null) {
            throw new IllegalArgumentException(
                    "Role has not been associated with this attribute.");
        }

        roleAttributeDAO.update(new RoleAttributeEntity(attribute));
    }


    /* ------------- Group to Role Methods --------------------------------- */

    public void addGroupToRole(String serviceId, String roleId, String groupId) {
        // TODO Auto-generated method stub
        if (roleId == null)
            throw new IllegalArgumentException("roleId is null");
        if (serviceId == null)
            throw new IllegalArgumentException("serviceId is null");
        if (groupId == null)
            throw new IllegalArgumentException("groupId is null");

        roleDao.addGroupToRole(serviceId, roleId, groupId);

    }

    public Group[] getGroupsInRole(String serviceId, String roleId) {
        RoleEntity rl = roleDao.findById(new RoleEmbeddableId(serviceId, roleId));
        if (rl == null) {
            log.error("Role not found for roleId =" + roleId);
            throw new ObjectNotFoundException();
        }
        //org.hibernate.Hibernate.initialize(rl.getGroups());
        Set<GroupEntity> grpSetEntities = rl.getGroups();
        if (grpSetEntities == null || grpSetEntities.isEmpty()) {
            return null;
        }
        Set<Group> grpSet = new HashSet<Group>();
        for (GroupEntity groupEntity : grpSetEntities) {
            grpSet.add(new Group(groupEntity));
        }
        return grpSet.toArray(new Group[grpSet.size()]);

    }

    public boolean isGroupInRole(String serviceId, String roleId, String groupId) {

        RoleEntity rl = roleDao.findById(new RoleEmbeddableId(serviceId, roleId));
        if (rl == null) {
            log.error("Role not found for roleId =" + roleId);
            throw new ObjectNotFoundException();
        }
        //org.hibernate.Hibernate.initialize(rl.getGroups());
        Set<GroupEntity> grpSet = rl.getGroups();
        if (grpSet == null || grpSet.isEmpty()) {
            return false;
        }
        Iterator<GroupEntity> it = grpSet.iterator();
        while (it.hasNext()) {
            GroupEntity g = it.next();
            if (g.getGrpId().equalsIgnoreCase(groupId)) {
                return true;
            }
        }
        return false;
    }

    public void removeGroupFromRole(String serviceId, String roleId,
                                    String groupId) {
        if (roleId == null)
            throw new IllegalArgumentException("roleId is null");
        if (serviceId == null)
            throw new IllegalArgumentException("serviceId object is null");
        if (groupId == null)
            throw new IllegalArgumentException("groupId object is null");

        this.roleDao.removeGroupFromRole(serviceId, roleId, groupId);

    }

    public void removeAllGroupsFromRole(String serviceId, String roleId) {
        if (roleId == null)
            throw new IllegalArgumentException("roleId is null");
        if (serviceId == null)
            throw new IllegalArgumentException("serviceId object is null");

        roleDao.removeAllGroupsFromRole(serviceId, roleId);
    }

    public List<Role> getRolesInGroup(String groupId) {
        // TODO Auto-generated method stub

        if (groupId == null)
            throw new IllegalArgumentException("groupid is null");

        List<RoleEntity> roleEntityList = roleDao.findRolesInGroup(groupId);
        if (roleEntityList == null || roleEntityList.isEmpty())
            return null;

        List<Role> roleList = new LinkedList<Role>();
        for (RoleEntity roleEntity : roleEntityList) {
            roleList.add(new Role(roleEntity));
        }
        return roleList;


    }


    /* ------------- User to Role Methods --------------------------------- */

    /**
     * Adds a user to a role using the UserRole object. Similar to addUserToRole, but allows you to update attributes likes start and end date.
     */
    public void assocUserToRole(UserRole ur) {
        if (ur.getRoleId() == null)
            throw new IllegalArgumentException("roleId is null");
        if (ur.getServiceId() == null)
            throw new IllegalArgumentException("domainId object is null");
        if (ur.getUserId() == null)
            throw new IllegalArgumentException("userId object is null");

        ur.setUserRoleId(null);
        userRoleDao.add(new UserRoleEntity(ur));
    }

    /**
     * Updates the attributes in the user role object.
     *
     * @param ur
     */
    public void updateUserRoleAssoc(UserRole ur) {
        if (ur.getRoleId() == null)
            throw new IllegalArgumentException("roleId is null");
        if (ur.getServiceId() == null)
            throw new IllegalArgumentException("domainId object is null");
        if (ur.getUserId() == null)
            throw new IllegalArgumentException("userId object is null");
        userRoleDao.update(new UserRoleEntity(ur));
    }

    public UserRole getUserRoleById(String userRoleId) {
        if (userRoleId == null) {
            throw new IllegalArgumentException("userRoleId is null");
        }
        UserRoleEntity entity = userRoleDao.findById(userRoleId);
        return entity != null ? new UserRole(entity) : null;
    }

    public List<UserRole> getUserRolesForUser(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is null");
        }
        List<UserRoleEntity> entityList = userRoleDao.findUserRoleByUser(userId);
        List<UserRole> roleList = null;
        if (entityList != null) {
            roleList = new LinkedList<UserRole>();
            for (UserRoleEntity entity : entityList) {
                roleList.add(new UserRole(entity));
            }
        }
        return roleList;
    }


    public void addUserToRole(String domainId, String roleId, String userId) {

        if (roleId == null)
            throw new IllegalArgumentException("roleId is null");
        if (domainId == null)
            throw new IllegalArgumentException("domainId object is null");
        if (userId == null)
            throw new IllegalArgumentException("userId object is null");

        UserRoleEntity ur = new UserRoleEntity(userId, domainId, roleId);

        this.userRoleDao.add(ur);

    }

    public boolean isUserInRole(String serviceId, String roleId, String userId) {
        if (roleId == null)
            throw new IllegalArgumentException("roleId is null");
        if (serviceId == null)
            throw new IllegalArgumentException("serviceId object is null");
        if (userId == null)
            throw new IllegalArgumentException("userIdId object is null");

        // check if the user is directly linked to a role
/*		Role rl = roleDao.findDirectRoleForUser(serviceId, roleId, userId);
		log.info("findDirectRoleForUser = " + rl);
		if (rl != null) {
			return true;
		}
		// check if the user is linked to a role through a group.
		List<Role> roleList =  roleDao.findIndirectUserRoles(userId);
		log.info("findInDirectUserRoles = " + roleList);
		if (roleList != null)
			return true;		
		return false;
*/
        List<Role> userRoleList = this.getUserRolesAsFlatList(userId);
        if (userRoleList == null) {
            return false;
        }
        for (Role rl : userRoleList) {
            if (rl.getId().getRoleId().equalsIgnoreCase(roleId) &&
                    rl.getId().getServiceId().equalsIgnoreCase(serviceId)) {
                return true;
            }
        }
        return false;
    }

    public void removeUserFromRole(String domainId, String roleId, String userId) {
        if (roleId == null)
            throw new IllegalArgumentException("roleId is null");
        if (domainId == null)
            throw new IllegalArgumentException("domainId object is null");
        if (userId == null)
            throw new IllegalArgumentException("userId object is null");

        this.userRoleDao.removeUserFromRole(domainId, roleId, userId);


    }

    /**
     * Returns the roles that are directly associated with a user; ie. Does not take into
     * account roles that may be associated with a user becuase of a group relationship.
     *
     * @param userId
     * @return
     */
    public List<Role> getUserRolesDirect(String userId) {
        if (userId == null)
            throw new IllegalArgumentException("userIdId is null");

        List<RoleEntity> roleList = roleDao.findUserRoles(userId);
        if (roleList == null || roleList.size() == 0)
            return null;

        Set<RoleEntity> newRoleSet = new HashSet<RoleEntity>();

        if (roleList != null && !roleList.isEmpty()) {
            updateRoleAssociation(roleList, RoleConstant.DIRECT, newRoleSet);
        }
        if (roleList == null || roleList.size() == 0) {
            return null;
        }
        List<Role> roles = new LinkedList<Role>();
        for (RoleEntity roleEntity : roleList) {
            roles.add(new Role(roleEntity));
        }
        return roles;

    }


    /**
     * Returns an array of roles that a user belongs to.
     */
/*	public List<Role> getUserRoles(String userId) {
		if (userId == null)
			throw new IllegalArgumentException("userIdId is null");

		List<Role> roleList = roleDao.findUserRoles(userId);
		
		log.debug("getUserRoles for userId=" + userId);
		log.debug(" - findUserRoles = " + roleList);
		
		
		Set<Role> newRoleSet = new HashSet();
		
		if (roleList != null && !roleList.isEmpty()) {
			updateRoleAssociation(roleList, RoleConstant.DIRECT, newRoleSet);
		}
		
		roleList =  roleDao.findIndirectUserRoles(userId);
		
		log.debug(" - findIndirectUserRoles = " + roleList);
		
		if (roleList != null && !roleList.isEmpty()) {
			updateRoleAssociation(roleList, RoleConstant.INDIRECT, newRoleSet);
		}
		if (newRoleSet == null || newRoleSet.size() == 0) {
			return null;
		}
		List<Role> newRoles = new ArrayList<Role>(newRoleSet);
		return newRoles;
		

	}
*/
    private RoleEntity getParentRole(RoleEntity rl) {
        RoleEmbeddableId id = new RoleEmbeddableId(rl.getRoleId().getServiceId(), rl.getParentRoleId());
        RoleEntity pRole = this.roleDao.findById(id);
        log.info("Got parent role = " + pRole);
        if (pRole != null) {
            // add the child role to the parentRole
            pRole.getChildRoles().add(rl);
        }
        if (pRole.getParentRoleId() != null) {
            log.info("Found another parent role - make a recursive call. parentId =" + pRole.getParentRoleId());
            return getParentRole(pRole);
        }
        return pRole;
    }

    /**
     * Returns an array of Role objects that indicate the Roles a user is
     * associated to.
     *
     * @param userId
     * @return
     */
    public List<Role> getUserRoles(String userId) {
        if (userId == null)
            throw new IllegalArgumentException("userIdId is null");

        List<RoleEntity> roleList = roleDao.findUserRoles(userId);

        log.debug("getUserRoles for userId=" + userId);
        log.debug(" - findUserRoles = " + roleList);


        Set<RoleEntity> newRoleSet = new HashSet<RoleEntity>();

        if (roleList != null && !roleList.isEmpty()) {
            updateRoleAssociation(roleList, RoleConstant.DIRECT, newRoleSet);
        }

        roleList = roleDao.findIndirectUserRoles(userId);

        log.debug(" - findIndirectUserRoles = " + roleList);

        if (roleList != null && !roleList.isEmpty()) {
            updateRoleAssociation(roleList, RoleConstant.INDIRECT, newRoleSet);
        }
        if (newRoleSet == null || newRoleSet.size() == 0) {
            return null;
        }
        List<RoleEntity> newRoles = new ArrayList<RoleEntity>(newRoleSet);
        // for each of these roles, figure out if there are roles above it in the hierarchy

        List<RoleEntity> newRoleList = new ArrayList<RoleEntity>();
        for (RoleEntity rl : newRoles) {
            log.info("Role id=" + rl.getRoleId() + " parentId=" + rl.getParentRoleId());
            if (rl.getParentRoleId() == null) {
                newRoleList.add(rl);
            } else {
                log.info("Get the parent role for parentId=" + rl.getParentRoleId());
                newRoleList.add(getParentRole(rl));
            }
        }
        List<Role> roles = new LinkedList<Role>();
        for (RoleEntity roleEntity : newRoleList) {
            roles.add(new Role(roleEntity));
        }
        return roles;


        //return newRoles;

    }

    private List<RoleEntity> getParentRoleFlat(RoleEntity rl) {
        List<RoleEntity> roleList = new LinkedList<RoleEntity>();
        RoleEmbeddableId id = new RoleEmbeddableId(rl.getRoleId().getServiceId(), rl.getParentRoleId());
        RoleEntity pRole = roleDao.findById(id);
        log.info("Got parent role = " + pRole);
        if (pRole != null) {
            // add the child role to the list of  role
            roleList.add(pRole);
        }
        if (pRole.getParentRoleId() != null) {
            log.info("Found another parent role - make a recursive call. parentId =" + pRole.getParentRoleId());
            roleList.addAll(getParentRoleFlat(pRole));
            return roleList;
        }
        return roleList;
    }

    /**
     * Returns a list of roles that a user belongs to. Roles can be hierarchical and this operation traverses the tree to roles that are in the
     * hierarchy.
     *
     * @param userId
     * @return
     */
    public List<Role> getUserRolesAsFlatList(String userId) {
        if (userId == null)
            throw new IllegalArgumentException("userIdId is null");

        List<RoleEntity> roleList = roleDao.findUserRoles(userId);

        log.debug("getUserRoles for userId=" + userId);
        log.debug(" - findUserRoles = " + roleList);


        Set<RoleEntity> newRoleSet = new HashSet<RoleEntity>();

        if (roleList != null && !roleList.isEmpty()) {
            updateRoleAssociation(roleList, RoleConstant.DIRECT, newRoleSet);
        }

        roleList = roleDao.findIndirectUserRoles(userId);

        log.debug(" - findIndirectUserRoles = " + roleList);

        if (roleList != null && !roleList.isEmpty()) {
            updateRoleAssociation(roleList, RoleConstant.INDIRECT, newRoleSet);
        }
        if (newRoleSet == null || newRoleSet.size() == 0) {
            return null;
        }
        List<RoleEntity> newRoles = new LinkedList<RoleEntity>(newRoleSet);
        // for each of these roles, figure out if there are roles above it in the hierarchy

        // store the roles in sorted order
        Set<RoleEntity> roleSet = new TreeSet<RoleEntity>();
        //List<Role> newRoleList = new ArrayList<Role>();
        for (RoleEntity rl : newRoles) {

            log.debug("Role id=" + rl.getRoleId().getRoleId() + " parentId=" + rl.getParentRoleId());

            if (rl.getParentRoleId() == null) {
                roleSet.add(rl);
            } else {

                log.debug("Get the parent role for parentId=" + rl.getParentRoleId());

                roleSet.add(rl);
                roleSet.addAll(getParentRoleFlat(rl));
            }
        }

        List<Role> newRoleList = new ArrayList<Role>();
        for (RoleEntity entity : roleSet) {
            newRoleList.add(new Role(entity));
        }
        return newRoleList;
    }


    public List<Role> getUserRolesByDomain(String domainId, String userId) {
        if (userId == null)
            throw new IllegalArgumentException("userIdId is null");

        List<RoleEntity> roleList = roleDao.findUserRolesByService(domainId, userId);
        if (roleList == null || roleList.size() == 0)
            return null;

        Set<RoleEntity> newRoleSet = new HashSet<RoleEntity>();

        if (roleList != null && !roleList.isEmpty()) {
            updateRoleAssociation(roleList, RoleConstant.DIRECT, newRoleSet);
        }

        roleList = roleDao.findIndirectUserRoles(userId);
        if (roleList != null && !roleList.isEmpty()) {
            updateRoleAssociation(roleList, RoleConstant.INDIRECT, newRoleSet);
        }
        if (roleList == null || roleList.size() == 0) {
            return null;
        }
        List<Role> roles = new LinkedList<Role>();
        for (RoleEntity entity : roleList) {
            roles.add(new Role(entity));
        }
        return roles;

    }


    public User[] getUsersInRole(String domainId, String roleId) {
        if (domainId == null)
            throw new IllegalArgumentException("domainId is null");
        if (roleId == null)
            throw new IllegalArgumentException("roleId is null");

        /* Get the users that are directly associated */
        Role rl = getRole(domainId, roleId);


        //System.out.println("in getUsersInRole: rl=" + rl);
        //System.out.println("in getUsersInRole: users =" + rl.getUsers());

        List<UserEntity> userList = userRoleDao.findUserByRole(domainId, roleId);

        // No direct association, continue with indirect
        if (userList == null || userList.isEmpty())
            userList = new LinkedList<UserEntity>();

        Set<UserEntity> newUserSet = updateUserRoleAssociation(userList, UserConstant.DIRECT);

        /* Get the users that are linked through a group */
        Set<Group> groupSet = rl.getGroups();
        // ensure that we have a unique set of users.
        // iterate through the groups
        if (groupSet != null && !groupSet.isEmpty()) {
            Iterator<Group> it = groupSet.iterator();
            while (it.hasNext()) {
                Group grp = it.next();
                List<UserEntity> userLst = userGroupDao.findUserByGroup(grp.getGrpId());
                //Set<User> grpUsers = grp.getUsers();
                userSetToNewUserSet(userLst, UserConstant.INDIRECT, newUserSet);
            }
        }
        Set<User> userSet = new HashSet<User>();
        for (UserEntity userEntity : newUserSet) {
            userSet.add(new User(userEntity));
        }
        int size = userSet.size();
        // no users found, return null
        if (size == 0)
            return null;
        User[] userAry = new User[size];

        return userSet.toArray(userAry);
    }


    /** **************** Helper Methods ***************************** */

    /**
     * Converts a list of Role objects into an Array
     *
     * @param roleList
     * @return
     */
    private Role[] roleListToArray(List<Role> roleList) {

        if (roleList == null || roleList.size() == 0)
            return null;

        int size = roleList.size();
        Role[] roleAry = new Role[size];
        for (int ctr = 0; ctr < size; ctr++) {
            Role rl = roleList.get(ctr);
            roleAry[ctr] = rl;
        }
        return roleAry;

    }

    private RoleAttribute[] roleAttrSetToArray(Set<RoleAttribute> attrSet) {

        int size = attrSet.size();
        RoleAttribute[] roleAttrAry = new RoleAttribute[size];
        Iterator<RoleAttribute> it = attrSet.iterator();
        int ctr = 0;
        while (it.hasNext()) {
            RoleAttribute ra = it.next();
            roleAttrAry[ctr++] = ra;
        }
        return roleAttrAry;

    }

    private Set<UserEntity> updateUserRoleAssociation(List<UserEntity> userList, int roleAssociationMethod) {

        Set<UserEntity> newUserSet = new HashSet();

        for (UserEntity u : userList) {
            newUserSet.add(u);
        }

        return newUserSet;


    }

    private void updateRoleAssociation(List<RoleEntity> roleList, int associationMethod, Set<RoleEntity> newRoleSet) {
        int size = roleList.size();
        for (int i = 0; i < size; i++) {
            RoleEntity rl = roleList.get(i);
            rl.setUserAssociationMethod(RoleConstant.DIRECT);
            newRoleSet.add(rl);
        }
        //return newRoleSet;
    }

    private void userSetToNewUserSet(List<UserEntity> userList, int roleAssociationMethod, Set<UserEntity> newUserSet) {

        for (UserEntity u : userList) {
            newUserSet.add(u);
        }

    }

    public List<Role> search(RoleSearch search) {
        if (search == null) {
            throw new IllegalArgumentException("Search parameter is null");
        }
        List<RoleEntity> roleList = roleDao.search(search);
        if (roleList == null || roleList.isEmpty()) {
            return null;
        }
        if (roleList == null || roleList.size() == 0) {
            return null;
        }
        List<Role> roles = new LinkedList<Role>();
        for (RoleEntity entity : roleList) {
            roles.add(new Role(entity));
        }

        return roles;
    }

    public UserDataService getUserManager() {
        return userManager;
    }

    public void setUserManager(UserDataService userManager) {
        this.userManager = userManager;
    }

    public RoleAttributeDAO getRoleAttributeDAO() {
        return roleAttributeDAO;
    }

    public void setRoleAttributeDAO(RoleAttributeDAO roleAttributeDAO) {
        this.roleAttributeDAO = roleAttributeDAO;
    }

    public UserRoleDAO getUserRoleDao() {
        return userRoleDao;
    }

    public void setUserRoleDao(UserRoleDAO userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    public UserGroupDAO getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDAO userGroupDao) {
        this.userGroupDao = userGroupDao;
    }

    /* Role Policies */
    public RolePolicy addRolePolicy(RolePolicy rPolicy) {
        if (rPolicy == null) {
            throw new NullPointerException("rPolicy is null");
        }
        RolePolicyEntity rolePolicyEntity = new RolePolicyEntity(rPolicy);
        rolePolicyDao.add(rolePolicyEntity);
        rPolicy.setRolePolicyId(rolePolicyEntity.getRolePolicyId());
        return rPolicy;
    }

    public RolePolicy updateRolePolicy(RolePolicy rPolicy) {
        if (rPolicy == null) {
            throw new NullPointerException("rPolicy is null");
        }
        RolePolicyEntity rolePolicyEntity = rolePolicyDao.update(new RolePolicyEntity(rPolicy));
        return new RolePolicy(rolePolicyEntity);
    }

    public List<RolePolicy> getAllRolePolicies(String domainId, String roleId) {
        if (domainId == null) {
            throw new NullPointerException("domainId is null");
        }
        if (roleId == null) {
            throw new NullPointerException("roleId is null");
        }
        List<RolePolicyEntity> entityList = rolePolicyDao.findRolePolicies(domainId, roleId);
        List<RolePolicy> policies = null;
        if (entityList != null) {
            policies = new LinkedList<RolePolicy>();
            for (RolePolicyEntity entity : entityList) {
                policies.add(new RolePolicy(entity));
            }
        }
        return policies;
    }

    public RolePolicy getRolePolicy(String rolePolicyId) {
        if (rolePolicyId == null) {
            throw new NullPointerException("rolePolicyId is null");
        }
        RolePolicyEntity policyEntity = rolePolicyDao.findById(rolePolicyId);

        return new RolePolicy(policyEntity);
    }

    public void removeRolePolicy(RolePolicy rPolicy) {
        if (rPolicy == null) {
            throw new NullPointerException("rPolicy is null");
        }
        rolePolicyDao.remove(new RolePolicyEntity(rPolicy));

    }

    public RolePolicyDAO getRolePolicyDao() {
        return rolePolicyDao;
    }

    public void setRolePolicyDao(RolePolicyDAO rolePolicyDao) {
        this.rolePolicyDao = rolePolicyDao;
    }

    public ResourceRoleDAO getResRoleDao() {
        return resRoleDao;
    }

    public void setResRoleDao(ResourceRoleDAO resRoleDao) {
        this.resRoleDao = resRoleDao;
    }

    public ServiceDAO getServiceDao() {
        return serviceDao;
    }

    public void setServiceDao(ServiceDAO serviceDao) {
        this.serviceDao = serviceDao;
    }
}
