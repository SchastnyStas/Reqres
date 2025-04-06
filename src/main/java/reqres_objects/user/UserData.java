package reqres_objects.user;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class UserData {

    @Expose
    UserForUsersList data;
}
