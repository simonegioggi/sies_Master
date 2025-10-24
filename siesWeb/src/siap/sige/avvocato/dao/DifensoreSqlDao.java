package siap.sige.avvocato.dao;

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.StringUtils;
import f3b.util.Utils;
import siap.dao.SIAPSqlDAO;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.udienzaparti.model.AvvocatoParteModel;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;

public class DifensoreSqlDao extends SIAPSqlDAO {

	public DifensoreSqlDao(Connection con) {
		super(con);
	}

	public void ricercaDifensoreAttualeFascicolo(AvvocatoModel aModel, AvvocatoFascicoloSigeModel aFModel)
			throws DAOException {

		String lStatement = new String("");

		lStatement += "SELECT ID_AVVOCATO, COGNOME, NOME, FORO, INDIRIZZO, "
				+ "TELEFONO, FAX, E_MAIL, COD_FISCALE, PROVINCIA, AVVOCATO.CAP, "
				+ "FLAG_VISUALIZZA, ID_AVVOCATO_STANDARD, AVVOCATO.NOTE NOTEAVV, "
				// MEV_21: aggiunti sei campi in tabella + 2 descrittivi
				// + "COMSEDEFORO.DESCRIZIONE descComuneSedeForo, "
				+ "SN.RV_MEANING DESCR_STATO_NASCITA, PEC, FLAG_REGINDE, DESCR_COMUNE_STUDIO, "
				+ "COD_STATO_NASCITA_AVV, DESC_LUOGO_NAS_REGINDE, ID_AVVOCATO_BONIFICATO, "
				+ "AVVOCATO.COD_OPERATORE_INSERIMENTO, AVVOCATO.DATA_INSERIMENTO, "
				+ "AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, AVVOCATO.COD_UFFICIO_INSERIMENTO, "
				+ "AVVOCATO.DATA_AGGIORNAMENTO, AVVTIPODESC.RV_MEANING DESCRTIPO, "
				+ "AVVOCATO.DATA_SOSPESO_FINO_AL, AVVOCATO.DATA_RADIATO_DAL, COD_NON_ATTIVITA, "
				+ "COD_LUOGO_NASCITA, COD_COMUNE_RESIDENZA, AVVOCATO.FLAG_CANCELLATO, "
				+ "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, AVVOCATO.COD_UFFICIO_APPARTENENZA, "
				+ "AVVOCATO.DATA_NASCITA, CG.RV_MEANING DESCR_NON_ATTIVITA, "
				+ "AVVOCATO_FASCICOLO_SIGE.DATA_INIZIO_VALIDITA, "
				+ "AVVOCATO_FASCICOLO_SIGE.ID_AVVOCATO_FASCICOLO_SIGE ";
		lStatement += " FROM AVVOCATO, AVVOCATO_FASCICOLO_SIGE, CG_REF_CODES AVVTIPODESC, CG_REF_CODES CG, "
				+ "COMUNE DESNASCITA, COMUNE DESCR";
		// INIZIO: MEV_21 (avvocati)
		// lStatement += ", CG_REF_CODES AVVFORO, COMUNE COMSEDEFORO";
		lStatement += ", CG_REF_CODES SN";
		// FINE: MEV_21
		lStatement += " WHERE";
		lStatement += " DATA_FINE_VALIDITA IS NULL AND";
		lStatement += " AVVOCATO.ID_AVVOCATO=AVVOCATO_FASCICOLO_SIGE.AVV_ID_AVVOCATO AND";
		lStatement += " AVVTIPODESC.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIGE.COD_TIPO_AVVOCATO";
		lStatement += " AND AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO'";
		lStatement += " AND DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA";
		lStatement += " AND CG.RV_DOMAIN = 'NON_ATTIVITA'";
		// INIZIO: MEV_21 (avvocati)
		// lStatement += " AND AVVOCATO.FORO = AVVFORO.RV_MEANING";
		// lStatement += " AND AVVFORO.RV_DOMAIN = 'FORO_AVVOCATI'";
		// lStatement += " AND COMSEDEFORO.COD_COMUNE = AVVFORO.RV_ALT2_VALUE";
		// 20250616 [SG]: risolto problema ricerca avvocato senza cod stato nascita
		// Ticket#202506120155 - avvocato con foro incompetente- impossibilità aggiornamento secondo avvocato
		lStatement += " AND SN.RV_LOW_VALUE(+) = COD_STATO_NASCITA_AVV";
		lStatement += " AND SN.RV_DOMAIN(+) = 'NAZIONE'";
		// FINE: MEV_21

		lStatement += " " + setCondizione(aModel, aFModel);

		// Gli avvocati sono ordinati in ordine crescente di ID_AVVOCATO
		lStatement += " ORDER BY ID_AVVOCATO_FASCICOLO_SIGE ASC";

		setStatement(lStatement);
	}

