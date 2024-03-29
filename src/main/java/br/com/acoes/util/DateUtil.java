package br.com.acoes.util;

import static java.util.Objects.isNull;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtil {
		
	public static Timestamp convertStringToTimestampApenasData(String strDate) {
		try {
			StringBuilder dateTime = new StringBuilder(strDate);
			dateTime.append(" 23:00:00");
			DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
			LocalDateTime localDateTime = LocalDateTime.from(formatDateTime.parse(dateTime));
			return Timestamp.valueOf(localDateTime);
		} catch (Exception e) {
			log.error("Erro ao converter string para Timestamp. - Date: {} [Método: convertStringToTimestamp]",
					isNull(strDate) ? "Nulo" : strDate);
			return null;
		}
	}

	public static Timestamp convertStringToTimestamp(String strDate) {
	    try {
	    	if (!strDate.isBlank()) {
	    		SimpleDateFormat dataFormatter = new SimpleDateFormat(Constantes.FORMATO_DATA_PADRAO);
	    		return new Timestamp(dataFormatter.parse(new SimpleDateFormat(Constantes.FORMATO_DATA_PADRAO).format(new SimpleDateFormat(Constantes.FORMATO_DATA_BB).parse(strDate))).getTime());				
			}
	    	return null;
	    } catch (ParseException e) {
	    	log.error("Erro ao converter string para Timestamp. - Date: {} [Método: convertStringToTimestamp]", isNull(strDate)? "Nulo" : strDate);
	    	return null; 
	    }
	}
	
	public static String convertTimeStampToString(Timestamp date) {
		return date != null ? new SimpleDateFormat(Constantes.FORMATO_DATA_BB).format(date) : null;
	}
	
	public static String convertTimeStampToStringItau(Timestamp date) {
		return date != null ? new SimpleDateFormat(Constantes.FORMATO_DATA_ITAU).format(date) : null;
	}
	
	public static String convertTimeStampToStringDth(Timestamp date) {
		return date != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date) : null;
	}
	
	public static String convertLocalDateTimeToString(LocalDateTime date, String pattern) {
		if (date != null) {
			if(pattern == null) {
				Timestamp timestamp = Timestamp.valueOf(date);
				return convertTimeStampToString(timestamp);			
			}
			Timestamp timestamp = Timestamp.valueOf(date);
			return convertTimeStampToString(timestamp, pattern);
		}
		return null;
	}
	
	private static String convertTimeStampToString(Timestamp timestamp, String pattern) {
		return timestamp != null ? new SimpleDateFormat(pattern).format(timestamp) : null;
	}

	public static String convertLocalDateTime2ToString(LocalDateTime date) {
		if (date != null) {
			Timestamp timestamp = Timestamp.valueOf(date);
			return convertTimeStampToStringDth(timestamp);			
		}
		return null;
	}
	
	public static LocalDateTime getDataAtual() {
		return LocalDateTime.now(Constantes.ZONE_ID);
	}
	
	public static boolean canExecute() {

		int razao = DateUtil.getDataAtual().getMinute() % 5;

		if (razao == 0) {
			return true;
		}

		return false;
	}

	public static String convertStringToDateItau(String strDate) {
		try {
			DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate data = LocalDate.parse(strDate.replace(".","/"), formato);
			return data.toString();
		} catch (Exception e) {
			log.error("Erro ao formatar string. - Date: {} [Método: convertStringToDateItau]", isNull(strDate)? "Nulo" : strDate);
			return null;
		}
	}
	
	public static LocalDate converteStringToLocalDate(String data) {
		data = convertStringToDateItau(data);
		DateTimeFormatter parser = DateTimeFormatter.ofPattern(Constantes.FORMATO_DATA_ITAU);
		return LocalDate.parse(data, parser);	
	}
	
	public static String retornaDiasAteDataVencimento(String dataVencimento) {
		Long dias = ChronoUnit.DAYS.between(LocalDate.now(), converteStringToLocalDate(dataVencimento));
		return dias.toString();
	}

	public static String convertDateToString(Date date) {
		return date != null ? new SimpleDateFormat(Constantes.FORMATO_DATA_BB).format(date) : null;
	}
	
	public static String convertLocalDateTimeToString(LocalDateTime date) {
		if (date != null) {
			Timestamp timestamp = Timestamp.valueOf(date);
			return convertTimeStampToString(timestamp);
		}
		return null;
	}
}