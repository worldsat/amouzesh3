package com.makancompany.assistant.Kernel.Interface;

public interface PercentUploadCallback {
    void percent(long totalSize, long sendSize, float percent, float speed, boolean canceled);
}
