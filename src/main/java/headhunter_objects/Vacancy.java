package headhunter_objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data

public class Vacancy {
    @Expose
    String name;
    @Expose
    Salary salary;
    @SerializedName("alternate_url")
    @Expose
    String alternateUrl;
    String additionalInfo;
}