	public void ricercaDifensore(AvvocatoModel aModel) throws DAOException {

		String lStatement = new String("");

		lStatement += "SELECT ID_AVVOCATO, COGNOME, NOME, FORO, INDIRIZZO, "
				+ "TELEFONO, FAX, E_MAIL, COD_FISCALE, PROVINCIA, AVVOCATO.CAP, "
				+ "FLAG_VISUALIZZA, ID_AVVOCATO_STANDARD, AVVOCATO.NOTE NOTEAVV, "
				// MEV_21: aggiunti sei campi in tabella
				+ "AVVOCATO.PEC, AVVOCATO.FLAG_REGINDE, AVVOCATO.DESCR_COMUNE_STUDIO, "
				+ "AVVOCATO.COD_STATO_NASCITA_AVV, AVVOCATO.DESC_LUOGO_NAS_REGINDE, "
				+ "AVVOCATO.ID_AVVOCATO_BONIFICATO, AVVOCATO.COD_OPERATORE_INSERIMENTO, "
				+ "AVVOCATO.DATA_INSERIMENTO, AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, "
				+ "AVVOCATO.COD_UFFICIO_INSERIMENTO, AVVOCATO.DATA_AGGIORNAMENTO, "
				+ "COGNOME  DESCRTIPO, AVVOCATO.DATA_SOSPESO_FINO_AL, AVVOCATO.DATA_RADIATO_DAL, "
				+ "COD_NON_ATTIVITA, COD_LUOGO_NASCITA, COD_COMUNE_RESIDENZA, "
				+ "AVVOCATO.FLAG_CANCELLATO, DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, AVVOCATO.COD_UFFICIO_APPARTENENZA, "
				+ "AVVOCATO.DATA_NASCITA, CG.RV_MEANING DESCR_NON_ATTIVITA, "
				+ "DATA_INSERIMENTO DATA_INIZIO_VALIDITA";
		lStatement += " FROM AVVOCATO, CG_REF_CODES CG, COMUNE DESNASCITA,COMUNE DESCR";
		lStatement += " WHERE";
		lStatement += " DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA'";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA";

		lStatement += " " + setCondizione(aModel);
		int lPos = lStatement.indexOf("WHERE");
		String lSql1 = lStatement.substring(0, lPos);
		String lSql2 = lStatement.substring(lPos + 5, lStatement.length());
		lStatement += " MINUS " + lSql1;
		lStatement += " where cod_ufficio_appartenenza='00000' and id_avvocato_standard in ";
		lStatement += " (select id_avvocato_standard from avvocato where flag_visualizza = 1 ";
		// 20191028 [SG]: aggiunta and condition
		lStatement += "AND FLAG_CANCELLATO = 'N'";
		lStatement += " and cod_ufficio_appartenenza = '"
				+ StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase());
		lStatement += "') and " + lSql2;
		lStatement += " ORDER BY COGNOME,NOME ASC";

