package com.luna.framework.excel;

public interface ISheet {
    IRow getRow(int rowIndex);

    int getLastRowNum();
}
