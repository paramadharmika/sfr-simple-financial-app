package com.ssudio.sfr.configuration;

public class AppConfiguration implements IAppConfiguration {

    @Override
    public String getBaseUrl() {
        return "http://laporan-kegiatan.xyz/";
    }
}
