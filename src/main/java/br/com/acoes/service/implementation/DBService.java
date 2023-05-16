package br.com.acoes.service.implementation;

import java.text.ParseException;

import org.springframework.stereotype.Service;

@Service
public class DBService {

//	private static final String URL_OAUTH_HOMOL_BANCO_1 = "https://oauth.sandbox.bb.com.br";
//
//	private static final String URL_OAUTH_PROD_BANCO_1 = "https://oauth.bb.com.br";
//
//	private static final String URL_HOMOL_BANCO_1 = "https://api.bb.com.br/cobrancas/v2";
//
//	private static final String URL_PROD_BANCO_1 = "https://api.hm.bb.com.br/cobrancas/v2";
//	
//	private static final String URL_OAUTH_HOMOL_BANCO_2 = "https://devportal.itau.com.br";
//
//	private static final String URL_OAUTH_PROD_BANCO_2 = "https://sts.itau.com.br/as/token.oauth2";
//
//	private static final String URL_HOMOL_BANCO_2 = "https://devportal.itau.com.br/sandboxapi/cash_management_ext_v2/v2";
//
//	private static final String URL_PROD_BANCO_2 = "https://api.itau.com.br/cash_management/v2";
//
//
//	private static final String CLIENT_SECRET_CONTA_2 = "3940d769-2108-43ae-afad-f1417224e6ce";
//
//	private static final String CLIENT_SECRET_CONTA_1 = "eyJpZCI6IjMwMGE2ZjAtYWUxOS00NWQ4LTg2OGEtMjBmNzA2IiwiY29kaWdvUHVibGljYWRvciI6MCwiY29kaWdvU29mdHdhcmUiOjI1NzI2LCJzZXF1ZW5jaWFsSW5zdGFsYWNhbyI6MSwic2VxdWVuY2lhbENyZWRlbmNpYWwiOjEsImFtYmllbnRlIjoiaG9tb2xvZ2FjYW8iLCJpYXQiOjE2Mzc4NDg1OTM2MTJ9";
//
//	private static final String CLIENT_ID_CONTA_2 = "5e995e1a-4d62-4c61-8a3b-6ced492c1371";
//
//	private static final String CLIENT_ID_CONTA_1 = "eyJpZCI6IjE3YjI4ZjEtOWVmOS00NWYyLWFlZDgtMDU5NWYiLCJjb2RpZ29QdWJsaWNhZG9yIjowLCJjb2RpZ29Tb2Z0d2FyZSI6MjU3MjYsInNlcXVlbmNpYWxJbnN0YWxhY2FvIjoxfQ";
//
//	@Autowired
//	private ContaRepository contaRepository;
//	
//	@Autowired
//	private BancoRepository bancoRepository;
//
//	@Autowired
//	private ParceiroRepository parceiroRepository;
//
//	@Autowired
//	private TermoRepository termoRepository;
//
//	@Autowired
//	private ServicoRepository servicoRepository;
//
//	@Autowired
//	private ContratoRepository contratoRepository;
//	
//	@Autowired
//	private LogEnvioRepository logEnvioRepository;
//
	public void instantiateTestDatabase() throws ParseException {}
//		
//		Banco banco_1 = new Banco(1, "Banco do Brasil S.A", URL_PROD_BANCO_1, URL_HOMOL_BANCO_1, URL_OAUTH_PROD_BANCO_1, URL_OAUTH_HOMOL_BANCO_1, true);
//		Banco banco_2 = new Banco(341, "Banco Itaú S.A.", URL_PROD_BANCO_2, URL_HOMOL_BANCO_2, URL_OAUTH_PROD_BANCO_2, URL_OAUTH_HOMOL_BANCO_2, true);
//		bancoRepository.saveAll(Arrays.asList(banco_1, banco_2));
//
//		Servico servico_1 = new Servico(1, "Boleto Rápido por API - Banco do Brasil", 0.04);
//		Servico servico_2 = new Servico(2, "Boleto Rápido por API - Banco Itaú", 0.1);
//		
//		servicoRepository.saveAll(Arrays.asList(servico_1, servico_2));
//		
//		Parceiro parceiro_1 = new Parceiro(2, "João Miguel");
//		Parceiro parceiro_2 = new Parceiro(72, "Sankhya Teste");
//		Parceiro parceiro_3 = new Parceiro(647, "SANKHYA GESTÃO DE NEGÓCIOS LTDA");
//		
//		parceiroRepository.saveAll(Arrays.asList(parceiro_1, parceiro_2, parceiro_3));
//		
//		Termo termo_1 = new Termo(1, "<p dir='ltr' style='text-align:center'><strong>Contrato de licen&ccedil;a de uso do servi&ccedil;o Boleto R&aacute;pido por API</strong></p>", LocalDateTime.now());
//		termoRepository.saveAll(Arrays.asList(termo_1));
//		
//		Audit audit_1 = new Audit(LocalDateTime.now(), LocalDateTime.now());
//
//		Conta conta_1 = new Conta();
//			conta_1.setId(1);
//			conta_1.setBanco(banco_1);
//			conta_1.setCodage(981);
//			conta_1.setCodcta(22);
//			conta_1.setCodctabco(54786);
//			conta_1.setConvenio(3228569);
//			conta_1.setCarteira(190);
//			conta_1.setVariacao(35);
//			conta_1.setModalidade(1);
//			conta_1.setUltnumbol("5999964000");
//			conta_1.setStatusapi("S");
//			conta_1.setRegistrobase(1);
//			conta_1.setClientid(CLIENT_ID_CONTA_1);
//			conta_1.setClientsecret(CLIENT_SECRET_CONTA_1);
//			conta_1.setDataDesativacao(LocalDateTime.now());
//			conta_1.setIndicadorPix(null);
//			conta_1.setAudit(audit_1);
//			conta_1.setParceiro(parceiro_3);
//
//		Conta conta_2 = new Conta();
//			conta_2.setId(2);
//			conta_2.setBanco(banco_2);
//			conta_2.setCodage(148);
//			conta_2.setCodcta(4);
//			conta_2.setCodctabco(979313);
//			conta_2.setConvenio(null);
//			conta_2.setCarteira(17);
//			conta_2.setVariacao(150);
//			conta_2.setModalidade(1);
//			conta_2.setUltnumbol("99900950");
//			conta_2.setStatusapi("S");
//			conta_2.setRegistrobase(1);
//			conta_2.setClientid(CLIENT_ID_CONTA_2);
//			conta_2.setClientsecret(CLIENT_SECRET_CONTA_2);
//			conta_2.setDataDesativacao(LocalDateTime.now());
//			conta_2.setIndicadorPix(null);
//			conta_2.setAudit(audit_1);
//			conta_2.setParceiro(parceiro_3);
//
//		contaRepository.saveAll(Arrays.asList(conta_1, conta_2));
//		
//		Contrato contrato_1 = new Contrato(1, conta_1, parceiro_2, servico_1, termo_1, audit_1, 0.1, parceiro_2.getNome(), "S");
//		Contrato contrato_2 = new Contrato(2, conta_2, parceiro_3, servico_1, termo_1, audit_1, 0.04, parceiro_3.getNome(), "S");
//		contratoRepository.saveAll(Arrays.asList(contrato_1, contrato_2));
//		
//		LogEnvio logEnvio_1 = new LogEnvio(1, "109991509601", null, null, "O teste das credenciais com o banco falhou", null, 3, 2, null);
//		LogEnvio logEnvio_2 = new LogEnvio(2, "109991509742", java.sql.Timestamp.valueOf("2023-01-18 13:47:46.033"), "Retorno recebido do Banco", "As credenciais inseridas apresentaram erro no teste de integração. Por favor, tente novamente", 1, 3, 2, 1);
//		LogEnvio logEnvio_3 = new LogEnvio(3, "00031778812605", java.sql.Timestamp.valueOf("2023-01-19 12:29:28.278"), "Emissão de boleto enviada para o Banco", null, 0, 0, 1, 0);
//		LogEnvio logEnvio_4 = new LogEnvio(4, "00031778812604", java.sql.Timestamp.valueOf("2023-01-19 12:59:15.260"), "Emissão de boleto enviada para o Banco", "Status 401: Não autorizado", 0, 0, 2, 0);
//		logEnvioRepository.saveAll(Arrays.asList(logEnvio_1, logEnvio_2, logEnvio_3, logEnvio_4));
//		
//	}
	
}
