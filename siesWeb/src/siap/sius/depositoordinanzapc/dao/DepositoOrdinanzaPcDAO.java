package siap.sius.depositoordinanzapc.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;

/**
 * DepositoOrdinanzaPcDAO - Classe DAO che rappresenta la tabella DepositoOrdinanzaPc
 *
 * @version 1.0
 */
public class DepositoOrdinanzaPcDAO extends SIAPTableDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public DepositoOrdinanzaPcDAO(Connection con) {

		super(con);

		setTable("DEPOSITO_ORDINANZA_PC");
		setFieldKey("ID_DEPOSITO_ORDINANZA_PC", BIG_DECIMAL);
		setSequenceField("ID_DEPOSITO_ORDINANZA_PC", "DEP_ORD_SEQ");

		setField("ID_DEPOSITO_ORDINANZA_PC", BIG_DECIMAL);
		setField("ANNO_S3", BIG_DECIMAL);
		setField("NUM_S3", BIG_DECIMAL);
		setField("OGGETTO_PROCEDIMENTO", STRING);
		setField("DATA_UDIENZA", DATE);
		setField("DATA_CAMERA_CONSIGLIO", DATE);
		setField("DATA_DEPOSITO", DATE);
		setField("COD_NATURA_PROVVEDIMENTO", STRING);
		setField("ID_CSSA_COMP", BIG_DECIMAL);
		setField("COD_USSM", BIG_DECIMAL);
		setField("COD_UFFICIO_MAGISTRATO_COMP", STRING);
		setField("LUOGO_SVOLGIMENTO_PROVA", STRING);
		setField("SERVIZIO_TERAPEUTICO_COMP", STRING);
		setField("NUM_GIORNI_DETENZIONE_DOM", BIG_DECIMAL);
		setField("NUM_MESI_DETENZIONE_DOM", BIG_DECIMAL);
		setField("NUM_ANNI_DETENZIONE_DOM", BIG_DECIMAL);
		setField("NUM_GIORNI_PERMESSO_ACCORDATI", BIG_DECIMAL);
		setField("NUM_GIORNI_RIDUZIONE_PENA", BIG_DECIMAL);
		setField("NUM_GIORNI_RIDUZIONE_USUFRUITI", BIG_DECIMAL);
		setField("COD_UFF_TDS_CONCESSO_RIDUZIONE", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("GEN_PRID_GENERALE_PROCEDIMENTO", BIG_DECIMAL);
		setField("COD_MAGISTRATO", STRING);
		setField("ID_EVENTO_GENERATO", BIG_DECIMAL);
		setField("NUM_GIORNI_LIBANTICIPATA", BIG_DECIMAL);
		setField("FLAG_ELABORATO", STRING);
		setField("COD_TIPO_ORDINANZA", STRING);
		// Nuovi campi 9-6-2004
		setField("DATA_FINE_MISURA", DATE);
		setField("DATA_DECORRENZA", DATE);
		setField("DATA_INIZIO_PERIODO", DATE);
		setField("FLAG_ESISTENZA_REATOOSTATIVO", STRING);
		setField("FLAG_ESPIAZIONE_REATOOSTATIVO", STRING);
		setField("AUTORITA_VIGILANTE", STRING);
		setField("DATA_TRASMISSIONE", DATE);
		setField("DATA_COMP_FOGLIO_COMPLEMENTARE", DATE);
		// Nuovi campi. Luigi 8-05-2006
		setField("NUM_GIORNI_ARRESTO_REV", BIG_DECIMAL);
		setField("NUM_MESI_ARRESTO_REV", BIG_DECIMAL);
		setField("NUM_ANNI_ARRESTO_REV", BIG_DECIMAL);
		setField("ULTERIORE_DESCRIZIONE", STRING);
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		setField("DATA_SOSPENSIONE_SS", DATE);
		setField("GIORNI_RECUPERO_SS", BIG_DECIMAL);
		setField("FLAG_RECUPERO_SS", STRING);
		setField("DATA_SCADENZA_SOSPENSIONE_SS", DATE);
		setField("SOSPENSIONE_GG", BIG_DECIMAL);
		setField("SOSPENSIONE_MM", BIG_DECIMAL);
		setField("SOSPENSIONE_AA", BIG_DECIMAL);
		setField("FLAG_NOMINA_COMM_ACTA", STRING);
		setField("DESCR_COMM_ACTA", STRING);
		// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
		setField("TIPO_CONTROLLO_ESECUZIONE", STRING);
		// 10102014 - DL 92 2014 Violazione CEDU
		setField("SOMMA_RISARC_DANNI", BIG_DECIMAL);
		// MEV_2023-35: aggiunti campi
		setField("COD_TIPO_SANZIONE", STRING);
		setField("COD_TIPO_PENA_ACCESSORIA", STRING);
		setField("DURATA", STRING);
		setField("NUM_ANNI", BIG_DECIMAL);
		setField("NUM_MESI", BIG_DECIMAL);
		setField("NUM_GIORNI", BIG_DECIMAL);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdDepositoOrdinanzaPc() throws DAOException {
		return getBigDecimal("ID_DEPOSITO_ORDINANZA_PC");
	}

	public BigDecimal getAnnoS3() throws DAOException {
		return getBigDecimal("ANNO_S3");
	}

	public BigDecimal getNumS3() throws DAOException {
		return getBigDecimal("NUM_S3");
	}

	public String getOggettoProcedimento() throws DAOException {
		return getString("OGGETTO_PROCEDIMENTO");
	}

	public Date getDataUdienza() throws DAOException {
		return getDate("DATA_UDIENZA");
	}

	public Date getDataCameraConsiglio() throws DAOException {
		return getDate("DATA_CAMERA_CONSIGLIO");
	}

	public Date getDataDeposito() throws DAOException {
		return getDate("DATA_DEPOSITO");
	}

	public String getCodNaturaProvvedimento() throws DAOException {
		return getString("COD_NATURA_PROVVEDIMENTO");
	}

	public BigDecimal getIdCssaComp() throws DAOException {
		return getBigDecimal("ID_CSSA_COMP");
	}

	public BigDecimal getCodUssm() throws DAOException {
		return getBigDecimal("COD_USSM");
	}

	public String getCodUfficioMagistratoComp() throws DAOException {
		return getString("COD_UFFICIO_MAGISTRATO_COMP");
	}

	public String getLuogoSvolgimentoProva() throws DAOException {
		return getString("LUOGO_SVOLGIMENTO_PROVA");
	}

	public String getServizioTerapeuticoComp() throws DAOException {
		return getString("SERVIZIO_TERAPEUTICO_COMP");
	}

	public BigDecimal getNumGiorniDetenzioneDom() throws DAOException {
		return getBigDecimal("NUM_GIORNI_DETENZIONE_DOM");
	}

	public BigDecimal getNumMesiDetenzioneDom() throws DAOException {
		return getBigDecimal("NUM_MESI_DETENZIONE_DOM");
	}

	public BigDecimal getNumAnniDetenzioneDom() throws DAOException {
		return getBigDecimal("NUM_ANNI_DETENZIONE_DOM");
	}

	public BigDecimal getNumGiorniPermessoAccordati() throws DAOException {
		return getBigDecimal("NUM_GIORNI_PERMESSO_ACCORDATI");
	}

	public BigDecimal getNumGiorniRiduzionePena() throws DAOException {
		return getBigDecimal("NUM_GIORNI_RIDUZIONE_PENA");
	}

	public BigDecimal getNumGiorniRiduzioneUsufruiti() throws DAOException {
		return getBigDecimal("NUM_GIORNI_RIDUZIONE_USUFRUITI");
	}

	public String getCodUffTdsConcessoRiduzione() throws DAOException {
		return getString("COD_UFF_TDS_CONCESSO_RIDUZIONE");
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

	public String getCodUfficioAggiornamento() throws DAOException {
		return getString("COD_UFFICIO_AGGIORNAMENTO");
	}

	public Date getDataAggiornamento() throws DAOException {
		return getDate("DATA_AGGIORNAMENTO");
	}

	public BigDecimal getGenPridGeneraleProcedimento() throws DAOException {
		return getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO");
	}

	public String getCodMagistrato() throws DAOException {
		return getString("COD_MAGISTRATO");
	}

	public BigDecimal getIdEventoGenerato() throws DAOException {
		return getBigDecimal("ID_EVENTO_GENERATO");
	}

	public BigDecimal getNumGiorniLibanticipata() throws DAOException {
		return getBigDecimal("NUM_GIORNI_LIBANTICIPATA");
	}

	public String getFlagElaborato() throws DAOException {
		return getString("FLAG_ELABORATO");
	}

	public String getCodTipoOrdinanza() throws DAOException {
		return getString("COD_TIPO_ORDINANZA");
	}

	// Nuovi campi 9-6-2004
	public Date getDataFineMisura() throws DAOException {
		return getDate("DATA_FINE_MISURA");
	}

	public Date getDataDecorrenza() throws DAOException {
		return getDate("DATA_DECORRENZA");
	}

	public Date getDataInizioPeriodo() throws DAOException {
		return getDate("DATA_INIZIO_PERIODO");
	}

	public String getFlagEsistenzaReatoostativo() throws DAOException {
		return getString("FLAG_ESISTENZA_REATOOSTATIVO");
	}

	public String getFlagEspiazioneReatoostativo() throws DAOException {
		return getString("FLAG_ESPIAZIONE_REATOOSTATIVO");
	}

	public String getAutoritaVigilante() throws DAOException {
		return getString("AUTORITA_VIGILANTE");
	}

	public Date getDataTrasmissione() throws DAOException {
		return getDate("DATA_TRASMISSIONE");
	}

	public Date getDataCompFoglioComplementare() throws DAOException {
		return getDate("DATA_COMP_FOGLIO_COMPLEMENTARE");
	}

	public BigDecimal getNumGiorniArrestoRev() throws DAOException {
		return getBigDecimal("NUM_GIORNI_ARRESTO_REV");
	}

	public BigDecimal getNumMesiArrestoRev() throws DAOException {
		return getBigDecimal("NUM_MESI_ARRESTO_REV");
	}

	public BigDecimal getNumAnniArrestoRev() throws DAOException {
		return getBigDecimal("NUM_ANNI_ARRESTO_REV");
	}

	public String getUlterioreDescrizione() throws DAOException {
		return getString("ULTERIORE_DESCRIZIONE");
	}

	// Nuovi campi per Sospensione Sanzioni Sostitutive
	public Date getDataSospensioneSS() throws DAOException {
		return getDate("DATA_SOSPENSIONE_SS");
	}

	public BigDecimal getGiorniRecuperoSS() throws DAOException {
		return getBigDecimal("GIORNI_RECUPERO_SS");
	}

	public String getFlagRecuperoSS() throws DAOException {
		return getString("FLAG_RECUPERO_SS");
	}

	public Date getDataScadenzaSospensioneSS() throws DAOException {
		return getDate("DATA_SCADENZA_SOSPENSIONE_SS");
	}

	public BigDecimal getSospensioneGGSS() throws DAOException {
		return getBigDecimal("SOSPENSIONE_GG");
	}

	public BigDecimal getSospensioneMMSS() throws DAOException {
		return getBigDecimal("SOSPENSIONE_MM");
	}

	public BigDecimal getSospensioneAASS() throws DAOException {
		return getBigDecimal("SOSPENSIONE_AA");
	}

	public String getFlagNominaComActa() throws DAOException {
		return getString("FLAG_NOMINA_COMM_ACTA");
	}

	public String getDescrCommActa() throws DAOException {
		return getString("DESCR_COMM_ACTA");
	}

	// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
	public String getCodTipoControlloEsecuzione() throws DAOException {
		return getString("TIPO_CONTROLLO_ESECUZIONE");
	}

	// 10102014 - DL 92 2014 Violazione CEDU
	public BigDecimal getSommaRisarcimento() throws DAOException {
		return getBigDecimal("SOMMA_RISARC_DANNI");
	}

	public String getCodTipoSanzione() throws DAOException {
		return getString("COD_TIPO_SANZIONE");
	}

	public String getCodTipoPenaAccessoria() throws DAOException {
		return getString("COD_TIPO_PENA_ACCESSORIA");
	}

	public String getDurata() throws DAOException {
		return getString("DURATA");
	}

	public BigDecimal getNumAnni() throws DAOException {
		return getBigDecimal("NUM_ANNI");
	}

	public BigDecimal getNumMesi() throws DAOException {
		return getBigDecimal("NUM_MESI");
	}

	public BigDecimal getNumGiorni() throws DAOException {
		return getBigDecimal("NUM_GIORNI");
	}

	//
	// METODI SET()
	//
	public void setIdDepositoOrdinanzaPc(BigDecimal aValore) {
		setBigDecimal("ID_DEPOSITO_ORDINANZA_PC", aValore);
	}

	public void setAnnoS3(BigDecimal aValore) {
		setBigDecimal("ANNO_S3", aValore);
	}

	public void setNumS3(BigDecimal aValore) {
		setBigDecimal("NUM_S3", aValore);
	}

	public void setOggettoProcedimento(String aValore) {
		setString("OGGETTO_PROCEDIMENTO", aValore);
	}

	public void setDataUdienza(Date aValore) {
		setDate("DATA_UDIENZA", aValore);
	}

	public void setDataCameraConsiglio(Date aValore) {
		setDate("DATA_CAMERA_CONSIGLIO", aValore);
	}

	public void setDataDeposito(Date aValore) {
		setDate("DATA_DEPOSITO", aValore);
	}

	public void setCodNaturaProvvedimento(String aValore) {
		setString("COD_NATURA_PROVVEDIMENTO", aValore);
	}

	public void setIdCssaComp(BigDecimal aValore) {
		setBigDecimal("ID_CSSA_COMP", aValore);
	}

	public void setCodUssm(BigDecimal aValore) {
		setBigDecimal("COD_USSM", aValore);
	}

	public void setCodUfficioMagistratoComp(String aValore) {
		setString("COD_UFFICIO_MAGISTRATO_COMP", aValore);
	}

	public void setLuogoSvolgimentoProva(String aValore) {
		setString("LUOGO_SVOLGIMENTO_PROVA", aValore);
	}

	public void setServizioTerapeuticoComp(String aValore) {
		setString("SERVIZIO_TERAPEUTICO_COMP", aValore);
	}

	public void setNumGiorniDetenzioneDom(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_DETENZIONE_DOM", aValore);
	}

	public void setNumMesiDetenzioneDom(BigDecimal aValore) {
		setBigDecimal("NUM_MESI_DETENZIONE_DOM", aValore);
	}

	public void setNumAnniDetenzioneDom(BigDecimal aValore) {
		setBigDecimal("NUM_ANNI_DETENZIONE_DOM", aValore);
	}

	public void setNumGiorniPermessoAccordati(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_PERMESSO_ACCORDATI", aValore);
	}

	public void setNumGiorniRiduzionePena(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_RIDUZIONE_PENA", aValore);
	}

	public void setNumGiorniRiduzioneUsufruiti(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_RIDUZIONE_USUFRUITI", aValore);
	}

	public void setCodUffTdsConcessoRiduzione(String aValore) {
		setString("COD_UFF_TDS_CONCESSO_RIDUZIONE", aValore);
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

	public void setCodUfficioAggiornamento(String aValore) {
		setString("COD_UFFICIO_AGGIORNAMENTO", aValore);
	}

	public void setDataAggiornamento(Date aValore) {
		setDate("DATA_AGGIORNAMENTO", aValore);
	}

	public void setGenPridGeneraleProcedimento(BigDecimal aValore) {
		setBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO", aValore);
	}

	public void setCodMagistrato(String aValore) {
		setString("COD_MAGISTRATO", aValore);
	}

	public void setIdEventoGenerato(BigDecimal aValore) {
		setBigDecimal("ID_EVENTO_GENERATO", aValore);
	}

	public void setNumGiorniLibanticipata(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_LIBANTICIPATA", aValore);
	}

	public void setFlagElaborato(String aValore) {
		setString("FLAG_ELABORATO", aValore);
	}

	public void setCodTipoOrdinanza(String aValore) {
		setString("COD_TIPO_ORDINANZA", aValore);
	}

	// Nuovi campi 9-6-2004
	public void setDataFineMisura(Date aValore) {
		setDate("DATA_FINE_MISURA", aValore);
	}

	public void setDataDecorrenza(Date aValore) {
		setDate("DATA_DECORRENZA", aValore);
	}

	public void setDataInizioPeriodo(Date aValore) {
		setDate("DATA_INIZIO_PERIODO", aValore);
	}

	public void setFlagEsistenzaReatoostativo(String aValore) {
		setString("FLAG_ESISTENZA_REATOOSTATIVO", aValore);
	}

	public void setFlagEspiazioneReatoostativo(String aValore) {
		setString("FLAG_ESPIAZIONE_REATOOSTATIVO", aValore);
	}

	public void setAutoritaVigilante(String aValore) {
		setString("AUTORITA_VIGILANTE", aValore);
	}

	public void setDataTrasmissione(Date aValore) {
		setDate("DATA_TRASMISSIONE", aValore);
	}

	public void setDataCompFoglioComplementare(Date aValore) {
		setDate("DATA_COMP_FOGLIO_COMPLEMENTARE", aValore);
	}

	public void setNumGiorniArrestoRev(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_ARRESTO_REV", aValore);
	}

	public void setNumMesiArrestoRev(BigDecimal aValore) {
		setBigDecimal("NUM_MESI_ARRESTO_REV", aValore);
	}

	public void setNumAnniArrestoRev(BigDecimal aValore) {
		setBigDecimal("NUM_ANNI_ARRESTO_REV", aValore);
	}

	public void setUlterioreDescrizione(String aValore) {
		setString("ULTERIORE_DESCRIZIONE", aValore);
	}

	// Nuovi campi per Sospensione Sanzioni Sostitutive
	public void setDataSospensioneSS(Date aValore) {
		setDate("DATA_SOSPENSIONE_SS", aValore);
	}

	public void setGiorniRecuperoSS(BigDecimal aValore) {
		setBigDecimal("GIORNI_RECUPERO_SS", aValore);
	}

	public void setFlagRecuperoSS(String aValore) {
		setString("FLAG_RECUPERO_SS", aValore);
	}

	public void setDataScadenzaSospensioneSS(Date aValore) {
		setDate("DATA_SCADENZA_SOSPENSIONE_SS", aValore);
	}

	public void setSospensioneGGSS(BigDecimal aValore) {
		setBigDecimal("SOSPENSIONE_GG", aValore);
	}

	public void setSospensioneMMSS(BigDecimal aValore) {
		setBigDecimal("SOSPENSIONE_MM", aValore);
	}

	public void setSospensioneAASS(BigDecimal aValore) {
		setBigDecimal("SOSPENSIONE_AA", aValore);
	}

	public void setFlagNominaComActa(String aValore) {
		setString("FLAG_NOMINA_COMM_ACTA", aValore);
	}

	public void setDescrCommActa(String aValore) {
		setString("DESCR_COMM_ACTA", aValore);
	}

	// 20140603 - P.M. ( SIUS - Implementazione per il D.L. 146 )
	public void setCodTipoControlloEsecuzione(String aValore) {
		setString("TIPO_CONTROLLO_ESECUZIONE", aValore);
	}

	// 10102014 - DL 92 2014 Violazione CEDU
	public void setSommaRisarcimento(BigDecimal aValore) {
		setBigDecimal("SOMMA_RISARC_DANNI", aValore);
	}

	public void setCodTipoSanzione(String aValore) {
		setString("COD_TIPO_SANZIONE", aValore);
	}

	public void setCodTipoPenaAccessoria(String aValore) {
		setString("COD_TIPO_PENA_ACCESSORIA", aValore);
	}

	public void setDurata(String aValore) {
		setString("DURATA", aValore);
	}

	public void setNumAnni(BigDecimal aValore) {
		setBigDecimal("NUM_ANNI", aValore);
	}

	public void setNumMesi(BigDecimal aValore) {
		setBigDecimal("NUM_MESI", aValore);
	}

	public void setNumGiorni(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI", aValore);
	}

	public GenericModel getModel() throws DAOException {

		return new DepositoOrdinanzaPcModel(getIdDepositoOrdinanzaPc(), getAnnoS3(), getNumS3(),
				getOggettoProcedimento(), getDataUdienza(), getDataCameraConsiglio(), getDataDeposito(),
				getCodNaturaProvvedimento(), "", getIdCssaComp(), "", getCodUssm(), "",
				getCodUfficioMagistratoComp(), "", getLuogoSvolgimentoProva(), getServizioTerapeuticoComp(),
				getNumGiorniDetenzioneDom(), getNumMesiDetenzioneDom(), getNumAnniDetenzioneDom(),
				getNumGiorniPermessoAccordati(), getNumGiorniRiduzionePena(),
				getNumGiorniRiduzioneUsufruiti(), getCodUffTdsConcessoRiduzione(), "",
				getCodOperatoreInserimento(), getDataInserimento(), getCodUfficioInserimento(), "",
				getCodOperatoreAggiornamento(), getCodUfficioAggiornamento(), "", getDataAggiornamento(),
				getGenPridGeneraleProcedimento(), getCodMagistrato(), getIdEventoGenerato(),
				getNumGiorniLibanticipata(), getFlagElaborato(), getCodTipoOrdinanza(), "",
				getDataFineMisura(), getDataDecorrenza(), getDataInizioPeriodo(),
				getFlagEsistenzaReatoostativo(), getFlagEspiazioneReatoostativo(), getAutoritaVigilante(),
				getDataTrasmissione(), getDataCompFoglioComplementare(), getNumGiorniArrestoRev(),
				getNumMesiArrestoRev(), getNumAnniArrestoRev(), getUlterioreDescrizione(),
				// Nuovi campi per Sospensione Sanzioni Sostitutive
				getDataSospensioneSS(), getGiorniRecuperoSS(), getFlagRecuperoSS(),
				getDataScadenzaSospensioneSS(), getSospensioneGGSS(), getSospensioneMMSS(),
				getSospensioneAASS(), getFlagNominaComActa(), getDescrCommActa(),
				// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
				getCodTipoControlloEsecuzione(), "",
				// 10102014 - DL 92 2014 Violazione CEDU
				getSommaRisarcimento(), getCodTipoSanzione(), getCodTipoPenaAccessoria(), "", getDurata(), "",
				getNumAnni(), getNumMesi(), getNumGiorni());
	}

	public void setDAOFromModel(DepositoOrdinanzaPcModel aModel) throws DAOException {

		setIdDepositoOrdinanzaPc(aModel.getIdDepositoOrdinanzaPc());
		setAnnoS3(aModel.getAnnoS3());
		setNumS3(aModel.getNumS3());
		setOggettoProcedimento(aModel.getOggettoProcedimento());
		setDataUdienza(aModel.getDataUdienza());
		setDataCameraConsiglio(aModel.getDataCameraConsiglio());
		setDataDeposito(aModel.getDataDeposito());
		setCodNaturaProvvedimento(aModel.getCodNaturaProvvedimento());
		setIdCssaComp(aModel.getIdCssaComp());
		setCodUssm(aModel.getCodUssm());
		setCodUfficioMagistratoComp(aModel.getCodUfficioMagistratoComp());
		setLuogoSvolgimentoProva(aModel.getLuogoSvolgimentoProva());
		setServizioTerapeuticoComp(aModel.getServizioTerapeuticoComp());
		setNumGiorniDetenzioneDom(aModel.getNumGiorniDetenzioneDom());
		setNumMesiDetenzioneDom(aModel.getNumMesiDetenzioneDom());
		setNumAnniDetenzioneDom(aModel.getNumAnniDetenzioneDom());
		setNumGiorniPermessoAccordati(aModel.getNumGiorniPermessoAccordati());
		setNumGiorniRiduzionePena(aModel.getNumGiorniRiduzionePena());
		setNumGiorniRiduzioneUsufruiti(aModel.getNumGiorniRiduzioneUsufruiti());
		setCodUffTdsConcessoRiduzione(aModel.getCodUffTdsConcessoRiduzione());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setGenPridGeneraleProcedimento(aModel.getGenPridGeneraleProcedimento());
		setCodMagistrato(aModel.getCodMagistrato());
		setIdEventoGenerato(aModel.getIdEventoGenerato());
		setNumGiorniLibanticipata(aModel.getNumGiorniLibanticipata());
		setFlagElaborato(aModel.getFlagElaborato());
		setCodTipoOrdinanza(aModel.getCodTipoOrdinanza());
		setDataFineMisura(aModel.getDataFineMisura());
		setDataDecorrenza(aModel.getDataDecorrenza());
		setDataInizioPeriodo(aModel.getDataInizioPeriodo());
		setFlagEsistenzaReatoostativo(aModel.getFlagEsistenzaReatoostativo());
		setFlagEspiazioneReatoostativo(aModel.getFlagEspiazioneReatoostativo());
		setAutoritaVigilante(aModel.getAutoritaVigilante());
		setDataTrasmissione(aModel.getDataTrasmissione());
		setDataCompFoglioComplementare(aModel.getDataCompFoglioComplementare());
		setNumGiorniArrestoRev(aModel.getNumGiorniArrestoRev());
		setNumMesiArrestoRev(aModel.getNumMesiArrestoRev());
		setNumAnniArrestoRev(aModel.getNumAnniArrestoRev());
		setUlterioreDescrizione(aModel.getUlterioreDescrizione());
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		setDataSospensioneSS(aModel.getDataSospensioneSS());
		setGiorniRecuperoSS(aModel.getGiorniRecuperoSS());
		setFlagRecuperoSS(aModel.getFlagRecuperoSS());
		setDataScadenzaSospensioneSS(aModel.getDataScadenzaSospensioneSS());
		setSospensioneGGSS(aModel.getSospensioneGGSS());
		setSospensioneMMSS(aModel.getSospensioneMMSS());
		setSospensioneAASS(aModel.getSospensioneAASS());
		setFlagNominaComActa(aModel.getFlagNominaComActa());
		setDescrCommActa(aModel.getDescrCommActa());
		// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
		setCodTipoControlloEsecuzione(aModel.getCodTipoControlloEsecuzione());
		// 10102014 - DL 92 2014
		setSommaRisarcimento(aModel.getSommaRisarcimento());
		setCodTipoSanzione(aModel.getCodTipoSanzione());
		setCodTipoPenaAccessoria(aModel.getCodTipoPenaAccessoria());
		setDurata(aModel.getDurata());
		setNumAnni(aModel.getNumAnni());
		setNumMesi(aModel.getNumMesi());
		setNumGiorni(aModel.getNumGiorni());
	}

	public void setDAOFromModelForUpdate(DepositoOrdinanzaPcModel aModel) throws DAOException {

		setIdDepositoOrdinanzaPc(aModel.getIdDepositoOrdinanzaPc());
		setAnnoS3(aModel.getAnnoS3());
		setNumS3(aModel.getNumS3());
		setOggettoProcedimento(aModel.getOggettoProcedimento());
		setDataUdienza(aModel.getDataUdienza());
		setDataCameraConsiglio(aModel.getDataCameraConsiglio());
		setDataDeposito(aModel.getDataDeposito());
		setCodNaturaProvvedimento(aModel.getCodNaturaProvvedimento());
		setIdCssaComp(aModel.getIdCssaComp());
		setCodUssm(aModel.getCodUssm());
		setCodUfficioMagistratoComp(aModel.getCodUfficioMagistratoComp());
		setLuogoSvolgimentoProva(aModel.getLuogoSvolgimentoProva());
		setServizioTerapeuticoComp(aModel.getServizioTerapeuticoComp());
		setNumGiorniDetenzioneDom(aModel.getNumGiorniDetenzioneDom());
		setNumMesiDetenzioneDom(aModel.getNumMesiDetenzioneDom());
		setNumAnniDetenzioneDom(aModel.getNumAnniDetenzioneDom());
		setNumGiorniPermessoAccordati(aModel.getNumGiorniPermessoAccordati());
		setNumGiorniRiduzionePena(aModel.getNumGiorniRiduzionePena());
		setNumGiorniRiduzioneUsufruiti(aModel.getNumGiorniRiduzioneUsufruiti());
		setCodUffTdsConcessoRiduzione(aModel.getCodUffTdsConcessoRiduzione());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setGenPridGeneraleProcedimento(aModel.getGenPridGeneraleProcedimento());
		setCodMagistrato(aModel.getCodMagistrato());
		setIdEventoGenerato(aModel.getIdEventoGenerato());
		setNumGiorniLibanticipata(aModel.getNumGiorniLibanticipata());
		setFlagElaborato(aModel.getFlagElaborato());
		setCodTipoOrdinanza(aModel.getCodTipoOrdinanza());
		setDataFineMisura(aModel.getDataFineMisura());
		setDataDecorrenza(aModel.getDataDecorrenza());
		setDataInizioPeriodo(aModel.getDataInizioPeriodo());
		setFlagEsistenzaReatoostativo(aModel.getFlagEsistenzaReatoostativo());
		setFlagEspiazioneReatoostativo(aModel.getFlagEspiazioneReatoostativo());
		setAutoritaVigilante(aModel.getAutoritaVigilante());
		setDataTrasmissione(aModel.getDataTrasmissione());
		setDataCompFoglioComplementare(aModel.getDataCompFoglioComplementare());
		setNumGiorniArrestoRev(aModel.getNumGiorniArrestoRev());
		setNumMesiArrestoRev(aModel.getNumMesiArrestoRev());
		setNumAnniArrestoRev(aModel.getNumAnniArrestoRev());
		setUlterioreDescrizione(aModel.getUlterioreDescrizione());
		// Nuovi campi per Sospensione Sanzioni Sostitutive
		setDataSospensioneSS(aModel.getDataSospensioneSS());
		setGiorniRecuperoSS(aModel.getGiorniRecuperoSS());
		setFlagRecuperoSS(aModel.getFlagRecuperoSS());
		setDataScadenzaSospensioneSS(aModel.getDataScadenzaSospensioneSS());
		setSospensioneGGSS(aModel.getSospensioneGGSS());
		setSospensioneMMSS(aModel.getSospensioneMMSS());
		setSospensioneAASS(aModel.getSospensioneAASS());
		setFlagNominaComActa(aModel.getFlagNominaComActa());
		setDescrCommActa(aModel.getDescrCommActa());
		// 20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
		setCodTipoControlloEsecuzione(aModel.getCodTipoControlloEsecuzione());
		// 10102014 - DL 92 2014 Violazione CEDU
		setSommaRisarcimento(aModel.getSommaRisarcimento());
		setCodTipoSanzione(aModel.getCodTipoSanzione());
		setCodTipoPenaAccessoria(aModel.getCodTipoPenaAccessoria());
		setDurata(aModel.getDurata());
		setNumAnni(aModel.getNumAnni());
		setNumMesi(aModel.getNumMesi());
		setNumGiorni(aModel.getNumGiorni());
		setCondizioneUpdate(aModel.getIdDepositoOrdinanzaPc());
	}

	/**
	 * Valorizza le condizioni di filtro in base al contenuto del model DepositoOrdinanzaPcModel passato.
	 *
	 * @param aModel
	 */
	public void setCondizione(DepositoOrdinanzaPcModel aModel) {

		String lCondizioni = new String("");
		String lAppoggio = new String("");
		lInserito = false;

		if (aModel != null) {
			if (aModel.getAnnoS3() != null)
				lAppoggio = " ANNO_S3 = " + aModel.getAnnoS3();
			lCondizioni += setAND(lAppoggio);

			if (aModel.getNumS3() != null)
				lAppoggio = " NUM_S3 = " + aModel.getNumS3();
			lCondizioni += setAND(lAppoggio);

			if (aModel.getCodUfficioInserimento() != null
					&& aModel.getCodUfficioInserimento().trim().length() > 0)
				lAppoggio = " COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
			lCondizioni += setAND(lAppoggio);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Condizione -> " + lCondizioni);

			setCondition(lCondizioni);
		}
	}

	boolean lInserito = false;

	private String setAND(String aCondizioni) {

		if (lInserito)
			aCondizioni = " AND " + aCondizioni;

		lInserito = true;

		return aCondizioni;
	}

	public void setCondizioneUpdate(BigDecimal aKey) {

		setCondition(" ID_DEPOSITO_ORDINANZA_PC = " + aKey);
	}

}