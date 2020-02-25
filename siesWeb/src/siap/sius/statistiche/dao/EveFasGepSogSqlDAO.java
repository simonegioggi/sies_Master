package siap.sius.statistiche.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.statistiche.model.EveFasGepSogModel;
import siap.sius.statistiche.model.RicercaOrdinanzaModel;
import siap.sius.statistiche.model.RicercaProvvedimentoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: EveFasGepSogSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che gestisce l'accesso per ricerca alla JOIN delle seguenti tabelle: Evento,
 * Fascicolo_Sius, Generale_Procedimento, Soggetto.
 * </p>
 * La classe definisce un metodo di ricerca abstract che viene definito nelle classi figlie, per questo motivo
 * la classe è abstract e quindi non istanziabile.
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public abstract class EveFasGepSogSqlDAO extends SIAPSqlDAO {
	public EveFasGepSogSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	/**
	 * Restituisce lo statement completo per l'implementazione della select.
	 */
	protected String getSqlQuery(String aCodTipoDocumentoAllegato) {
		String lStatement = getSqlQuerySelect() + " FROM EVENTO E "
				+ getSqlQueryJoin(aCodTipoDocumentoAllegato);

		return lStatement;
	}

	/**
	 * 
	 * @return
	 */
	protected String getSqlQuerySelect() {
		String lStatement = getSqlQuerySelectNoDocAll();

		lStatement += ", " + getSqlQueryDocumentoAllegato();

		return lStatement;
	}

	protected String getSqlQuerySelectNoDocAll() {
		String lStatement = new String("");

		lStatement += " SELECT " + getSqlQueryEvento() + ", " + getSqlQueryFascicoloSius() + ", "
				+ getSqlQueryGeneraleProcedimento() + ", " + getSqlQuerySoggetto();
		return lStatement;
	}

	protected String getSqlQueryJoinNoDocAll() {
		String lStatement = new String("");

		lStatement += "  join FASCICOLO_SIUS F"
				+ " on (E.FAS_SIU_ID_FASCICOLO_SIUS = F.ID_FASCICOLO_SIUS)"
				+ " join GENERALE_PROCEDIMENTO G on (G.FAS_SIU_ID_FASCICOLO_SIUS = F.ID_FASCICOLO_SIUS ) "
				+ " join SOGGETTO S on (S.ID_SOGGETTO = F.SOG_ID_SOGGETTO ) "
				+ " left join COMUNE SCN ON (S.COD_COMUNE_NASCITA = SCN.COD_COMUNE) "
				+ " left outer join MAGISTRATO_RELATORE MR ON (MR.FAS_SIU_ID_FASCICOLO_SIUS = F.ID_FASCICOLO_SIUS AND MR.DATA_FINE IS NULL) "
				+ " left outer join ESPERTO ESP ON (ESP.ID_ESPERTO = MR.ESP_ID_ESPERTO) ";

		return lStatement;
	}

	protected String getSqlQueryJoin(String aCodTipoDocumentoAllegato) {
		String lStatement = getSqlQueryJoinNoDocAll();

		lStatement += getSqlQueryJoinDocumentoAllegato(aCodTipoDocumentoAllegato);

		return lStatement;
	}

	protected String getSqlQueryJoinDocumentoAllegato(String aCodTipoDocumentoAllegato) {
		String lStatement = new String("");

		lStatement += " left outer join DOCUMENTO_ALLEGATO DA" + " on (DA.EVE_ID_EVENTO = E.ID_EVENTO";
		if (aCodTipoDocumentoAllegato != null)
			lStatement += " AND DA.COD_TIPO_DOCUMENTO = '" + aCodTipoDocumentoAllegato + "'";

		lStatement += ")";
		return lStatement;
	}

	/**
	 * Attraverso la lettura della fetch valorizza il model strutturato EveFasGepSogModel. A tal fine utilizza
	 * 5 funzioni di tipo getModel che restituiscono i 5 model semplici di cui si compone EveFasGepSogModel e
	 * che sono: EventoModel, FascicoloSiusModel, GeneraleProcedimentoModel, SoggettoModel,
	 * DocumentoAllegatoModel.
	 * 
	 * @return GenericModel.
	 */
	public GenericModel getModel() throws DAOException {
		EventoModel lEvento = getEventoModel();
		FascicoloSiusModel lFascicoloSius = getFascicoloSiusModel();
		GeneraleProcedimentoModel lGeneraleProcedimento = getGeneraleProcedimentoModel();
		SoggettoModel lSoggetto = getSoggettoModel();
		DocumentoAllegatoModel lDocAllegato = getDocumentoAllegatoModel();

		EveFasGepSogModel lModel = new EveFasGepSogModel(lEvento, lFascicoloSius, lGeneraleProcedimento,
				lSoggetto, lDocAllegato);
		return lModel;
	}

	public GenericModel getModelNoDocAll() throws DAOException {
		EventoModel lEvento = getEventoModel();
		FascicoloSiusModel lFascicoloSius = getFascicoloSiusModel();
		GeneraleProcedimentoModel lGeneraleProcedimento = getGeneraleProcedimentoModel();
		SoggettoModel lSoggetto = getSoggettoModel();

		EveFasGepSogModel lModel = new EveFasGepSogModel(lEvento, lFascicoloSius, lGeneraleProcedimento,
				lSoggetto);
		return lModel;
	}

	protected EventoModel getEventoModel() throws DAOException {
		// Lettura dei campi dalla tabella EVNTO
		EventoModel lEvento = new EventoModel();

		lEvento.setIdEvento(getBigDecimal("ID_EVENTO"));
		lEvento.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
		lEvento.setDescrTipoEvento("");
		lEvento.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
		lEvento.setDescrTipoProvvedimento(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
				.getTipoProvvedimenti(), lEvento.getCodTipoProvvedimento()));
		lEvento.setCodMotivo(getString("COD_MOTIVO"));
		lEvento.setDescrMotivo(decodificaCodMotivo(lEvento.getCodMotivo()));
		lEvento.setDataEmissione(getDate("DATA_EMISSIONE"));
		lEvento.setCodEsito(getString("COD_ESITO"));
		try {
			// si ricava la descrizione dell'Esito Provvedimento dalle Decodifiche in memoria per risparmiare
			// una JOIN
			lEvento.setDescrEsito(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
					.getEsitoProvvedimento(), lEvento.getCodEsito()));
		} catch (DAOException de) {
			throw de;
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}

		lEvento.setFlagDocumentoRegistrato(getString("FLAG_DOCUMENTO_REGISTRATO"));
		lEvento.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));

		return lEvento;
	}

	protected String getSqlQueryEvento() {
		String lStatement = new String("");

		lStatement += " E.ID_EVENTO, " + " E.COD_TIPO_EVENTO," + " E.COD_TIPO_PROVVEDIMENTO,"
				+ " E.COD_MOTIVO," + " E.DATA_EMISSIONE," + " E.COD_ESITO, "
				+ " E.FLAG_DOCUMENTO_REGISTRATO," + " E.EVE_ID_EVENTO";
		return lStatement;
	}

	protected FascicoloSiusModel getFascicoloSiusModel() throws DAOException {
		FascicoloSiusModel lModel = new FascicoloSiusModel();
		// Lettura campi Tabella Fascicolo_sius
		lModel.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
		lModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		lModel.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		lModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		lModel.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		lModel.setDescrStatoFascicolo("");
		return lModel;
	}

	protected String getSqlQueryFascicoloSius() {
		String lStatement = new String("");

		lStatement += " F.ID_FASCICOLO_SIUS, " + " F.CHIAVE_ANNO," + " F.CHIAVE_UFFICIO,"
				+ " F.CHIAVE_PROGR," + " F.COD_STATO_FASCICOLO";
		return lStatement;
	}

	protected GeneraleProcedimentoModel getGeneraleProcedimentoModel() throws DAOException {
		GeneraleProcedimentoModel lModel = new GeneraleProcedimentoModel();

		lModel.setIdGeneraleProcedimento(getBigDecimal("ID_GENERALE_PROCEDIMENTO"));
		lModel.setAnnoS1(getBigDecimal("ANNO_S1"));
		lModel.setProgrS1(getBigDecimal("PROGR_S1"));
		lModel.setDataCameraConsiglio(getDate("DATA_CAMERA_CONSIGLIO"));
		return lModel;
	}

	protected String getSqlQueryGeneraleProcedimento() {
		String lStatement = new String("");

		lStatement += " G.ID_GENERALE_PROCEDIMENTO, " + " G.ANNO_S1," + " G.PROGR_S1,"
				+ " G.DATA_CAMERA_CONSIGLIO";
		return lStatement;
	}

	protected SoggettoModel getSoggettoModel() throws DAOException {
		SoggettoModel lModel = new SoggettoModel();

		lModel.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
		lModel.setCognome(getString("COGNOME"));
		lModel.setNome(getString("NOME"));
		lModel.setDataNascita(getDate("DATA_NASCITA"));

		// lModel.setDataNascitaPresunta(getString("DATA_NASCITA_PRESUNTA") );
		lModel.setCodComuneNascita(getString("COD_COMUNE_NASCITA"));
		lModel.setDescrComuneNascita(getString("LUOGO_NASCITA"));

		lModel.setCodProvinciaNascita(getString("COD_PROVINCIA_NASCITA"));
		// La descrizione della Provincia di nascita la si ricava dalle Decodifiche in memoria per risparmiare
		// una JOIN
		lModel.setDescrProvinciaNascita(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
				.getProvincie(), lModel.getCodProvinciaNascita()));

		lModel.setCodStatoNascita(getString("COD_STATO_NASCITA"));
		// La descrizione dello stato di nascita la si ricava dalle Decodifiche in memoria per risparmiare una
		// JOIN
		lModel.setDescrStatoNascita(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
				.getNazioni(), lModel.getCodStatoNascita()));
		lModel.setDescComuneNascitaEstero(getString("DESC_COMUNE_NASCITA_ESTERO"));

		lModel.setNazionalita(getString("NAZIONALITA"));
		// La descrizione della nazionalità la si ricava dalle Decodifiche in memoria per risparmiare una JOIN
		lModel.setDescrNazionalita(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
				.getNazionalita(), lModel.getNazionalita()));

		return lModel;
	}

	protected String getSqlQuerySoggetto() {
		String lStatement = new String("");

		lStatement += " S.ID_SOGGETTO, " + " S.COGNOME, " + " S.NOME, " + " S.DATA_NASCITA, "
				+ " S.COD_COMUNE_NASCITA, " + " SCN.DESCRIZIONE LUOGO_NASCITA, "
				+ " S.COD_PROVINCIA_NASCITA, " + " S.DESC_COMUNE_NASCITA_ESTERO, " + " S.COD_STATO_NASCITA, "
				+ " S.NAZIONALITA ";
		return lStatement;
	}

	protected String getSqlQueryEsperto() {
		String lStatement = new String("");

		lStatement += " ESP.ID_ESPERTO ";
		return lStatement;
	}

	protected DocumentoAllegatoModel getDocumentoAllegatoModel() throws DAOException {
		DocumentoAllegatoModel lModel = new DocumentoAllegatoModel();
		lModel.setIdDocumentoAllegato(getBigDecimal("ID_ALLEGATO"));
		lModel.setFlagDocumentoRegistrato(getString("FLAG_ALLEGATO_REGISTRATO"));
		lModel.setAnnoFoglioComplementare(getBigDecimal("ANNO_FOGLIO_COMPLEMENTARE"));
		lModel.setProgrFoglioComplementare(getBigDecimal("PROGR_FOGLIO_COMPLEMENTARE"));
		lModel.setDataEmissione(getDate("DATA_EMISSIONE_ALLEGATO"));

		return lModel;
	}

	/**
	 * Il metodo restituisce la parte dello statement di select che riporta i campi da leggere dalla tabella
	 * DOCUMENTO_ALLEGATO.
	 * 
	 * @return
	 */
	protected String getSqlQueryDocumentoAllegato() {
		String lStatement = new String("");

		lStatement += " DA.ID_DOCUMENTO_ALLEGATO ID_ALLEGATO, DA.FLAG_DOCUMENTO_REGISTRATO FLAG_ALLEGATO_REGISTRATO, DA.ANNO_FOGLIO_COMPLEMENTARE, DA.PROGR_FOGLIO_COMPLEMENTARE, DA.DATA_EMISSIONE DATA_EMISSIONE_ALLEGATO ";

		return lStatement;
	}

	/*
	 * poichè questa funzione viene utilizzata anche in altri moduli sarebbe conveniente spostarla in un punto
	 * accessibile pubblicamente. STUB Luigi
	 */

	/*
	 * COD_MOTIVO nell'Evento può essere tradotto come MOTIVO_PROVVEDIMENTO oppure come OGGETTO_PROCEDIMENTO
	 * (!!) La funzione tenta i due dominii che sono alternativi.
	 */

	private String decodificaCodMotivo(String aCodMotivo) throws DAOException {
		String lDecodifica = new String("");
		if (aCodMotivo != null) {
			// Si prova prima come MOTIVO_PROVVEDIMENTO
			lDecodifica = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
					.getMotivoProvvedimento(), aCodMotivo);
			// Poi come OGGETTO_PROCEDIMENTO
			if (!(lDecodifica.trim().length() > 0))
				lDecodifica = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
						.getOggettoProcedimento(), aCodMotivo);
		}
		return lDecodifica;
	}

	/**
	 * Metodo abstract che verrà implementato in maniera specifica nelle classi specializzate. Questo è
	 * l'unico metodo abstract della classe. Se fosse necessario rendere la classe istanziabile si deve creare
	 * una definizione per questo metodo.
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	abstract public void ricercaProcSiusXProvvedimenti(RicercaOrdinanzaModel aModel) throws DAOException;

	/**
	 * MEV10-s3: aggiunto metodo astratto per l'interfaccia
	 * 
	 * @param aModel
	 * @throws DAOException
	 */
	abstract public void ricercaProcSiusXProvvedimenti(RicercaProvvedimentoModel aModel) throws DAOException;

}