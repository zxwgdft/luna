package com.luna.framework.excel;

public interface IRow {
    ICell getCell(int cellIndex);

    int getLastCellNum();
}
