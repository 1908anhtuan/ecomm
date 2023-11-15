package fontys.individual.school.business.impl.Converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Optional;

public class OptionalSerializer extends JsonSerializer<Optional<?>> {
    @Override
    public void serialize(Optional<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value.isPresent()) {
            serializers.defaultSerializeValue(value.get(), gen);
        } else {
            gen.writeNull();
        }
    }
}