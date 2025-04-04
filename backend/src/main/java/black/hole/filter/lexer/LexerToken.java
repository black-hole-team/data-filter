package black.hole.filter.lexer;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Токен
 */
public interface LexerToken {

    /**
     * Возвращает тип токена
     * @return тип токена
     */
    LexerTokenType getType();

    /**
     * Возвращает позицию токена
     * @return позиция токена
     */
    int getPosition();

    /**
     * Возвращает содержимое как строку
     * @return содержимое как строку
     */
    String getAsString();

    /**
     * Возвращает содержимое токена как целочисленное значение
     * @return содержимое токена как целочисленное значение
     */
    default int getAsInt() {
        return Integer.parseInt(getAsString());
    }

    /**
     * Возвращает содержимое токена как целочисленное длинное значение
     * @return содержимое токена как целочисленное длинное значение
     */
    default long getAsLong() {
        return Long.parseLong(getAsString());
    }

    /**
     * Возвращает содержимое токена как дробное значение
     * @return содержимое токена как дробное значение
     */
    default double getAsDouble() {
        return Double.parseDouble(getAsString());
    }

    /**
     * Возвращает содержимое токена как длинное дробное значение
     * @return содержимое токена как длинное дробное значение
     */
    default BigDecimal getAsBigDecimal() {
        return new BigDecimal(getAsString());
    }

    /**
     * Возвращает содержимое токена как длинное числовое значение
     * @return содержимое токена как длинное числовое значение
     */
    default BigInteger getAsBigInteger() {
        return new BigInteger(getAsString());
    }

    /**
     * Возвращает булевое содержимое токена
     * @return булевое содержимое токена
     */
    default Boolean getAsBoolean() {
        return "true".equals(getAsString());
    }

    /**
     * Возвращает содержимое токена как числовое значение
     * @return содержимое токена как числовое значение
     */
    default Number getAsNumber() {
        var input = getAsString();
        var split = input.split("\\.");
        if (split.length == 1) {
            try {
                return Integer.parseInt(split[0]);
            } catch (NumberFormatException ignore1) {
                try {
                    return Long.parseLong(split[0]);
                } catch (NumberFormatException ignore) {
                    return new BigInteger(split[0]);
                }
            }
        } else if (split.length == 2) {
            try {
                return Double.parseDouble(split[0] + "." + split[1]);
            } catch (NumberFormatException ignore1) {
                return new BigInteger(split[0] + "." + split[1]);
            }
        } else {
            return null;
        }
    }
}
