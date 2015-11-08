package com.brest.bank.soap;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataFixture {

    //--- addBankDeposit
    public static ServletInputStream addBankDepositSoapRequest(){

        String soapRequest = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<addBankDeposit>" +
                "<depositId>1</depositId>" +
                "<depositName>MockName1</depositName>" +
                "<depositMinTerm>1</depositMinTerm>" +
                "<depositMinAmount>1</depositMinAmount>" +
                "<depositCurrency>USD</depositCurrency>" +
                "<depositInterestRate>1</depositInterestRate>" +
                "<depositAddConditions>conditions</depositAddConditions>" +
                "</addBankDeposit>" +
                "</soap:Body>" +
                "</soap:Envelope>";

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(soapRequest.getBytes(StandardCharsets.UTF_8));
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        return servletInputStream;
    }

    //--- getBankDepositById
    public static ServletInputStream getBankDepositByIdSoapRequest(){

        String soapRequest = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<getBankDepositById>" +
                "<depositId>1</depositId>" +
                "</getBankDepositById>" +
                "</soap:Body>" +
                "</soap:Envelope>";

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(soapRequest.getBytes(StandardCharsets.UTF_8));
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        return servletInputStream;
    }

    //--- getBankDepositByName
    public static ServletInputStream getBankDepositByNameSoapRequest(){

        String soapRequest = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<getBankDepositByName>" +
                "<depositName>MockName1</depositName>" +
                "</getBankDepositByName>" +
                "</soap:Body>" +
                "</soap:Envelope>";

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(soapRequest.getBytes(StandardCharsets.UTF_8));
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        return servletInputStream;
    }

    //--- updateBankDeposit
    public static ServletInputStream updateBankDepositSoapRequest(){

        String soapRequest = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<updateBankDeposit>" +
                "<depositId>1</depositId>" +
                "<depositName>MockName1</depositName>" +
                "<depositMinTerm>1</depositMinTerm>" +
                "<depositMinAmount>1</depositMinAmount>" +
                "<depositCurrency>USD</depositCurrency>" +
                "<depositInterestRate>1</depositInterestRate>" +
                "<depositAddConditions>conditions</depositAddConditions>" +
                "</updateBankDeposit>" +
                "</soap:Body>" +
                "</soap:Envelope>";

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(soapRequest.getBytes(StandardCharsets.UTF_8));
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        return servletInputStream;
    }

    //--- deleteBankDeposit
    public static ServletInputStream deleteBankDepositSoapRequest(){

        String soapRequest = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<removeDeposit>" +
                "<depositId>1</depositId>" +
                "</removeDeposit>" +
                "</soap:Body>" +
                "</soap:Envelope>";

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(soapRequest.getBytes(StandardCharsets.UTF_8));
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        return servletInputStream;
    }

    //--- addBankDepositor
    public static ServletInputStream addBankDepositorSoapRequest(){

        String soapRequest = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<addBankDepositor>" +
                "<depositId>1</depositId>" +
                "<depositorId>1</depositorId>" +
                "<depositorName>MockName11</depositorName>" +
                "<depositorDateDeposit>2015-06-06</depositorDateDeposit>" +
                "<depositorAmountDeposit>1000</depositorAmountDeposit>" +
                "<depositorAmountPlusDeposit>100</depositorAmountPlusDeposit>" +
                "<depositorAmountMinusDeposit>100</depositorAmountMinusDeposit>" +
                "<depositorDateReturnDeposit>2015-07-07</depositorDateReturnDeposit>" +
                "<depositorMarkReturnDeposit>0</depositorMarkReturnDeposit>" +
                "</addBankDepositor>" +
                "</soap:Body>" +
                "</soap:Envelope>";

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(soapRequest.getBytes(StandardCharsets.UTF_8));
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        return servletInputStream;
    }

    //--- getBankDepositorById
    public static ServletInputStream getBankDepositorByIdSoapRequest(){

        String soapRequest = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<getBankDepositorById>" +
                "<depositorId>1</depositorId>" +
                "</getBankDepositorById>" +
                "</soap:Body>" +
                "</soap:Envelope>";

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(soapRequest.getBytes(StandardCharsets.UTF_8));
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        return servletInputStream;
    }

    //--- getBankDepositorByName
    public static ServletInputStream getBankDepositorByNameSoapRequest(){

        String soapRequest = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<getBankDepositorByName>" +
                "<depositorName>MockName11</depositorName>" +
                "</getBankDepositorByName>" +
                "</soap:Body>" +
                "</soap:Envelope>";

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(soapRequest.getBytes(StandardCharsets.UTF_8));
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        return servletInputStream;
    }

    //--- updateBankDepositor
    public static ServletInputStream updateBankDepositorSoapRequest(){

        String soapRequest = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<updateBankDepositor>" +
                "<depositId>1</depositId>" +
                "<depositorId>1</depositorId>" +
                "<depositorName>MockName11</depositorName>" +
                "<depositorDateDeposit>2015-06-06</depositorDateDeposit>" +
                "<depositorAmountDeposit>1000</depositorAmountDeposit>" +
                "<depositorAmountPlusDeposit>100</depositorAmountPlusDeposit>" +
                "<depositorAmountMinusDeposit>100</depositorAmountMinusDeposit>" +
                "<depositorDateReturnDeposit>2015-07-07</depositorDateReturnDeposit>" +
                "<depositorMarkReturnDeposit>0</depositorMarkReturnDeposit>" +
                "</updateBankDepositor>" +
                "</soap:Body>" +
                "</soap:Envelope>";

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(soapRequest.getBytes(StandardCharsets.UTF_8));
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        return servletInputStream;
    }

    //--- removeBankDepositor
    public static ServletInputStream removeBankDepositorSoapRequest(){

        String soapRequest = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<removeDepositor>" +
                "<depositorId>1</depositorId>" +
                "</removeDepositor>" +
                "</soap:Body>" +
                "</soap:Envelope>";

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(soapRequest.getBytes(StandardCharsets.UTF_8));
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        return servletInputStream;
    }
}
