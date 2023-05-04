package br.com.boleto.util;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.com.boleto.persistence.entity.BoletoStatusResponse;

public class InMemoryDatabase {

	private final ConcurrentHashMap<BigDecimal, BoletoStatusResponse> cache;
	private final ScheduledExecutorService scheduler;

	public InMemoryDatabase(long timeout, TimeUnit unit) {
    cache = new ConcurrentHashMap<>();
    scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate(this::removeExpiredEntries, 0, timeout, unit);
  }

	public void put(BigDecimal codConta, BoletoStatusResponse boletos) {
		
		boletos.setExpires_at( System.currentTimeMillis() + TimeUnit.HOURS.toMillis(3));
		cache.put(codConta, boletos);
		
	}

	public BoletoStatusResponse get(BigDecimal codConta) {
		return cache.get(codConta);
	}

	private void removeExpiredEntries() {
		long now = System.currentTimeMillis();
		cache.entrySet().removeIf(entry -> isExpired(entry.getValue(), now));
	}

	private boolean isExpired(BoletoStatusResponse boletos, long now) {

		if(boletos.getExpires_at() <= now)
			return true;
		
		return false;
	}
}