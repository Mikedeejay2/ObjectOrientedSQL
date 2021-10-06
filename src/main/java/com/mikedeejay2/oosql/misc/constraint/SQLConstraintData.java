package com.mikedeejay2.oosql.misc.constraint;

public class SQLConstraintData {
    protected static final SQLConstraintData NOT_NULL = new SQLConstraintData(SQLConstraint.NOT_NULL);
    protected static final SQLConstraintData UNIQUE = new SQLConstraintData(SQLConstraint.UNIQUE);
    protected static final SQLConstraintData AUTO_INCREMENT = new SQLConstraintData(SQLConstraint.AUTO_INCREMENT);
    protected static final SQLConstraintData PRIMARY_KEY = new SQLConstraintData(SQLConstraint.PRIMARY_KEY);

    protected final SQLConstraint constraint;

    protected final String checkCondition;

    protected final String defaultValue;

    protected final String referenceTable;
    protected final String referenceColumn;

    private SQLConstraintData(SQLConstraint constraint, String checkCondition, String defaultValue, String referenceTable, String referenceColumn) {
        this.constraint = constraint;
        this.checkCondition = checkCondition;
        this.defaultValue = defaultValue;
        this.referenceTable = referenceTable;
        this.referenceColumn = referenceColumn;
    }

    private SQLConstraintData(SQLConstraint constraint) {
        this(constraint, null, null, null, null);
    }

    public String get() {
        return constraint.get();
    }

    public SQLConstraint getConstraint() {
        return constraint;
    }

    public String getCheckCondition() {
        return checkCondition;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getReferenceTable() {
        return referenceTable;
    }

    public String getReferenceColumn() {
        return referenceColumn;
    }

    public boolean isNotNull() {
        return constraint == SQLConstraint.NOT_NULL;
    }

    public boolean isUnique() {
        return constraint == SQLConstraint.UNIQUE;
    }

    public boolean isPrimaryKey() {
        return constraint == SQLConstraint.PRIMARY_KEY;
    }

    public boolean isForeignKey() {
        return constraint == SQLConstraint.FOREIGN_KEY;
    }

    public boolean isCheck() {
        return constraint == SQLConstraint.CHECK;
    }

    public boolean isDefault() {
        return constraint == SQLConstraint.DEFAULT;
    }

    public boolean isAutoIncrement() {
        return constraint == SQLConstraint.AUTO_INCREMENT;
    }

    public boolean isTableConstraint() {
        return constraint.isTableConstraint();
    }

    public boolean useParams() {
        return constraint.useParams();
    }

    public boolean isDataConstraint() {
        return constraint.isDataConstraint();
    }

    public boolean useReference() {
        return constraint.useReference();
    }

    public static SQLConstraintData ofNotNull() {
        return NOT_NULL;
    }

    public static SQLConstraintData ofUnique() {
        return UNIQUE;
    }

    public static SQLConstraintData ofPrimaryKey() {
        return PRIMARY_KEY;
    }

    public static SQLConstraintData ofForeignKey(String referenceTable, String referenceColumn) {
        return new SQLConstraintData(SQLConstraint.FOREIGN_KEY, null, null, referenceTable, referenceColumn);
    }

    public static SQLConstraintData ofCheck(String checkCondition) {
        return new SQLConstraintData(SQLConstraint.CHECK, checkCondition, null, null, null);
    }

    public static SQLConstraintData ofDefault(String defaultValue) {
        return new SQLConstraintData(SQLConstraint.DEFAULT, null, defaultValue, null, null);
    }

    public static SQLConstraintData ofAutoIncrement() {
        return AUTO_INCREMENT;
    }
}
