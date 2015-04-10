package com.valueclickbrands.solr.util;

interface Serializable {
    byte[] serialize();
    void unserialize(byte[] ss);
}