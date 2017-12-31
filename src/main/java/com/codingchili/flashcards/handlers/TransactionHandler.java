package com.codingchili.flashcards.handlers;

import io.vertx.core.json.JsonObject;
import java.util.concurrent.atomic.AtomicReference;

/*import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.ipc.WindowsIpcService;*/

import com.codingchili.core.context.CoreContext;
import com.codingchili.core.listener.CoreHandler;
import com.codingchili.core.listener.Request;
import com.codingchili.core.logging.Logger;
import com.codingchili.core.protocol.Address;
import com.codingchili.core.protocol.Api;
import com.codingchili.core.protocol.Protocol;
import com.codingchili.core.protocol.Roles;
import com.codingchili.core.protocol.Serializer;
import static com.codingchili.core.protocol.RoleMap.PUBLIC;

@Roles(PUBLIC)
@Address("transactions")
public class TransactionHandler implements CoreHandler {
  /*  private Protocol<Request> protocol = new Protocol<>(this);
    private AtomicReference<EthBlock.Block> block = new AtomicReference<>();
    private Logger logger;*/

    @Override
    public void init(CoreContext core) {
/*        this.logger = core.logger(TransactionHandler.class);
        logger.log("Listening for blockchain events..");


        Web3j web3 = Web3j.build(new WindowsIpcService("\\\\.\\pipe\\geth.ipc"));
        core.logger(getClass()).log("listening blocks!");

        web3.blockObservable(false).subscribe(event -> {
            block.set(event.getBlock());
            core.logger(getClass()).log("got block number: " + event.getBlock().getNumberRaw());
        });*/
    }

    @Api
    public void lastblock(Request request) {
        //request.write(new JsonObject().put("block", Serializer.json(block.get())));
    }

    @Override
    public void handle(Request request) {
        //protocol.get(request.route()).submit(request);
    }
}