		setStatement(lStatement);
	}

	public GenericModel getModel() throws DAOException {

		AvvocatoSigeModel aModel = new AvvocatoSigeModel();

		// Inserire le opportune set delle descrizioni!
		aModel.getAvvocato().setIdAvvocato(getBigDecimal("ID_AVVOCATO"));
		aModel.getAvvocato().setCognome(getString("COGNOME"));
		aModel.getAvvocato().setNome(getString("NOME"));
		aModel.getAvvocato().setForo(getString("FORO"));
		aModel.getAvvocato().setIndirizzo(getString("INDIRIZZO"));
		aModel.getAvvocato().setTelefono(getString("TELEFONO"));
		aModel.getAvvocato().setFax(getString("FAX"));
		aModel.getAvvocato().setEMail(getString("E_MAIL"));
		aModel.getAvvocato().setNote(getString("NOTEAVV"));
		aModel.getAvvocato().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.getAvvocato().setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.getAvvocato().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.getAvvocato().setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.getAvvocato().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.getAvvocato().setDescrTipo(getString("DESCRTIPO"));
		aModel.getAvvocato().setCodLuogoNascita(getString("COD_LUOGO_NASCITA"));
		aModel.getAvvocato().setDescLuogoNascita(getString("DESCR_LUOGO_NASCITA"));
		aModel.getAvvocato().setDescrNonAttivita(getString("DESCR_NON_ATTIVITA"));
		aModel.getAvvocato().setDescComuneResidenza(getString("DESCR_COMUNE_RESIDENZA"));
		aModel.getAvvocato().setCodComuneResidenza(getString("COD_COMUNE_RESIDENZA"));
		aModel.getAvvocato().setDataNascita(getDate("DATA_NASCITA"));
		aModel.getAvvocato().setDataSospensione(getDate("DATA_SOSPESO_FINO_AL"));
		aModel.getAvvocato().setDataRadiazione(getDate("DATA_RADIATO_DAL"));
		aModel.getAvvocato().setCodNonAttivita(getString("COD_NON_ATTIVITA"));
		aModel.getAvvocato().setCodUffAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));
		aModel.getAvvocatoFascicoloSigeModel().setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		// aModel.getAvvocatoFascicoloSigeModel().setIdAvvocatoFascicoloSige(getBigDecimal("ID_AVVOCATO_FASCICOLO_SIGE"));
		aModel.getAvvocato().setFlagCancellato(getString("FLAG_CANCELLATO"));
		aModel.getAvvocato().setCodiceFiscale(getString("COD_FISCALE"));
		aModel.getAvvocato().setProvincia(getString("PROVINCIA"));
		aModel.getAvvocato().setCap(getString("CAP"));
		aModel.getAvvocato().setFlagVisualizza(getBigDecimal("FLAG_VISUALIZZA"));
		aModel.getAvvocato().setIdAvvocatoStandard(getBigDecimal("ID_AVVOCATO_STANDARD"));
		// MEV_21 (avvocati): aggiunti sei campi in tabella + 1 descrittivo
		if (findColumn("DescComuneSedeForo"))
			aModel.getAvvocato().setDescComuneSedeForo(getString("DescComuneSedeForo"));
		aModel.getAvvocato().setPec(getString("PEC"));
		aModel.getAvvocato().setFlagRegInde(getString("FLAG_REGINDE"));
		aModel.getAvvocato().setDescrComuneStudio(getString("DESCR_COMUNE_STUDIO"));
		aModel.getAvvocato().setCodStatoNascita(getString("COD_STATO_NASCITA_AVV"));
		aModel.getAvvocato().setDescLuogoNasRegInde(getString("DESC_LUOGO_NAS_REGINDE"));
		aModel.getAvvocato().setIdAvvocatoBonificato(getBigDecimal("ID_AVVOCATO_BONIFICATO"));
		if (findColumn("DESCR_STATO_NASCITA"))
			aModel.getAvvocato().setDescStatoNascita(getString("DESCR_STATO_NASCITA"));
		return aModel;
	}

	public AvvocatoSigeModel getModelAvvSige() throws DAOException {

		AvvocatoSigeModel aModel = (AvvocatoSigeModel) getModel();
		aModel.getAvvocatoFascicoloSigeModel().setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.getAvvocatoFascicoloSigeModel()
				.setIdAvvocatoFascicoloSige(getBigDecimal("ID_AVVOCATO_FASCICOLO_SIGE"));
		return aModel;
	}

	private String setCondizione(AvvocatoModel aModel, AvvocatoFascicoloSigeModel aFModel) {

		String lCondizioni = new String();
		if (aModel != null) {
			if (aModel.getIdAvvocato() != null)
				lCondizioni = " AND ID_AVVOCATO=" + aModel.getIdAvvocato();
			if (aModel.getCognome() != null && aModel.getCognome() != "")
				lCondizioni += " AND COGNOME LIKE '" + aModel.getCognome().toUpperCase() + "%'";
		}

		if (aFModel != null) {
			if (aFModel.getFasSigeIdFascicoloSige() != null) {
				lCondizioni += " AND FAS_SIGE_ID_FASCICOLO_SIGE=" + aFModel.getFasSigeIdFascicoloSige();
			} else {
				lCondizioni += " AND FLAG_VISUALIZZA = 1 ";
			}
		}
		// 11/12/2007 lCondizioni += " AND FLAG_CANCELLATO ='N'";

		return lCondizioni;
	}

	private String setCondizione(AvvocatoModel aModel) {

		String lCondizioni = new String();
		if (aModel.getIdAvvocato() != null) {
			lCondizioni = "  AND ID_AVVOCATO=" + aModel.getIdAvvocato();
		} else {
			if (aModel.getCognome() != null && !aModel.getCognome().equals("")) {
				lCondizioni += " AND COGNOME  LIKE '"
						+ StringUtils.convertSqlString(aModel.getCognome().toUpperCase()) + "%'";
			}

			if (aModel.getNome() != null && !aModel.getNome().equals("")) {
				lCondizioni += " AND NOME  LIKE '"
						+ StringUtils.convertSqlString(aModel.getNome().toUpperCase()) + "%'";
			}

			if (aModel.getForo() != null && !aModel.getForo().equals("")) {
				lCondizioni += " AND FORO LIKE '"
						+ StringUtils.convertSqlString(aModel.getForo().toUpperCase()) + "%'";

			}
			if (aModel.getCodUffAppartenenza() != null && aModel.getCodUffAppartenenza() != "") {
				lCondizioni += " AND (COD_UFFICIO_APPARTENENZA ='"
						+ StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase()) + "'";
				lCondizioni += " OR COD_UFFICIO_APPARTENENZA ='00000' ";
				// lCondizioni +=
				// " OR COD_OPERATORE_INSERIMENTO like 'E%' or COD_OPERATORE_INSERIMENTO like 'D%'";
				lCondizioni += ")";

			}

			// 11/12/2007 lCondizioni += " AND FLAG_CANCELLATO ='N' AND FLAG_VISUALIZZA = 1 ";
			lCondizioni += " AND FLAG_VISUALIZZA = 1 ";

			// 20191028 [SG]: aggiunta and condition
			lCondizioni += " AND FLAG_CANCELLATO = 'N' ";
		}

		return lCondizioni;
	}

	private String setCondizioneAssDifDallaListaSIEP(AvvocatoModel aModel, SentenzaSigeModel sentenza) {

		String lCondizioni = new String();
		if (aModel.getIdAvvocato() != null) {
			lCondizioni = "  AND ID_AVVOCATO=" + aModel.getIdAvvocato();
		} else {
			if (sentenza != null && sentenza.getFasSieIdFascicoloSiep() != null) {
				lCondizioni += "  AND AF.FAS_SIE_ID_FASCICOLO_SIEP = " + sentenza.getFasSieIdFascicoloSiep();
			}
			if (aModel.getNome() != null && !aModel.getNome().equals("")) {
				lCondizioni += " AND NOME  LIKE '"
						+ StringUtils.convertSqlString(aModel.getNome().toUpperCase()) + "%'";
			}
			// MEV_21: modifico condizione
			// if (aModel.getCodUffAppartenenza() != null && aModel.getCodUffAppartenenza() != "") {
			// lCondizioni += " AND (COD_UFFICIO_APPARTENENZA ='"
			// + StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase()) + "'";
			// lCondizioni += " OR COD_UFFICIO_APPARTENENZA ='00000' ";
			// lCondizioni += ")";
			// }
			lCondizioni += " AND COD_UFFICIO_APPARTENENZA ='00000'";
			// MEV_21: aggiungo condizione
			lCondizioni += " AND FLAG_REGINDE = 'SI'";

			lCondizioni += " AND FLAG_VISUALIZZA = 1 ";
		}

		return lCondizioni;
	}

	public void ricercaDifensoreAttualeParteUdienza(AvvocatoModel aAvvocatoMod,
			PartiUdienzaDifensoreModel aPartiUdienzaDifMod) throws DAOException {

		String lStatement = new String("");

		lStatement += "SELECT ID_AVVOCATO, COGNOME, NOME, FORO, INDIRIZZO, "
				+ "TELEFONO, FAX, E_MAIL, COD_FISCALE, PROVINCIA, AVVOCATO.CAP, "
				+ "FLAG_VISUALIZZA, ID_AVVOCATO_STANDARD, AVVOCATO.NOTE NOTEAVV, "
				// MEV_21: aggiunti sei campi in tabella + 2 descrittivi
				+ "COMSEDEFORO.DESCRIZIONE descComuneSedeForo, SN.RV_MEANING DESCR_STATO_NASCITA, "
				+ "AVVOCATO.PEC, AVVOCATO.FLAG_REGINDE, AVVOCATO.DESCR_COMUNE_STUDIO, "
				+ "AVVOCATO.COD_STATO_NASCITA_AVV, AVVOCATO.DESC_LUOGO_NAS_REGINDE, "
				+ "AVVOCATO.ID_AVVOCATO_BONIFICATO, AVVOCATO.COD_OPERATORE_INSERIMENTO, "
				+ "AVVOCATO.DATA_INSERIMENTO, AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, "
				+ "AVVOCATO.COD_UFFICIO_INSERIMENTO, AVVOCATO.DATA_AGGIORNAMENTO, "
				+ "AVVTIPODESC.RV_MEANING DESCRTIPO, AVVOCATO.DATA_SOSPESO_FINO_AL, "
				+ "AVVOCATO.DATA_RADIATO_DAL, COD_NON_ATTIVITA, COD_LUOGO_NASCITA, "
				+ "COD_COMUNE_RESIDENZA, AVVOCATO.FLAG_CANCELLATO, "
				+ "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, AVVOCATO.COD_UFFICIO_APPARTENENZA, "
				+ "AVVOCATO.DATA_NASCITA, CG.RV_MEANING DESCR_NON_ATTIVITA, "
				+ "PARTI_UDIENZA_DIFENSORE.DATA_INIZIO_VALIDITA, "
				+ "PARTI_UDIENZA_DIFENSORE.ID_AVVOCATO_PARTE_UDIENZA";
		lStatement += " FROM AVVOCATO, PARTI_UDIENZA_DIFENSORE, CG_REF_CODES AVVTIPODESC, CG_REF_CODES CG, "
				+ "COMUNE DESNASCITA, COMUNE DESCR";
		// INIZIO: MEV_21 (avvocati)
		lStatement += ", CG_REF_CODES AVVFORO, COMUNE COMSEDEFORO";
		lStatement += ", CG_REF_CODES SN";
		// FINE: MEV_21
		lStatement += " WHERE";
		lStatement += " DATA_FINE_VALIDITA IS NULL";
		lStatement += " AND AVVOCATO.ID_AVVOCATO = PARTI_UDIENZA_DIFENSORE.AVV_ID_AVVOCATO";
		lStatement += " AND AVVTIPODESC.RV_LOW_VALUE = PARTI_UDIENZA_DIFENSORE.COD_TIPO_AVVOCATO";
		lStatement += " AND AVVTIPODESC.RV_DOMAIN = 'TIPO_AVVOCATO'";
		lStatement += " AND DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA";
		lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA";
		lStatement += " AND CG.RV_DOMAIN = 'NON_ATTIVITA'";
		lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA";
		// INIZIO: MEV_21 (avvocati)
		lStatement += " AND AVVOCATO.FORO = AVVFORO.RV_MEANING";
		lStatement += " AND AVVFORO.RV_DOMAIN = 'FORO_AVVOCATI'";
		lStatement += " AND COMSEDEFORO.COD_COMUNE = AVVFORO.RV_ALT2_VALUE";
		// 20250616 [SG]: risolto problema ricerca avvocato senza cod stato nascita
		// Ticket#202506120155 - avvocato con foro incompetente- impossibilità aggiornamento secondo avvocato
		lStatement += " AND SN.RV_LOW_VALUE(+) = COD_STATO_NASCITA_AVV";
		lStatement += " AND SN.RV_DOMAIN(+) = 'NAZIONE'";
		// FINE: MEV_21

		lStatement += " " + setCondizioneParteUdienza(aAvvocatoMod, aPartiUdienzaDifMod);

		lStatement += " ORDER BY ID_AVVOCATO_PARTE_UDIENZA ASC";

		setStatement(lStatement);
	}

	private String setCondizioneParteUdienza(AvvocatoModel aAvvocatoMod,
			PartiUdienzaDifensoreModel aPartiUdienzaDifMod) {

		String lCondizioni = new String();
		if (aAvvocatoMod != null) {
			if (aAvvocatoMod.getIdAvvocato() != null) {
				lCondizioni = " AND ID_AVVOCATO=" + aAvvocatoMod.getIdAvvocato();
			}
			if (aAvvocatoMod.getCognome() != null && aAvvocatoMod.getCognome() != "") {
				lCondizioni += " AND COGNOME LIKE '" + aAvvocatoMod.getCognome().toUpperCase() + "%'";
			}
		}

		if (aPartiUdienzaDifMod != null) {
			if (aPartiUdienzaDifMod.getSoggIdSoggetto() != null) {
				lCondizioni += " AND SOGG_ID_SOGGETTO =" + aPartiUdienzaDifMod.getSoggIdSoggetto();
			} else {
				lCondizioni += " AND FLAG_VISUALIZZA = 1 ";
			}
		}

		return lCondizioni;
	}

	public AvvocatoParteModel getModelAvvParteUdienza() throws DAOException {

		AvvocatoParteModel aModel = (AvvocatoParteModel) getModelParte();
		aModel.getAvvocatoParteUdienzaModel().setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.getAvvocatoParteUdienzaModel()
				.setIdAvvocatoParteUdienza(getBigDecimal("ID_AVVOCATO_PARTE_UDIENZA"));
		return aModel;
	}

	public GenericModel getModelParte() throws DAOException {

		AvvocatoParteModel aModel = new AvvocatoParteModel();

		// Inserire le opportune set delle descrizioni!
		aModel.getAvvocato().setIdAvvocato(getBigDecimal("ID_AVVOCATO"));
		aModel.getAvvocato().setCognome(getString("COGNOME"));
		aModel.getAvvocato().setNome(getString("NOME"));
		aModel.getAvvocato().setForo(getString("FORO"));
		aModel.getAvvocato().setIndirizzo(getString("INDIRIZZO"));
		aModel.getAvvocato().setTelefono(getString("TELEFONO"));
		aModel.getAvvocato().setFax(getString("FAX"));
		aModel.getAvvocato().setEMail(getString("E_MAIL"));
		aModel.getAvvocato().setNote(getString("NOTEAVV"));
		aModel.getAvvocato().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.getAvvocato().setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.getAvvocato().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.getAvvocato().setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.getAvvocato().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.getAvvocato().setDescrTipo(getString("DESCRTIPO"));
		aModel.getAvvocato().setCodLuogoNascita(getString("COD_LUOGO_NASCITA"));
		aModel.getAvvocato().setDescLuogoNascita(getString("DESCR_LUOGO_NASCITA"));
		aModel.getAvvocato().setDescrNonAttivita(getString("DESCR_NON_ATTIVITA"));
		aModel.getAvvocato().setDescComuneResidenza(getString("DESCR_COMUNE_RESIDENZA"));
		aModel.getAvvocato().setCodComuneResidenza(getString("COD_COMUNE_RESIDENZA"));
		aModel.getAvvocato().setDataNascita(getDate("DATA_NASCITA"));
		aModel.getAvvocato().setDataSospensione(getDate("DATA_SOSPESO_FINO_AL"));
		aModel.getAvvocato().setDataRadiazione(getDate("DATA_RADIATO_DAL"));
		aModel.getAvvocato().setCodNonAttivita(getString("COD_NON_ATTIVITA"));
		aModel.getAvvocato().setCodUffAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));
		aModel.getAvvocatoParteUdienzaModel().setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
		aModel.getAvvocato().setFlagCancellato(getString("FLAG_CANCELLATO"));
		aModel.getAvvocato().setCodiceFiscale(getString("COD_FISCALE"));
		aModel.getAvvocato().setProvincia(getString("PROVINCIA"));
		aModel.getAvvocato().setCap(getString("CAP"));
		aModel.getAvvocato().setFlagVisualizza(getBigDecimal("FLAG_VISUALIZZA"));
		aModel.getAvvocato().setIdAvvocatoStandard(getBigDecimal("ID_AVVOCATO_STANDARD"));
		// MEV_21 (avvocati): aggiunti sei campi in tabella
		aModel.getAvvocato().setPec(getString("PEC"));
		aModel.getAvvocato().setFlagRegInde(getString("FLAG_REGINDE"));
		aModel.getAvvocato().setDescrComuneStudio(getString("DESCR_COMUNE_STUDIO"));
		aModel.getAvvocato().setCodStatoNascita(getString("COD_STATO_NASCITA_AVV"));
		aModel.getAvvocato().setDescLuogoNasRegInde(getString("DESC_LUOGO_NAS_REGINDE"));
		aModel.getAvvocato().setIdAvvocatoBonificato(getBigDecimal("ID_AVVOCATO_BONIFICATO"));
		if (findColumn("DescComuneSedeForo"))
			aModel.getAvvocato().setDescComuneSedeForo(getString("DescComuneSedeForo"));
		if (findColumn("DESCR_STATO_NASCITA"))
			aModel.getAvvocato().setDescStatoNascita(getString("DESCR_STATO_NASCITA"));

		return aModel;
	}

	public void ricercaDifensoreDallaListaSiep(AvvocatoModel aModel, SentenzaSigeModel sentenza)
			throws DAOException {

		String lStatement = new String("");

		lStatement += "SELECT AVVOCATO.ID_AVVOCATO, AVVOCATO.COGNOME, AVVOCATO.NOME, "
				+ "AVVOCATO.FORO, AVVOCATO.INDIRIZZO, AVVOCATO.TELEFONO, AVVOCATO.FAX, "
				+ "AVVOCATO.E_MAIL, AVVOCATO.COD_FISCALE, AVVOCATO.PROVINCIA, AVVOCATO.CAP, "
				+ "AVVOCATO.FLAG_VISUALIZZA, AVVOCATO.ID_AVVOCATO_STANDARD, AVVOCATO.NOTE NOTEAVV, "
				// MEV_21: aggiunti sei campi in tabella + 2 descrittiv1
				+ "COMSEDEFORO.DESCRIZIONE descComuneSedeForo, SN.RV_MEANING DESCR_STATO_NASCITA, "
				+ "AVVOCATO.PEC, AVVOCATO.FLAG_REGINDE, AVVOCATO.DESCR_COMUNE_STUDIO, "
				+ "AVVOCATO.COD_STATO_NASCITA_AVV, AVVOCATO.DESC_LUOGO_NAS_REGINDE, "
				+ "AVVOCATO.ID_AVVOCATO_BONIFICATO, AVVOCATO.COD_OPERATORE_INSERIMENTO, "
				+ "AVVOCATO.DATA_INSERIMENTO, AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, "
				+ "AVVOCATO.COD_UFFICIO_INSERIMENTO, AVVOCATO.DATA_AGGIORNAMENTO, "
				+ "AVVOCATO.COGNOME  DESCRTIPO, AVVOCATO.DATA_SOSPESO_FINO_AL, "
				+ "AVVOCATO.DATA_RADIATO_DAL, COD_NON_ATTIVITA, COD_LUOGO_NASCITA, "
				+ "COD_COMUNE_RESIDENZA, AVVOCATO.FLAG_CANCELLATO, "
				+ "DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "
				+ "DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, AVVOCATO.COD_UFFICIO_APPARTENENZA, "
				+ "AVVOCATO.DATA_NASCITA, CG.RV_MEANING DESCR_NON_ATTIVITA, "
				+ "AVVOCATO.DATA_INSERIMENTO DATA_INIZIO_VALIDITA";
		lStatement += " FROM AVVOCATO, CG_REF_CODES CG, COMUNE DESNASCITA, COMUNE DESCR, "
				+ "AVVOCATO_FASCICOLO_SIEP AF, CG_REF_CODES AVVTIPODESC";
		// INIZIO: MEV_21 (avvocati)
		lStatement += ", CG_REF_CODES AVVFORO, COMUNE COMSEDEFORO";
		lStatement += ", CG_REF_CODES SN";
		// FINE: MEV_21
		lStatement += " WHERE";
		lStatement += " AVVOCATO.ID_AVVOCATO=AF.AVV_ID_AVVOCATO AND";
		lStatement += " AVVTIPODESC.RV_LOW_VALUE=AF.COD_TIPO_AVVOCATO AND";
		lStatement += " AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO' AND";
		lStatement += "	DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA AND";
		lStatement += "	DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA AND";
		lStatement += "	CG.RV_DOMAIN  = 'NON_ATTIVITA' AND";
		lStatement += "	CG.RV_LOW_VALUE = COD_NON_ATTIVITA AND";
		// 20250618 [SG]: controllo di consistenza per la sentenza (rivisto il 20251007)
		String fasSieIdFascicoloSiep = (Utils.isNullObj(sentenza)
				&& Utils.isNullObj(sentenza.getFasSieIdFascicoloSiep()))
						? "0"
						: sentenza.getFasSieIdFascicoloSiep().toString();
		lStatement += "	FAS_SIE_ID_FASCICOLO_SIEP=" + fasSieIdFascicoloSiep + " AND";
		lStatement += "	FLAG_CANCELLATO ='N' and data_fine_validita is NULL";
		// INIZIO: MEV_21 (avvocati)
		lStatement += " AND AVVOCATO.FORO = AVVFORO.RV_MEANING";
		lStatement += " AND AVVFORO.RV_DOMAIN = 'FORO_AVVOCATI'";
		lStatement += " AND COMSEDEFORO.COD_COMUNE = AVVFORO.RV_ALT2_VALUE";
		// 20250616 [SG]: risolto problema ricerca avvocato senza cod stato nascita
		// Ticket#202506120155 - avvocato con foro incompetente- impossibilità aggiornamento secondo avvocato
		lStatement += " AND SN.RV_LOW_VALUE(+) = COD_STATO_NASCITA_AVV";
		lStatement += " AND SN.RV_DOMAIN(+) = 'NAZIONE'";
		// FINE: MEV_21

		lStatement += " " + setCondizioneAssDifDallaListaSIEP(aModel, sentenza);
		// MEV_21: tolgo la parte del MINUS
		// int lPos = lStatement.indexOf("WHERE");
		// String lSql1 = lStatement.substring(0, lPos);
		// String lSql2 = lStatement.substring(lPos + 5, lStatement.length());
		// lStatement += " MINUS " + lSql1;
		// lStatement += " where AVVOCATO.cod_ufficio_appartenenza='00000' and AVVOCATO.id_avvocato_standard
		// in ";
		// lStatement += " (select AVVOCATO.id_avvocato_standard from avvocato where AVVOCATO.flag_visualizza
		// = 1 ";
		// lStatement += " and AVVOCATO.cod_ufficio_appartenenza = '"
		// + StringUtils.convertSqlString(aModel.getCodUffAppartenenza().toUpperCase());
		// lStatement += "') and " + lSql2;
		lStatement += " ORDER BY COGNOME, NOME ASC";

		setStatement(lStatement);
	}

}