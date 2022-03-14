
import Frame.MyFrame;
import Models.BlockChain;
import Models.BlockModel;
import service.APIService;
import service.DbWorker;
import service.SignService;

import java.util.Arrays;
import java.util.List;

public class TestClient {

    public static void main(String[] args) throws Exception {

        DbWorker dbWorker = new DbWorker();

        SignService signService = new SignService(dbWorker);

        APIService apiService = new APIService();

        // get all blocks
        List<BlockModel> blockList = Arrays.stream(apiService.getBlockChain()).toList();


        BlockChain blockChain = new BlockChain();

        blockChain.setDbWorker(dbWorker);
        blockChain.setSignService(signService);
        blockChain.setApiService(apiService);
        blockChain.setBlockModelList(blockList);

        // send list to db
        blockChain.addToDB();


        new MyFrame(blockChain);

    }

}
