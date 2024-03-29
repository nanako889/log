package com.qbw.l;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author QBW
 */


public class L {

    private static String sCommonFilterTag = "[log]";
    public static final L GL = new L();

    private boolean mEnabled = false;

    private String mLogDirPath;

    private String mFilterTag = "";

    private final int JSON_INDENT = 4;

    private final String LINE_SEPARATOR = System.getProperty("line.separator");

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        setEnabled(enabled, "");
    }

    public void setEnabled(boolean enabled, String logDirPath) {
        mEnabled = enabled;
        mLogDirPath = logDirPath;
    }


    public void setFilterTag(String filterTag) {
        mFilterTag = filterTag;
    }

    public void v(String logFormat, Object... logParam) {
        l('v', logFormat, logParam);
    }

    public void d(String logFormat, Object... logParam) {
        l('d', logFormat, logParam);
    }

    public void i(String logFormat, Object... logParam) {
        l('i', logFormat, logParam);
    }

    public void w(String logFormat, Object... logParam) {
        l('w', logFormat, logParam);
    }

    public void w(Throwable e) {
        if (null != e) {
            String message = e.getMessage();
            if (!TextUtils.isEmpty(message)) {
                l('w', message);
            } else {
                l('w', "message is empty");
            }
        }
    }

    public void e(String logFormat, Object... logParam) {
        l('e', logFormat, logParam);
    }

    public void e(Throwable e) {
        if (null != e) {
            String message = e.getMessage();
            if (!TextUtils.isEmpty(message)) {
                l('e', message);
            } else {
                l('e', "message is empty");
            }
        }
    }

    public void jsonV(String message) {
        if (mEnabled) log('v', createLog(jsonLog(message), 5));
    }

    public void jsonD(String message) {
        if (mEnabled) log('d', createLog(jsonLog(message), 5));
    }

    public void jsonI(String message) {
        if (mEnabled) log('i', createLog(jsonLog(message), 5));
    }

    public void jsonW(String message) {
        if (mEnabled) log('w', createLog(jsonLog(message), 5));
    }

    public void jsonE(String message) {
        if (mEnabled) log('e', createLog(jsonLog(message), 5));
    }

    public void urlV(String url, Map<String, Object> mapParam) {
        if (mEnabled) l('v', urlLog(url, mapParam));
    }

    public void urlD(String url, Map<String, Object> mapParam) {
        if (mEnabled) l('d', urlLog(url, mapParam));
    }

    public void urlI(String url, Map<String, Object> mapParam) {
        if (mEnabled) l('i', urlLog(url, mapParam));
    }

    public void urlW(String url, Map<String, Object> mapParam) {
        if (mEnabled) l('w', urlLog(url, mapParam));
    }

    public void urlE(String url, Map<String, Object> mapParam) {
        if (mEnabled) l('e', urlLog(url, mapParam));
    }

    private void l(char type, String logFormat, Object... logParam) {
        if (!mEnabled) {
            return;
        }
        try {
            boolean isWriteToFile = !TextUtils.isEmpty(mLogDirPath);
            String[] logs = createLog(String.format(logFormat, logParam));
            log(type, logs);
            if (isWriteToFile) {
                writeToFile(logs[0], logs[1]);
            }
        } catch (Exception e) {
            if (e != null) {
                String msg = e.getMessage();
                if (!TextUtils.isEmpty(msg)) {
                    Log.w("[L]156", msg);
                }
            }
        }
    }

    public void logV(String log) {
        if (mEnabled) log('v', createLog(log, 5));
    }

    public void logD(String log) {
        if (mEnabled) log('d', createLog(log, 5));
    }

    public void logI(String log) {
        if (mEnabled) log('i', createLog(log, 5));
    }

    public void logW(String log) {
        if (mEnabled) log('w', createLog(log, 5));
    }

    public void logE(String log) {
        if (mEnabled) log('e', createLog(log, 5));
    }

    private void log(char level, String[] logs) {
        log(level, logs[0], logs[1]);
    }

    /**
     * when log is too long,split it
     *
     * @param level
     * @param tag
     * @param text
     */
    private void log(char level, String tag, String text) {
        final int PART_LEN = 3000;

        do {
            int clipLen = text.length() > PART_LEN ? PART_LEN : text.length();
            String clipText = text.substring(0, clipLen);
            text = clipText.length() == text.length() ? "" : text.substring(clipLen);
            switch (level) {
                case 'i':
                    Log.i(tag, clipText);
                    break;
                case 'd':
                    Log.d(tag, clipText);
                    break;
                case 'w':
                    Log.w(tag, clipText);
                    break;
                case 'v':
                    Log.v(tag, clipText);
                    break;
                case 'e':
                    Log.e(tag, clipText);
                    break;
                default:
                    break;
            }
        } while (text.length() > 0);
    }

    public String jsonLog(String message) {
        if (TextUtils.isEmpty(message)) {
            return "";
        }
        try {
            int job = message.indexOf("{");
            int joe = message.lastIndexOf("}");
            int jab = message.indexOf("[");
            int jae = message.lastIndexOf("]");
            /**
             * -1,不存在json格式字符串
             * 0,jsonobject
             * 1,jsonarray
             */
            int type;
            if (job != -1 && (-1 == jab || job < jab) && joe != -1 && joe > job) {
                type = 0;
            } else if (jab != -1 && (-1 == job || jab < job) && jae != -1 && jae > jab) {
                type = 1;
            } else {
                type = -1;
            }
            if (type == -1) {
                return message;
            } else {
                StringBuilder jsonLog = new StringBuilder();

                switch (type) {
                    case 0:
                        jsonLog.append(message.substring(0, job)).append(LINE_SEPARATOR);
                        jsonLog.append(new JSONObject(message.substring(job, joe + 1)).toString(
                                JSON_INDENT)).append(LINE_SEPARATOR);
                        jsonLog.append(message.substring(joe + 1, message.length()))
                                .append(LINE_SEPARATOR);
                        break;
                    case 1:
                        jsonLog.append(message.substring(0, jab)).append(LINE_SEPARATOR);
                        jsonLog.append(new JSONArray(message.substring(jab, jae + 1)).toString(
                                JSON_INDENT)).append(LINE_SEPARATOR);
                        jsonLog.append(message.substring(jae + 1, message.length()))
                                .append(LINE_SEPARATOR);
                        break;
                    default:
                        break;
                }

                return jsonLog.toString();
            }
        } catch (Exception e) {
            if (e != null) {
                String msg = e.getMessage();
                if (!TextUtils.isEmpty(msg)) {
                    Log.w("xlog", msg);
                }
            }
        }
        return "";
    }

    public String urlLog(String url, Map<String, Object> mapParam) {
        if (!mEnabled) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url + "?");
        if (null != mapParam && !mapParam.isEmpty()) {
            Set<Map.Entry<String, Object>> entrySet = mapParam.entrySet();
            for (Map.Entry entry : entrySet) {
                stringBuilder.append(entry.getKey() + "=" + entry.getValue() + "&");
            }

        }
        url = stringBuilder.toString();
        return url.substring(0, url.length() - 1);
    }

    public void line(boolean top) {
        if (top) {
            l('v',
                    "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            l('v',
                    "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }

    private String[] createLog(String log) {
        return createLog(log, 7);
    }

    private String[] createLog(String log, int depth) {
        if (null == log) {
            log = "";
        }
        String tag = mEnabled ? getFileNameMethodLineNumber(depth) : "";
        if (null == tag) {
            tag = "";
        }

        return new String[]{sCommonFilterTag + mFilterTag, tag + log};
    }

    /**
     * @param tag
     * @param msg
     */
    private void writeToFile(String tag, String msg) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.e("", "no external storage!!!");
            return;
        }

        Date date = Calendar.getInstance().getTime();
        final String logName = String.format("%1$04d%2$02d%3$02d.txt",
                date.getYear() + 1900,
                date.getMonth() + 1,
                date.getDate());
        File fLogDir = new File(mLogDirPath);
        if (!fLogDir.exists()) {
            if (!fLogDir.mkdirs()) {
                Log.e("", "create dir[" + mLogDirPath + "]failed!!!");
                return;
            }
        }

        try {
            File f = new File(mLogDirPath + File.separator + logName);
            if (!f.exists()) {
                if (!f.createNewFile()) {
                    Log.e("", "create file failed");
                    return;
                }
            }
            FileOutputStream fout = new FileOutputStream(f, true);
            OutputStreamWriter swriter = new OutputStreamWriter(fout);
            BufferedWriter bwriter = new BufferedWriter(swriter);
            bwriter.write(String.format("[%1$02d:%2$02d:%3$02d]%4$50s:%5$s\n",
                    date.getHours(),
                    date.getMinutes(),
                    date.getSeconds(),
                    tag,
                    msg));
            bwriter.flush();
            bwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("exception", e.getMessage());
        }

    }

    /**
     * @param depth 2,the method it self;3,the method who call this method
     * @return filename + method name + line number
     */
    private String getFileNameMethodLineNumber(int depth) {
        String info = "";
        try {
            StackTraceElement e = Thread.currentThread().getStackTrace()[depth];
            if (!TextUtils.isEmpty(e.getFileName()) && !TextUtils.isEmpty(e.getMethodName())) {
                info = String.format("[%1$s,%2$s,%3$s]",
                        e.getFileName(),
                        e.getMethodName(),
                        e.getLineNumber());
            }
        } catch (Exception e) {
            Log.e("log", "get stack trace element failed!!!");
        }
        return info;
    }
}
