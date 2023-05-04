package br.com.boleto.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Constantes {
	public static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");
	public static final String FORMATO_DATA_PADRAO = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMATO_DATA = "yyyy-MM-dd";
	public static final String FORMATO_DATA_PADRAO_MS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String FORMATO_DATA_BB = "dd.MM.yyyy";
	protected static final String FORMATO_DATA_ITAU = "yyyy-MM-dd";
	public static final Integer SETE_DIAS = 7;
	public static final LocalDateTime DATA_ATUAL = LocalDateTime.now(ZONE_ID);
}