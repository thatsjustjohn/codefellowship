package info.johnnywinters.codefellowship;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    AppUser creator;
    String body;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at", updatable = false)
    Date createdAt;

    public long getId() {
        return this.id;
    }

    public AppUser getCreator() {
        return this.creator;
    }

    public String getBody() {
        return this.body;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
