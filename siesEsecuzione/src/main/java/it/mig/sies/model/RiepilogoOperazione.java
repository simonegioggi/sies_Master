package it.mig.sies.model;

import java.util.List;

/**
 * SIES FASE 2 - Classe model che contiene i dati mostrati all'utente al termine dell'operazione di
 * trasferimento
 * 
 * @author Federico Paparoni
 */
public class RiepilogoOperazione extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7263617268588009592L;

	private Soggetto soggetto;
	private List<TitoloGiudiziario> titoloGiudiziarioList;
	private TitoloEsecutivo titoloEsecutivo;

	/**
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto
	 *            the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the titoloGiudiziarioList
	 */
	public List<TitoloGiudiziario> getTitoloGiudiziarioList() {
		return titoloGiudiziarioList;
	}

	/**
	 * @param titoloGiudiziarioList
	 *            the titoloGiudiziarioList to set
	 */
	public void setTitoloGiudiziarioList(List<TitoloGiudiziario> titoloGiudiziarioList) {
		this.titoloGiudiziarioList = titoloGiudiziarioList;
	}

	/**
	 * @return the titoloEsecutivo
	 */
	public TitoloEsecutivo getTitoloEsecutivo() {
		return titoloEsecutivo;
	}

	/**
	 * @param titoloEsecutivo
	 *            the titoloEsecutivo to set
	 */
	public void setTitoloEsecutivo(TitoloEsecutivo titoloEsecutivo) {
		this.titoloEsecutivo = titoloEsecutivo;
	}

}