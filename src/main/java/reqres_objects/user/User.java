package reqres_objects.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    String name;
    String job;
    String id;
    String createdAt;
    String email;
    String password;
    String token;
    String error;
}