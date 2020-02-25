package it.mig.sies.model;

/**
 * MEV 16 CUMULO: aggiunta classe modello per le PA associate al cumulo
 * 
 * @author Gioggi
 *
 */
public class PenaAccessoriaCumulo extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7760082459681216118L;

	private String codiceTipoPA;
	private String codiceTipoDurataPA;
	private int numeroAnniPA;
	private int numeroMesiPA;
	private int numeroGiorniPA;

	/**
	 * @return the codiceTipoPA
	 */
	public String getCodiceTipoPA() {
		return codiceTipoPA;
	}

	/**
	 * @param codiceTipoPA
	 *            the codiceTipoPA to set
	 */
	public void setCodiceTipoPA(String codiceTipoPA) {
		this.codiceTipoPA = codiceTipoPA;
	}

	/**
	 * @return the codiceTipoDurataPA
	 */
	public String getCodiceTipoDurataPA() {
		return codiceTipoDurataPA;
	}

	/**
	 * @param codiceTipoDurataPA
	 *            the codiceTipoDurataPA to set
	 */
	public void setCodiceTipoDurataPA(String codiceTipoDurataPA) {
		this.codiceTipoDurataPA = codiceTipoDurataPA;
	}

	/**
	 * @return the numeroAnniPA
	 */
	public int getNumeroAnniPA() {
		return numeroAnniPA;
	}

	/**
	 * @param numeroAnniPA
	 *            the numeroAnniPA to set
	 */
	public void setNumeroAnniPA(int numeroAnniPA) {
		this.numeroAnniPA = numeroAnniPA;
	}

	/**
	 * @return the numeroMesiPA
	 */
	public int getNumeroMesiPA() {
		return numeroMesiPA;
	}

	/**
	 * @param numeroMesiPA
	 *            the numeroMesiPA to set
	 */
	public void setNumeroMesiPA(int numeroMesiPA) {
		this.numeroMesiPA = numeroMesiPA;
	}

	/**
	 * @return the numeroGiorniPA
	 */
	public int getNumeroGiorniPA() {
		return numeroGiorniPA;
	}

	/**
	 * @param numeroGiorniPA
	 *            the numeroGiorniPA to set
	 */
	public void setNumeroGiorniPA(int numeroGiorniPA) {
		this.numeroGiorniPA = numeroGiorniPA;
	}

}