package br.com.emmanuelneri.app.model;

/**
 * Delegate class for the SysMgmt DACs. Note this is a final class with ONLY
 * static methods so everything must be passed into the methods. Nothing
 * injected.
 */
public final class ProdutoTransforme {

	/** The Constant ZERO. */
	private static final Integer ZERO = 0;

	/**
	 * Fetch objects by request.
	 *
	 * @param sqlSession
	 *            the sql session
	 * @param request
	 *            the request
	 * @param countStatement
	 *            the count statement
	 * @param fetchPagedStatement
	 *            the fetch paged statement
	 * @param response
	 *            the response
	 */
	@SuppressWarnings("unchecked")
	public static ProdutoMaintenanceRequest maintainInsertBase(ProdutoMaintenanceRequest resquest) {

		ProdutoMaintenanceRequest request01 = new ProdutoMaintenanceRequest();

		return request01;

	}
}
