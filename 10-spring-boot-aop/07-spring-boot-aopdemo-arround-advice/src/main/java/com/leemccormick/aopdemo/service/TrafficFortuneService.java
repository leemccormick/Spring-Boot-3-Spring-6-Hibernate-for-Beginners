package com.leemccormick.aopdemo.service;

public interface TrafficFortuneService {
    String getFortune();

    String getFortune(boolean tripWire);

    String getFortuneRethrow(boolean tripWire);
}
