package com.shy.lib.http;

/**
 * Created by ZhangL on 2020-04-02.
 */
public interface ProgressListener {
    void progress(long totle, int current);
}
