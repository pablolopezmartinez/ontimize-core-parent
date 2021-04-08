package com.ontimize.windows.office;

public final class ExecutionResult {

    int result = 0;

    String output = "";

    byte[] bytes = null;

    public ExecutionResult(int result, String output) {
        this.result = result;
        this.output = output;
        if (output != null) {
            this.bytes = output.getBytes();
        }
    }

    public ExecutionResult(int result, byte[] bytes) {
        this.result = result;
        this.bytes = bytes;
        if (bytes != null) {
            this.output = new String(bytes);
        }
    }

    public int getResult() {
        return this.result;
    }

    public String getOuput() {
        return this.output;
    }

    public String getOutput() {
        return this.output;
    }

    public byte[] getOutputBytes() {
        return this.bytes;
    }

}
