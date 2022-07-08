package thoth_core.thoth_lite.db_lite_structure;

import thoth_core.thoth_lite.EnumPublicator;
import thoth_core.thoth_lite.db_lite_structure.full_structure.StructureDescription;

public enum AvaliableTables
        implements EnumPublicator {
    CountTypes(StructureDescription.CountTypes.TABLE_NAME),
    ExpensesTypes(StructureDescription.ExpensesTypes.TABLE_NAME),
    IncomesTypes(StructureDescription.IncomesTypes.TABLE_NAME),
    OrderStatus(StructureDescription.OrderStatus.TABLE_NAME),
    ProductTypes(StructureDescription.ProductTypes.TABLE_NAME),
    Storing(StructureDescription.Storage.TABLE_NAME),

    Currencies(StructureDescription.Currency.TABLE_NAME),
    Expenses(StructureDescription.Expenses.TABLE_NAME),
    Incomes(StructureDescription.Incomes.TABLE_NAME),
    Orderable(StructureDescription.Orders.TABLE_NAME),
    Partners(StructureDescription.Partners.TABLE_NAME),
    Storagable(StructureDescription.Products.TABLE_NAME),
    Purchasable(StructureDescription.Purchase.NAME),
    Projectable(StructureDescription.Project.NAME);

    private String tableName;

    AvaliableTables(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
