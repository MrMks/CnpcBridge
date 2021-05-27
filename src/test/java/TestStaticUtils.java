import com.github.mrmks.mc.cnpcbridge.StaticUtils;
import org.junit.Test;

public class TestStaticUtils {
    @Test
    public void testStatic() {
        String[] ary = {"say", "1", "|", "say", "2"};
        StaticUtils.LastCommand cmd = StaticUtils.findLastCommand(new String[]{"say", "1"}, "|");
        System.out.println(cmd.cmd);
    }
}
