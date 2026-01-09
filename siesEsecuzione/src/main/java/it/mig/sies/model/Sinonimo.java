package it.mig.sies.model;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MEV 16 + MEV 31- Classe model relativa al sinonimo
 * 
 * @author Simone Gioggi
 * @version 1.0
 */
public class Sinonimo extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5366131803071215804L;

	private BigInteger idSinonimo;
	private String cognomeSinonimo;
	private String nomeSinonimo;
	private String codiceLuogoNascitaSinonimo;
	private String descLuogoNascitaSinonimo;
	private String codiceNazioneNascitaSinonimo;
	private String descNazioneNascitaSinonimo;
	private String descComuneEsteroNascitaSinonimo;
	private Date dataNascitaSinonimo;
	private String sessoSinonimo;
	private String codiceFiscaleSinonimo;
	private String codiceIdentificativoSinonimo;
	private String paternitaSinonimo;
	// private String outputSinonimo;
	private byte[] certificatoControlloSinonimo;
	private long idCertificatoControlloSinonimo;
	private String flagAliasRichiamoSinonimo;
	private String isEqualCognome;
	private String isEqualNome;
	private String isEqualLuogoNascita;
	private String isEqualDataNascita;
	private String isEqualSesso;
	private String isEqualPaternita;
	private String isEqualCodiceFiscale;
	private String isEqualFlagAliasRichiamo;

	/**
	 * @return the outputSinonimo
	 */
	// public String getOutputSinonimo() {
	// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	// StringBuffer sb = new StringBuffer();
	// sb.append(cognomeSinonimo).append(" / ").append(nomeSinonimo).append(" - ");
	// if ((descLuogoNascitaSinonimo == null) || (descLuogoNascitaSinonimo.equals(""))
	// || (descLuogoNascitaSinonimo.equals("-"))) {
	// if ((descComuneEsteroNascitaSinonimo != null) && (descNazioneNascitaSinonimo != null))
	// sb.append(descComuneEsteroNascitaSinonimo).append(" (")
	// .append(descNazioneNascitaSinonimo.toUpperCase()).append(") - ");
	// else {
	// if (descComuneEsteroNascitaSinonimo != null)
	// sb.append(descComuneEsteroNascitaSinonimo).append(" - ");
	// if (descNazioneNascitaSinonimo != null)
	// sb.append(descNazioneNascitaSinonimo.toUpperCase()).append(" - ");
	// }
	// } else
	// sb.append(descLuogoNascitaSinonimo).append(" - ");
	// sb.append(sdf.format(dataNascitaSinonimo)).append(" - ");
	// sb.append(sessoSinonimo);
	// if (PropertyUtil.isPresent(paternitaSinonimo))
	// sb.append(" - ").append(paternitaSinonimo);
	// if (PropertyUtil.isPresent(codiceFiscaleSinonimo))
	// sb.append(" - ").append(codiceFiscaleSinonimo);
	// else if (PropertyUtil.isPresent(codiceIdentificativoSinonimo))
	// sb.append(" - ").append(codiceIdentificativoSinonimo);
	//
	// outputSinonimo = sb.toString();
	// return outputSinonimo;
	// }

	/**
	 * @param outputSinonimo
	 *            the outputSinonimo to set
	 */
	// public void setOutputSinonimo(String outputSinonimo) {
	// this.outputSinonimo = outputSinonimo;
	// }

	/**
	 * @return the idSinonimo
	 */
	public BigInteger getIdSinonimo() {
		return idSinonimo;
	}

	/**
	 * @param idSinonimo
	 *            the idSinonimo to set
	 */
	public void setIdSinonimo(BigInteger idSinonimo) {
		this.idSinonimo = idSinonimo;
	}

	/**
	 * @return the cognomeSinonimo
	 */
	public String getCognomeSinonimo() {
		return cognomeSinonimo;
	}

	/**
	 * @param cognomeSinonimo
	 *            the cognomeSinonimo to set
	 */
	public void setCognomeSinonimo(String cognomeSinonimo) {
		this.cognomeSinonimo = cognomeSinonimo;
	}

	/**
	 * @return the nomeSinonimo
	 */
	public String getNomeSinonimo() {
		return nomeSinonimo;
	}

	/**
	 * @param nomeSinonimo
	 *            the nomeSinonimo to set
	 */
	public void setNomeSinonimo(String nomeSinonimo) {
		this.nomeSinonimo = nomeSinonimo;
	}

	/**
	 * @return the codiceLuogoNascitaSinonimo
	 */
	public String getCodiceLuogoNascitaSinonimo() {
		return codiceLuogoNascitaSinonimo;
	}

	/**
	 * @param codiceLuogoNascitaSinonimo
	 *            the codiceLuogoNascitaSinonimo to set
	 */
	public void setCodiceLuogoNascitaSinonimo(String codiceLuogoNascitaSinonimo) {
		this.codiceLuogoNascitaSinonimo = codiceLuogoNascitaSinonimo;
	}

	/**
	 * @return the descLuogoNascitaSinonimo
	 */
	public String getDescLuogoNascitaSinonimo() {
		return descLuogoNascitaSinonimo;
	}

	/**
	 * @param descLuogoNascitaSinonimo
	 *            the descLuogoNascitaSinonimo to set
	 */
	public void setDescLuogoNascitaSinonimo(String descLuogoNascitaSinonimo) {
		this.descLuogoNascitaSinonimo = descLuogoNascitaSinonimo;
	}

	/**
	 * @return the codiceNazioneNascitaSinonimo
	 */
	public String getCodiceNazioneNascitaSinonimo() {
		return codiceNazioneNascitaSinonimo;
	}

	/**
	 * @param codiceNazioneNascitaSinonimo
	 *            the codiceNazioneNascitaSinonimo to set
	 */
	public void setCodiceNazioneNascitaSinonimo(String codiceNazioneNascitaSinonimo) {
		this.codiceNazioneNascitaSinonimo = codiceNazioneNascitaSinonimo;
	}

	/**
	 * @return the descNazioneNascitaSinonimo
	 */
	public String getDescNazioneNascitaSinonimo() {
		return descNazioneNascitaSinonimo;
	}

	/**
	 * @param descNazioneNascitaSinonimo
	 *            the descNazioneNascitaSinonimo to set
	 */
	public void setDescNazioneNascitaSinonimo(String descNazioneNascitaSinonimo) {
		this.descNazioneNascitaSinonimo = descNazioneNascitaSinonimo;
	}

	/**
	 * @return the descComuneEsteroNascitaSinonimo
	 */
	public String getDescComuneEsteroNascitaSinonimo() {
		return descComuneEsteroNascitaSinonimo;
	}

	/**
	 * @param descComuneEsteroNascitaSinonimo
	 *            the descComuneEsteroNascitaSinonimo to set
	 */
	public void setDescComuneEsteroNascitaSinonimo(String descComuneEsteroNascitaSinonimo) {
		this.descComuneEsteroNascitaSinonimo = descComuneEsteroNascitaSinonimo;
	}

	/**
	 * @return the dataNascitaSinonimo
	 */
	public Date getDataNascitaSinonimo() {
		return dataNascitaSinonimo;
	}

	/**
	 * @param dataNascitaSinonimo
	 *            the dataNascitaSinonimo to set
	 */
	public void setDataNascitaSinonimo(Date dataNascitaSinonimo) {
		this.dataNascitaSinonimo = dataNascitaSinonimo;
	}

	/**
	 * @return the sessoSinonimo
	 */
	public String getSessoSinonimo() {
		return sessoSinonimo;
	}

	/**
	 * @param sessoSinonimo
	 *            the sessoSinonimo to set
	 */
	public void setSessoSinonimo(String sessoSinonimo) {
		this.sessoSinonimo = sessoSinonimo;
	}

	/**
	 * @return the codiceFiscaleSinonimo
	 */
	public String getCodiceFiscaleSinonimo() {
		return codiceFiscaleSinonimo;
	}

	/**
	 * @param codiceFiscaleSinonimo
	 *            the codiceFiscaleSinonimo to set
	 */
	public void setCodiceFiscaleSinonimo(String codiceFiscaleSinonimo) {
		this.codiceFiscaleSinonimo = codiceFiscaleSinonimo;
	}

	/**
	 * @return the codiceIdentificativoSinonimo
	 */
	public String getCodiceIdentificativoSinonimo() {
		return codiceIdentificativoSinonimo;
	}

	/**
	 * @param codiceIdentificativoSinonimo
	 *            the codiceIdentificativoSinonimo to set
	 */
	public void setCodiceIdentificativoSinonimo(String codiceIdentificativoSinonimo) {
		this.codiceIdentificativoSinonimo = codiceIdentificativoSinonimo;
	}

	/**
	 * @return the paternitaSinonimo
	 */
	public String getPaternitaSinonimo() {
		return paternitaSinonimo;
	}

	/**
	 * @param paternitaSinonimo
	 *            the paternitaSinonimo to set
	 */
	public void setPaternitaSinonimo(String paternitaSinonimo) {
		this.paternitaSinonimo = paternitaSinonimo;
	}

	/**
	 * @return Ritorna la data di nascita del soggetto nel formato gg/mm/aaaa
	 */
	public String getDataNascitaSinonimoFormat() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// valore di ritorno
		return dateFormat.format(dataNascitaSinonimo);
	}

	/**
	 * @return the certificatoControlloSinonimo
	 */
	public byte[] getCertificatoControlloSinonimo() {
		return certificatoControlloSinonimo;
	}

	/**
	 * @param certificatoControlloSinonimo
	 *            the certificatoControlloSinonimo to set
	 */
	public void setCertificatoControlloSinonimo(byte[] certificatoControlloSinonimo) {
		this.certificatoControlloSinonimo = certificatoControlloSinonimo;
	}

	/**
	 * @return the idCertificatoControlloSinonimo
	 */
	public long getIdCertificatoControlloSinonimo() {
		return idCertificatoControlloSinonimo;
	}

	/**
	 * @param idCertificatoControlloSinonimo
	 *            the idCertificatoControlloSinonimo to set
	 */
	public void setIdCertificatoControlloSinonimo(long idCertificatoControlloSinonimo) {
		this.idCertificatoControlloSinonimo = idCertificatoControlloSinonimo;
	}

	/**
	 * @return the isEqualCognome
	 */
	public String getIsEqualCognome() {
		return isEqualCognome;
	}

	/**
	 * @param isEqualCognome
	 *            the isEqualCognome to set
	 */
	public void setIsEqualCognome(String isEqualCognome) {
		this.isEqualCognome = isEqualCognome;
	}

	/**
	 * @return the isEqualNome
	 */
	public String getIsEqualNome() {
		return isEqualNome;
	}

	/**
	 * @param isEqualNome
	 *            the isEqualNome to set
	 */
	public void setIsEqualNome(String isEqualNome) {
		this.isEqualNome = isEqualNome;
	}

	/**
	 * @return the isEqualLuogoNascita
	 */
	public String getIsEqualLuogoNascita() {
		return isEqualLuogoNascita;
	}

	/**
	 * @param isEqualLuogoNascita
	 *            the isEqualLuogoNascita to set
	 */
	public void setIsEqualLuogoNascita(String isEqualLuogoNascita) {
		this.isEqualLuogoNascita = isEqualLuogoNascita;
	}

	/**
	 * @return the isEqualDataNascita
	 */
	public String getIsEqualDataNascita() {
		return isEqualDataNascita;
	}

	/**
	 * @param isEqualDataNascita
	 *            the isEqualDataNascita to set
	 */
	public void setIsEqualDataNascita(String isEqualDataNascita) {
		this.isEqualDataNascita = isEqualDataNascita;
	}

	/**
	 * @return the isEqualSesso
	 */
	public String getIsEqualSesso() {
		return isEqualSesso;
	}

	/**
	 * @param isEqualSesso
	 *            the isEqualSesso to set
	 */
	public void setIsEqualSesso(String isEqualSesso) {
		this.isEqualSesso = isEqualSesso;
	}

	/**
	 * @return the isEqualPaternita
	 */
	public String getIsEqualPaternita() {
		return isEqualPaternita;
	}

	/**
	 * @param isEqualPaternita
	 *            the isEqualPaternita to set
	 */
	public void setIsEqualPaternita(String isEqualPaternita) {
		this.isEqualPaternita = isEqualPaternita;
	}

	/**
	 * @return the isEqualCodiceFiscale
	 */
	public String getIsEqualCodiceFiscale() {
		return isEqualCodiceFiscale;
	}

	/**
	 * @param isEqualCodiceFiscale
	 *            the isEqualCodiceFiscale to set
	 */
	public void setIsEqualCodiceFiscale(String isEqualCodiceFiscale) {
		this.isEqualCodiceFiscale = isEqualCodiceFiscale;
	}

	/**
	 * @return the flagAliasRichiamoSinonimo
	 */
	public String getFlagAliasRichiamoSinonimo() {
		return flagAliasRichiamoSinonimo;
	}

	/**
	 * @param flagAliasRichiamoSinonimo
	 *            the flagAliasRichiamoSinonimo to set
	 */
	public void setFlagAliasRichiamoSinonimo(String flagAliasRichiamoSinonimo) {
		this.flagAliasRichiamoSinonimo = flagAliasRichiamoSinonimo;
	}

	/**
	 * @return the isEqualFlagAliasRichiamo
	 */
	public String getIsEqualFlagAliasRichiamo() {
		return isEqualFlagAliasRichiamo;
	}

	/**
	 * @param isEqualFlagAliasRichiamo
	 *            the isEqualFlagAliasRichiamo to set
	 */
	public void setIsEqualFlagAliasRichiamo(String isEqualFlagAliasRichiamo) {
		this.isEqualFlagAliasRichiamo = isEqualFlagAliasRichiamo;
	}

}