package it.mig.sies.model;

/**
 * MEV 16 CUMULO: aggiunta classe modello per le MS associate al cumulo
 * 
 * @author Gioggi
 *
 */
public class MisuraSicurezzaCumulo extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4679752749478731613L;

	private String codiceTipoMS;
	private String codiceTipoDurataMS;
	private int numeroAnniMS;
	private int numeroMesiMS;
	private int numeroGiorniMS;

	/**
	 * @return the codiceTipoMS
	 */
	public String getCodiceTipoMS() {
		return codiceTipoMS;
	}

	/**
	 * @param codiceTipoMS
	 *            the codiceTipoMS to set
	 */
	public void setCodiceTipoMS(String codiceTipoMS) {
		this.codiceTipoMS = codiceTipoMS;
	}

	/**
	 * @return the codiceTipoDurataMS
	 */
	public String getCodiceTipoDurataMS() {
		return codiceTipoDurataMS;
	}

	/**
	 * @param codiceTipoDurataMS
	 *            the codiceTipoDurataMS to set
	 */
	public void setCodiceTipoDurataMS(String codiceTipoDurataMS) {
		this.codiceTipoDurataMS = codiceTipoDurataMS;
	}

	/**
	 * @return the numeroAnniMS
	 */
	public int getNumeroAnniMS() {
		return numeroAnniMS;
	}

	/**
	 * @param numeroAnniMS
	 *            the numeroAnniMS to set
	 */
	public void setNumeroAnniMS(int numeroAnniMS) {
		this.numeroAnniMS = numeroAnniMS;
	}

	/**
	 * @return the numeroMesiMS
	 */
	public int getNumeroMesiMS() {
		return numeroMesiMS;
	}

	/**
	 * @param numeroMesiMS
	 *            the numeroMesiMS to set
	 */
	public void setNumeroMesiMS(int numeroMesiMS) {
		this.numeroMesiMS = numeroMesiMS;
	}

	/**
	 * @return the numeroGiorniMS
	 */
	public int getNumeroGiorniMS() {
		return numeroGiorniMS;
	}

	/**
	 * @param numeroGiorniMS
	 *            the numeroGiorniMS to set
	 */
	public void setNumeroGiorniMS(int numeroGiorniMS) {
		this.numeroGiorniMS = numeroGiorniMS;
	}

}