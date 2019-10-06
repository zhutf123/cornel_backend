package com.demai.cornel.datasource;

public interface DataSource {
    public static final String MASTER = "master";
    public static final String SLAVE = "slave";

    String getSource();
}
