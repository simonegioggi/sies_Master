package siap.siep.fascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.MinorMask;

/**
 * <p>
 * Title: FascicoloSiepSoggettoSqlDAO
 * </p>
 * <p>
 * Description: Realizza Sql DAo del Fascicolo Siep
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class FascicoloSiepSoggettoSqlDAO extends SIAPSqlDAO {

	public FascicoloSiepSoggettoSqlDAO(Connection aCon) {
		super(aCon);
	}

	/**
	 * Esegue la ricerca del fascicolo siep per ID Sentenza e ritorna solo l'ID
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void ricercaFascicoloByIDSentenza(BigDecimal aID) throws DAOException {
		String lStatement = getFascicoloSqlSoggetto("");

		lStatement += " AND FASC.SEN_ID_SENTENZA = '" + aID + "'";
		setStatement(lStatement);
	}

	public void ricercaFascicoloMajorSoggetto(SoggettoModel aModel, String codUfficio) {
		String soggettoFascicolo = "";
		soggettoFascicolo += getFascicoloMajorSqlSoggettoQuery();
		soggettoFascicolo += setCondizione(aModel);

		soggettoFascicolo += " and FASC.id_fascicolo_siep = vse.FAS_SIE_ID_FASCICOLO_SIEP ";
		soggettoFascicolo += MinorMask.minorCondition("vse", "FASC", codUfficio);

		soggettoFascicolo += setOrder();
		setStatement(soggettoFascicolo);
	}

	// MEV_57: aggiunto parametro di passaggio
	public void ricercaFascicoloMajorSoggettoPerSige(SoggettoModel aModel, String majorOffice) {
		String soggettoFascicolo = "";
		soggettoFascicolo += getFascicoloSqlSoggettoQuery();
		soggettoFascicolo += setCondizione(aModel);

		// MEV_57: aggiunto parametro di passaggio ed and condition
		soggettoFascicolo += " and FASC.id_fascicolo_siep = vse.FAS_SIE_ID_FASCICOLO_SIEP ";
		soggettoFascicolo += MinorMask.minorCondition("vse", "FASC", majorOffice);

		soggettoFascicolo += setOrder();
		setStatement(soggettoFascicolo);
	}

	public void ricercaFascicoloSoggettoPerSIGE(SoggettoModel aModel) {
		String soggettoFascicolo = "";
		soggettoFascicolo += getFascicoloSqlSoggettoQuery();
		soggettoFascicolo += setCondizionePerSIGE(aModel);
		soggettoFascicolo += setOrderSoggDataNascIrrev();
		setStatement(soggettoFascicolo);
	}

	public void ricercaFascicoloSoggetto(SoggettoModel aModel) {
		String soggettoFascicolo = "";
		soggettoFascicolo += getFascicoloSqlSoggettoQuery();
		soggettoFascicolo += setCondizione(aModel);
		soggettoFascicolo += setOrderSoggDataNascIrrev();
		setStatement(soggettoFascicolo);
	}

	public void ricercaFascicoloRGNR(SentenzaModel aModel, String majorOffice) {
		String soggettoFascicolo = "";
		soggettoFascicolo += getFascicoloSqlSoggettoQuery();
		soggettoFascicolo += setCondizioneRGNR(aModel, majorOffice);
		// soggettoFascicolo += setOrder();
		soggettoFascicolo += " order by ANNO_REGE_PM, RGNR, CHIAVE_ANNO, CHIAVE_PROGR ";
		soggettoFascicolo = soggettoFascicolo.replaceAll("AND FASC.FLAG_VALIDATO = 'S'", "");
		setStatement(soggettoFascicolo);
	}

	public void ricercaFascicoloSuperSoggetto(SoggettoModel aModel, String majorOffice) {
		String soggettoFascicolo = "";
		// 23/04/2010 soggettoFascicolo += getFascicoloSqlSoggettoQuery();
		soggettoFascicolo += getFascicoloSqlSoggetto(majorOffice);
		soggettoFascicolo += setCondizioneSuperSoggetto(aModel);
		soggettoFascicolo += setOrder();
		setStatement(soggettoFascicolo);
	}

	public void RicercaFascicoliBySoggettoProprioUfficioPaged(SoggettoModel aModel,
			String lCodUfficioUtenteConnesso, int aPage) {
		String soggettoFascicolomioUffi = "";
		String lPaginedStatement = new String("");

		soggettoFascicolomioUffi += getFascicoloMioUffiSqlSoggettoQuery(aPage);
		soggettoFascicolomioUffi += setCondizioneMioUfficio(lCodUfficioUtenteConnesso);
		soggettoFascicolomioUffi += setCondizioneSoggettoParziale(aModel);
		soggettoFascicolomioUffi += setOrder();
		// soggettoFascicolomioUffi += setOrderByCoNome();

		// Se apage = 0 , la Query serve per il totale
		if (aPage == 0) {
			setStatement(soggettoFascicolomioUffi);
		} else {
			lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + soggettoFascicolomioUffi
					+ "  ) INNER ) WHERE rn between  " + ((aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1)
					+ " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE;
			setStatement(lPaginedStatement);
		}
	}

	public void ricercaFascicoloSuperSoggettoPerSige(SoggettoModel aModel) {
		String soggettoFascicolo = getFascicoloSqlSoggetto("");
		soggettoFascicolo += setCondizioneSuperSoggettoPerSige(aModel);
		soggettoFascicolo += setOrder();
		setStatement(soggettoFascicolo);
	}

	/**
	 * paolo cherubini 04/08/2009 condizioni del SuperSoggetto Esegue la ricerca dei fascicoli in base al
	 * super soggetto
	 *
	 * @param strCodiceDistrettoUtente
	 */
	protected String setCondizioneSuperSoggetto(SoggettoModel aModel) {
		String lCondizioni = new String();
		// 27/10/2010 Utilizzo di UPPER e toUpperCase per normalizzare il
		// controllo di uguaglianza x
		// (PATERNITA, COGNOME_MADRE, ATTO_NASCITA, COD_AFIS)
		if (aModel.getIdSoggetto().doubleValue() != 0) {
			lCondizioni = " AND ID_SOGGETTO = " + aModel.getIdSoggetto();
		} else {
			if (aModel.getAnnoNascita() != null)
				lCondizioni += " AND sogg.ANNO_NASCITA = '" + aModel.getAnnoNascita() + "'";
			else
				lCondizioni += " AND sogg.ANNO_NASCITA is null";

			// MEV_39: risolta casistica per unix (l'atto di nascita comprende il carattere ' --> NA'00 287
			if (aModel.getAttoNascita() != null && aModel.getAttoNascita().length() > 0)
				lCondizioni += " AND UPPER(sogg.ATTO_NASCITA) = '"
						+ StringUtils.convertSqlString(aModel.getAttoNascita().toUpperCase()) + "'";
			else
				lCondizioni += " AND sogg.ATTO_NASCITA is null";

			if (aModel.getCodAfis() != null && aModel.getCodAfis().length() > 0)
				lCondizioni += " AND UPPER(sogg.COD_AFIS) = '" + aModel.getCodAfis().toUpperCase() + "'";
			else
				lCondizioni += " AND sogg.COD_AFIS is null";

			if (aModel.getCodComuneNascita() != null && aModel.getCodComuneNascita().length() > 0)
				lCondizioni += " AND sogg.COD_COMUNE_NASCITA = '" + aModel.getCodComuneNascita() + "'";
			else
				lCondizioni += " AND sogg.COD_COMUNE_NASCITA is null";

			if (aModel.getCodCs() != null && aModel.getCodCs().length() > 0)
				lCondizioni += " AND sogg.COD_CS = '" + aModel.getCodCs() + "'";
			else
				lCondizioni += " AND sogg.COD_CS is null";

			if (aModel.getCodFiscale() != null && aModel.getCodFiscale().length() > 0)
				lCondizioni += " AND sogg.COD_FISCALE = '" + aModel.getCodFiscale() + "'";
			else
				lCondizioni += " AND sogg.COD_FISCALE is null";

			if (aModel.getCodProvinciaNascita() != null && aModel.getCodProvinciaNascita().length() > 0)
				lCondizioni += " AND sogg.COD_PROVINCIA_NASCITA = '" + aModel.getCodProvinciaNascita() + "'";
			else
				lCondizioni += " AND sogg.COD_PROVINCIA_NASCITA is null";

			if (aModel.getCodStatoNascita() != null && aModel.getCodStatoNascita().length() > 0)
				lCondizioni += " AND sogg.COD_STATO_NASCITA = '" + aModel.getCodStatoNascita() + "'";
			else
				lCondizioni += " AND sogg.COD_STATO_NASCITA is null";

			if (aModel.getCognome() != null && aModel.getCognome().length() > 0)
				lCondizioni += " AND sogg.COGNOME = '" + StringUtils.convertSqlString(aModel.getCognome())
						+ "'";
			else
				lCondizioni += " AND sogg.COGNOME is null";

			if (aModel.getNome() != null && aModel.getNome().length() > 0)
				lCondizioni += " AND sogg.NOME = '" + StringUtils.convertSqlString(aModel.getNome()) + "'";
			else
				lCondizioni += " AND sogg.NOME is null";

			if (aModel.getDataNascita() != null)
				// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
				lCondizioni += " AND trunc(sogg.DATA_NASCITA) = to_date('"
						+ DateUtils.getDateToString(aModel.getDataNascita(), "dd/MM/yyyy")
						+ "','DD-MM-YYYY')";
			else
				lCondizioni += " AND sogg.DATA_NASCITA is null";

			if (aModel.getDataNascitaPresunta() != null && aModel.getDataNascitaPresunta().length() > 0)
				lCondizioni += " AND sogg.DATA_NASCITA_PRESUNTA = '" + aModel.getDataNascitaPresunta() + "'";
			else
				lCondizioni += " AND sogg.DATA_NASCITA_PRESUNTA is null";

			if (aModel.getEtaPresuntaAnni() != null)
				lCondizioni += " AND sogg.ETA_PRESUNTA_ANNI = " + aModel.getEtaPresuntaAnni();
			else
				lCondizioni += " AND sogg.ETA_PRESUNTA_ANNI is null";

			if (aModel.getEtaPresuntaMesi() != null)
				lCondizioni += " AND sogg.ETA_PRESUNTA_MESI = " + aModel.getEtaPresuntaMesi();
			else
				lCondizioni += " AND sogg.ETA_PRESUNTA_MESI is null";

			if (aModel.getDescComuneNascitaEstero() != null
					&& aModel.getDescComuneNascitaEstero().length() > 0)
				lCondizioni += " AND sogg.DESC_COMUNE_NASCITA_ESTERO = '"
						+ StringUtils.convertSqlString(aModel.getDescComuneNascitaEstero()) + "'";
			else
				lCondizioni += " AND sogg.DESC_COMUNE_NASCITA_ESTERO is null";

			if (aModel.getNazionalita() != null && aModel.getNazionalita().length() > 0)
				lCondizioni += " AND sogg.NAZIONALITA = '" + aModel.getNazionalita() + "'";
			else
				lCondizioni += " AND sogg.NAZIONALITA is null";

			if (aModel.getPaternita() != null && aModel.getPaternita().length() > 0)
				lCondizioni += " AND UPPER(sogg.PATERNITA) = '"
						+ StringUtils.convertSqlString(aModel.getPaternita().toUpperCase()) + "'";
			else
				lCondizioni += " AND sogg.PATERNITA is null";

			if (aModel.getCognomeMadre() != null && aModel.getCognomeMadre().length() > 0)
				lCondizioni += " AND UPPER(sogg.COGNOME_MADRE) = '"
						+ StringUtils.convertSqlString(aModel.getCognomeMadre().toUpperCase()) + "'";
			else
				lCondizioni += " AND sogg.COGNOME_MADRE is null";

			if (aModel.getNomeMadre() != null && aModel.getNomeMadre().length() > 0)
				lCondizioni += " AND UPPER(sogg.NOME_MADRE) = '"
						+ StringUtils.convertSqlString(aModel.getNomeMadre().toUpperCase()) + "'";
			else
				lCondizioni += " AND sogg.NOME_MADRE is null";

			if (aModel.getSesso() != null && aModel.getSesso().length() > 0)
				lCondizioni += " AND sogg.SESSO = '" + aModel.getSesso() + "'";
			else
				lCondizioni += " AND sogg.SESSO is null";

			if (aModel.getMeseNascita() != null)
				lCondizioni += " AND sogg.MESE_NASCITA = " + aModel.getMeseNascita();
			else
				lCondizioni += " AND sogg.MESE_NASCITA is null";
		}
		return lCondizioni;
	}

	protected String setCondizioneSuperSoggettoPerSige(SoggettoModel aModel) {

		String lCondizioni = new String();
		// 27/10/2010 Utilizzo di UPPER e toUpperCase per normalizzare il
		// controllo di uguaglianza x (PATERNITA, COGNOME_MADRE, ATTO_NASCITA, COD_AFIS)
		if (aModel.getIdSoggetto().doubleValue() != 0) {
			lCondizioni = " AND ID_SOGGETTO = " + aModel.getIdSoggetto();
		} else {
			// Ticket#20200226013 — SIGE - ricerca omonimi (vedi anche ticket 20200218019 Catania)
			// su segnalazione della MAFFUCCI si ripristina il vecchio funzionamento
			// Modifica del 21/12/2016 MEV_15_S4
			// Come richiesto da Michele, e confermato da Vito, la ricerca per Soggetto viene ristretta ai
			// soli campi Cognome, Nome,
			// Ticket#20200226013 — SIGE - ricerca omonimi (vedi anche ticket 20200218019 Catania)
			// START
			if (aModel.getAnnoNascita() != null)
				lCondizioni += " AND sogg.ANNO_NASCITA = '" + aModel.getAnnoNascita() + "'";
			else
				lCondizioni += " AND sogg.ANNO_NASCITA is null";

			if (aModel.getAttoNascita() != null && aModel.getAttoNascita().length() > 0)
				lCondizioni += " AND UPPER(sogg.ATTO_NASCITA) = '" + aModel.getAttoNascita().toUpperCase()
						+ "'";
			else
				lCondizioni += " AND sogg.ATTO_NASCITA is null";

			if (aModel.getCodAfis() != null && aModel.getCodAfis().length() > 0)
				lCondizioni += " AND UPPER(sogg.COD_AFIS) = '" + aModel.getCodAfis().toUpperCase() + "'";
			else
				lCondizioni += " AND sogg.COD_AFIS is null";

			if (aModel.getCodComuneNascita() != null && aModel.getCodComuneNascita().length() > 0)
				lCondizioni += " AND sogg.COD_COMUNE_NASCITA = '" + aModel.getCodComuneNascita() + "'";
			else
				lCondizioni += " AND sogg.COD_COMUNE_NASCITA is null";

			if (aModel.getCodCs() != null && aModel.getCodCs().length() > 0)
				lCondizioni += " AND sogg.COD_CS = '" + aModel.getCodCs() + "'";
			else
				lCondizioni += " AND sogg.COD_CS is null";

			if (aModel.getCodFiscale() != null && aModel.getCodFiscale().length() > 0)
				lCondizioni += " AND sogg.COD_FISCALE = '" + aModel.getCodFiscale() + "'";
			else
				lCondizioni += " AND sogg.COD_FISCALE is null";

			if (aModel.getCodProvinciaNascita() != null && aModel.getCodProvinciaNascita().length() > 0)
				lCondizioni += " AND sogg.COD_PROVINCIA_NASCITA = '" + aModel.getCodProvinciaNascita() + "'";
			else
				lCondizioni += " AND sogg.COD_PROVINCIA_NASCITA is null";

			if (aModel.getCodStatoNascita() != null && aModel.getCodStatoNascita().length() > 0)
				lCondizioni += " AND sogg.COD_STATO_NASCITA = '" + aModel.getCodStatoNascita() + "'";
			else
				lCondizioni += " AND sogg.COD_STATO_NASCITA is null";

			if (aModel.getCognome() != null && aModel.getCognome().length() > 0)
				lCondizioni += " AND sogg.COGNOME = '" + StringUtils.convertSqlString(aModel.getCognome())
						+ "'";
			else
				lCondizioni += " AND sogg.COGNOME is null";

			if (aModel.getNome() != null && aModel.getNome().length() > 0)
				lCondizioni += " AND sogg.NOME = '" + StringUtils.convertSqlString(aModel.getNome()) + "'";
			else
				lCondizioni += " AND sogg.NOME is null";

			if (aModel.getDataNascita() != null)
				// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
				lCondizioni += " AND trunc(sogg.DATA_NASCITA) = to_date('"
						+ DateUtils.getDateToString(aModel.getDataNascita(), "dd/MM/yyyy")
						+ "','DD-MM-YYYY')";
			else
				lCondizioni += " AND sogg.DATA_NASCITA is null";

			if (aModel.getDataNascitaPresunta() != null && aModel.getDataNascitaPresunta().length() > 0)
				lCondizioni += " AND sogg.DATA_NASCITA_PRESUNTA = '" + aModel.getDataNascitaPresunta() + "'";
			else
				lCondizioni += " AND sogg.DATA_NASCITA_PRESUNTA is null";

			if (aModel.getEtaPresuntaAnni() != null)
				lCondizioni += " AND sogg.ETA_PRESUNTA_ANNI = '" + aModel.getEtaPresuntaAnni() + "'";
			else
				lCondizioni += " AND sogg.ETA_PRESUNTA_ANNI is null";

			if (aModel.getEtaPresuntaMesi() != null)
				lCondizioni += " AND sogg.ETA_PRESUNTA_MESI = '" + aModel.getEtaPresuntaMesi() + "'";
			else
				lCondizioni += " AND sogg.ETA_PRESUNTA_MESI is null";

			if (aModel.getDescComuneNascitaEstero() != null
					&& aModel.getDescComuneNascitaEstero().length() > 0)
				lCondizioni += " AND sogg.DESC_COMUNE_NASCITA_ESTERO = '"
						+ StringUtils.convertSqlString(aModel.getDescComuneNascitaEstero()) + "'";
			else
				lCondizioni += " AND sogg.DESC_COMUNE_NASCITA_ESTERO is null";

			if (aModel.getNazionalita() != null && aModel.getNazionalita().length() > 0)
				lCondizioni += " AND sogg.NAZIONALITA = '" + aModel.getNazionalita() + "'";
			else
				lCondizioni += " AND sogg.NAZIONALITA is null";

			if (aModel.getPaternita() != null && aModel.getPaternita().length() > 0)
				lCondizioni += " AND UPPER(sogg.PATERNITA) = '"
						+ StringUtils.convertSqlString(aModel.getPaternita().toUpperCase()) + "'";
			else
				lCondizioni += " AND sogg.PATERNITA is null";

			if (aModel.getCognomeMadre() != null && aModel.getCognomeMadre().length() > 0)
				lCondizioni += " AND UPPER(sogg.COGNOME_MADRE) = '"
						+ StringUtils.convertSqlString(aModel.getCognomeMadre().toUpperCase()) + "'";
			else
				lCondizioni += " AND sogg.COGNOME_MADRE is null";

			if (aModel.getNomeMadre() != null && aModel.getNomeMadre().length() > 0)
				lCondizioni += " AND UPPER(sogg.NOME_MADRE) = '"
						+ StringUtils.convertSqlString(aModel.getNomeMadre().toUpperCase()) + "'";
			else
				lCondizioni += " AND sogg.NOME_MADRE is null";

			if (aModel.getSesso() != null && aModel.getSesso().length() > 0)
				lCondizioni += " AND sogg.SESSO = '" + aModel.getSesso() + "'";
			else
				lCondizioni += " AND sogg.SESSO is null";

			if (aModel.getMeseNascita() != null)
				lCondizioni += " AND sogg.MESE_NASCITA = " + aModel.getMeseNascita();
			else
				lCondizioni += " AND sogg.MESE_NASCITA is null";
			// END
		}
		return lCondizioni;
	}

	/**
	 * Settaggio della condizione sul Soggetto
	 *
	 * @param aSm
	 * @return
	 */
	private String setCondizione(SoggettoModel aSm) {
		String lCondizioni = new String();
		if (aSm.getIdSoggetto().doubleValue() == 0) {
			lCondizioni += " AND (( 1=1 ";
			if (!(aSm.getCognome().equals(""))) {
				lCondizioni += " AND COGNOME like '" + StringUtils.convertSqlString(aSm.getCognome()) + "%'";
			}

			if (!(aSm.getNome().equals(""))) {
				lCondizioni += " AND NOME like '" + StringUtils.convertSqlString(aSm.getNome()) + "%'";
			}

			if (!(aSm.getCodComuneNascita().equals(""))) {
				lCondizioni += " AND COD_COMUNE_NASCITA = '" + aSm.getCodComuneNascita() + "'";
			}

			if (!(aSm.getPaternita().equals(""))) {
				lCondizioni += " AND PATERNITA LIKE '"
						+ StringUtils.convertSqlString(aSm.getPaternita().toUpperCase()) + "%'";
			}

			if (!(aSm.getNomeMadre().equals(""))) {
				lCondizioni += " AND NOME_MADRE LIKE '"
						+ StringUtils.convertSqlString(aSm.getNomeMadre().toUpperCase()) + "%'";
			}

			if (!(aSm.getCognomeMadre().equals(""))) {
				lCondizioni += " AND COGNOME_MADRE LIKE '"
						+ StringUtils.convertSqlString(aSm.getCognomeMadre().toUpperCase()) + "%'";
			}

			if (!(aSm.getCodStatoNascita().equals(""))) {
				lCondizioni += " AND COD_STATO_NASCITA = '" + aSm.getCodStatoNascita() + "'";
			}

			if (aSm.getDataNascita() != null) {
				// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
				lCondizioni += " AND trunc(DATA_NASCITA) = TO_DATE('"
						+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";
			}

			lCondizioni += " ) ";

			// MEV 15 - Revisione SIGE - Codice CUI
			if (aSm.getCodAfis() != null && aSm.getCodAfis().length() > 0) {
				lCondizioni += " OR (COD_AFIS = UPPER('" + aSm.getCodAfis() + "') )";
			}

			lCondizioni += " ) ";
		} else {
			lCondizioni = " AND ID_SOGGETTO = " + aSm.getIdSoggetto();
		}

		// lCondizioni += " ORDER BY COGNOME, NOME ";

		return lCondizioni;
	}

	private String setCondizioneRGNR(SentenzaModel aModel, String majorOffice) {
		String lCondizioni = new String();

		if (aModel.getAnnoRGNRIniziale() != null) {
			lCondizioni += " AND ANNO_REGE_PM >= " + aModel.getAnnoRGNRIniziale();
		}

		if (aModel.getNumeroRGNRIniziale() != null) {
			lCondizioni += " AND TO_NUMBER (RTRIM (NUMERO_REGE_PM, 'NC')) >= '"
					+ aModel.getNumeroRGNRIniziale() + "' ";
		}

		if (aModel.getAnnoRGNRFinale() != null) {
			lCondizioni += " AND ANNO_REGE_PM <= " + aModel.getAnnoRGNRFinale();
		}

		if (aModel.getNumeroRGNRFinale() != null) {
			lCondizioni += " AND TO_NUMBER (RTRIM (NUMERO_REGE_PM, 'NC')) <= '" + aModel.getNumeroRGNRFinale()
					+ "' ";
		}

		lCondizioni += " AND SENT.FLAG_VISIBILITA IS NULL ";

		// lCondizioniUD += " WHERE uff.cod_ufficio = fasc.chiave_ufficio ";
		// lCondizioniUD += " AND fasc.chiave_ufficio = '" +
		// strCodUfficioUtenteConnesso + "' ";
		lCondizioni += " AND fasc.id_fascicolo_siep = vse.fas_sie_id_fascicolo_siep ";
		if (StringUtils.checkValidValue(majorOffice)) {
			lCondizioni += MinorMask.minorCondition("vse", "FASC", majorOffice);
		}

		return lCondizioni;
	}

	protected String getFascicoloMajorSqlSoggettoQuery() {
		String lStatement = new String();
		lStatement += " SELECT FASC.ID_FASCICOLO_SIEP ID_FASCICOLO_SIEP, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA,";
		lStatement += "   SENT.DATA_PROVVEDIMENTO DATA_PROVVEDIMENTO,SENT.COD_TIPO_PROVVEDIMENTO COD_TIPO_PROVVEDIMENTO,";
		lStatement += "   SENT.COD_TIPO_AUTORITA_EMITTENTE COD_TIPO_AUTORITA_EMITTENTE, ";
		lStatement += "   SENT.COD_LUOGO_EMITTENTE COD_LUOGO_EMITTENTE,LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE,";
		lStatement += "   FASC.DATA_IRREVOCABILITA DATA_IRREVOCABILITA,FASC.CHIAVE_ANNO CHIAVE_ANNO, ";
		lStatement += "   FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, ";
		lStatement += "   UFF.COD_TIPO_UFFICIO COD_TIPO_UFFICIO, "; // 12/11/2010
																	// Recupero
																	// Tipo
																	// Ufficio
		lStatement += " 	ANNO_REGE_GIP, NUMERO_REGE_GIP, ANNO_REGE_DIB, NUMERO_REGE_DIB, ANNO_REGE_CAS, ";
		lStatement += "	NUMERO_REGE_CAS, ANNO_REGE_CAP, NUMERO_REGE_CAP, ANNO_REGE_CASAP, NUMERO_REGE_CASAP, ";
		// MEV_66: aggiunte quattro nuove proprietà
		lStatement += " ANNO_REGE_GUP, NUMERO_REGE_GUP, ANNO_REGE_CAPSM, NUMERO_REGE_CAPSM,";
		// Paolo Cherubini: aggiungo questo campo per ordinare x RGNR
		lStatement += "	ANNO_REGE_PM, NUMERO_REGE_PM, TO_NUMBER(RTRIM(NUMERO_REGE_PM,'NC')) RGNR,";
		lStatement += "   ANNO_SENTENZA, NUMERO_SENTENZA, FLAG_VALIDATO, ID_SENTENZA ";

		lStatement += " FROM FASCICOLO_SIEP FASC, SENTENZA SENT, SOGGETTO SOGG,";
		lStatement += "    COMUNE LUOGO_EMITTENTE, UFFICIO UFF, COMUNE DESCR_COM_UFF, V_SOGGETTO_ETA vse ";

		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += "   AND FASC.SEN_ID_SENTENZA = SENT.ID_SENTENZA";
		lStatement += "   AND FASC.FLAG_VALIDATO = 'S'";
		lStatement += " 	AND LUOGO_EMITTENTE.COD_COMUNE = SENT.COD_LUOGO_EMITTENTE";
		lStatement += " 	AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += "   AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";

		return lStatement;
	}

	private String setCondizionePerSIGE(SoggettoModel aSm) {
		String lCondizioni = new String();
		boolean flagOr = false;
		if (aSm.getIdSoggetto().doubleValue() == 0) {
			lCondizioni += " AND (( 1=1 ";

			if (!(aSm.getCognome().equals(""))) {
				lCondizioni += " AND COGNOME like '" + StringUtils.convertSqlString(aSm.getCognome()) + "%'";
				flagOr = true;
			}

			if (!(aSm.getNome().equals(""))) {
				lCondizioni += " AND NOME like '" + StringUtils.convertSqlString(aSm.getNome()) + "%'";
				flagOr = true;
			}

			if (!(aSm.getCodComuneNascita().equals(""))) {
				lCondizioni += " AND COD_COMUNE_NASCITA = '" + aSm.getCodComuneNascita() + "'";
				flagOr = true;
			}

			if (!(aSm.getPaternita().equals(""))) {
				lCondizioni += " AND PATERNITA LIKE '"
						+ StringUtils.convertSqlString(aSm.getPaternita().toUpperCase()) + "%'";
				flagOr = true;
			}

			if (!(aSm.getNomeMadre().equals(""))) {
				lCondizioni += " AND NOME_MADRE LIKE '"
						+ StringUtils.convertSqlString(aSm.getNomeMadre().toUpperCase()) + "%'";
				flagOr = true;
			}

			if (!(aSm.getCognomeMadre().equals(""))) {
				lCondizioni += " AND COGNOME_MADRE LIKE '"
						+ StringUtils.convertSqlString(aSm.getCognomeMadre().toUpperCase()) + "%'";
				flagOr = true;
			}

			if (!(aSm.getCodStatoNascita().equals(""))) {
				lCondizioni += " AND COD_STATO_NASCITA = '" + aSm.getCodStatoNascita() + "'";
				flagOr = true;
			}

			if (aSm.getDataNascita() != null) {
				// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
				lCondizioni += " AND trunc(DATA_NASCITA) = TO_DATE('"
						+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";
				flagOr = true;
			}

			lCondizioni += " ) ";

			// MEV 15 - Revisione SIGE - Codice CUI
			if (aSm.getCodAfis() != null && aSm.getCodAfis().length() > 0) {
				String operator = " AND ";

				// Si elimina poichè estrai dati che non trovano riscontro con le altre ricerche.
				if (flagOr)
					operator = " OR ";

				lCondizioni += " " + operator + " (COD_AFIS = UPPER('" + aSm.getCodAfis() + "') )";
			}

			lCondizioni += " ) ";
		} else {
			lCondizioni = " AND ID_SOGGETTO = " + aSm.getIdSoggetto();
		}
		return lCondizioni;
	}

	protected String getFascicoloSqlSoggettoQuery() {
		String lStatement = new String();
		lStatement += " SELECT FASC.ID_FASCICOLO_SIEP ID_FASCICOLO_SIEP, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA,";
		lStatement += "   SENT.DATA_PROVVEDIMENTO DATA_PROVVEDIMENTO,SENT.COD_TIPO_PROVVEDIMENTO COD_TIPO_PROVVEDIMENTO,";
		lStatement += "   SENT.COD_TIPO_AUTORITA_EMITTENTE COD_TIPO_AUTORITA_EMITTENTE, ";
		lStatement += "   SENT.COD_LUOGO_EMITTENTE COD_LUOGO_EMITTENTE,LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE,";
		lStatement += "   FASC.DATA_IRREVOCABILITA DATA_IRREVOCABILITA,FASC.CHIAVE_ANNO CHIAVE_ANNO, ";
		lStatement += "   FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO, DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, ";
		lStatement += "   UFF.COD_TIPO_UFFICIO COD_TIPO_UFFICIO, "; // 12/11/2010
																	// Recupero
																	// Tipo
																	// Ufficio
		lStatement += " 	ANNO_REGE_GIP, NUMERO_REGE_GIP, ANNO_REGE_DIB, NUMERO_REGE_DIB, ANNO_REGE_CAS, ";
		lStatement += "	NUMERO_REGE_CAS, ANNO_REGE_CAP, NUMERO_REGE_CAP, ANNO_REGE_CASAP, NUMERO_REGE_CASAP, ";
		// MEV_66: aggiunte quattro nuove proprietà
		lStatement += " ANNO_REGE_GUP, NUMERO_REGE_GUP, ANNO_REGE_CAPSM, NUMERO_REGE_CAPSM,";
		// Paolo Cherubini aggiungo questo campo per ordinare x RGNR
		lStatement += "	ANNO_REGE_PM, NUMERO_REGE_PM, TO_NUMBER(RTRIM(NUMERO_REGE_PM,'NC')) RGNR,";
		lStatement += "   ANNO_SENTENZA, NUMERO_SENTENZA, FLAG_VALIDATO, ID_SENTENZA ";

		lStatement += " FROM FASCICOLO_SIEP FASC, SENTENZA SENT, SOGGETTO SOGG,";
		lStatement += "    COMUNE LUOGO_EMITTENTE, UFFICIO UFF, COMUNE DESCR_COM_UFF ";
		lStatement += "    ,v_soggetto_eta vse ";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += "   AND FASC.SEN_ID_SENTENZA = SENT.ID_SENTENZA";
		lStatement += "   AND FASC.FLAG_VALIDATO = 'S'";
		lStatement += " 	AND LUOGO_EMITTENTE.COD_COMUNE = SENT.COD_LUOGO_EMITTENTE";
		lStatement += " 	AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += "   AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";

		// MERGE v10: aggiunta condizione
		lStatement += "   AND FASC.id_fascicolo_siep = vse.fas_sie_id_fascicolo_siep ";
		return lStatement;
	}

	protected String getFascicoloSqlSoggetto(String majorOffice) {

		String lStatement = new String();

		lStatement += " SELECT FASC.ID_FASCICOLO_SIEP ID_FASCICOLO_SIEP, SOGG.COGNOME COGNOME, SOGG.NOME NOME, SOGG.DATA_NASCITA, LUOGO_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, ";
		lStatement += " SOGG.ETA_PRESUNTA_ANNI ETA_PRESUNTA_ANNI, SOGG.ETA_PRESUNTA_MESI ETA_PRESUNTA_MESI, ";
		/*
		 * lStatement +=
		 * " SENT.DATA_PROVVEDIMENTO DATA_PROVVEDIMENTO,SENT.COD_TIPO_PROVVEDIMENTO COD_TIPO_PROVVEDIMENTO,";
		 * lStatement += " SENT.COD_TIPO_AUTORITA_EMITTENTE COD_TIPO_AUTORITA_EMITTENTE, ";
		 */
		lStatement += " SENT.COD_LUOGO_EMITTENTE COD_LUOGO_EMITTENTE,LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE,";

		// lStatement += " SENT.*, "; non e' possibile usare questi comandi
		// crea ambiguita' di colonna
		lStatement += " SENT.ID_SENTENZA, SENT.COD_TIPO_PROVVEDIMENTO, SENT.ANNO_REGE_PM, SENT.NUMERO_REGE_PM, SENT.DATA_PROVVEDIMENTO,";
		lStatement += " SENT.COD_TIPO_AUTORITA_EMITTENTE,  SENT.NUM_SEZIONE_AUTORITA_EMITTENTE, SENT.ANNO_SENTENZA,";
		lStatement += " SENT.NUMERO_SENTENZA, SENT.COD_TIPO_PROVV_RIF, SENT.DATA_PROVV_RIF, SENT.ANNO_PROVV_RIF, SENT.NUMERO_PROVV_RIF,";
		lStatement += " SENT.COD_LUOGO_PROVV_RIF,SENT.NUM_SEZIONE_AUTORITA_PROVV_RIF,SENT.COD_TIPO_DECISIONE_CASSAZIONE,SENT.NOTE1_DECISIONE_CASSAZIONE,";
		lStatement += " SENT.NOTE2_DECISIONE_CASSAZIONE,SENT.ANNO_SENTENZA_CASSAZIONE,SENT.NUMERO_SENTENZA_CASSAZIONE,SENT.ANNO_RACCOLTA_GENERALE,";
		lStatement += " SENT.NUMERO_RACCOLTA_GENERALE ,SENT.FLAG_ALTRE_SENTENZE ,SENT.DESCR_ALTRE_SENTENZE ,SENT.ANNO_REGISTRO_35 ,SENT.NUM_REGISTRO_35,";
		lStatement += " SENT.NOTE,  SENT.ANNO_REGE_GIP ,SENT.NUMERO_REGE_GIP ,SENT.ANNO_REGE_DIB ,SENT.NUMERO_REGE_DIB ,SENT.ANNO_REGE_CAS,";
		lStatement += " SENT.NUMERO_REGE_CAS ,SENT.ANNO_REGE_CAP ,SENT.NUMERO_REGE_CAP ,SENT.ANNO_REGE_CASAP ,SENT.NUMERO_REGE_CASAP,";
		// MEV_66: aggiunte quattro nuove proprietà
		lStatement += " SENT.ANNO_REGE_GUP, SENT.NUMERO_REGE_GUP, SENT.ANNO_REGE_CAPSM, SENT.NUMERO_REGE_CAPSM,";
		lStatement += " SENT.COD_OPERATORE_INSERIMENTO, SENT.DATA_INSERIMENTO, SENT.COD_UFFICIO_INSERIMENTO, SENT.COD_OPERATORE_AGGIORNAMENTO,";
		lStatement += " SENT.DATA_AGGIORNAMENTO, SENT.DATA_ISCRIZIONE, SENT.COD_UFFICIO_AGGIORNAMENTO, SENT.COD_BILANCIAMENTO_CIRCOSTANZE, ";
		lStatement += " SENT.FLAG_GIUDIZIO_ABBREVIATO,  SENT.COD_TIPO_RITO,  SENT.COD_TIPO_PROVVEDIMENTO_RIF, SENT.COD_TIPO_PROVVEDIMENTO_ALTRO,";
		lStatement += " SENT.COD_SEDE_NOTIZIA_REATO, SENT.COD_TIPO_AUTORITA_PROVV_RIF, ";
		lStatement += "   FASC.DATA_IRREVOCABILITA DATA_IRREVOCABILITA,FASC.CHIAVE_ANNO CHIAVE_ANNO, FLAG_VALIDATO, ";
		lStatement += "   FASC.CHIAVE_PROGR CHIAVE_PROGR, FASC.CHIAVE_UFFICIO CHIAVE_UFFICIO , DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO";
		lStatement += "   , UFF.COD_TIPO_UFFICIO COD_TIPO_UFFICIO "; // 12/11/2010 Recupero Tipo Ufficio
		lStatement += " FROM FASCICOLO_SIEP FASC, SENTENZA SENT, SOGGETTO SOGG,";
		lStatement += "    COMUNE LUOGO_EMITTENTE, COMUNE LUOGO_NASCITA, UFFICIO UFF, COMUNE DESCR_COM_UFF ";
		lStatement += "   , v_soggetto_eta vse ";
		lStatement += " WHERE FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO";
		lStatement += "   AND FASC.SEN_ID_SENTENZA = SENT.ID_SENTENZA";
		lStatement += " 	AND LUOGO_NASCITA.COD_COMUNE = SOGG.COD_COMUNE_NASCITA";
		lStatement += " 	AND LUOGO_EMITTENTE.COD_COMUNE = SENT.COD_LUOGO_EMITTENTE";
		lStatement += " 	AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO";
		lStatement += "   AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE";
		lStatement += "   AND FASC.id_fascicolo_siep = vse.fas_sie_id_fascicolo_siep ";
		if (StringUtils.checkValidValue(majorOffice))
			lStatement += MinorMask.minorCondition("vse", "FASC", majorOffice);

		return lStatement;
	}

	private String setOrder() {
		String lOrder = new String();

		lOrder = " ORDER BY CHIAVE_ANNO, CHIAVE_PROGR DESC";

		return lOrder;
	}

	// MEV42 - CUMULO
	// preso da getFascicoloSqlSoggettoQuery
	protected String getFascicoloMioUffiSqlSoggettoQuery(int aPage) {
		String lStatement = new String();

		if (aPage > 0) {
			lStatement += " SELECT S.COGNOME COGNOME, S.NOME NOME, S.DATA_NASCITA DATA_NASCITA,";
			lStatement += " S.COD_COMUNE_NASCITA COD_COMUNE_NASCITA, COMUNE_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA,";
			lStatement += "   S.ID_SOGGETTO, S.COD_AFIS, S.ANNO_NASCITA, S.COD_STATO_NASCITA, S.NAZIONALITA,";
			lStatement += "   S.MESE_NASCITA, ";
			lStatement += "   F.ID_FASCICOLO_SIEP, F.CHIAVE_ANNO, F.CHIAVE_PROGR, F.DATA_ISCRIZIONE, F.DATA_IRREVOCABILITA, F.CHIAVE_UFFICIO,";
			lStatement += "   F.COD_STATO_FASCICOLO, STATO_FASC.RV_MEANING STATO_FASCICOLO, F.KEY_PROVV_NSC, ";
			lStatement += "   ST.DATA_PROVVEDIMENTO , ST.ID_SENTENZA, ST.ANNO_SENTENZA, ST.NUMERO_SENTENZA,";
			lStatement += "   ST.COD_TIPO_PROVVEDIMENTO, PROVV.RV_MEANING DESCR_PROVVEDIMENTO,";
			lStatement += "   ST.COD_TIPO_AUTORITA_EMITTENTE COD_TIPO_AUTORITA_EMITTENTE, AUTORITA_EMITTENTE.RV_MEANING DESCR_AUTO_EMITTENTE,";
			lStatement += "   ST.COD_LUOGO_EMITTENTE COD_LUOGO_EMITTENTE, LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE";
		} else {
			lStatement += " Select count(*) HowManyRecords ";
		}

		lStatement += " FROM FASCICOLO_SIEP F, SENTENZA ST, SOGGETTO S,";
		lStatement += " UFFICIO_DESCR UD, UFFICIO U, COMUNE C, CG_REF_CODES AUTORITA_EMITTENTE, ";
		lStatement += " COMUNE COMUNE_NASCITA, COMUNE LUOGO_EMITTENTE, ";
		lStatement += " CG_REF_CODES STATO_FASC,";
		lStatement += " CG_REF_CODES PROVV";
		// lStatement +=
		// " LEFT OUTER JOIN CG_REF_CODES STATO_FASC ON (F.COD_STATO_FASCICOLO = STATO_FASC.RV_LOW_VALUE AND
		// STATO_FASC.RV_DOMAIN = 'STATO_FASCICOLO')";

		lStatement += " WHERE F.SOG_ID_SOGGETTO = S.ID_SOGGETTO ";
		lStatement += " AND F.SEN_ID_SENTENZA = ST.ID_SENTENZA";
		lStatement += " AND F.CHIAVE_UFFICIO = U.COD_UFFICIO ";
		lStatement += " AND U.COD_COMUNE = C.COD_COMUNE ";
		lStatement += " AND S.COD_COMUNE_NASCITA = COMUNE_NASCITA.COD_COMUNE ";
		lStatement += " AND F.CHIAVE_UFFICIO = UD.COD_UFFICIO ";
		lStatement += " AND LUOGO_EMITTENTE.COD_COMUNE = ST.COD_LUOGO_EMITTENTE";
		lStatement += " AND NVL(ST.COD_TIPO_AUTORITA_EMITTENTE, '-') = AUTORITA_EMITTENTE.RV_LOW_VALUE AND AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO_EMITTENTE'";
		lStatement += " AND NVL(ST.COD_TIPO_PROVVEDIMENTO, '-') = PROVV.RV_LOW_VALUE AND PROVV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'";
		lStatement += " AND nvl(F.COD_STATO_FASCICOLO,'-') = STATO_FASC.RV_LOW_VALUE AND STATO_FASC.RV_DOMAIN = 'STATO_FASCICOLO' ";
		// lStatement +=

		return lStatement;
	}

	private String setCondizioneSoggettoParziale(SoggettoModel aSm) {
		String lCondizioni = new String();

		if (!(aSm.getCognome().equals(""))) {
			lCondizioni += " AND COGNOME like '" + StringUtils.convertSqlString(aSm.getCognome()) + "%'";
		}

		if (!(aSm.getCodAfis().equals(""))) {
			lCondizioni += " AND COD_AFIS = '" + aSm.getCodAfis() + "'";
		}

		if (aSm.getDataNascita() != null) {
			// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
			lCondizioni += " AND trunc(DATA_NASCITA) = TO_DATE('"
					+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + "', 'DDMMYYYY') ";
		}

		if (!(aSm.getNome().equals(""))) {
			lCondizioni += " AND NOME like '" + StringUtils.convertSqlString(aSm.getNome()) + "%'";
		}

		if (!(aSm.getCodComuneNascita().equals(""))) {
			lCondizioni += " AND COD_COMUNE_NASCITA = '" + aSm.getCodComuneNascita() + "'";
		}

		if (!(aSm.getCodStatoNascita().equals(""))) {
			lCondizioni += " AND COD_STATO_NASCITA = '" + aSm.getCodStatoNascita() + "'";
		}

		return lCondizioni;
	}

	private String setCondizioneMioUfficio(String MioUfficio) {
		String lCondizioni = new String();
		lCondizioni = " AND F.CHIAVE_UFFICIO = '" + MioUfficio + "'";
		return lCondizioni;
	}

	// private String setOrderByCoNome() {
	// String lOrder = new String();
	// lOrder = " ORDER BY S.COGNOME, S.NOME DESC";
	// return lOrder;
	// }
	// END - MEV42 - CUMULO

	private String setOrderSoggDataNascIrrev() {
		String lOrder = new String();

		lOrder = " ORDER BY COGNOME, NOME, DATA_NASCITA, DATA_IRREVOCABILITA DESC";

		return lOrder;
	}

	/**
	 *
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getModel() throws DAOException {
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		// 12/11/2010 Correzioni Decodifica Tipo Ufficio
		// lFascicolo.setDescrTipoUfficio(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoUfficio(),lFascicolo.getChiaveUfficio()));
		lFascicolo.setCodTipoUfficio(getString("COD_TIPO_UFFICIO"));
		lFascicolo.setDescrTipoUfficio(DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getTipoUfficio(), lFascicolo.getCodTipoUfficio()));

		lFascicolo.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA")); // paolo
																			// cherubini
																			// 03/08/2009
		lFascicolo.setFlagValidato(getString("FLAG_VALIDATO"));

		SoggettoModel lSoggetto = new SoggettoModel();
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		// MEV 15 - Revisione SIGE
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));

		SentenzaModel lSentenza = new SentenzaModel();
		lSentenza.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lSentenza.setDescrTipoProvvedimento(
				DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoProvvedimenti(),
						lSentenza.getCodTipoProvvedimento()));
		lSentenza.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
		lSentenza.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		lSentenza.setDescrTipoAutoritaEmittente(DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getTipoUfficio(), lSentenza.getCodTipoAutoritaEmittente()));
		lSentenza.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		lSentenza.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));

		lSentenza.setIdSentenza(getBigDecimal("ID_SENTENZA"));
		lSentenza.setAnnoSentenza(getBigDecimal("ANNO_SENTENZA"));
		lSentenza.setNumeroSentenza(getString("NUMERO_SENTENZA"));
		lSentenza.setAnnoRegePm(getBigDecimal("ANNO_REGE_PM"));
		lSentenza.setNumeroRegePm(getString("NUMERO_REGE_PM"));
		lSentenza.setAnnoRegeGip(getBigDecimal("ANNO_REGE_GIP"));
		lSentenza.setNumeroRegeGip(getString("NUMERO_REGE_GIP"));
		lSentenza.setAnnoRegeDib(getBigDecimal("ANNO_REGE_DIB"));
		lSentenza.setNumeroRegeDib(getString("NUMERO_REGE_DIB"));
		lSentenza.setAnnoRegeCas(getBigDecimal("ANNO_REGE_CAS"));
		lSentenza.setNumeroRegeCas(getString("NUMERO_REGE_CAS"));
		lSentenza.setAnnoRegeCap(getBigDecimal("ANNO_REGE_CAP"));
		lSentenza.setNumeroRegeCap(getString("NUMERO_REGE_CAP"));
		lSentenza.setAnnoRegeCasap(getBigDecimal("ANNO_REGE_CASAP"));
		lSentenza.setNumeroRegeCasap(getString("NUMERO_REGE_CASAP"));
		// MEV_66: aggiunte quattro nuove proprietà
		lSentenza.setAnnoRegeGup(getBigDecimal("ANNO_REGE_GUP"));
		lSentenza.setNumeroRegeGup(getString("NUMERO_REGE_GUP"));
		lSentenza.setAnnoRegeCapsm(getBigDecimal("ANNO_REGE_CAPSM"));
		lSentenza.setNumeroRegeCapsm(getString("NUMERO_REGE_CAPSM"));
		// modifica conseguente alla variazione di SentenzaModel - Romaggioli
		// 29/07/2009
		// lSentenza.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA") );

		lFascicolo.setSoggetto(lSoggetto);
		lFascicolo.setSentenza(lSentenza);

		return lFascicolo;
	}

	/**
	 *
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getModelFascSogg() throws DAOException {
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.setDescrComuneUfficio(getString("DESCR_COMUNE_UFFICIO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		// 12/11/2010 Correzioni Decodifica Tipo Ufficio
		// lFascicolo.setDescrTipoUfficio(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoUfficio(),lFascicolo.getChiaveUfficio()));
		lFascicolo.setCodTipoUfficio(getString("COD_TIPO_UFFICIO"));
		lFascicolo.setDescrTipoUfficio(DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getTipoUfficio(), lFascicolo.getCodTipoUfficio()));
		lFascicolo.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA"));
		lFascicolo.setFlagValidato(getString("FLAG_VALIDATO"));

		SoggettoModel lSoggetto = new SoggettoModel();

		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));

		SentenzaModel lSentenza = new SentenzaModel();

		lSentenza.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lSentenza.setDescrTipoProvvedimento(
				DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoProvvedimenti(),
						lSentenza.getCodTipoProvvedimento()));
		lSentenza.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
		lSentenza.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		lSentenza.setDescrTipoAutoritaEmittente(DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getTipoUfficio(), lSentenza.getCodTipoAutoritaEmittente()));
		lSentenza.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		lSentenza.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));

		lSentenza.setIdSentenza(getBigDecimal("ID_SENTENZA"));
		lSentenza.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lSentenza.setAnnoRegePm(getBigDecimal("ANNO_REGE_PM"));
		lSentenza.setNumeroRegePm(getString("NUMERO_REGE_PM"));
		lSentenza.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
		lSentenza.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		lSentenza.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		lSentenza.setNumSezioneAutoritaEmittente(getString("NUM_SEZIONE_AUTORITA_EMITTENTE"));
		lSentenza.setAnnoSentenza(getBigDecimal("ANNO_SENTENZA"));
		lSentenza.setNumeroSentenza(getString("NUMERO_SENTENZA"));
		lSentenza.setCodTipoProvvRif(getString("COD_TIPO_PROVV_RIF"));
		lSentenza.setDataProvvRif(getDate("DATA_PROVV_RIF"));
		lSentenza.setCodTipoAutoritaProvvRif(getString("COD_TIPO_AUTORITA_PROVV_RIF"));
		lSentenza.setAnnoProvvRif(getBigDecimal("ANNO_PROVV_RIF"));
		lSentenza.setNumeroProvvRif(getString("NUMERO_PROVV_RIF"));
		lSentenza.setCodLuogoProvvRif(getString("COD_LUOGO_PROVV_RIF"));
		lSentenza.setNumSezioneAutoritaProvvRif(getString("NUM_SEZIONE_AUTORITA_PROVV_RIF"));
		lSentenza.setCodTipoDecisioneCassazione(getString("COD_TIPO_DECISIONE_CASSAZIONE"));
		lSentenza.setNote1DecisioneCassazione(getString("NOTE1_DECISIONE_CASSAZIONE"));
		lSentenza.setNote2DecisioneCassazione(getString("NOTE2_DECISIONE_CASSAZIONE"));
		lSentenza.setAnnoSentenzaCassazione(getBigDecimal("ANNO_SENTENZA_CASSAZIONE"));
		lSentenza.setNumeroSentenzaCassazione(getString("NUMERO_SENTENZA_CASSAZIONE"));
		lSentenza.setAnnoRaccoltaGenerale(getBigDecimal("ANNO_RACCOLTA_GENERALE"));
		lSentenza.setNumeroRaccoltaGenerale(getString("NUMERO_RACCOLTA_GENERALE"));
		lSentenza.setFlagAltreSentenze(getString("FLAG_ALTRE_SENTENZE"));
		lSentenza.setDescrAltreSentenze(getString("DESCR_ALTRE_SENTENZE"));
		lSentenza.setAnnoRegistro35(getBigDecimal("ANNO_REGISTRO_35"));
		lSentenza.setNumRegistro35(getString("NUM_REGISTRO_35"));
		lSentenza.setNote(getString("NOTE"));
		lSentenza.setAnnoRegeGip(getBigDecimal("ANNO_REGE_GIP"));
		lSentenza.setNumeroRegeGip(getString("NUMERO_REGE_GIP"));
		lSentenza.setAnnoRegeDib(getBigDecimal("ANNO_REGE_DIB"));
		lSentenza.setNumeroRegeDib(getString("NUMERO_REGE_DIB"));
		lSentenza.setAnnoRegeCas(getBigDecimal("ANNO_REGE_CAS"));
		lSentenza.setNumeroRegeCas(getString("NUMERO_REGE_CAS"));
		lSentenza.setAnnoRegeCap(getBigDecimal("ANNO_REGE_CAP"));
		lSentenza.setNumeroRegeCap(getString("NUMERO_REGE_CAP"));
		lSentenza.setAnnoRegeCasap(getBigDecimal("ANNO_REGE_CASAP"));
		lSentenza.setNumeroRegeCasap(getString("NUMERO_REGE_CASAP"));
		lSentenza.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		lSentenza.setDataInserimento(getDate("DATA_INSERIMENTO"));
		lSentenza.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		lSentenza.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		lSentenza.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		lSentenza.setDataIscrizione(getDate("DATA_ISCRIZIONE"));

		lSentenza.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		lSentenza.setCodBilanciamentoCircostanze(getString("COD_BILANCIAMENTO_CIRCOSTANZE"));
		lSentenza.setFlagGiudizioAbbreviato(getString("FLAG_GIUDIZIO_ABBREVIATO"));
		lSentenza.setCodTipoRito(getString("COD_TIPO_RITO"));

		lSentenza.setCodTipoProvvedimentoRif(getString("COD_TIPO_PROVVEDIMENTO_RIF"));
		lSentenza.setCodTipoProvvedimentoAltro(getString("COD_TIPO_PROVVEDIMENTO_ALTRO"));
		lSentenza.setCodSedeNotiziaReato(getString("COD_SEDE_NOTIZIA_REATO"));

		// modifica conseguente alla variazione di SentenzaModel - Romaggioli
		// 29/07/2009
		// lSentenza.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA") );

		// MEV_66: aggiunte quattro nuove proprietà
		lSentenza.setAnnoRegeGup(getBigDecimal("ANNO_REGE_GUP"));
		lSentenza.setNumeroRegeGup(getString("NUMERO_REGE_GUP"));
		lSentenza.setAnnoRegeCapsm(getBigDecimal("ANNO_REGE_CAPSM"));
		lSentenza.setNumeroRegeCapsm(getString("NUMERO_REGE_CAPSM"));

		lFascicolo.setSoggetto(lSoggetto);
		lFascicolo.setSentenza(lSentenza);

		return lFascicolo;
	}

	public GenericModel getModelFascSoggSentMioUfficio() throws DAOException {
		FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
		lFascicolo.setIdFascicoloSiep(getBigDecimal("ID_FASCICOLO_SIEP"));
		lFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lFascicolo.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lFascicolo.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		lFascicolo.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA"));
		lFascicolo.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		lFascicolo.setDescrStatoFascicolo(getString("STATO_FASCICOLO"));
		lFascicolo.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lFascicolo.setKeyProvvNsc(getBigDecimal("KEY_PROVV_NSC"));

		SoggettoModel lSoggetto = new SoggettoModel();
		lSoggetto.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lSoggetto.setCognome(getString("COGNOME"));
		lSoggetto.setNome(getString("NOME"));
		lSoggetto.setDataNascita(getDate("DATA_NASCITA"));
		lSoggetto.setAnnoNascita(getBigDecimal("ANNO_NASCITA"));
		lSoggetto.setMeseNascita(getBigDecimal("MESE_NASCITA"));
		lSoggetto.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		lSoggetto.setDescrComuneNascita(getString("DESCR_COMUNE_NASCITA"));
		lSoggetto.setCodAfis(getString("COD_AFIS"));
		lSoggetto.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		lSoggetto.setNazionalita(getString("NAZIONALITA"));

		// UD.DESCR_COMUNE DESCR_COMUNE, ";

		SentenzaModel lSentenza = new SentenzaModel();
		lSentenza.setIdSentenza(getBigDecimal("ID_SENTENZA"));
		lSentenza.setAnnoSentenza(getBigDecimal("ANNO_SENTENZA"));
		lSentenza.setNumeroSentenza(getString("NUMERO_SENTENZA"));
		lSentenza.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
		lSentenza.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
		lSentenza.setDescrTipoAutoritaEmittente(DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getTipoUfficio(), lSentenza.getCodTipoAutoritaEmittente()));
		lSentenza.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
		lSentenza.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));
		lSentenza.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lSentenza.setDescrTipoProvvedimento(
				DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoProvvedimenti(),
						lSentenza.getCodTipoProvvedimento()));

		lFascicolo.setSoggetto(lSoggetto);
		lFascicolo.setSentenza(lSentenza);

		return lFascicolo;
	}

}