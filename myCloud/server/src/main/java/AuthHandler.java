import DB.BDApp;
import DB.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import messages.SignInMessage;
import messages.SignUpMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AuthHandler extends ChannelInboundHandlerAdapter {

    MainHandler mh;
    User loggedUser;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        AuthResult authResult = new AuthResult(false);
        try {
            User newUser;
            if (msg instanceof SignInMessage) {
                SignInMessage sim = (SignInMessage) msg;
                BDApp bd = new BDApp();
                bd.isRegistered(sim.getLogin());
                if (bd.isLogged(sim.getLogin(), sim.getPassword()))
                {
                    this.loggedUser = bd.get(sim.getLogin());
                    //mh = new MainHandler(loggedUser);
                    if (!loggedUser.isServerStorageDirectoryExists() || !loggedUser.isClientStorageDirectoryExists()) {
                        Path path = Paths.get(loggedUser.serverStorageDirectory);
                        Files.createDirectories(path);
                        path = Paths.get(loggedUser.clientStorageDirectory);
                        Files.createDirectories(path);
                    }
                    authResult.setLoggedIn(true);
                    //ctx.pipeline().addLast(new MainHandler(this.loggedUser));
                    //ctx.fireChannelRead(msg);
                    //ctx.writeAndFlush(authResult);

                }
                System.out.println(bd.isRegistered(sim.getLogin()));
            } else if (msg instanceof SignUpMessage) {
                SignUpMessage sup = (SignUpMessage) msg;
                BDApp bd = new BDApp();
                newUser = new User(53, sup.getName(),
                        sup.getAge(),
                        sup.getLogin(),
                        sup.getPassword());
                bd.insert(newUser);
            }
        } finally {
            ctx.writeAndFlush(authResult);
            ctx.pipeline().addLast(new MainHandler(this.loggedUser));
            ctx.fireChannelRead(msg);
            ctx.pipeline().remove(this);
            ReferenceCountUtil.release(msg);
        }
    }
}
