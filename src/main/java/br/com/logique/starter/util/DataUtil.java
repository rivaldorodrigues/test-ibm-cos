package br.com.logique.starter.util;

import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

/**
 * Classe utilitária para manipulação de data.
 *
 * @author Rivaldo
 * Created on 12/04/2018.
 */
@Slf4j
public final class DataUtil {

    public static final ZoneId TIMEZONE_UTC = ZoneId.of("UTC");
    public static final ZoneId TIMEZONE_BRASIL = ZoneId.of("America/Recife");
    public static final Integer BRAZIL_OFFSET = -3;

    private DataUtil() {
    }

    /**
     * Converte uma data de uma determinada zona para a data e hora local.
     *
     * @param zonedDateTime a data a ser convertida
     * @return a informação de data e hora local
     */
    public static LocalDateTime toLocal(@NotNull ZonedDateTime zonedDateTime) {
        return converterTimeZoneLocal(zonedDateTime).toLocalDateTime();
    }

    /**
     * Converte uma data e hora local para a mesma data e hora com informação de time zone do brasil.
     *
     * @param localDateTime a data a ser convertida
     * @return o ZonedDateTime com a mesma data e hora e time zone do Brasil
     */
    public static ZonedDateTime toZoned(@NotNull LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, TIMEZONE_BRASIL);
    }

    /**
     * Aplica um offset de hora e converte a data e hora para local.
     *
     * @param dia a data contendo as informações do dia desejado
     * @param offsetHora o offset de horas a serem adicionadas a hora já existente no dia
     * @return a informação de data e hora local
     */
    public static LocalDateTime toLocal(ZonedDateTime dia, int offsetHora) {
        ZonedDateTime data = dia.plusHours(offsetHora);
        return toLocal(data);
    }

    /**
     * Recupera os milissegundos até a data desejada em timezone local.
     *
     * @param data a data deseja
     * @return a quantidade de milissegundo
     */
    public static long toLocalMili(@NotNull LocalDateTime data) {
        ZonedDateTime zdt = data.atZone(TIMEZONE_BRASIL);
        return zdt.toInstant().toEpochMilli();
    }

    /**
     * Recupera os milissegundos até a data desejada em timezone local.
     *
     * @param data a data deseja
     * @return a quantidade de milissegundo
     */
    public static long toLocalMiliUTC(@NotNull LocalDateTime data) {
        ZonedDateTime zdt = data.atZone(TIMEZONE_UTC);
        return zdt.toInstant().toEpochMilli();
    }

    /**
     * Recupera os milissegundos até a data desejada em timezone local.
     *
     * @param data a data deseja
     * @return a quantidade de milissegundo
     */
    public static long toLocalMili(@NotNull ZonedDateTime data) {
        return converterTimeZoneLocal(data).toInstant().toEpochMilli();
    }

    /**
     * Converte a data para uma string no formato local contendo a data.
     *
     * @param data    a data a ser convertida
     * @param formato o formato desejado para conversão
     * @return a string que representa a data
     */
    public static String formatarData(LocalDateTime data, FormatStyle formato) {

        if (data != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(formato);
            return data.format(formatter);
        } else {
            return null;
        }
    }

    /**
     * Converte a data para uma string no formato local contendo a data.
     *
     * @param data    a data a ser convertida
     * @param formato o formato desejado para conversão
     * @return a string que representa a data
     */
    public static String formatarData(LocalDate data, String formato) {

        if (data != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
            return data.format(formatter);
        } else {
            return null;
        }
    }

    /**
     * Converte a data para uma string no formato local contendo a data.
     *
     * @param data    a data a ser convertida
     * @param formato o formato desejado para conversão
     * @return a string que representa a data
     */
    public static String formatarDataLocal(@NotNull ZonedDateTime data, FormatStyle formato) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(formato);
        return toLocal(data).format(formatter);
    }

    /**
     * Converte a data para uma string no formato local contendo data e hora.
     *
     * @param data    a data a ser convertida
     * @param formato o formato desejado para conversão
     * @return a string que representa a data
     */
    public static String formatarDataHora(@NotNull LocalDateTime data, FormatStyle formato) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(formato);
        return data.format(formatter);
    }

    /**
     * Converte a data para uma string no formato local contendo data e hora.
     *
     * @param data    a data a ser convertida
     * @param formato o formato desejado para conversão
     * @return a string que representa a data
     */
    public static String formatarDataHoraLocal(@NotNull ZonedDateTime data, FormatStyle formato) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(formato);
        return toLocal(data).format(formatter);
    }

    /**
     * A partir do dia, mês e ano, criar uma data com horário do início do dia em UTC
     *
     * @param ano ano requerido
     * @param mes mês requerido
     * @param dia dia requerido
     * @return
     */
    public static ZonedDateTime dataUTC(Integer ano, Integer mes, Integer dia) {
        return ZonedDateTime.of(ano, mes, dia, 0, 0, 0, 0, TIMEZONE_BRASIL);
    }

    /**
     * Retorna a data desejada no inicio do dia.
     * I.E. as 0 horas do dia
     *
     * @param data a data com dia, mês e ano desejado
     * @return uma nova data no inicio do dia
     */
    public static ZonedDateTime dataNoInicioDoDia(ZonedDateTime data) {
        return data.with(LocalTime.MIN);
    }

    public static ZonedDateTime dataNoFimDoDia(ZonedDateTime data) {
        return data.with(LocalTime.MAX);
    }

    /**
     * Converte uma data para o time zone do Brasil
     * @param data a data a ser convertida
     * @return um novo {@link ZonedDateTime} com a zona do Brasil
     */
    public static ZonedDateTime converterTimeZoneLocal(@NotNull ZonedDateTime data) {
        return data.withZoneSameInstant(TIMEZONE_BRASIL);
    }

    /**
     * Converte uma data para o time zone UTC
     *
     * @param data a data a ser convertida
     * @return um novo {@link ZonedDateTime} com a zona UTC
     */
    public static ZonedDateTime converterTimeZoneUTC(@NotNull ZonedDateTime data) {
        return data.withZoneSameInstant(TIMEZONE_UTC);
    }

    /**
     * Indica o número de dias do mês e ano especificado
     *
     * @param mes valor do mês de 1 a 12
     * @param ano ano da data especificada
     */
    public static int getNumeroDiasDoMes(int mes, int ano) {
        return YearMonth.of(ano, mes).lengthOfMonth();
    }

    /**
     * Recupera uma data no inicio do ano.
     *
     * @param ano o ano em questão
     * @return o {@link ZonedDateTime}
     */
    public static ZonedDateTime dataNoPrimeiroDiaDoAno(int ano) {
        return ZonedDateTime.of(ano, 1, 1, 0, 0, 0, 0, DataUtil.TIMEZONE_BRASIL);
    }

    /**
     * Recupera uma data no final do ano.
     *
     * @param ano o ano em questão
     * @return o {@link ZonedDateTime}
     */
    public static ZonedDateTime dataNoUltimoDiaDoAno(int ano) {
        return ZonedDateTime.of(ano, 12, 31, 23, 59, 59, 999, DataUtil.TIMEZONE_BRASIL);
    }

    /**
     * Recupera uma data no inicio de um mês especificado de determinado ano.
     *
     * @param ano o ano em questão
     * @param mes o mês em questão
     * @return o {@link ZonedDateTime}
     */
    public static ZonedDateTime dataNoPrimeiroDiaDoMes(int ano, int mes) {
        return ZonedDateTime.of(ano, mes, 1, 0, 0, 0, 0, DataUtil.TIMEZONE_BRASIL);
    }

    public static ZonedDateTime dataNoPrimeiroDiaDoMes(YearMonth mesAnoAtual) {
        return ZonedDateTime.of(mesAnoAtual.getYear(), mesAnoAtual.getMonthValue(), 1, 0, 0, 0, 0, DataUtil.TIMEZONE_BRASIL);
    }

    /**
     * Recupera uma data no fim de um mês especificado de determinado ano.
     *
     * @param ano o ano em questão
     * @param mes o mês em questão
     * @return o {@link ZonedDateTime}
     */
    public static ZonedDateTime dataNoUltimoDiaDoMes(int ano, int mes) {
        return ZonedDateTime.of(ano, mes, DataUtil.getNumeroDiasDoMes(mes, ano), 23, 59, 59, 999, DataUtil.TIMEZONE_BRASIL);
    }

    /**
     * Converte um Date para seu correspondente em LocalDateTime
     *
     * @param date {@link Date} a data que será convertida
     */
    public static LocalDateTime fromDate(Date date) {

        if (date != null) {
            return date.toInstant().atZone(TIMEZONE_BRASIL).toLocalDateTime();
        } else {
            return null;
        }

    }

    /**
     * Converte um Date para seu correspondente em LocalDateTime
     *
     * @param date {@link Date} a data que será convertida
     */
    public static LocalDateTime fromDateUTC(Date date) {

        if (date != null) {
            return date.toInstant().atZone(TIMEZONE_UTC).toLocalDateTime();
        } else {
            return null;
        }

    }

    /**
     * Converte um LocalDateTime para seu correspondente em Date
     *
     * @param localDateTime {@link LocalDateTime} a data que será convertida
     */
    public static Date toDate(LocalDateTime localDateTime) {

        Date resultado = null;

        if (localDateTime != null) {
            resultado = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }

        return resultado;
    }

    public static LocalDateTime localNow() {
        return LocalDateTime.now(TIMEZONE_UTC);
    }

    public static ZonedDateTime today() {
        return LocalDateTime.now()
                .atZone(DataUtil.TIMEZONE_BRASIL)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    public static Date quintoDiautilDoMesAtual() {
        return Date.from(today()
                .withDayOfMonth(5)
                .toInstant());
    }

    public static Date quintoDiautilDoMesAnterior() {
        return Date.from(today()
                .withDayOfMonth(5)
                .minusMonths(1)
                .toInstant());
    }

    public static Date quintoDiaUtilDeTresMesesAtras() {
        return Date.from(today()
                .minusMonths(3)
                .withDayOfMonth(5)
                .toInstant());
    }

    public static Date quintoDiaUtilDeQuatroMesesAtras() {
        return Date.from(today()
                .minusMonths(4)
                .withDayOfMonth(5)
                .toInstant());
    }

    public static Date toDate(ZonedDateTime dateTime) {
        Date data = Date.from(dateTime.toInstant());
        return data;
    }

    public static LocalDate toLocalDate(ZonedDateTime dateTime) {
        return dateTime.toLocalDate();
    }

    public static ZonedDateTime toZonedDate(Date date) {
        return ZonedDateTime.of(fromDateUTC(date), TIMEZONE_UTC);
    }

    public static ZonedDateTime toZonedDate(LocalDate date) {
        return date.atStartOfDay(TIMEZONE_UTC);
    }
}
