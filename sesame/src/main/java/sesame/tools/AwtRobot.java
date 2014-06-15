package sesame.tools;

import java.awt.*;

public class AwtRobot {

    public java.awt.Robot robot;

    public AwtRobot() {
        try {
            robot = new java.awt.Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Robot init failed", e);
        }
    }

    public void sendKey(int i) throws Exception {
        robot.keyPress(i);
        robot.keyRelease(i);
        robot.waitForIdle();
    }

    public void sendText(String pathToFile) {
        try {
            char[] chars = pathToFile.toCharArray();
            for (int i = 0; i <= chars.length; i++) {
                int code = (int) chars[i];
                // keycode only handles [A-Z] (which is ASCII decimal [65-90])
                if (code > 96 && code < 123) code = code - 32;
                // 523 is int value of VK_UNDERSCORE
                // 95 is ASCII code of underscore
                if (code == 95) {
                    robot.keyPress(16);
                    robot.keyPress(523);
                    robot.keyRelease(523);
                    robot.keyRelease(16);
                }
                if (Character.isUpperCase(chars[i])) {
                    // 16 is int value of VK_SHIFT
                    robot.keyPress(16);
                }
                robot.keyPress(code);
                robot.keyRelease(code);
                if (Character.isUpperCase(chars[i])) {
                    robot.keyRelease(16);
                }
            }
            robot.waitForIdle();
        } catch (Exception e) {
            throw new RuntimeException("sendText failed", e);
        }
    }

    public void waitForIdle() {
        robot.waitForIdle();
    }
}
