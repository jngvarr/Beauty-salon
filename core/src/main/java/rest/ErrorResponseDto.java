package rest;

import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Value
public class ErrorResponseDto {
    String message;
    UUID uuid = UUID.randomUUID();
    List<ErrorData> errors;

    public ErrorResponseDto(String code) {
        this(code, code);
    }

    public ErrorResponseDto(String code, String message) {
        this(new ErrorData(code, message));
    }

    public ErrorResponseDto(List<ErrorData> errors) {
        this.errors = List.copyOf(errors);
        message = compileMessage(this.errors);
    }

    public ErrorResponseDto(ErrorData... errors) {
        this.errors = List.of(errors);
        message = compileMessage(this.errors);
    }

    private static String compileMessage(List<ErrorData> errors) {
        List<String> messages = errors.stream()
                .map(ErrorData::getMessage)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        return (messages.size() > 1 ? messages.size() + " error messages:\n" : StringUtils.EMPTY) +
                String.join("\n", messages);
    }

    @Value
    public static class ErrorData {
        String code;
        String message;
    }
}
