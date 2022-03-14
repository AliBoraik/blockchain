package Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewBlockResponse {
    private int status; // 0 - ok, 2 - error
    private String statusString; // error description
    private BlockModel block;    // new blok data
}
