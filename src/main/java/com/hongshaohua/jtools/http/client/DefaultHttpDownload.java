package com.hongshaohua.jtools.http.client;

import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Aska on 2017/6/18.
 */
public class DefaultHttpDownload extends DefaultHttpGet {

    private static final Logger logger = LoggerFactory.getLogger(DefaultHttpDownload.class);

    private String path;
    private String tmpPath;

    public DefaultHttpDownload(String url, String path, String tmpPath) {
        super(url);
        this.path = path;
        this.tmpPath = tmpPath;
    }

    public String path() {
        return path;
    }

    public String tmpPath() {
        return tmpPath;
    }

    private boolean saveFile(InputStream in, String path, String tmpPath) throws Exception {
        boolean success = false;
        File destFile = new File(path);
        FileOutputStream out = null;
        File tmpFile = null;
        try {
            if(destFile.exists()) {
                destFile.delete();
            }
            tmpFile = new File(tmpPath);
            if(tmpFile.exists()) {
                tmpFile.delete();
            }
            tmpFile.getParentFile().mkdirs();
            out = new FileOutputStream(tmpFile);
            byte[] buffer = new byte[10240];
            int ch = 0;
            while((ch = in.read(buffer)) != -1) {
                out.write(buffer, 0, ch);
            }
            out.flush();
            success = true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            success = false;
        } finally {
            try {
                if(out != null) {
                    out.close();
                }
            } catch (Exception e2) {
                logger.error(e2.getMessage(), e2);
            }
        }

        if(success && tmpFile != null) {
            destFile.getParentFile().mkdirs();
            success = tmpFile.renameTo(destFile);
        }

        return success;
    }

    @Override
    protected boolean responseEntiry(HttpEntity entity) throws Exception {
        boolean success = false;
        InputStream in = null;
        try {
            in = entity.getContent();
            if(this.saveFile(in, this.path, this.tmpPath)) {
                success = true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            success = false;
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                logger.error(e2.getMessage(), e2);
            }
        }
        return success;
    }
}
