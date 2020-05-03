package morphia2;

import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class BigDecimalConverter extends TypeConverter implements SimpleValueConverter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BigDecimalConverter() {
        super(BigDecimal.class);
    }

    @Override
    protected boolean isSupported(Class<?> c, MappedField optionalExtraInfo) {
        return BigDecimal.class.isAssignableFrom(c);
    }

    @Override
    public Object encode(Object value, MappedField optionalExtraInfo) {
        if (value == null) return null;

        return value.toString();
    }

    @Override
    public Object decode(Class targetClass, Object fromDBObject, MappedField optionalExtraInfo) throws MappingException {
        if (fromDBObject == null) return null;
        try {
            if (fromDBObject instanceof String) {
                return new BigDecimal((String) fromDBObject);
            } else if (fromDBObject instanceof Number) {
                return new BigDecimal(((Number) fromDBObject).doubleValue());
            } else {
                logger.warn(fromDBObject.toString());
            }
        } catch (NumberFormatException e) {
            logger.warn(fromDBObject.toString());
        }
        return null;
    }

}