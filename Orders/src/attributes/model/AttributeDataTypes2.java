package attributes.model;


import java.util.logging.Logger;

public enum  AttributeDataTypes2 {
    Int("4348", "Целое"),
    Text("4350", "Текст"),
    Float("4346", "Число"),
    Boolean("4337", "Логический"),
    DateTime("4342", "Дата"),
    String("4300", "Строка");

    private static Logger _log = Logger.getLogger(AttributeDataTypes2.class.getName());
    private String code;
    private String label;

    private AttributeDataTypes2(String code, String label)   {
        this.code = code;
        this.label = label;
    }
    public static AttributeDataTypes2 getAttributeDataTypes2(String code) {
        /*_log.info("Переданный КОД типа данных: " + code);*/
        for (AttributeDataTypes2 dataType : AttributeDataTypes2.values()) {
            if (dataType.code.equals(code)) {
                 return dataType;
            }
        }
        return null;
    }
    public static AttributeDataTypes2 getAttributeDataTypes2Label(String label) {
        _log.info("Входящее имя типа данных: " + label);
        for (AttributeDataTypes2 dataType : AttributeDataTypes2.values()) {
            if (dataType.name().equals(label)) {
                _log.info("Найден тип данных по имени: " + dataType.getCode());
                return dataType;
            }else {_log.info("Тип данных по имени НЕ НАЙДЕН!" + label + "::" + dataType.name());}
        }
        return null;
    }
    public String getCode() {
        return this.code;
    }

    public String getLabel() {
        return this.label;
    }
}
