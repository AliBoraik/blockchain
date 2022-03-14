package Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataModel {
    private String data;
    private String name;

    // return as normalized JSON object
    public String toString() {
        return new StringBuilder().append("{")
                .append("\"data\":\"").append(data).append("\",")
                .append("\"name\":\"").append(name).append("\"}")
                .toString();
    }
}
