package attributes.model;


public enum Gender {
    Текст("Текст", "4350"),
    Целое("Целое", "4348"),
    Число("Число", "4346"),
    Логический("Логический", "4346"),
    Дата("Дата", "4342");

    private final String label;
    private final String code;

    private Gender(String label, String code) {
        this.label = label;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return this.label;
    }
}
