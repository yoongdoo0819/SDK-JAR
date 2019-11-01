package assets.model;

/*import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;*/

//import javax.persistence.*;
import java.util.Date;
import java.util.List;


public class User {

//    @Id
//    @Column(length=100)
    private String id;

    private Date createDate;

//    @Column(nullable = true, length = 200)
    private String name;
    
//    @Column(nullable = false, length = 500)
    private String password;

    /*public User() {

    }*/

    public User(String _id, String _passwd) {
        id = _id;
        password = _passwd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreatedate(Date createDate) {
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinColumn(name="id")
	//private List<UserConnectLog> connectLogs;
}