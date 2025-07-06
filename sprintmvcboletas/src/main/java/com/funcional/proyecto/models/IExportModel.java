package com.funcional.proyecto.models;

public interface IExportModel {
    int countHeader();
    String getHeader(int index);
    boolean isValueArray(int index);
    String getValue(int index);
    int countValueArray();
    String getSubValue(int index);
}
