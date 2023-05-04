package br.com.boleto.persistence.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcaoRetornoDto4 {
	private String symbol;
	private String shortName;
	private String longName;
	private String currency;
	private Double regularMarketPrice;
	private Integer regularMarketDayHigh;
	private Integer regularMarketDayLow;
	private String regularMarketDayRange;
	private Double regularMarketChange;
	private Double regularMarketChangePercent;
	private String regularMarketTime;
	private Long marketCap;
	private Integer regularMarketVolume;
	private Double regularMarketPreviousClose;
	private Integer regularMarketOpen;
	private Long averageDailyVolume10Day;
	private Long averageDailyVolume3Month;
	private Double fiftyTwoWeekLowChange;
	private String fiftyTwoWeekRange;
	private Double fiftyTwoWeekHighChange;
	private Double fiftyTwoWeekHighChangePercent;
	private Integer fiftyTwoWeekLow;
	private Double fiftyTwoWeekHigh;
	private Double twoHundredDayAverage;
	private Double twoHundredDayAverageChange;
	private Double twoHundredDayAverageChangePercent;
}
