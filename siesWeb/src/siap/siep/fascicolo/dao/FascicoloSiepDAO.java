package siap.siep.fascicolo.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.fascicolo.model.FascicoloSiepCertBlobModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: FascicoloSiepDAO
 * </p>
 * <p>
 * Description: Rappresenta l'entità DAO del Fascicolo Siep
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class FascicoloSiepDAO extends SIAPTableDAO {
	public FascicoloSiepDAO(Connection con) {
		super(con);

		setTable("FASCICOLO_SIEP");

		setSequenceField("ID_FASCICOLO_SIEP", "FAS_SIE_SEQ");

		setFieldKey("ID_FASCICOLO_SIEP", BIG_DECIMAL);

		setField("ID_FASCICOLO_SIEP", BIG_DECIMAL);
		setField("CHIAVE_ANNO", BIG_DECIMAL);
		setField("CHIAVE_UFFICIO", STRING);
		setField("CHIAVE_PROGR", BIG_DECIMAL);
		setField("COD_STATO_FASCICOLO", STRING);
		setField("DATA_ISCRIZIONE", DATE);
		setField("DATA_ARCHIVIAZIONE", DATE);
		setField("COD_MOTIVO_ARCHIVIAZIONE", STRING);
		setField("LETTERA_FASCICOLO", STRING);
		setField("ANNO_FASCICOLO_UNIONE", STRING);
		setField("NUM_FASCICOLO_UNIONE", STRING);
		setField("DATA_UNIONE", DATE);
		setField("NOTE", STRING);
		setField("COD_TIPO_POS_LIBERO", STRING);
		setField("FLAG_VALIDATO", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("SOG_ID_SOGGETTO", BIG_DECIMAL);
		setField("SEN_ID_SENTENZA", BIG_DECIMAL);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
		setField("FLAG_ALTRA_CAUSA", STRING);
		setField("DATA_IRREVOCABILITA", DATE);
		setField("FLAG_CUMULANTE", STRING);
		setField("FLAG_CUMULATO", STRING);
		setField("COD_UFFICIO_UNIONE", STRING);
		setField("KEY_PROVV_NSC", BIG_DECIMAL);
		setField("DATA_ARRIVO_ATTO", DATE);
		// Modifica Accorpamento Uffici
		setField("CHIAVE_PROGR_ORIG", BIG_DECIMAL);
		// Modifica MEV 12 (Richiesta Certificato Penale)
		setField("CERTIFICATO_PENALE", TBLOB);
		setField("VISIBILITA_EX_MINORENNE", STRING);
		setField("DATA_ULTIMA_RIAPERTURA", DATE);
		setField("COD_MOTIVO_RIAPERTURA", STRING);
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdFascicoloSiep() throws DAOException {
		return getBigDecimal("ID_FASCICOLO_SIEP");
	}

	public BigDecimal getChiaveAnno() throws DAOException {
		return getBigDecimal("CHIAVE_ANNO");
	}

	public String getChiaveUfficio() throws DAOException {
		return getString("CHIAVE_UFFICIO");
	}

	public BigDecimal getChiaveProgr() throws DAOException {
		return getBigDecimal("CHIAVE_PROGR");
	}

	public String getCodStatoFascicolo() throws DAOException {
		return getString("COD_STATO_FASCICOLO");
	}

	public Date getDataIscrizione() throws DAOException {
		return getDate("DATA_ISCRIZIONE");
	}

	public Date getDataArchiviazione() throws DAOException {
		return getDate("DATA_ARCHIVIAZIONE");
	}

	public String getCodMotivoArchiviazione() throws DAOException {
		return getString("COD_MOTIVO_ARCHIVIAZIONE");
	}

	public String getLetteraFascicolo() throws DAOException {
		return getString("LETTERA_FASCICOLO");
	}

	public String getAnnoFascicoloUnione() throws DAOException {
		return getString("ANNO_FASCICOLO_UNIONE");
	}

	public String getNumFascicoloUnione() throws DAOException {
		return getString("NUM_FASCICOLO_UNIONE");
	}

	public Date getDataUnione() throws DAOException {
		return getDate("DATA_UNIONE");
	}

	public String getNote() throws DAOException {
		return getString("NOTE");
	}

	public String getCodTipoPosLibero() throws DAOException {
		return getString("COD_TIPO_POS_LIBERO");
	}

	public String getFlagValidato() throws DAOException {
		return getString("FLAG_VALIDATO");
	}

	public String getCodOperatoreInserimento() throws DAOException {
		return getString("COD_OPERATORE_INSERIMENTO");
	}

	public Date getDataInserimento() throws DAOException {
		return getDate("DATA_INSERIMENTO");
	}

	public String getCodUfficioInserimento() throws DAOException {
		return getString("COD_UFFICIO_INSERIMENTO");
	}

	public String getCodOperatoreAggiornamento() throws DAOException {
		return getString("COD_OPERATORE_AGGIORNAMENTO");
	}

	public Date getDataAggiornamento() throws DAOException {
		return getDate("DATA_AGGIORNAMENTO");
	}

	public String getCodUfficioAggiornamento() throws DAOException {
		return getString("COD_UFFICIO_AGGIORNAMENTO");
	}

	public BigDecimal getSogIdSoggetto() throws DAOException {
		return getBigDecimal("SOG_ID_SOGGETTO");
	}

	public BigDecimal getSenIdSentenza() throws DAOException {
		return getBigDecimal("SEN_ID_SENTENZA");
	}

	public BigDecimal getFasSieIdFascicoloSiep() throws DAOException {
		return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP");
	}

	public String getFlagAltraCausa() throws DAOException {
		return getString("FLAG_ALTRA_CAUSA");
	}

	public Date getDataIrrevocabilita() throws DAOException {
		return getDate("DATA_IRREVOCABILITA");
	}

	public String getFlagCumulante() throws DAOException {
		return getString("FLAG_CUMULANTE");
	}

	public String getFlagCumulato() throws DAOException {
		return getString("FLAG_CUMULATO");
	}

	public String getCodUfficioUnione() throws DAOException {
		return getString("COD_UFFICIO_UNIONE");
	}

	public BigDecimal getKeyProvvNsc() throws DAOException {
		return getBigDecimal("KEY_PROVV_NSC");
	}

	public Date getDataArrivoAtto() throws DAOException {
		return getDate("DATA_ARRIVO_ATTO");
	}

	public BigDecimal getChiaveProgrOrig() throws DAOException {
		return getBigDecimal("CHIAVE_PROGR_ORIG");
	}

	public ByteArrayOutputStream getCertPenaleBlob() throws DAOException {
		return getBlob("CERTIFICATO_PENALE");
	}

	public String getVisibilitaMinorenne() throws DAOException {
		return getString("VISIBILITA_EX_MINORENNE");
	}

	public Date getDataUltimaRiapertura() throws DAOException {
		return getDate("DATA_ULTIMA_RIAPERTURA");
	}

	public String getCodMotivoRiapertura() throws DAOException {
		return getString("COD_MOTIVO_RIAPERTURA");
	}

	//
	// METODI SET()
	//

	public void setIdFascicoloSiep(BigDecimal aValore) {
		setBigDecimal("ID_FASCICOLO_SIEP", aValore);
	}

	public void setChiaveAnno(BigDecimal aValore) {
		setBigDecimal("CHIAVE_ANNO", aValore);
	}

	public void setChiaveUfficio(String aValore) {
		setString("CHIAVE_UFFICIO", aValore);
	}

	public void setChiaveProgr(BigDecimal aValore) {
		setBigDecimal("CHIAVE_PROGR", aValore);
	}

	public void setCodStatoFascicolo(String aValore) {
		setString("COD_STATO_FASCICOLO", aValore);
	}

	public void setDataIscrizione(Date aValore) {
		setDate("DATA_ISCRIZIONE", aValore);
	}

	public void setDataArchiviazione(Date aValore) {
		setDate("DATA_ARCHIVIAZIONE", aValore);
	}

	public void setCodMotivoArchiviazione(String aValore) {
		setString("COD_MOTIVO_ARCHIVIAZIONE", aValore);
	}

	public void setLetteraFascicolo(String aValore) {
		setString("LETTERA_FASCICOLO", aValore);
	}

	public void setAnnoFascicoloUnione(String aValore) {
		setString("ANNO_FASCICOLO_UNIONE", aValore);
	}

	public void setNumFascicoloUnione(String aValore) {
		setString("NUM_FASCICOLO_UNIONE", aValore);
	}

	public void setDataUnione(Date aValore) {
		setDate("DATA_UNIONE", aValore);
	}

	public void setNote(String aValore) {
		setString("NOTE", aValore);
	}

	public void setCodTipoPosLibero(String aValore) {
		setString("COD_TIPO_POS_LIBERO", aValore);
	}

	public void setFlagValidato(String aValore) {
		setString("FLAG_VALIDATO", aValore);
	}

	public void setCodOperatoreInserimento(String aValore) {
		setString("COD_OPERATORE_INSERIMENTO", aValore);
	}

	public void setDataInserimento(Date aValore) {
		setDate("DATA_INSERIMENTO", aValore);
	}

	public void setCodUfficioInserimento(String aValore) {
		setString("COD_UFFICIO_INSERIMENTO", aValore);
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		setString("COD_OPERATORE_AGGIORNAMENTO", aValore);
	}

	public void setDataAggiornamento(Date aValore) {
		setDate("DATA_AGGIORNAMENTO", aValore);
	}

	public void setCodUfficioAggiornamento(String aValore) {
		setString("COD_UFFICIO_AGGIORNAMENTO", aValore);
	}

	public void setSogIdSoggetto(BigDecimal aValore) {
		setBigDecimal("SOG_ID_SOGGETTO", aValore);
	}

	public void setSenIdSentenza(BigDecimal aValore) {
		setBigDecimal("SEN_ID_SENTENZA", aValore);
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore);
	}

	public void setFlagAltraCausa(String aValore) {
		setString("FLAG_ALTRA_CAUSA", aValore);
	}

	public void setDataIrrevocabilita(Date aValore) {
		setDate("DATA_IRREVOCABILITA", aValore);
	}

	public void setFlagCumulante(String aValore) {
		setString("FLAG_CUMULANTE", aValore);
	}

	public void setFlagCumulato(String aValore) {
		setString("FLAG_CUMULATO", aValore);
	}

	public void setCodUfficioUnione(String aValore) {
		setString("COD_UFFICIO_UNIONE", aValore);
	}

	public void setKeyProvvNsc(BigDecimal aValore) {
		setBigDecimal("KEY_PROVV_NSC", aValore);
	}

	public void setDataArrivoAtto(Date aValore) {
		setDate("DATA_ARRIVO_ATTO", aValore);
	}

	public void setChiaveProgrOrig(BigDecimal aValore) {
		setBigDecimal("CHIAVE_PROGR_ORIG", aValore);
	}

	public void setCertPenaleBlob(ByteArrayInputStream aValore) {
		setBlob("CERTIFICATO_PENALE", aValore);
	}

	public void setVisibilitaMinorenne(String aValore) {
		setString("VISIBILITA_EX_MINORENNE", aValore);
	}

	public void setDataUltimaRiapertura(Date aValore) {
		setDate("DATA_ULTIMA_RIAPERTURA", aValore);
	}

	public void setCodMotivoRiapertura(String aValore) {
		setString("COD_MOTIVO_RIAPERTURA", aValore);
	}

	public GenericModel getModel() throws DAOException {
		return new FascicoloSiepModel(getIdFascicoloSiep(), getChiaveAnno(), getChiaveUfficio(), "", "",
				getChiaveProgr(), getCodStatoFascicolo(), "", getDataIscrizione(), getDataArchiviazione(),
				getCodMotivoArchiviazione(), "", getLetteraFascicolo(), getAnnoFascicoloUnione(),
				getNumFascicoloUnione(), getDataUnione(), getNote(), getCodTipoPosLibero(), "",
				getFlagValidato(), getCodOperatoreInserimento(), getDataInserimento(),
				getCodUfficioInserimento(), getCodOperatoreAggiornamento(), getDataAggiornamento(),
				getCodUfficioAggiornamento(), getSogIdSoggetto(), getSenIdSentenza(),
				getFasSieIdFascicoloSiep(), getFlagAltraCausa(), null, "", "", getDataIrrevocabilita(),
				getFlagCumulante(), getFlagCumulato(), getCodUfficioUnione(), "", "", getKeyProvvNsc(),
				getDataArrivoAtto(), getChiaveProgrOrig(), getVisibilitaMinorenne(),
				getDataUltimaRiapertura(), getCodMotivoRiapertura(), "");
	}

	/**
	 * Imposta la selezione della condizione di aggiornamento.
	 * <p>
	 * 
	 * @param key
	 *            id del fascicolo, da modificare.
	 */
	public void selCondizioneUpdate(BigDecimal key) {
		String lCondizioni = " ID_FASCICOLO_SIEP = " + key;

		setCondition(lCondizioni);
	}

	/**
	 * Imposta la selezione della condizione di aggiornamento.
	 * <p>
	 * 
	 * @param key
	 *            fas sie id del fascicolo, da modificare.
	 */
	public void selCondizioneUpdateFasSieid(BigDecimal key) {
		String lCondizioni = " FAS_SIE_ID_FASCICOLO_SIEP = " + key;

		setCondition(lCondizioni);
	}

	/**
	 * Imposta la selezione per Numero Siep.
	 * <p>
	 * 
	 * @param key
	 *            id del fascicolo, da modificare.
	 */
	public void selCondizioneByNumeroSiep(BigDecimal aChiaveProgr, BigDecimal aChiaveAnno,
			String aChiaveUfficio) {
		String lCondizioni = " CHIAVE_PROGR = " + aChiaveProgr + " AND CHIAVE_ANNO = " + aChiaveAnno
				+ " AND CHIAVE_UFFICIO = '" + aChiaveUfficio + "'";

		setCondition(lCondizioni);
	}

	/**
	 * Imposta la selezione dei fascicoli validati per id sentenza
	 * <p>
	 * 
	 * @param key
	 *            id della sentenza.
	 */
	public void ricercaFascicoloBySentenza(BigDecimal aKey) {
		String lCondizioni = " SEN_ID_SENTENZA=" + aKey + " AND FLAG_VALIDATO='S'";

		setCondition(lCondizioni);
	}

	/**
	 * Setta il DAO dal Model passato
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void setDAOFromModel(FascicoloSiepModel aModel) throws DAOException {
		setIdFascicoloSiep(aModel.getIdFascicoloSiep());
		setChiaveAnno(aModel.getChiaveAnno());
		setChiaveUfficio(aModel.getChiaveUfficio());
		setChiaveProgr(aModel.getChiaveProgr());
		setCodStatoFascicolo(aModel.getCodStatoFascicolo());
		setDataIscrizione(aModel.getDataIscrizione());
		setDataArchiviazione(aModel.getDataArchiviazione());
		setCodMotivoArchiviazione(aModel.getCodMotivoArchiviazione());
		setLetteraFascicolo(aModel.getLetteraFascicolo());
		setAnnoFascicoloUnione(aModel.getAnnoFascicoloUnione());
		setNumFascicoloUnione(aModel.getNumFascicoloUnione());
		setDataUnione(aModel.getDataUnione());
		setNote(aModel.getNote());
		setCodTipoPosLibero(aModel.getCodTipoPosLibero());
		setFlagValidato(aModel.getFlagValidato());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setSogIdSoggetto(aModel.getSogIdSoggetto());
		setSenIdSentenza(aModel.getSenIdSentenza());
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setFlagAltraCausa(aModel.getFlagAltraCausa());
		setDataIrrevocabilita(aModel.getDataIrrevocabilita());
		setFlagCumulante(aModel.getFlagCumulante());
		setFlagCumulato(aModel.getFlagCumulato());
		setCodUfficioUnione(aModel.getCodUfficioUnione());
		setKeyProvvNsc(aModel.getKeyProvvNsc());
		setDataArrivoAtto(aModel.getDataArrivoAtto());
		setVisibilitaMinorenne(aModel.getVisibilitaMinorenne());
		setDataUltimaRiapertura(aModel.getDataUltimaRiapertura());
		setCodMotivoRiapertura(aModel.getCodMotivoRiapertura());
    
	  // MEV29 - Si inserisce anche il ProgrOrigine
    setChiaveProgrOrig (aModel.getChiaveProgrOrig());
	}

	/**
	 * Setta il DAO dal Model passato per l'update
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void setDAOFromModelForUpdate(FascicoloSiepModel aModel) throws DAOException {
		// setIdFascicoloSiep( aModel.getIdFascicoloSiep() );
		// setChiaveAnno( aModel.getChiaveAnno() );
		// setChiaveUfficio( aModel.getChiaveUfficio() );
		// setChiaveProgr( aModel.getChiaveProgr() );
		// setCodStatoFascicolo( aModel.getCodStatoFascicolo() );
		setDataIscrizione(aModel.getDataIscrizione()); // ***
		// setDataArchiviazione( aModel.getDataArchiviazione() );
		// setCodMotivoArchiviazione( aModel.getCodMotivoArchiviazione() );
		// setLetteraFascicolo( aModel.getLetteraFascicolo() );
		// setAnnoFascicoloUnione( aModel.getAnnoFascicoloUnione() );
		// setNumFascicoloUnione( aModel.getNumFascicoloUnione() );
		// setDataUnione( aModel.getDataUnione() );
		setNote(aModel.getNote());
		// setCodTipoPosLibero( aModel.getCodTipoPosLibero() );
		// setFlagValidato( aModel.getFlagValidato() );
		// setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
		// setDataInserimento( aModel.getDataInserimento() ); // ***
		// setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		// setSogIdSoggetto( aModel.getSogIdSoggetto() );
		// setSenIdSentenza( aModel.getSenIdSentenza() );
		// setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );

		/*
		 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice decommentato
		 * Numero MEV : SIES v10
		 * Autore : gioggi
		 * Data : 27/gen/2016
		 * Branch : MEV_SIES v10
		 */
		setFlagAltraCausa(aModel.getFlagAltraCausa());
		// ***** FINE INTERVENTO MEV_SIES v10 *****//

		setFlagCumulante(aModel.getFlagCumulante());
		setFlagCumulato(aModel.getFlagCumulato());
		setCodUfficioUnione(aModel.getCodUfficioUnione());

		// STUB 15/12/2005
		if (aModel.getDataIrrevocabilita() != null)
			setDataIrrevocabilita(aModel.getDataIrrevocabilita());

		// inizio modifica marzo 2010
		setDataArrivoAtto(aModel.getDataArrivoAtto());
		// fine modifica marzo 2010

		if (aModel.getDataUltimaRiapertura() != null)
			setDataUltimaRiapertura(aModel.getDataUltimaRiapertura());

		if (aModel.getCodMotivoRiapertura() != null)
			setCodMotivoRiapertura(aModel.getCodMotivoRiapertura());
	}

	/**
	 * Setta il DAO dal Model passato per l'update a seguito del trasferimento.
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	public void setDAOFromModelForUpdateDaTrasferimento(FascicoloSiepModel aModel) throws DAOException {
		// setIdFascicoloSiep( aModel.getIdFascicoloSiep() );
		// setChiaveAnno( aModel.getChiaveAnno() );
		// setChiaveUfficio( aModel.getChiaveUfficio() );
		// setChiaveProgr( aModel.getChiaveProgr() );
		setCodStatoFascicolo(aModel.getCodStatoFascicolo());
		setDataIscrizione(aModel.getDataIscrizione()); // ***
		setDataArchiviazione(aModel.getDataArchiviazione());
		setCodMotivoArchiviazione(aModel.getCodMotivoArchiviazione());
		setLetteraFascicolo(aModel.getLetteraFascicolo());
		setAnnoFascicoloUnione(aModel.getAnnoFascicoloUnione());
		setNumFascicoloUnione(aModel.getNumFascicoloUnione());
		setDataUnione(aModel.getDataUnione());
		setNote(aModel.getNote());
		setCodTipoPosLibero(aModel.getCodTipoPosLibero());
		setFlagValidato(aModel.getFlagValidato());
		// setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
		// setDataInserimento( aModel.getDataInserimento() ); // ***
		// setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		// setSogIdSoggetto( aModel.getSogIdSoggetto() );
		// setSenIdSentenza( aModel.getSenIdSentenza() );
		// setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
		setFlagAltraCausa(aModel.getFlagAltraCausa());
		setFlagCumulante(aModel.getFlagCumulante());
		setFlagCumulato(aModel.getFlagCumulato());
		setCodUfficioUnione(aModel.getCodUfficioUnione());

		if (aModel.getDataIrrevocabilita() != null)
			setDataIrrevocabilita(aModel.getDataIrrevocabilita());

		if (aModel.getDataUltimaRiapertura() != null)
			setDataUltimaRiapertura(aModel.getDataUltimaRiapertura());

		if (aModel.getCodMotivoRiapertura() != null)
			setCodMotivoRiapertura(aModel.getCodMotivoRiapertura());
    
    //MEV26 - Aggiunto aggiornamento chiavi NSC
    if (aModel.getKeyProvvNsc()!=null)
      setKeyProvvNsc( aModel.getKeyProvvNsc() );
	}

	public void setDAOFromModelForUpdateKeyNsc(FascicoloSiepModel aModel) throws DAOException {
		setKeyProvvNsc(aModel.getKeyProvvNsc());
	}

	public void setDAOFromModelForUpdateFasSiesIdFascicolo(FascicoloSiepModel aModel) throws DAOException {
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
	}

	public void setDAOFromModelForUpdateBlob(FascicoloSiepCertBlobModel aModel) throws DAOException {
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setCertPenaleBlob(aModel.ritornaCertPenaleBlobIn());
	}

}