package it.mig.sies.model;

import java.io.Serial;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SIES FASE 2 - Classe model relativa al soggetto
 * 
 * @author Federico Paparoni
 */
public class Soggetto extends BaseModel {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -5739180165634539571L;

	private long chiaveSies;
	private long chiaveNSC;
	private String cognome;
	private String nome;
	private String luogoNascita;
	private String nazioneNascita;
	private String descComuneEsteroNascita;
	private String descNazioneNascita;
	private Date dataNascita;
	private String sesso;
	private String codiceFiscale;
	private String codiceAfis;
	private String paternita;
	private String descComuneNascita;
	private String output;
	// MEV 16 + 31: aggiunta variabile per gestire l'elenco dei sinonimi
	private String flagAliasRichiamo;

	/**
	 * @return the chiaveSies
	 */
	public long getChiaveSies() {
		return chiaveSies;
	}

	/**
	 * @param chiaveSies
	 *            the chiaveSies to set
	 */
	public void setChiaveSies(long chiaveSies) {
		this.chiaveSies = chiaveSies;
	}

	/**
	 * @return the chiaveNSC
	 */
	public long getChiaveNSC() {
		return chiaveNSC;
	}

	/**
	 * @param chiaveNSC
	 *            the chiaveNSC to set
	 */
	public void setChiaveNSC(long chiaveNSC) {
		this.chiaveNSC = chiaveNSC;
	}

	/**
	 * @return the cognome
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * @param cognome
	 *            the cognome to set
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the luogoNascita
	 */
	public String getLuogoNascita() {
		return luogoNascita;
	}

	/**
	 * @param luogoNascita
	 *            the luogoNascita to set
	 */
	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	/**
	 * @return the nazioneNascita
	 */
	public String getNazioneNascita() {
		return nazioneNascita;
	}

	/**
	 * @param nazioneNascita
	 *            the nazioneNascita to set
	 */
	public void setNazioneNascita(String nazioneNascita) {
		this.nazioneNascita = nazioneNascita;
	}

	/**
	 * @return the descComuneEsteroNascita
	 */
	public String getDescComuneEsteroNascita() {
		return descComuneEsteroNascita;
	}

	/**
	 * @param descComuneEsteroNascita
	 *            the descComuneEsteroNascita to set
	 */
	public void setDescComuneEsteroNascita(String descComuneEsteroNascita) {
		this.descComuneEsteroNascita = descComuneEsteroNascita;
	}

	/**
	 * @return the dataNascita
	 */
	public Date getDataNascita() {
		return dataNascita;
	}

	/**
	 * @param dataNascita
	 *            the dataNascita to set
	 */
	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	/**
	 * @return the sesso
	 */
	public String getSesso() {
		return sesso;
	}

	/**
	 * @param sesso
	 *            the sesso to set
	 */
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	/**
	 * @return the codiceFiscale
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	/**
	 * @param codiceFiscale
	 *            the codiceFiscale to set
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	/**
	 * @return the codiceAfis
	 */
	public String getCodiceAfis() {
		return codiceAfis;
	}

	/**
	 * @param codiceAfis
	 *            the codiceAfis to set
	 */
	public void setCodiceAfis(String codiceAfis) {
		this.codiceAfis = codiceAfis;
	}

	/**
	 * @return the paternita
	 */
	public String getPaternita() {
		return paternita;
	}

	/**
	 * @param paternita
	 *            the paternita to set
	 */
	public void setPaternita(String paternita) {
		this.paternita = paternita;
	}

	/**
	 * @return the descComuneNascita
	 */
	public String getDescComuneNascita() {
		return descComuneNascita;
	}

	/**
	 * @param descComuneNascita
	 *            the descComuneNascita to set
	 */
	public void setDescComuneNascita(String descComuneNascita) {
		this.descComuneNascita = descComuneNascita;
	}

	/**
	 * Richiamato in fase di visualizzazione
	 * 
	 * @return the output
	 */
	public String getOutput() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		StringBuffer sb = new StringBuffer();
		sb.append(cognome).append(" / ").append(nome).append(" - ");
		sb.append(format.format(dataNascita)).append(" ");
		if ((descComuneNascita == null) || (descComuneNascita.equals(""))
				|| (descComuneNascita.equals("-"))) {
			if ((descComuneEsteroNascita != null) && (descNazioneNascita != null))
				sb.append(descComuneEsteroNascita).append(" ").append(descNazioneNascita.toUpperCase());
			else {
				if (descComuneEsteroNascita != null)
					sb.append(descComuneEsteroNascita);
				if (descNazioneNascita != null)
					sb.append(descNazioneNascita.toUpperCase());
			}
		} else
			sb.append(descComuneNascita);
		output = sb.toString();
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
	 * @return the descNazioneNascita
	 */
	public String getDescNazioneNascita() {
		return descNazioneNascita;
	}

	/**
	 * @param descNazioneNascita
	 *            the descNazioneNascita to set
	 */
	public void setDescNazioneNascita(String descNazioneNascita) {
		this.descNazioneNascita = descNazioneNascita;
	}

	/**
	 * @return the flagAliasRichiamo
	 */
	public String getFlagAliasRichiamo() {
		return flagAliasRichiamo;
	}

	/**
	 * @param flagAliasRichiamo
	 *            the flagAliasRichiamo to set
	 */
	public void setFlagAliasRichiamo(String flagAliasRichiamo) {
		this.flagAliasRichiamo = flagAliasRichiamo;
	}

}