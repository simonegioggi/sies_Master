package it.mig.sies.model;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SIES FASE 2 - Classe model relativa al titolo principale
 * 
 * @author Federico Paparoni
 */
public class TitoloGiudiziario extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4649548299217304601L;

	private BigInteger chiaveSies;
	private BigInteger chiaveNSC;
	private Date dataProvvedimento;
	private String numeroSentenza;
	private int annoSentenza;
	private String codiceAutoritaCentrale;
	private String sedeAutorita;
	private String flagCumulante;
	private String descrizioneAutorita;
	private String descrizioneSedeAutorita;
	private String output;
	// MEV_ENG_31_BIS: aggiunta variabile di classe
	private Date dataImpugnazione;
	// MEV 16 CUMULO: aggiunte variabili di classe
	private BigInteger chiaveAnagraficaSies;
	private BigInteger chiaveAnagraficaNSC;
	private String presenteSIC;
	private String statoTitoloEsecSIC;
	private String alNomeDi;
	private String codiceEsito;
	private String descTipoProvvedimento;

	/**
	 * Richiamato in fase di visualizzazione
	 * 
	 * @return the output
	 */
	public String getOutput() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		output = format.format(dataProvvedimento) + " " + descrizioneAutorita + " " + descrizioneSedeAutorita;
		return output;
	}

	/**
	 * @param output
	 *            the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * @return the chiaveSies
	 */
	public BigInteger getChiaveSies() {
		return chiaveSies;
	}

	/**
	 * @param chiaveSies
	 *            the chiaveSies to set
	 */
	public void setChiaveSies(BigInteger chiaveSies) {
		this.chiaveSies = chiaveSies;
	}

	/**
	 * @return the chiaveNSC
	 */
	public BigInteger getChiaveNSC() {
		return chiaveNSC;
	}

	/**
	 * @param chiaveNSC
	 *            the chiaveNSC to set
	 */
	public void setChiaveNSC(BigInteger chiaveNSC) {
		this.chiaveNSC = chiaveNSC;
	}

	/**
	 * @return the dataProvvedimento
	 */
	public Date getDataProvvedimento() {
		return dataProvvedimento;
	}

	/**
	 * @param dataProvvedimento
	 *            the dataProvvedimento to set
	 */
	public void setDataProvvedimento(Date dataProvvedimento) {
		this.dataProvvedimento = dataProvvedimento;
	}

	/**
	 * @return the numeroSentenza
	 */
	public String getNumeroSentenza() {
		return numeroSentenza;
	}

	/**
	 * @param numeroSentenza
	 *            the numeroSentenza to set
	 */
	public void setNumeroSentenza(String numeroSentenza) {
		this.numeroSentenza = numeroSentenza;
	}

	/**
	 * @return the annoSentenza
	 */
	public int getAnnoSentenza() {
		return annoSentenza;
	}

	/**
	 * @param annoSentenza
	 *            the annoSentenza to set
	 */
	public void setAnnoSentenza(int annoSentenza) {
		this.annoSentenza = annoSentenza;
	}

	/**
	 * @return the codiceAutoritaCentrale
	 */
	public String getCodiceAutoritaCentrale() {
		return codiceAutoritaCentrale;
	}

	/**
	 * @param codiceAutoritaCentrale
	 *            the codiceAutoritaCentrale to set
	 */
	public void setCodiceAutoritaCentrale(String codiceAutoritaCentrale) {
		this.codiceAutoritaCentrale = codiceAutoritaCentrale;
	}

	/**
	 * @return the sedeAutorita
	 */
	public String getSedeAutorita() {
		return sedeAutorita;
	}

	/**
	 * @param sedeAutorita
	 *            the sedeAutorita to set
	 */
	public void setSedeAutorita(String sedeAutorita) {
		this.sedeAutorita = sedeAutorita;
	}

	/**
	 * @return the flagCumulante
	 */
	public String getFlagCumulante() {
		return flagCumulante;
	}

	/**
	 * @param flagCumulante
	 *            the flagCumulante to set
	 */
	public void setFlagCumulante(String flagCumulante) {
		this.flagCumulante = flagCumulante;
	}

	/**
	 * @return the descrizioneAutorita
	 */
	public String getDescrizioneAutorita() {
		return descrizioneAutorita;
	}

	/**
	 * @param descrizioneAutorita
	 *            the descrizioneAutorita to set
	 */
	public void setDescrizioneAutorita(String descrizioneAutorita) {
		this.descrizioneAutorita = descrizioneAutorita;
	}

	/**
	 * @return the descrizioneSedeAutorita
	 */
	public String getDescrizioneSedeAutorita() {
		return descrizioneSedeAutorita;
	}

	/**
	 * @param descrizioneSedeAutorita
	 *            the descrizioneSedeAutorita to set
	 */
	public void setDescrizioneSedeAutorita(String descrizioneSedeAutorita) {
		this.descrizioneSedeAutorita = descrizioneSedeAutorita;
	}

	/**
	 * @return the dataImpugnazione
	 */
	public Date getDataImpugnazione() {
		return dataImpugnazione;
	}

	/**
	 * @param dataImpugnazione
	 *            the dataImpugnazione to set
	 */
	public void setDataImpugnazione(Date dataImpugnazione) {
		this.dataImpugnazione = dataImpugnazione;
	}

	/**
	 * @return the chiaveAnagraficaSies
	 */
	public BigInteger getChiaveAnagraficaSies() {
		return chiaveAnagraficaSies;
	}

	/**
	 * @param chiaveAnagraficaSies
	 *            the chiaveAnagraficaSies to set
	 */
	public void setChiaveAnagraficaSies(BigInteger chiaveAnagraficaSies) {
		this.chiaveAnagraficaSies = chiaveAnagraficaSies;
	}

	/**
	 * @return the chiaveAnagraficaNSC
	 */
	public BigInteger getChiaveAnagraficaNSC() {
		return chiaveAnagraficaNSC;
	}

	/**
	 * @param chiaveAnagraficaNSC
	 *            the chiaveAnagraficaNSC to set
	 */
	public void setChiaveAnagraficaNSC(BigInteger chiaveAnagraficaNSC) {
		this.chiaveAnagraficaNSC = chiaveAnagraficaNSC;
	}

	/**
	 * @return the presenteSIC
	 */
	public String getPresenteSIC() {
		return presenteSIC;
	}

	/**
	 * @param presenteSIC
	 *            the presenteSIC to set
	 */
	public void setPresenteSIC(String presenteSIC) {
		this.presenteSIC = presenteSIC;
	}

	/**
	 * @return the statoTitoloEsecSIC
	 */
	public String getStatoTitoloEsecSIC() {
		return statoTitoloEsecSIC;
	}

	/**
	 * @param statoTitoloEsecSIC
	 *            the statoTitoloEsecSIC to set
	 */
	public void setStatoTitoloEsecSIC(String statoTitoloEsecSIC) {
		this.statoTitoloEsecSIC = statoTitoloEsecSIC;
	}

	/**
	 * @return the alNomeDi
	 */
	public String getAlNomeDi() {
		return alNomeDi;
	}

	/**
	 * @param alNomeDi
	 *            the alNomeDi to set
	 */
	public void setAlNomeDi(String alNomeDi) {
		this.alNomeDi = alNomeDi;
	}

	/**
	 * @return Ritorna la data del provvedimento del soggetto nel formato gg/mm/aaaa
	 */
	public String getDataProvvedimentoFormat() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// valore di ritorno
		return dateFormat.format(dataProvvedimento);
	}

	/**
	 * @return the codiceEsito
	 */
	public String getCodiceEsito() {
		return codiceEsito;
	}

	/**
	 * @param codiceEsito
	 *            the codiceEsito to set
	 */
	public void setCodiceEsito(String codiceEsito) {
		this.codiceEsito = codiceEsito;
	}

	/**
	 * @return the descTipoProvvedimento
	 */
	public String getDescTipoProvvedimento() {
		return descTipoProvvedimento;
	}

	/**
	 * @param descTipoProvvedimento
	 *            the descTipoProvvedimento to set
	 */
	public void setDescTipoProvvedimento(String descTipoProvvedimento) {
		this.descTipoProvvedimento = descTipoProvvedimento;
	}

}