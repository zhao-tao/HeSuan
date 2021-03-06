package com.sxonecard.http.bean;

/**
 * Created by pc on 2017-10-17.
 */

public class FunctionData {
    private int testNumber;//试管编号.
    private int testSample;//试管样本类型 1:标准 2:未知 3:XX
    private double testConcentration;//试管浓度.
    private String testResult; //时光计算结果.
    private boolean isSetValue; //是否设置了改试管浓度.

    public int getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

    public int getTestSample() {
        return testSample;
    }

    public void setTestSample(int testSample) {
        this.testSample = testSample;
    }

    public double getTestConcentration() {
        return testConcentration;
    }

    public void setTestConcentration(double testConcentration) {
        this.testConcentration = testConcentration;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public boolean isSetValue() {
        return isSetValue;
    }

    public void setSetValue(boolean setValue) {
        isSetValue = setValue;
    }
}
