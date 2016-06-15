package br.com.emmanuelneri.app.model;

public class ProdutoMaintenanceRequest {

	/** Attributes. */
	private ProdutoDto produto;

	/**
	 * The Constructor.
	 */
	public ProdutoMaintenanceRequest() {

	}

	/**
	 * Gets the produto.
	 *
	 * @return the produto
	 */
	public ProdutoDto getProduto() {
		return produto;
	}

	/**
	 * Sets the produto.
	 *
	 * @param produto
	 *            the produto
	 */
	public void setProduto(ProdutoDto produto) {
		this.produto = produto;
	}

	@Override
	public String toString() {
		return "ProdutoMaintenanceRequest [getProduto()=" + getProduto() + ", toString()=" + super.toString() + "]";
	}
}