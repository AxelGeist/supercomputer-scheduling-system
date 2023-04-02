package nl.tudelft.sem.template.authentication.domain.user;

import commons.Role;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA Converter for the NetID value object.
 */
@Converter
public class RoleAttributeConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        return role.getAuthority();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        return new Role(dbData);
    }

}
