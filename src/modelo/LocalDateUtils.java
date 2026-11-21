package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class LocalDateUtils {
	private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private LocalDateUtils() {
    }

    public static String formatar(LocalDate data) {
        return data == null ? "-" : data.format(FORMATO);
    }

    public static LocalDate parse(String texto) {
        return LocalDate.parse(texto, FORMATO);
    }

    public static long diasEntre(LocalDate inicio, LocalDate fim) {
        return ChronoUnit.DAYS.between(inicio, fim);
    }

    public static boolean estaAtrasada(LocalDate dataPrevista) {
        return dataPrevista != null && LocalDate.now().isAfter(dataPrevista);
    }
}
