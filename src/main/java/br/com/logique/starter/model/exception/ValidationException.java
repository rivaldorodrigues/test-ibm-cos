package br.com.logique.starter.model.exception;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

@Getter
public class ValidationException extends NestedRuntimeException {

    @Nullable
    private final String message;

    private Map<String, List<String>> validation = Maps.newHashMap();

    private ValidationException(@Nullable String message, Map<String, List<String>> validation) {
        super(message);
        this.message = message;
        this.validation = validation;
    }

    public static class Builder {

        @Nullable
        private String message;

        private Map<String, List<String>> validation = Maps.newHashMap();

        public Builder comMensagem(String mensagem) {
            this.message = mensagem;
            return this;
        }

        public Builder addValidationError(String campo, String mensagem) {

            if (!validation.containsKey(campo)) {
                validation.put(campo, Lists.newArrayList());
            }

            validation.get(campo).add(mensagem);

            return this;
        }

        public ValidationException build() {
            return new ValidationException(message, validation);
        }

        public Builder builder() {
            return this;
        }

        public boolean comErro() {
            return !this.validation.isEmpty();
        }
    }
}
