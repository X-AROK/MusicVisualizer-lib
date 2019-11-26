package ru.xarok.musicvisualizer;

public interface ProgressListener {
    void onProgressChange(int currentFrame, int allFrames);
    void onComplete();
}
