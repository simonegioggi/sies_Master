package siap.siep.modulocumulo.dao;

/**
* <p>Title: ProvvedimentoGeSorvCumDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella ProvvedimentoGeSorvCum</p>
* <p>Author: Intersistemi Italia S.p.A.</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel;

public class ProvvedimentoGeSorvCumDAO extends TableDAO {
	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public ProvvedimentoGeSorvCumDAO(Connection con) {
		super(con);
		setTable("PROVVEDIMENTO_GE_SORV_CUM");

		// Settare la Sequence e i campi chiave e commentare il setField del campo chiave
		setSequenceField("ID_PROVVEDIMENTO_GE_SORV_CUM", "PROVV_GE_SORV_CUM_SEQ");

		// setField("ID_PROVVEDIMENTO_GE_SORV_CUM" , BIG_DECIMAL);
		setField("COD_UFFICIO_EMITTENTE", STRING);
		setField("COD_LUOGO_EMITTENTE", STRING);
		setField("DATA_D", DATE);
		setField("ANNO_PROVV", BIG_DECIMAL);
		setField("NUMERO_PROVV", STRING);
		setField("FLAG_CONFORME", STRING);
		setField("FLAG_PIU_MENO_D", STRING);
		setField("NUM_ANNI_RECLUSIONE_D", BIG_DECIMAL);
		setField("NUM_MESI_RECLUSIONE_D", BIG_DECIMAL);
		setField("NUM_GIORNI_RECLUSIONE_D", BIG_DECIMAL);
		setField("IMPORTO_MULTA_D", BIG_DECIMAL);
		setField("NUM_ANNI_ARRESTO_D", BIG_DECIMAL);
		setField("NUM_MESI_ARRESTO_D", BIG_DECIMAL);
		setField("NUM_GIORNI_ARRESTO_D", BIG_DECIMAL);
		setField("IMPORTO_AMMENDA_D", BIG_DECIMAL);
		setField("NUM_GIORNI_LA_REV_D", BIG_DECIMAL);
		setField("NUM_GIORNI_LS_REV_D", BIG_DECIMAL);
		setField("NUM_GIORNI_LI_REV_D", BIG_DECIMAL);
		setField("MOTIVAZIONI_D", STRING);
		setField("COD_TIPO_PROVVEDIMENTO", STRING);
		setField("ANNO_SIUS", BIG_DECIMAL);
		setField("NUMERO_SIUS", STRING);
		setField("COD_TIPO_MS_D", STRING);
		setField("NUM_ANNI_MS_D", BIG_DECIMAL);
		setField("NUM_MESI_MS_D", BIG_DECIMAL);
		setField("NUM_GIORNI_MS_D", BIG_DECIMAL);
		setField("BEN_SOSP_COND", STRING);
		setField("BEN_NON_MENZIONE", STRING);
		setField("BEN_INDULTO", STRING);
		setField("COD_TIPO_PENA_ACCESSORIA_D", STRING);
		setField("COD_TIPO_DURATA_D", STRING);
		setField("NUM_ANNI_PA_D", BIG_DECIMAL);
		setField("NUM_MESI_PA_D", BIG_DECIMAL);
		setField("NUM_GIORNI_PA_D", BIG_DECIMAL);
		setField("DATA_REVOCA", DATE);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("RIC_ID_RICHIESTE_PM_IN_CUMULO", BIG_DECIMAL);
	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getIdProvvedimentoGeSorvCum() throws DAOException {
		return getBigDecimal("ID_PROVVEDIMENTO_GE_SORV_CUM");
	}

	public String getCodUfficioEmittente() throws DAOException {
		return getString("COD_UFFICIO_EMITTENTE");
	}

	public String getCodLuogoEmittente() throws DAOException {
		return getString("COD_LUOGO_EMITTENTE");
	}

	public Date getDataD() throws DAOException {
		return getDate("DATA_D");
	}

	public BigDecimal getAnnoProvv() throws DAOException {
		return getBigDecimal("ANNO_PROVV");
	}

	public String getNumeroProvv() throws DAOException {
		return getString("NUMERO_PROVV");
	}

	public String getFlagConforme() throws DAOException {
		return getString("FLAG_CONFORME");
	}

	public String getFlagPiuMenoD() throws DAOException {
		return getString("FLAG_PIU_MENO_D");
	}

	public BigDecimal getNumAnniReclusioneD() throws DAOException {
		return getBigDecimal("NUM_ANNI_RECLUSIONE_D");
	}

	public BigDecimal getNumMesiReclusioneD() throws DAOException {
		return getBigDecimal("NUM_MESI_RECLUSIONE_D");
	}

	public BigDecimal getNumGiorniReclusioneD() throws DAOException {
		return getBigDecimal("NUM_GIORNI_RECLUSIONE_D");
	}

	public BigDecimal getImportoMultaD() throws DAOException {
		return getBigDecimal("IMPORTO_MULTA_D");
	}

	public BigDecimal getNumAnniArrestoD() throws DAOException {
		return getBigDecimal("NUM_ANNI_ARRESTO_D");
	}

	public BigDecimal getNumMesiArrestoD() throws DAOException {
		return getBigDecimal("NUM_MESI_ARRESTO_D");
	}

	public BigDecimal getNumGiorniArrestoD() throws DAOException {
		return getBigDecimal("NUM_GIORNI_ARRESTO_D");
	}

	public BigDecimal getImportoAmmendaD() throws DAOException {
		return getBigDecimal("IMPORTO_AMMENDA_D");
	}

	public BigDecimal getNumGiorniRevocaLAD() throws DAOException {
		return getBigDecimal("NUM_GIORNI_LA_REV_D");
	}

	public BigDecimal getNumGiorniRevocaLSD() throws DAOException {
		return getBigDecimal("NUM_GIORNI_LS_REV_D");
	}

	public BigDecimal getNumGiorniRevocaLID() throws DAOException {
		return getBigDecimal("NUM_GIORNI_LI_REV_D");
	}

	public String getMotivazioniD() throws DAOException {
		return getString("MOTIVAZIONI_D");
	}

	public String getCodTipoProvvedimento() throws DAOException {
		return getString("COD_TIPO_PROVVEDIMENTO");
	}

	public BigDecimal getAnnoSIUS() throws DAOException {
		return getBigDecimal("ANNO_SIUS");
	}

	public String getNumeroSIUS() throws DAOException {
		return getString("NUMERO_SIUS");
	}

	public String getCodTipoMsD() throws DAOException {
		return getString("COD_TIPO_MS_D");
	}

	public BigDecimal getNumAnniMsD() throws DAOException {
		return getBigDecimal("NUM_ANNI_MS_D");
	}

	public BigDecimal getNumMesiMsD() throws DAOException {
		return getBigDecimal("NUM_MESI_MS_D");
	}

	public BigDecimal getNumGiorniMsD() throws DAOException {
		return getBigDecimal("NUM_GIORNI_MS_D");
	}

	public String getBenSospCond() throws DAOException {
		return getString("BEN_SOSP_COND");
	}

	public String getBenNonMenzione() throws DAOException {
		return getString("BEN_NON_MENZIONE");
	}

	public String getBenIndulto() throws DAOException {
		return getString("BEN_INDULTO");
	}

	public String getCodTipoPenaAccessoriaD() throws DAOException {
		return getString("COD_TIPO_PENA_ACCESSORIA_D");
	}

	public String getCodTipoDurataPaD() throws DAOException {
		return getString("COD_TIPO_DURATA_D");
	}

	public BigDecimal getNumAnniPaD() throws DAOException {
		return getBigDecimal("NUM_ANNI_PA_D");
	}

	public BigDecimal getNumMesiPaD() throws DAOException {
		return getBigDecimal("NUM_MESI_PA_D");
	}

	public BigDecimal getNumGiorniPaD() throws DAOException {
		return getBigDecimal("NUM_GIORNI_PA_D");
	}

	public Date getDataRevoca() throws DAOException {
		return getDate("DATA_REVOCA");
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

	public BigDecimal getRicIdRichiestePmInCumulo() throws DAOException {
		return getBigDecimal("RIC_ID_RICHIESTE_PM_IN_CUMULO");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setIdProvvedimentoGeSorvCum(BigDecimal aValore) {
		setBigDecimal("ID_PROVVEDIMENTO_GE_SORV_CUM", aValore);
	}

	public void setCodUfficioEmittente(String aValore) {
		setString("COD_UFFICIO_EMITTENTE", aValore);
	}

	public void setCodLuogoEmittente(String aValore) {
		setString("COD_LUOGO_EMITTENTE", aValore);
	}

	public void setDataD(Date aValore) {
		setDate("DATA_D", aValore);
	}

	public void setAnnoProvv(BigDecimal aValore) {
		setBigDecimal("ANNO_PROVV", aValore);
	}

	public void setNumeroProvv(String aValore) {
		setString("NUMERO_PROVV", aValore);
	}

	public void setFlagConforme(String aValore) {
		setString("FLAG_CONFORME", aValore);
	}

	public void setFlagPiuMenoD(String aValore) {
		setString("FLAG_PIU_MENO_D", aValore);
	}

	public void setNumAnniReclusioneD(BigDecimal aValore) {
		setBigDecimal("NUM_ANNI_RECLUSIONE_D", aValore);
	}

	public void setNumMesiReclusioneD(BigDecimal aValore) {
		setBigDecimal("NUM_MESI_RECLUSIONE_D", aValore);
	}

	public void setNumGiorniReclusioneD(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_RECLUSIONE_D", aValore);
	}

	public void setImportoMultaD(BigDecimal aValore) {
		setBigDecimal("IMPORTO_MULTA_D", aValore);
	}

	public void setNumAnniArrestoD(BigDecimal aValore) {
		setBigDecimal("NUM_ANNI_ARRESTO_D", aValore);
	}

	public void setNumMesiArrestoD(BigDecimal aValore) {
		setBigDecimal("NUM_MESI_ARRESTO_D", aValore);
	}

	public void setNumGiorniArrestoD(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_ARRESTO_D", aValore);
	}

	public void setImportoAmmendaD(BigDecimal aValore) {
		setBigDecimal("IMPORTO_AMMENDA_D", aValore);
	}

	public void setNumGiorniRevocaLAD(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_LA_REV_D", aValore);
	}

	public void setNumGiorniRevocaLSD(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_LS_REV_D", aValore);
	}

	public void setNumGiorniRevocaLID(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_LI_REV_D", aValore);
	}

	public void setMotivazioniD(String aValore) {
		setString("MOTIVAZIONI_D", aValore);
	}

	public void setCodTipoProvvedimento(String aValore) {
		setString("COD_TIPO_PROVVEDIMENTO", aValore);
	}

	public void setAnnoSIUS(BigDecimal aValore) {
		setBigDecimal("ANNO_SIUS", aValore);
	}

	public void setNumeroSIUS(String aValore) {
		setString("NUMERO_SIUS", aValore);
	}

	public void setCodTipoMsD(String aValore) {
		setString("COD_TIPO_MS_D", aValore);
	}

	public void setNumAnniMsD(BigDecimal aValore) {
		setBigDecimal("NUM_ANNI_MS_D", aValore);
	}

	public void setNumMesiMsD(BigDecimal aValore) {
		setBigDecimal("NUM_MESI_MS_D", aValore);
	}

	public void setNumGiorniMsD(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_MS_D", aValore);
	}

	public void setBenSospCond(String aValore) {
		setString("BEN_SOSP_COND", aValore);
	}

	public void setBenNonMenzione(String aValore) {
		setString("BEN_NON_MENZIONE", aValore);
	}

	public void setBenIndulto(String aValore) {
		setString("BEN_INDULTO", aValore);
	}

	public void setCodTipoPenaAccessoriaD(String aValore) {
		setString("COD_TIPO_PENA_ACCESSORIA_D", aValore);
	}

	public void setCodTipoDurataPaD(String aValore) {
		setString("COD_TIPO_DURATA_D", aValore);
	}

	public void setNumAnniPaD(BigDecimal aValore) {
		setBigDecimal("NUM_ANNI_PA_D", aValore);
	}

	public void setNumMesiPaD(BigDecimal aValore) {
		setBigDecimal("NUM_MESI_PA_D", aValore);
	}

	public void setNumGiorniPaD(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI_PA_D", aValore);
	}

	public void setDataRevoca(Date aValore) {
		setDate("DATA_REVOCA", aValore);
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

	public void setRicIdRichiestePmInCumulo(BigDecimal aValore) {
		setBigDecimal("RIC_ID_RICHIESTE_PM_IN_CUMULO", aValore);
	}

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new ProvvedimentoGeSorvCumModel(getIdProvvedimentoGeSorvCum(), getCodUfficioEmittente(), "",
				getCodLuogoEmittente(), "", getDataD(), getAnnoProvv(), getNumeroProvv(), getFlagConforme(),
				getFlagPiuMenoD(), getNumAnniReclusioneD(), getNumMesiReclusioneD(),
				getNumGiorniReclusioneD(), getImportoMultaD(), getNumAnniArrestoD(), getNumMesiArrestoD(),
				getNumGiorniArrestoD(), getImportoAmmendaD(), getNumGiorniRevocaLAD(),
				getNumGiorniRevocaLSD(), getNumGiorniRevocaLID(), getMotivazioniD(),
				getCodTipoProvvedimento(), "", getAnnoSIUS(), getNumeroSIUS(), getCodTipoMsD(), "",
				getNumAnniMsD(), getNumMesiMsD(), getNumGiorniMsD(), getBenSospCond(), getBenNonMenzione(),
				getBenIndulto(),

				getCodTipoPenaAccessoriaD(), "", getCodTipoDurataPaD(), "", getNumAnniPaD(), getNumMesiPaD(),
				getNumGiorniPaD(), getDataRevoca(),

				getCodOperatoreInserimento(), getDataInserimento(), getCodUfficioInserimento(),
				getCodOperatoreAggiornamento(), getDataAggiornamento(), getCodUfficioAggiornamento(),
				getRicIdRichiestePmInCumulo());
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(ProvvedimentoGeSorvCumModel aModel) throws DAOException {
		setIdProvvedimentoGeSorvCum(aModel.getIdProvvedimentoGeSorvCum());
		setCodUfficioEmittente(aModel.getCodUfficioEmittente());
		setCodLuogoEmittente(aModel.getCodLuogoEmittente());
		setDataD(aModel.getDataD());
		setAnnoProvv(aModel.getAnnoProvv());
		setNumeroProvv(aModel.getNumeroProvv());
		setFlagConforme(aModel.getFlagConforme());
		setFlagPiuMenoD(aModel.getFlagPiuMenoD());
		setNumAnniReclusioneD(aModel.getNumAnniReclusioneD());
		setNumMesiReclusioneD(aModel.getNumMesiReclusioneD());
		setNumGiorniReclusioneD(aModel.getNumGiorniReclusioneD());
		setImportoMultaD(aModel.getImportoMultaD());
		setNumAnniArrestoD(aModel.getNumAnniArrestoD());
		setNumMesiArrestoD(aModel.getNumMesiArrestoD());
		setNumGiorniArrestoD(aModel.getNumGiorniArrestoD());
		setImportoAmmendaD(aModel.getImportoAmmendaD());
		setNumGiorniRevocaLAD(aModel.getNumGiorniRevocaLaD());
		setNumGiorniRevocaLSD(aModel.getNumGiorniRevocaLsD());
		setNumGiorniRevocaLID(aModel.getNumGiorniRevocaLiD());

		setMotivazioniD(aModel.getMotivazioniD());
		setCodTipoProvvedimento(aModel.getCodTipoProvvedimento());
		setAnnoSIUS(aModel.getAnnoSIUS());
		setNumeroSIUS(aModel.getNumeroSIUS());
		setCodTipoMsD(aModel.getCodTipoMsD());
		setNumAnniMsD(aModel.getNumAnniMsD());
		setNumMesiMsD(aModel.getNumMesiMsD());
		setNumGiorniMsD(aModel.getNumGiorniMsD());
		setBenSospCond(aModel.getBenSospCond());
		setBenNonMenzione(aModel.getBenNonMenzione());
		setBenIndulto(aModel.getBenIndulto());

		setCodTipoPenaAccessoriaD(aModel.getCodTipoPenaAccessoriaD());
		setCodTipoDurataPaD(aModel.getCodTipoDurataPaD());
		setNumAnniPaD(aModel.getNumAnniPaD());
		setNumMesiPaD(aModel.getNumMesiPaD());
		setNumGiorniPaD(aModel.getNumGiorniPaD());
		setDataRevoca(aModel.getDataRevoca());

		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setRicIdRichiestePmInCumulo(aModel.getRicIdRichiestePmInCumulo());
	}

	/******************************************************************************
	 * Metodo specifico per effattuare l'aggiornamento dei solo campi aggiornabili a partire dal contenuto del
	 * model passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ******************************************************************************/
	public void setDAOFromModelForUpdate(ProvvedimentoGeSorvCumModel aModel) throws DAOException {
		// setIdProvvedimentoGeSorvCum ( aModel.getIdProvvedimentoGeSorvCum() );
		setCodUfficioEmittente(aModel.getCodUfficioEmittente());
		setCodLuogoEmittente(aModel.getCodLuogoEmittente());
		setDataD(aModel.getDataD());
		setAnnoProvv(aModel.getAnnoProvv());
		setNumeroProvv(aModel.getNumeroProvv());
		setFlagConforme(aModel.getFlagConforme());
		setFlagPiuMenoD(aModel.getFlagPiuMenoD());
		setNumAnniReclusioneD(aModel.getNumAnniReclusioneD());
		setNumMesiReclusioneD(aModel.getNumMesiReclusioneD());
		setNumGiorniReclusioneD(aModel.getNumGiorniReclusioneD());
		setImportoMultaD(aModel.getImportoMultaD());
		setNumAnniArrestoD(aModel.getNumAnniArrestoD());
		setNumMesiArrestoD(aModel.getNumMesiArrestoD());
		setNumGiorniArrestoD(aModel.getNumGiorniArrestoD());
		setImportoAmmendaD(aModel.getImportoAmmendaD());
		setNumGiorniRevocaLAD(aModel.getNumGiorniRevocaLaD());
		setNumGiorniRevocaLSD(aModel.getNumGiorniRevocaLsD());
		setNumGiorniRevocaLID(aModel.getNumGiorniRevocaLiD());
		setMotivazioniD(aModel.getMotivazioniD());
		setCodTipoProvvedimento(aModel.getCodTipoProvvedimento());
		setAnnoSIUS(aModel.getAnnoSIUS());
		setNumeroSIUS(aModel.getNumeroSIUS());
		setCodTipoMsD(aModel.getCodTipoMsD());
		setNumAnniMsD(aModel.getNumAnniMsD());
		setNumMesiMsD(aModel.getNumMesiMsD());
		setNumGiorniMsD(aModel.getNumGiorniMsD());
		setBenSospCond(aModel.getBenSospCond());
		setBenNonMenzione(aModel.getBenNonMenzione());
		setBenIndulto(aModel.getBenIndulto());

		setCodTipoPenaAccessoriaD(aModel.getCodTipoPenaAccessoriaD());
		setCodTipoDurataPaD(aModel.getCodTipoDurataPaD());
		setNumAnniPaD(aModel.getNumAnniPaD());
		setNumMesiPaD(aModel.getNumMesiPaD());
		setNumGiorniPaD(aModel.getNumGiorniPaD());
		setDataRevoca(aModel.getDataRevoca());

		// setCodOperatoreInserimento ( aModel.getCodOperatoreInserimento() );
		// setDataInserimento ( aModel.getDataInserimento() );
		// setCodUfficioInserimento ( aModel.getCodUfficioInserimento() );
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setRicIdRichiestePmInCumulo(aModel.getRicIdRichiestePmInCumulo());
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(ProvvedimentoGeSorvCumModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdProvvedimentoGeSorvCum() != null) {
			lCondizioni += " and ID_PROVVEDIMENTO_GE_SORV_CUM = " + aModel.getIdProvvedimentoGeSorvCum() + "";
		}
		if (aModel.getCodUfficioEmittente() != null && aModel.getCodUfficioEmittente().length() > 0) {
			lCondizioni += " and COD_UFFICIO_EMITTENTE = '" + aModel.getCodUfficioEmittente() + "' ";
		}
		if (aModel.getCodLuogoEmittente() != null && aModel.getCodLuogoEmittente().length() > 0) {
			lCondizioni += " and COD_LUOGO_EMITTENTE = '" + aModel.getCodLuogoEmittente() + "' ";
		}
		if (aModel.getDataD() != null) {
			lCondizioni += " and to_char(DATA_D,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataD(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getAnnoProvv() != null) {
			lCondizioni += " and ANNO_PROVV = " + aModel.getAnnoProvv() + "";
		}
		if (aModel.getNumeroProvv() != null && aModel.getNumeroProvv().length() > 0) {
			lCondizioni += " and NUMERO_PROVV = '" + aModel.getNumeroProvv() + "' ";
		}
		if (aModel.getFlagConforme() != null && aModel.getFlagConforme().length() > 0) {
			lCondizioni += " and FLAG_CONFORME = '" + aModel.getFlagConforme() + "' ";
		}
		if (aModel.getFlagPiuMenoD() != null && aModel.getFlagPiuMenoD().length() > 0) {
			lCondizioni += " and FLAG_PIU_MENO_D = '" + aModel.getFlagPiuMenoD() + "' ";
		}
		if (aModel.getNumAnniReclusioneD() != null) {
			lCondizioni += " and NUM_ANNI_RECLUSIONE_D = " + aModel.getNumAnniReclusioneD() + "";
		}
		if (aModel.getNumMesiReclusioneD() != null) {
			lCondizioni += " and NUM_MESI_RECLUSIONE_D = " + aModel.getNumMesiReclusioneD() + "";
		}
		if (aModel.getNumGiorniReclusioneD() != null) {
			lCondizioni += " and NUM_GIORNI_RECLUSIONE_D = " + aModel.getNumGiorniReclusioneD() + "";
		}
		if (aModel.getImportoMultaD() != null) {
			lCondizioni += " and IMPORTO_MULTA_D = " + aModel.getImportoMultaD() + "";
		}
		if (aModel.getNumAnniArrestoD() != null) {
			lCondizioni += " and NUM_ANNI_ARRESTO_D = " + aModel.getNumAnniArrestoD() + "";
		}
		if (aModel.getNumMesiArrestoD() != null) {
			lCondizioni += " and NUM_MESI_ARRESTO_D = " + aModel.getNumMesiArrestoD() + "";
		}
		if (aModel.getNumGiorniArrestoD() != null) {
			lCondizioni += " and NUM_GIORNI_ARRESTO_D = " + aModel.getNumGiorniArrestoD() + "";
		}
		if (aModel.getImportoAmmendaD() != null) {
			lCondizioni += " and IMPORTO_AMMENDA_D = " + aModel.getImportoAmmendaD() + "";
		}
		if (aModel.getNumGiorniRevocaLaD() != null) {
			lCondizioni += " and NUM_GIORNI_LA_REV_D = " + aModel.getNumGiorniRevocaLaD() + "";
		}
		if (aModel.getNumGiorniRevocaLsD() != null) {
			lCondizioni += " and NUM_GIORNI_LS_REV_D = " + aModel.getNumGiorniRevocaLsD() + "";
		}
		if (aModel.getNumGiorniRevocaLiD() != null) {
			lCondizioni += " and NUM_GIORNI_LI_REV_D = " + aModel.getNumGiorniRevocaLiD() + "";
		}
		if (aModel.getMotivazioniD() != null && aModel.getMotivazioniD().length() > 0) {
			lCondizioni += " and MOTIVAZIONI_D = '" + aModel.getMotivazioniD() + "' ";
		}
		if (aModel.getCodTipoProvvedimento() != null && aModel.getCodTipoProvvedimento().length() > 0) {
			lCondizioni += " and COD_TIPO_PROVVEDIMENTO = '" + aModel.getCodTipoProvvedimento() + "' ";
		}
		if (aModel.getAnnoSIUS() != null) {
			lCondizioni += " and ANNO_SIUS = " + aModel.getAnnoSIUS() + "";
		}
		if (aModel.getNumeroSIUS() != null && aModel.getNumeroSIUS().length() > 0) {
			lCondizioni += " and NUMERO_SIUS = '" + aModel.getNumeroSIUS() + "' ";
		}
		if (aModel.getCodTipoMsD() != null && aModel.getCodTipoMsD().length() > 0) {
			lCondizioni += " and COD_TIPO_MS_D = '" + aModel.getCodTipoMsD() + "' ";
		}
		if (aModel.getNumAnniMsD() != null) {
			lCondizioni += " and NUM_ANNI_MS_D = " + aModel.getNumAnniMsD() + "";
		}
		if (aModel.getNumMesiMsD() != null) {
			lCondizioni += " and NUM_MESI_MS_D = " + aModel.getNumMesiMsD() + "";
		}
		if (aModel.getNumGiorniMsD() != null) {
			lCondizioni += " and NUM_GIORNI_MS_D = " + aModel.getNumGiorniMsD() + "";
		}
		if (aModel.getBenSospCond() != null && aModel.getBenSospCond().length() > 0) {
			lCondizioni += " and BEN_SOSP_COND = '" + aModel.getBenSospCond() + "' ";
		}
		if (aModel.getBenNonMenzione() != null && aModel.getBenNonMenzione().length() > 0) {
			lCondizioni += " and BEN_NON_MENZIONE = '" + aModel.getBenNonMenzione() + "' ";
		}
		if (aModel.getBenIndulto() != null && aModel.getBenIndulto().length() > 0) {
			lCondizioni += " and BEN_INDULTO = '" + aModel.getBenIndulto() + "' ";
		}

		if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' ";
		}
		if (aModel.getDataInserimento() != null) {
			lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInserimento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' ";
		}
		if (aModel.getCodOperatoreAggiornamento() != null
				&& aModel.getCodOperatoreAggiornamento().length() > 0) {
			lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento()
					+ "' ";
		}
		if (aModel.getDataAggiornamento() != null) {
			lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAggiornamento(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) {
			lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' ";
		}
		if (aModel.getRicIdRichiestePmInCumulo() != null) {
			lCondizioni += " and RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aModel.getRicIdRichiestePmInCumulo()
					+ "";
		}
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		setCondition(lCondizioni);
	}

	/*****************************************************************************
	 * Imposta la condizione di where per l'operazione di update puntuale si entra sempre in chiave
	 * 
	 * @param key
	 ****************************************************************************/
	public void selCondizioneUpdate(BigDecimal aIdProvvedimentoGeSorvCum) {
		String lCondizioni = new String();

		lCondizioni += " and ID_PROVVEDIMENTO_GE_SORV_CUM = " + aIdProvvedimentoGeSorvCum;
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		setCondition(lCondizioni);
	}

	/*****************************************************************************
	 * Imposta la condizione di where per l'operazione di cancellazione si entra per idRichiesta
	 * 
	 * @param key
	 ****************************************************************************/
	public void selCondizioneByIdRichiesta(BigDecimal aIdRichiesta) {
		String lCondizioni = new String();

		lCondizioni += " and RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aIdRichiesta;
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		setCondition(lCondizioni);
	}

	/*****************************************************************************
	 * Imposta la condizione di order by per la ricerca
	 * 
	 *****************************************************************************/
	public void setOrderBy() {
		String orderBy = "";
		// =======================================================================
		// Lasciare orderBy="" se non si vuole scegliere un ordinamento,
		// altrimenti elencare i campi separati da virgola
		// n.b. non inserire la clausola ORDER BY, viene aggiunta automaticamente
		// =======================================================================

		setOrder(orderBy);
	}

}
