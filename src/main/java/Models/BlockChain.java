package Models;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.util.encoders.Hex;
import service.APIService;
import service.DbWorker;
import service.SignService;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockChain {

    private List<BlockModel> blockModelList = new ArrayList<>();
    private APIService apiService;
    private DbWorker dbWorker;
    private SignService signService;


    public final String FILE_NAME_CHAIN = System.getProperty("user.home") + File.separator + "block_chain_dis.json";

    public void readBlockChain() {
        File file = new File(FILE_NAME_CHAIN);

        if (file.exists()) {
            //   log.info("read chain from file " + FILE_NAME_CHAIN);
            ObjectMapper mapper = new ObjectMapper();
            try {
                BlockChain blockChain = mapper.readValue(file, BlockChain.class);
                if (blockChain != null && blockModelList != null && blockModelList.size() > 0) {
                    blockModelList.clear();
                }
            } catch (IOException e) {
                //  log.log(Level.SEVERE, "load chain from file ", e);

            }
        }
    }

    public void addToDB() {
        dbWorker.clean();
        blockModelList.forEach(dbWorker::addBlockModel);
    }

    public void addNewBlockChain(String data, String name) throws Exception {

        String prevHash = new String(Hex.encode(signService.getHash(blockModelList.get(blockModelList.size() - 1))));

        // add new block
        BlockModel blockModel = new BlockModel();

        blockModel.setPrevhash(prevHash);

        DataModel dataModel = new DataModel(data, name);
        blockModel.setData(dataModel);
        String signature = new String(Hex.encode(signService.generateRSAPSSSignature(dataModel.toString().getBytes())));
        blockModel.setSignature(signature);
        blockModel.setPublickey(SignService.publicKey);

        // send new block to server
        apiService.sendBlock(blockModel);
        // send new block to db
        dbWorker.addBlockModel(blockModel);

    }


    public void saveBlockChain() {
        File file = new File(FILE_NAME_CHAIN);
        //  log.info("save chain to file " + FILE_NAME_CHAIN);
        ObjectMapper mapper = new ObjectMapper();
        BlockChain blockChain = new BlockChain();
        blockModelList = blockModelList;
        try {
            mapper.writeValue(file, blockChain);
        } catch (IOException e) {
            //  log.log(Level.SEVERE, "save chain to file ", e);

        }
    }
}
