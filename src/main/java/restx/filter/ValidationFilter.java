package restx.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.net.MediaType;
import restx.RestxContext;
import restx.RestxFilter;
import restx.RestxHandler;
import restx.RestxHandlerMatch;
import restx.RestxRequest;
import restx.RestxRequestMatch;
import restx.RestxResponse;
import restx.StdRestxRequestMatch;
import restx.factory.Component;
import restx.http.HttpStatus;
import restx.jackson.FrontObjectMapperFactory;
import restx.validation.MethodArgumentNotValidException;

import javax.inject.Named;
import java.io.IOException;

@Component
public class ValidationFilter implements RestxFilter, RestxHandler {

    private ObjectMapper objectMapper;

    public ValidationFilter(@Named(FrontObjectMapperFactory.MAPPER_NAME) ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @SuppressWarnings("Guava")
    public Optional<RestxHandlerMatch> match(RestxRequest req) {
        return Optional.of(new RestxHandlerMatch(new StdRestxRequestMatch(req.getRestxPath()), this));
    }

    @Override
    public void handle(RestxRequestMatch match, RestxRequest req, RestxResponse resp, RestxContext ctx)
            throws IOException {
        try {
            ctx.nextHandlerMatch().handle(req, resp, ctx);
        } catch (MethodArgumentNotValidException e) {
            resp.setStatus(HttpStatus.PRECONDITION_FAILED);
            resp.setContentType(MediaType.JSON_UTF_8.toString());
            objectMapper.writeValue(resp.getOutputStream(), e.getViolations());
        }
    }
}
