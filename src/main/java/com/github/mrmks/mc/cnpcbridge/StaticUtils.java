package com.github.mrmks.mc.cnpcbridge;

import java.util.Arrays;

public class StaticUtils {

    public static String[] splitAndConcat(String[] args, String splitter) {
        if (args.length == 0) return args;
        int l = 0;
        int[] sAry = new int[args.length], eAry = new int[args.length];
        sAry[0] = -1;
        for (int i = 0; i < args.length; i++) if (args[i].equals(splitter)) {
            if (sAry[l] + 1 == i) sAry[l] = i; else {sAry[l+1] = eAry[l] = i; l++;}
        }
        eAry[l++] = args.length;
        String[] res = new String[l];
        for (int i = 0; i < l; i++) {
            StringBuilder buffer = new StringBuilder();
            for (int j = sAry[i] + 1; j < eAry[i]; j++) {
                buffer.append(args[j]);
                if(j + 1 != eAry[i]) buffer.append(" ");
            }
            res[i] = buffer.toString();
        }
        return res;
    }

    public static LastCommand findLastCommand(String[] args, String splitter) {
        int i = args.length - 1;
        while (i >= 0 && !args[i].equals(splitter)) i--;
        if (i == args.length - 1) return null;
        else {
            LastCommand cmd = new LastCommand();
            cmd.cmd = args[i + 1];
            cmd.args = i + 2 < args.length ? Arrays.copyOfRange(args, i + 2, args.length) : new String[0];
            return cmd;
        }
    }

    public static class LastCommand {
        public String cmd;
        public String[] args;
    }

    public static double parseCoordinate(String str, double base) throws NumberFormatException {
        double res = 0d;
        if (str.charAt(0) == '~') {
            res += base;
            str = str.substring(1);
        }
        if (str.length() > 0) {
            res += Double.parseDouble(str);
        }
        return res;
    }

    public static float parseAngle(String str, float base) throws NumberFormatException {
        return parseAngle(str, base, -180, 180);
    }

    public static float parsePitch(String str, float base) throws NumberFormatException {
        return parseAngle(str, base, -90, 90);
    }

    private static float parseAngle(String str, float base, float min, float max) throws NumberFormatException {
        float res = 0f;
        if (str.charAt(0) == '~') res += base;
        str = str.substring(1);
        if (str.length() > 0) res += Float.parseFloat(str);
        while (res < -180) res += 360;
        while (res > 180) res -= 360;
        return res >= min && max >= res ? res : base;
    }
}
