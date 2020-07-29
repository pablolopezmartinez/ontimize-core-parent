package com.ontimize.gui;

public class ExtendedOperationThread extends OperationThread {

    public static final int UNKNOWN = -1;

    protected int estimatedTimeLeft = ExtendedOperationThread.UNKNOWN;

    protected int progressDivisions = Integer.MAX_VALUE;

    protected int currentPosition = 0;

    public ExtendedOperationThread() {
        super();
    }

    public ExtendedOperationThread(String descr) {
        super(descr);
    }

    public int getEstimatedTimeLeft() {
        return this.estimatedTimeLeft;
    }

    public int getProgressDivisions() {
        return this.progressDivisions;
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    public String getEstimagedTimeLeftText() {
        return null;
    }

}
