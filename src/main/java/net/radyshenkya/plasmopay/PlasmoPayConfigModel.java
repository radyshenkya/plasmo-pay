package net.radyshenkya.plasmopay;

import io.wispforest.owo.config.annotation.Config;

@Config(name = "plasmo-pay-config", wrapperName = "PlasmoPayConfig")
public class PlasmoPayConfigModel {
    public String plasmoRpToken = "";
    public String lastUsedBankCard = "";
}
