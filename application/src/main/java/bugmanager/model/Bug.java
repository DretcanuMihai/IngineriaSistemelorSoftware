package bugmanager.model;

public class Bug {
    Integer id;
    String name;
    String description;
    BugStatus status;
    public enum BugStatus{
        WaitingFeedback,
        WaitingRetest,
        Solved
    }

    public Bug() {
    }

    public Bug(Integer id, String name, String description, BugStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BugStatus getStatus() {
        return status;
    }

    public void setStatus(BugStatus status) {
        this.status = status;
    }
}
