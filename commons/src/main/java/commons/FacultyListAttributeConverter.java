package commons;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA converter for the Faculty value object.
 */
@Converter
public class FacultyListAttributeConverter implements AttributeConverter<List<Faculty>, String> {
    @Override
    public String convertToDatabaseColumn(List<Faculty> faculties) {
        String dbvalue = "";

        for (Faculty f : faculties) {
            dbvalue += f.toString() + ";";
        }
        return dbvalue;
    }

    @Override
    public List<Faculty> convertToEntityAttribute(String dbData) {
        String[] faculties = dbData.split(";");
        List<Faculty> fac = new ArrayList<>();
        for (String f : faculties) {
            fac.add(new Faculty(f));
        }
        return fac;
    }
}
