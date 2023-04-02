package commons;

import javax.persistence.AttributeConverter;

/**
 * JPA converter for the Faculty value object.
 */
public class FacultyAttributeConverter implements AttributeConverter<Faculty, String>  {

    @Override
    public String convertToDatabaseColumn(Faculty faculty) {
        return faculty.toString();
    }

    @Override
    public Faculty convertToEntityAttribute(String dbData) {
        return new Faculty(dbData);
    }
}
