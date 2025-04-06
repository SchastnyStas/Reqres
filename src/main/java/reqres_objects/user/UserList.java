package reqres_objects.user;

import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class UserList {
    @Expose
    ArrayList<UserForUsersList> data;
}
