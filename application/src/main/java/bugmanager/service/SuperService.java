package bugmanager.service;

import bugmanager.model.Bug;
import bugmanager.model.User;
import bugmanager.persistence.BugRepository;
import bugmanager.persistence.UserRepository;

import java.util.List;

public class SuperService {
    BugRepository bugRepository;
    UserRepository userRepository;

    public SuperService(BugRepository bugRepository, UserRepository userRepository) {
        this.bugRepository = bugRepository;
        this.userRepository = userRepository;
    }

    public User login(String username, String password) throws ServiceException {
        validateUser(new User(username, password, false, User.UserRole.Tester));
        User user = userRepository.getUserByUsername(username);
        if (user == null || !user.isActivated() || !password.equals(user.getPassword()))
            throw new ServiceException("No activated user with given credentials;\n");
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void addUser(String username, String password, User.UserRole role) throws ServiceException {
        User user = new User(username, password, true, role);
        validateUser(user);
        if (role == User.UserRole.Admin) {
            throw new ServiceException("Can't add admin user;\n");
        }
        if (userRepository.getUserByUsername(username) != null) {
            throw new ServiceException("User with given username already exists;\n");
        }
        if (!userRepository.addUser(user)) {
            throw new ServiceException("Couldn't add user;\n");
        }
    }

    public void activateUser(String username) throws ServiceException {
        validateUser(new User(username, "p", true, User.UserRole.Admin));
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new ServiceException("No user with given username;\n");
        }
        if (user.getRole() == User.UserRole.Admin) {
            throw new ServiceException("Can't activate admin user;\n");
        }
        if (user.isActivated()) {
            throw new ServiceException("User already active;\n");
        }
        user.setActivated(true);
        if (!userRepository.updateUser(user)) {
            throw new ServiceException("Couldn't activate user;\n");
        }
    }

    public void deactivateUser(String username) throws ServiceException {
        validateUser(new User(username, "p", true, User.UserRole.Admin));
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new ServiceException("No user with given username;\n");
        }
        if (user.getRole() == User.UserRole.Admin) {
            throw new ServiceException("Can't deactivate admin user;\n");
        }
        if (!user.isActivated()) {
            throw new ServiceException("User already deactivated;\n");
        }
        user.setActivated(false);
        if (!userRepository.updateUser(user)) {
            throw new ServiceException("Couldn't deactivate user;\n");
        }
    }

    public void updateUser(String username, String password, User.UserRole role) throws ServiceException {
        User user = new User(username, password, false, role);
        validateUser(user);
        if (role == User.UserRole.Admin) {
            throw new ServiceException("Admin role can't be given through update;\n");
        }
        User current = userRepository.getUserByUsername(username);
        if (current == null) {
            throw new ServiceException("No user with given username;\n");
        }
        if (current.getRole() == User.UserRole.Admin) {
            throw new ServiceException("Can't update admin user;\n");
        }
        if (current.isActivated()) {
            throw new ServiceException("Can't update user info while he's active;\n");
        }
        if (!userRepository.updateUser(user)) {
            throw new ServiceException("Couldn't update user;\n");
        }
    }

    private void validateUser(User user) throws ServiceException {
        String username = user.getUsername();
        String password = user.getPassword();
        User.UserRole role = user.getRole();
        String error = "";
        if (username == null || username.equals("")) {
            error += "Username should be non-empty and non-null;\n";
        }
        if (password == null || password.equals("")) {
            error += "Password should be non-empty and non-null;\n";
        }
        if (role == null) {
            error += "Role should be non-null;\n";
        }
        if (!error.equals("")) {
            throw new ServiceException(error);
        }
    }

    public List<Bug> getAllActiveBugs() {
        return bugRepository.getAllActiveBugs();
    }

    public void solveBug(Integer id) throws ServiceException {
        validateBug(new Bug(id, "a", "a", Bug.BugStatus.Solved));
        Bug bug = bugRepository.getBugById(id);
        if (bug == null) {
            throw new ServiceException("No bug with given id;\n");
        }
        if (bug.getStatus() == Bug.BugStatus.Solved) {
            throw new ServiceException("Bug already solved;\n");
        }
        bug.setStatus(Bug.BugStatus.Solved);
        if (!bugRepository.updateBug(bug)) {
            throw new ServiceException("Couldn't solve bug;\n");
        }
    }

    public void updateBug(Integer id, String name, String description, Bug.BugStatus status)
            throws ServiceException {
        Bug bug = new Bug(id, name, description, status);
        validateBug(bug);
        if (status == Bug.BugStatus.Solved) {
            throw new ServiceException("Solved status can be given only through 'solve';\n");
        }
        Bug current = bugRepository.getBugById(id);
        if (current == null) {
            throw new ServiceException("No bug with given id;\n");
        }
        if (current.getStatus() == Bug.BugStatus.Solved) {
            throw new ServiceException("Can't update solved bug\n");
        }
        if (!bugRepository.updateBug(bug)) {
            throw new ServiceException("Couldn't update bug;\n");
        }
    }

    public void submitBug(String name, String description, Bug.BugStatus status) throws ServiceException {
        Bug bug = new Bug(0, name, description, status);
        validateBug(bug);
        if (status == Bug.BugStatus.Solved) {
            throw new ServiceException("Can't add solved bug;\n");
        }
        bug.setId(null);
        if (!bugRepository.addBug(bug)) {
            throw new ServiceException("Couldn't add bug;\n");
        }
    }

    private void validateBug(Bug bug) throws ServiceException {
        Integer id = bug.getId();
        String name = bug.getName();
        String description = bug.getDescription();
        Bug.BugStatus status = bug.getStatus();
        String error = "";
        if (id == null) {
            error += "Id must be non-null;\n";
        }
        if (name == null || name.equals("")) {
            error += "Name should be non-empty and non-null;\n";
        }
        if (description == null || description.equals("")) {
            error += "Description should be non-empty and non-null;\n";
        }
        if (status == null) {
            error += "Status should be non-null;\n";
        }
        if (!error.equals("")) {
            throw new ServiceException(error);
        }
    }
}
