package suhyeon;

import com.fasterxml.jackson.databind.JsonNode;
import suhyeon.exception.TemplateEngineException;

import java.util.HashMap;
import java.util.Map;

import static suhyeon.exception.TemplateEngineExceptionType.DATA_NOT_NULL;
import static suhyeon.exception.TemplateEngineExceptionType.KEY_NOT_NULL;

public class DataContext {
    private final Map<String, JsonNode> variables = new HashMap<>();

    public DataContext(final String key, final JsonNode data) {
        this.add(key, data);
    }

    public void add(final String key, final JsonNode data) {
        if (key == null) {
            throw new TemplateEngineException(KEY_NOT_NULL);
        }
        if (data == null) {
            throw new TemplateEngineException(DATA_NOT_NULL);
        }

        this.variables.put(key, data);
    }

    public JsonNode get(final String key) {
        if (key == null) {
            throw new TemplateEngineException(KEY_NOT_NULL);
        }
        return this.variables.get(key);
    }

    public void remove(final String key) {
        if (key == null) {
            throw new TemplateEngineException(KEY_NOT_NULL);
        }
        this.variables.remove(key);
    }
}
