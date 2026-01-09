package it.mig.sies.model;

import java.io.Serial;

/**
 * MEV 16 CUMULO: aggiunta classe modello per le richieste GE associate al cumulo
 * 
 * @author Gioggi
 *
 */
public class ConversionePPCumulo extends BaseModel {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 743421215791286543L;

	private int giorniLavoroSostitutivo;
	private int mesiLavoroSostitutivo;
	private int anniLavoroSostitutivo;
	private int giorniLibertaControllata;
	private int mesiLibertaControllata;
	private int anniLibertaControllata;

	/**
	 * @return the giorniLavoroSostitutivo
	 */
	public int getGiorniLavoroSostitutivo() {
		return giorniLavoroSostitutivo;
	}

	/**
	 * @param giorniLavoroSostitutivo
	 *            the giorniLavoroSostitutivo to set
	 */
	public void setGiorniLavoroSostitutivo(int giorniLavoroSostitutivo) {
		this.giorniLavoroSostitutivo = giorniLavoroSostitutivo;
	}

	/**
	 * @return the mesiLavoroSostitutivo
	 */
	public int getMesiLavoroSostitutivo() {
		return mesiLavoroSostitutivo;
	}

	/**
	 * @param mesiLavoroSostitutivo
	 *            the mesiLavoroSostitutivo to set
	 */
	public void setMesiLavoroSostitutivo(int mesiLavoroSostitutivo) {
		this.mesiLavoroSostitutivo = mesiLavoroSostitutivo;
	}

	/**
	 * @return the anniLavoroSostitutivo
	 */
	public int getAnniLavoroSostitutivo() {
		return anniLavoroSostitutivo;
	}

	/**
	 * @param anniLavoroSostitutivo
	 *            the anniLavoroSostitutivo to set
	 */
	public void setAnniLavoroSostitutivo(int anniLavoroSostitutivo) {
		this.anniLavoroSostitutivo = anniLavoroSostitutivo;
	}

	/**
	 * @return the giorniLibertaControllata
	 */
	public int getGiorniLibertaControllata() {
		return giorniLibertaControllata;
	}

	/**
	 * @param giorniLibertaControllata
	 *            the giorniLibertaControllata to set
	 */
	public void setGiorniLibertaControllata(int giorniLibertaControllata) {
		this.giorniLibertaControllata = giorniLibertaControllata;
	}

	/**
	 * @return the mesiLibertaControllata
	 */
	public int getMesiLibertaControllata() {
		return mesiLibertaControllata;
	}

	/**
	 * @param mesiLibertaControllata
	 *            the mesiLibertaControllata to set
	 */
	public void setMesiLibertaControllata(int mesiLibertaControllata) {
		this.mesiLibertaControllata = mesiLibertaControllata;
	}

	/**
	 * @return the anniLibertaControllata
	 */
	public int getAnniLibertaControllata() {
		return anniLibertaControllata;
	}

	/**
	 * @param anniLibertaControllata
	 *            the anniLibertaControllata to set
	 */
	public void setAnniLibertaControllata(int anniLibertaControllata) {
		this.anniLibertaControllata = anniLibertaControllata;
	}

}