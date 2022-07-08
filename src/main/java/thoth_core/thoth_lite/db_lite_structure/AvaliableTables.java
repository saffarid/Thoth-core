package thoth_core.thoth_lite.db_lite_structure;

import thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription;

public enum AvaliableTables {
    COUNT_TYPES(StructureDescription.CountTypes.TABLE_NAME),
    EXPENSES_TYPES(StructureDescription.ExpensesTypes.TABLE_NAME),
    INCOMES_TYPES(StructureDescription.IncomesTypes.TABLE_NAME),
    ORDER_STATUS(StructureDescription.OrderStatus.TABLE_NAME),
    PRODUCT_TYPES(StructureDescription.ProductTypes.TABLE_NAME),
    STORING(StructureDescription.Storage.TABLE_NAME),

    CURRENCIES(StructureDescription.Currency.TABLE_NAME),
    EXPENSES(StructureDescription.Expenses.TABLE_NAME),
    INCOMES(StructureDescription.Incomes.TABLE_NAME),
    ORDERABLE(StructureDescription.Orders.TABLE_NAME),
    PARTNERS(StructureDescription.Partners.TABLE_NAME),
    STORAGABLE(StructureDescription.Products.TABLE_NAME),
    PURCHASABLE(StructureDescription.Purchase.NAME),
    PROJECTABLE(StructureDescription.Project.NAME);

    private String tableName;
    AvaliableTables(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
