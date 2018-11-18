import DB.BDApp;
import DB.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import messages.FileRequest;
import messages.SignInMessage;
import messages.SignUpMessage;

import java.io.File;
import java.net.MalformedURLException;

public class AuthHandler extends ChannelInboundHandlerAdapter {

    MainHandler mh;
    String nick;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        AuthResult authResult = new AuthResult(false);
        try {
            User newUser;
            if (msg instanceof SignInMessage) {
                SignInMessage sim = (SignInMessage) msg;
                nick = sim.getLogin();
                BDApp bd = new BDApp();
                bd.isRegistered(sim.getLogin());
                if (bd.isLogged(sim.getLogin(), sim.getPassword())) {
                    authResult.setLoggedIn(true);
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
            System.out.println();
            ctx.pipeline().addLast(mh);
        } finally {
            ctx.writeAndFlush(authResult);
            mh = new MainHandler(nick);
            ctx.pipeline().addLast(mh);
            ctx.fireChannelRead(msg);
            ctx.pipeline().remove(this);
        }
    }
}
