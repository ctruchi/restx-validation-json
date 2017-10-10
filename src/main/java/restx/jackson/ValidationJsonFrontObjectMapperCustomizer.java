package restx.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import restx.factory.Component;
import restx.factory.NamedComponent;
import restx.factory.SingleComponentNameCustomizerEngine;

import javax.validation.Path;
import java.io.IOException;

@Component
public class ValidationJsonFrontObjectMapperCustomizer extends SingleComponentNameCustomizerEngine<ObjectMapper> {

    public ValidationJsonFrontObjectMapperCustomizer() {
        super(-10, FrontObjectMapperFactory.NAME);
    }

    @Override
    public NamedComponent<ObjectMapper> customize(NamedComponent<ObjectMapper> namedComponent) {
        namedComponent.getComponent().registerModule(new SimpleModule().addSerializer(Path.class,
                new JsonSerializer<Path>() {
                    @Override
                    public void serialize(Path path, JsonGenerator gen, SerializerProvider serializers)
                            throws IOException {
                        gen.writeString(path.toString());
                    }
                }));
        return namedComponent;
    }
}
