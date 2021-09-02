package br.com.logique.starter.config.converter;

import br.com.logique.starter.util.DataUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Optional;

@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        return Optional.ofNullable(zonedDateTime)
                .map(data -> data.withZoneSameInstant(DataUtil.TIMEZONE_UTC))
                .map(data -> Timestamp.valueOf(data.toLocalDateTime()))
                .orElse(null);
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp timestamp) {
        return Optional.ofNullable(timestamp)
                .map(data -> ZonedDateTime.of(data.toLocalDateTime(), DataUtil.TIMEZONE_UTC))
                .map(data -> data.withZoneSameInstant(DataUtil.TIMEZONE_BRASIL))
                .orElse(null);
    }
}
