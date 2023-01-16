package com.luna.his.core.permission.option;

/**
 * 进销存数据权限配置
 *
 * @author TontoZhou
 */
public enum WarehouseOption implements BitOption {

    PRICE_PURCHASE("允许查看下推采购入库单的采购价格", 1),
    COST_SINGLE_ITEM("允许仓库查看单个物品库内成本 ", 2),
    PRICE_APPLY("允许查看领用申请单的物品价格", 3);

    int position;
    String name;

    WarehouseOption(String name, int position) {
        this.position = position;
        this.name = name;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public String getName() {
        return name;
    }
}
