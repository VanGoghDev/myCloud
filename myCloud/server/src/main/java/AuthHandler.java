import DB.BDApp;
import DB.User;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import messages.SignInMessage;
import messages.SignUpMessage;

public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        User newUser;
        if (msg instanceof SignInMessage) {
            SignInMessage sim = (SignInMessage) msg;
            BDApp bd = new BDApp();
            bd.isRegistered(sim.getLogin());
            if (bd.isLogged(sim.getLogin(), sim.getPassword())) {
                ctx.fireChannelRead(msg);
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
    }
}
