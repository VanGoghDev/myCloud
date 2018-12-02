import DB.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import messages.FileMessage;
import messages.FilePush;
import messages.FileRequest;
import messages.UserRequest;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainHandler extends ChannelInboundHandlerAdapter {
    private User user;

    public MainHandler(User user) {
        this.user = user;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        int count = 0;
        try {
            UserRequest ur = new UserRequest(user);
            ctx.writeAndFlush(ur);
            count = 1;
            if (msg == null) {
                return;
            }
            if (msg instanceof FileRequest) {
                FileRequest fr = (FileRequest) msg;
                if (Files.exists(Paths.get(this.user.serverStorageDirectory+ fr.getFilename()))) {
                    FileMessage fm = new FileMessage(Paths.get(this.user.serverStorageDirectory + fr.getFilename()), user);
                    ctx.writeAndFlush(fm);
                }
            }
            if (msg instanceof FilePush) {
                FilePush fp = (FilePush) msg;
                if (!Files.exists(Paths.get(user.serverStorageDirectory + fp.getFilename()))) {
                    Files.write(Paths.get(fp.getUser().serverStorageDirectory + fp.getFilename()), fp.getData(), StandardOpenOption.CREATE);
                    fp.setResult(true);
                    ctx.writeAndFlush(fp);
                    /*FileMessage fm = new FileMessage(Paths.get(user.serverStorageDirectory + fp.getFilename()), user);
                    Files.write(Paths.get(user.serverStorageDirectory + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                    fp.setResult(true);
                    ctx.writeAndFlush(fp);*/
                }
                else {
                    fp.setResult(false);
                    ctx.writeAndFlush(fp);
                }
            }
        } finally {
            ctx.fireChannelRead(msg);
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
