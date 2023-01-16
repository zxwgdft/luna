package com.luna.framework.excel.read;

import com.luna.framework.excel.ICell;

public interface ReadPropertyConvert<T> {
	public T convert(ICell cell) throws ExcelReadException;
	
}
