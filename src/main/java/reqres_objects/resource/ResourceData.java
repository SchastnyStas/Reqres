package reqres_objects.resource;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class ResourceData {

    @Expose
    ResourceForResourcesList data;
}
