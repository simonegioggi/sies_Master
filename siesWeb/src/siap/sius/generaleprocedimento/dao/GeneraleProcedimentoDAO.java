package siap.sius.generaleprocedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;

/**
* <p>Title: GeneraleProcedimentoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella GeneraleProcedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class GeneraleProcedimentoDAO extends SIAPTableDAO {

	public GeneraleProcedimentoDAO(Connection con) {

		super(con);

		setTable("GENERALE_PROCEDIMENTO");

		setSequenceField("ID_GENERALE_PROCEDIMENTO", "GEN_PRO_SEQ");
		setFieldKey("ID_GENERALE_PROCEDIMENTO", BIG_DECIMAL);
		setField("ID_GENERALE_PROCEDIMENTO", BIG_DECIMAL);
		setField("ANNO_S1", BIG_DECIMAL);
		setField("PROGR_S1", BIG_DECIMAL);
		setField("COD_TIPO_REGISTRO", STRING);
		setField("COD_OGGETTO_PROCEDIMENTO", STRING);
		setField("DATA_RICHIESTA", DATE);
		setField("DATA_ARRIVO_CANCELLERIA", DATE);
		setField("DATA_CAMERA_CONSIGLIO", DATE);
		setField("DESCR_RICHIESTA_DELEGAZIONE", STRING);
		setField("COD_AUTORITA_DELEGATA", STRING);
		setField("DATA_RESTITUZ_DELEGAZIONE", DATE);
		setField("DATA_RICORSO_IMPUGN", DATE);
		setField("DATA_INVIO_ATTI_IMPUGN", DATE);
		setField("DATA_INVIO_ESECUZ_PROVVISORIA", DATE);
		setField("DATA_INVIO_ESECUZ_ORDINARIA", DATE);
		setField("DATA_COMPILAZ_COMPLEMENTARE", DATE);
		setField("COD_TIPO_FOGLIO_COMPLEMENTARE", STRING);
		setField("DATA_ANNOTAZIONE", DATE);
		setField("ANNOTAZIONE", STRING);
		setField("TIPO_DEFINIZIONE", STRING);
		setField("DATA_DEFINIZIONE", DATE);
		setField("DESCR_DEFINIZIONE", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("COD_TIPO_ATTO", STRING);
		setField("COD_SEDE_MITTENTE", STRING);
		setField("COD_TIPO_MITTENTE_ATTO", STRING);
		setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
		setField("SEZIONE", STRING);
		setField("DATA_FINE_PENA", DATE);
		setField("COD_POSIZIONE_GIURIDICA", STRING);
		setField("UDI_ID_UDIENZA", BIG_DECIMAL);
		setField("DESCR_MITTENTE", STRING);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdGeneraleProcedimento() throws DAOException {
		return getBigDecimal("ID_GENERALE_PROCEDIMENTO");
	}

	public BigDecimal getAnnoS1() throws DAOException {
		return getBigDecimal("ANNO_S1");
	}

	public BigDecimal getProgrS1() throws DAOException {
		return getBigDecimal("PROGR_S1");
	}

	public String getCodTipoRegistro() throws DAOException {
		return getString("COD_TIPO_REGISTRO");
	}

	public String getCodOggettoProcedimento() throws DAOException {
		return getString("COD_OGGETTO_PROCEDIMENTO");
	}

	public Date getDataRichiesta() throws DAOException {
		return getDate("DATA_RICHIESTA");
	}

	public Date getDataArrivoCancelleria() throws DAOException {
		return getDate("DATA_ARRIVO_CANCELLERIA");
	}

	public Date getDataCameraConsiglio() throws DAOException {
		return getDate("DATA_CAMERA_CONSIGLIO");
	}

	public String getDescrRichiestaDelegazione() throws DAOException {
		return getString("DESCR_RICHIESTA_DELEGAZIONE");
	}

	public String getCodAutoritaDelegata() throws DAOException {
		return getString("COD_AUTORITA_DELEGATA");
	}

	public Date getDataRestituzDelegazione() throws DAOException {
		return getDate("DATA_RESTITUZ_DELEGAZIONE");
	}

	public Date getDataRicorsoImpugn() throws DAOException {
		return getDate("DATA_RICORSO_IMPUGN");
	}

	public Date getDataInvioAttiImpugn() throws DAOException {
		return getDate("DATA_INVIO_ATTI_IMPUGN");
	}

	public Date getDataInvioEsecuzProvvisoria() throws DAOException {
		return getDate("DATA_INVIO_ESECUZ_PROVVISORIA");
	}

	public Date getDataInvioEsecuzOrdinaria() throws DAOException {
		return getDate("DATA_INVIO_ESECUZ_ORDINARIA");
	}

	public Date getDataCompilazComplementare() throws DAOException {
		return getDate("DATA_COMPILAZ_COMPLEMENTARE");
	}

	public String getCodTipoFoglioComplementare() throws DAOException {
		return getString("COD_TIPO_FOGLIO_COMPLEMENTARE");
	}

	public Date getDataAnnotazione() throws DAOException {
		return getDate("DATA_ANNOTAZIONE");
	}

	public String getAnnotazione() throws DAOException {
		return getString("ANNOTAZIONE");
	}

	public String getTipoDefinizione() throws DAOException {
		return getString("TIPO_DEFINIZIONE");
	}

	public Date getDataDefinizione() throws DAOException {
		return getDate("DATA_DEFINIZIONE");
	}

	public String getDescrDefinizione() throws DAOException {
		return getString("DESCR_DEFINIZIONE");
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

	public String getCodTipoAtto() throws DAOException {
		return getString("COD_TIPO_ATTO");
	}

	public String getDescrTipoAtto() throws DAOException {
		return getString("DESCR_TIPO_ATTO");
	}

	public String getCodSedeMittente() throws DAOException {
		return getString("COD_SEDE_MITTENTE");
	}

	public String getDescrSedeMittente() throws DAOException {
		return getString("DESCR_SEDE_MITTENTE");
	}

	public String getCodTipoMittenteAtto() throws DAOException {
		return getString("COD_TIPO_MITTENTE_ATTO");
	}

	public String getDescrTipoMittenteAtto() throws DAOException {
		return getString("DESCR_TIPO_MITTENTE_ATTO");
	}

	public BigDecimal getIdFascicoloSius() throws DAOException {
		return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS");
	}

	public String getSezione() throws DAOException {
		return getString("SEZIONE");
	}

	public Date getDataFinePena() throws DAOException {
		return getDate("DATA_FINE_PENA");
	}

	public String getCodPosGiuridica() throws DAOException {
		return getString("COD_POSIZIONE_GIURIDICA");
	}

	public String getDescrPosGiuridica() throws DAOException {
		return getString("DESCR_POSIZIONE_GIURIDICA");
	}

	public BigDecimal getUdiIdUdienza() throws DAOException {
		return getBigDecimal("UDI_ID_UDIENZA");
	}

	public String getDescrMittente() throws DAOException {
		return getString("DESCR_MITTENTE");
	}

	public Date getDataRestituzione() throws DAOException {
		return getDate("DATA_RESTITUZIONE");
	}

	public String getDescrRestituzione() throws DAOException {
		return getString("DESCR_RESTITUZIONE");
	}

	//
	// METODI SET()
	//
	public void setIdGeneraleProcedimento(BigDecimal aValore) {
		setBigDecimal("ID_GENERALE_PROCEDIMENTO", aValore);
	}

	public void setAnnoS1(BigDecimal aValore) {
		setBigDecimal("ANNO_S1", aValore);
	}

	public void setProgrS1(BigDecimal aValore) {
		setBigDecimal("PROGR_S1", aValore);
	}

	public void setCodTipoRegistro(String aValore) {
		setString("COD_TIPO_REGISTRO", aValore);
	}

	public void setCodOggettoProcedimento(String aValore) {
		setString("COD_OGGETTO_PROCEDIMENTO", aValore);
	}

	public void setDataRichiesta(Date aValore) {
		setDate("DATA_RICHIESTA", aValore);
	}

	public void setDataArrivoCancelleria(Date aValore) {
		setDate("DATA_ARRIVO_CANCELLERIA", aValore);
	}

	public void setDataCameraConsiglio(Date aValore) {
		setDate("DATA_CAMERA_CONSIGLIO", aValore);
	}

	public void setDescrRichiestaDelegazione(String aValore) {
		setString("DESCR_RICHIESTA_DELEGAZIONE", aValore);
	}

	public void setCodAutoritaDelegata(String aValore) {
		setString("COD_AUTORITA_DELEGATA", aValore);
	}

	public void setDataRestituzDelegazione(Date aValore) {
		setDate("DATA_RESTITUZ_DELEGAZIONE", aValore);
	}

	public void setDataRicorsoImpugn(Date aValore) {
		setDate("DATA_RICORSO_IMPUGN", aValore);
	}

	public void setDataInvioAttiImpugn(Date aValore) {
		setDate("DATA_INVIO_ATTI_IMPUGN", aValore);
	}

	public void setDataInvioEsecuzProvvisoria(Date aValore) {
		setDate("DATA_INVIO_ESECUZ_PROVVISORIA", aValore);
	}

	public void setDataInvioEsecuzOrdinaria(Date aValore) {
		setDate("DATA_INVIO_ESECUZ_ORDINARIA", aValore);
	}

	public void setDataCompilazComplementare(Date aValore) {
		setDate("DATA_COMPILAZ_COMPLEMENTARE", aValore);
	}

	public void setCodTipoFoglioComplementare(String aValore) {
		setString("COD_TIPO_FOGLIO_COMPLEMENTARE", aValore);
	}

	public void setDataAnnotazione(Date aValore) {
		setDate("DATA_ANNOTAZIONE", aValore);
	}

	public void setAnnotazione(String aValore) {
		setString("ANNOTAZIONE", aValore);
	}

	public void setTipoDefinizione(String aValore) {
		setString("TIPO_DEFINIZIONE", aValore);
	}

	public void setDataDefinizione(Date aValore) {
		setDate("DATA_DEFINIZIONE", aValore);
	}

	public void setDescrDefinizione(String aValore) {
		setString("DESCR_DEFINIZIONE", aValore);
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

	public void setCodTipoAtto(String aValore) {
		setString("COD_TIPO_ATTO", aValore);
	}

	public void setDescrTipoAtto(String aValore) {
		setString("DESCR_TIPO_ATTO", aValore);
	}

	public void setCodSedeMittente(String aValore) {
		setString("COD_SEDE_MITTENTE", aValore);
	}

	public void setDescrSedeMittente(String aValore) {
		setString("DESCR_SEDE_MITTENTE", aValore);
	}

	public void setCodTipoMittenteAtto(String aValore) {
		setString("COD_TIPO_MITTENTE_ATTO", aValore);
	}

	public void setDescrTipoMittenteAtto(String aValore) {
		setString("DESCR_TIPO_MITTENTE_ATTO", aValore);
	}

	public void setIdFascicoloSius(BigDecimal aValore) {
		setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore);
	}

	public void setSezione(String aValore) {
		setString("SEZIONE", aValore);
	}

	public void setDataFinePena(Date aValore) {
		setDate("DATA_FINE_PENA", aValore);
	}

	public void setCodPosGiuridica(String aValore) {
		setString("COD_POSIZIONE_GIURIDICA", aValore);
	}

	public void setDescrPosGiuridica(String aValore) {
		setString("DESCR_POSIZIONE_GIURIDICA", aValore);
	}

	public void setUdiIdUdienza(BigDecimal aValore) {
		setBigDecimal("UDI_ID_UDIENZA", aValore);
	}

	public void setDescrMittente(String aValore) {
		setString("DESCR_MITTENTE", aValore);
	}

	public void setDataRestituzione(Date aValore) throws DAOException {
		setDate("DATA_RESTITUZIONE", aValore);
	}

	public void setDescrRestituzione(String aValore) throws DAOException {
		setString("DESCR_RESTITUZIONE", aValore);
	}

	public GenericModel getModel() throws DAOException {

		return new GeneraleProcedimentoModel(getIdGeneraleProcedimento(), getAnnoS1(), getProgrS1(),
				getCodTipoRegistro(), "", getCodOggettoProcedimento(), "", getDataRichiesta(),
				getDataArrivoCancelleria(), getDataCameraConsiglio(), getDescrRichiestaDelegazione(),
				getCodAutoritaDelegata(), "", getDataRestituzDelegazione(), getDataRicorsoImpugn(),
				getDataInvioAttiImpugn(), getDataInvioEsecuzProvvisoria(), getDataInvioEsecuzOrdinaria(),
				getDataCompilazComplementare(), getCodTipoFoglioComplementare(), "", getDataAnnotazione(),
				getAnnotazione(), getTipoDefinizione(), getDataDefinizione(), getDescrDefinizione(),
				getCodOperatoreInserimento(), getDataInserimento(), getCodUfficioInserimento(), "",
				getCodOperatoreAggiornamento(), getDataAggiornamento(), getCodUfficioAggiornamento(), "",
				getCodTipoAtto(), getDescrTipoAtto(), getCodSedeMittente(), getDescrSedeMittente(),
				getCodTipoMittenteAtto(), getDescrTipoMittenteAtto(), getIdFascicoloSius(), getSezione(),
				getDataFinePena(), getCodPosGiuridica(), getDescrPosGiuridica(), getUdiIdUdienza(), "", "",
				getDescrMittente(), "");
	}

	public void setDAOFromModel(GeneraleProcedimentoModel aModel) throws DAOException {

		setIdGeneraleProcedimento(aModel.getIdGeneraleProcedimento());
		setAnnoS1(aModel.getAnnoS1());
		setProgrS1(aModel.getProgrS1());
		setCodTipoRegistro(aModel.getCodTipoRegistro());
		setCodOggettoProcedimento(aModel.getCodOggettoProcedimento());
		setDataRichiesta(aModel.getDataRichiesta());
		setDataArrivoCancelleria(aModel.getDataArrivoCancelleria());
		setDataCameraConsiglio(aModel.getDataCameraConsiglio());
		setDescrRichiestaDelegazione(aModel.getDescrRichiestaDelegazione());
		setCodAutoritaDelegata(aModel.getCodAutoritaDelegata());
		setDataRestituzDelegazione(aModel.getDataRestituzDelegazione());
		setDataRicorsoImpugn(aModel.getDataRicorsoImpugn());
		setDataInvioAttiImpugn(aModel.getDataInvioAttiImpugn());
		setDataInvioEsecuzProvvisoria(aModel.getDataInvioEsecuzProvvisoria());
		setDataInvioEsecuzOrdinaria(aModel.getDataInvioEsecuzOrdinaria());
		setDataCompilazComplementare(aModel.getDataCompilazComplementare());
		setCodTipoFoglioComplementare(aModel.getCodTipoFoglioComplementare());
		setDataAnnotazione(aModel.getDataAnnotazione());
		setAnnotazione(aModel.getAnnotazione());
		setTipoDefinizione(aModel.getTipoDefinizione());
		setDataDefinizione(aModel.getDataDefinizione());
		setDescrDefinizione(aModel.getDescrDefinizione());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setCodTipoAtto(aModel.getCodTipoAtto());
		setDescrTipoAtto(aModel.getDescrTipoAtto());
		setCodSedeMittente(aModel.getCodSedeMittente());
		setDescrSedeMittente(aModel.getDescrSedeMittente());
		setCodTipoMittenteAtto(aModel.getCodTipoMittenteAtto());
		setDescrTipoMittenteAtto(aModel.getDescrTipoMittenteAtto());
		setIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
		setSezione(aModel.getSezione());
		setDataFinePena(aModel.getDataFinePena());
		setCodPosGiuridica(aModel.getCodPosGiuridica());
		setDescrPosGiuridica(aModel.getDescrPosGiuridica());
		setUdiIdUdienza(aModel.getUdiIdUdienza());
		setDescrMittente(aModel.getDescrMittente());
	}

	// 03/03/2005 Aggiunto controllo dei campi per l'Update.
	public void setDAOFromModelForUpdate(GeneraleProcedimentoModel aModel) throws DAOException {

		if (aModel.getIdGeneraleProcedimento() != null)
			setIdGeneraleProcedimento(aModel.getIdGeneraleProcedimento());
		if (aModel.getAnnoS1() != null)
			setAnnoS1(aModel.getAnnoS1());
		if (aModel.getProgrS1() != null)
			setProgrS1(aModel.getProgrS1());
		if (aModel.getCodTipoRegistro() != "")
			setCodTipoRegistro(aModel.getCodTipoRegistro());
		if (aModel.getCodOggettoProcedimento() != "")
			setCodOggettoProcedimento(aModel.getCodOggettoProcedimento());
		if (aModel.getDataRichiesta() != null)
			setDataRichiesta(aModel.getDataRichiesta());
		if (aModel.getDataArrivoCancelleria() != null)
			setDataArrivoCancelleria(aModel.getDataArrivoCancelleria());
		if (aModel.getDataCameraConsiglio() != null)
			setDataCameraConsiglio(aModel.getDataCameraConsiglio());
		if (aModel.getDescrRichiestaDelegazione() != "")
			setDescrRichiestaDelegazione(aModel.getDescrRichiestaDelegazione());
		if (aModel.getCodAutoritaDelegata() != "")
			setCodAutoritaDelegata(aModel.getCodAutoritaDelegata());
		if (aModel.getDataRestituzDelegazione() != null)
			setDataRestituzDelegazione(aModel.getDataRestituzDelegazione());
		if (aModel.getDataRicorsoImpugn() != null)
			setDataRicorsoImpugn(aModel.getDataRicorsoImpugn());
		if (aModel.getDataInvioAttiImpugn() != null)
			setDataInvioAttiImpugn(aModel.getDataInvioAttiImpugn());
		if (aModel.getDataInvioEsecuzProvvisoria() != null)
			setDataInvioEsecuzProvvisoria(aModel.getDataInvioEsecuzProvvisoria());
		if (aModel.getDataInvioEsecuzOrdinaria() != null)
			setDataInvioEsecuzOrdinaria(aModel.getDataInvioEsecuzOrdinaria());
		if (aModel.getDataCompilazComplementare() != null)
			setDataCompilazComplementare(aModel.getDataCompilazComplementare());
		if (aModel.getCodTipoFoglioComplementare() != "")
			setCodTipoFoglioComplementare(aModel.getCodTipoFoglioComplementare());
		if (aModel.getDataAnnotazione() != null)
			setDataAnnotazione(aModel.getDataAnnotazione());
		if (aModel.getAnnotazione() != "")
			setAnnotazione(aModel.getAnnotazione());
		if (aModel.getTipoDefinizione() != "")
			setTipoDefinizione(aModel.getTipoDefinizione());
		if (aModel.getDataDefinizione() != null)
			setDataDefinizione(aModel.getDataDefinizione());
		if (aModel.getDescrDefinizione() != "")
			setDescrDefinizione(aModel.getDescrDefinizione());
		if (aModel.getCodOperatoreAggiornamento() != "")
			setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		if (aModel.getDataAggiornamento() != null)
			setDataAggiornamento(aModel.getDataAggiornamento());
		if (aModel.getCodUfficioAggiornamento() != "")
			setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		if (aModel.getCodTipoAtto() != "")
			setCodTipoAtto(aModel.getCodTipoAtto());
		if (aModel.getDescrTipoAtto() != "")
			setDescrTipoAtto(aModel.getDescrTipoAtto());
		if (aModel.getCodSedeMittente() != "")
			setCodSedeMittente(aModel.getCodSedeMittente());
		if (aModel.getDescrSedeMittente() != "")
			setDescrSedeMittente(aModel.getDescrSedeMittente());
		if (aModel.getCodTipoMittenteAtto() != "")
			setCodTipoMittenteAtto(aModel.getCodTipoMittenteAtto());
		if (aModel.getDescrTipoMittenteAtto() != "")
			setDescrTipoMittenteAtto(aModel.getDescrTipoMittenteAtto());
		if (aModel.getFasSiuIdFascicoloSius() != null)
			setIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
		if (aModel.getSezione() != "")
			setSezione(aModel.getSezione());
		if (aModel.getDataFinePena() != null)
			setDataFinePena(aModel.getDataFinePena());
		if (aModel.getCodPosGiuridica() != "")
			setCodPosGiuridica(aModel.getCodPosGiuridica());
		if (aModel.getDescrPosGiuridica() != "")
			setDescrPosGiuridica(aModel.getDescrPosGiuridica());
		if (aModel.getUdiIdUdienza() != null)
			setUdiIdUdienza(aModel.getUdiIdUdienza());
		if (aModel.getDescrMittente() != "")
			setDescrMittente(aModel.getDescrMittente());

		if (aModel.getIdGeneraleProcedimento() != null)
			setCondizioneUpdate(aModel.getIdGeneraleProcedimento());
	}

	public void setDAOFromModelForUpdateParziale(GeneraleProcedimentoModel aModel) throws DAOException {

		if (aModel.getAnnoS1() != null)
			setAnnoS1(aModel.getAnnoS1());
		if (aModel.getProgrS1() != null)
			setProgrS1(aModel.getProgrS1());
		if (aModel.getDataRichiesta() != null)
			setDataRichiesta(aModel.getDataRichiesta());
		if (aModel.getDataArrivoCancelleria() != null)
			setDataArrivoCancelleria(aModel.getDataArrivoCancelleria());
		if (aModel.getDataCameraConsiglio() != null)
			setDataCameraConsiglio(aModel.getDataCameraConsiglio());
		if (aModel.getCodAutoritaDelegata() != "")
			setCodAutoritaDelegata(aModel.getCodAutoritaDelegata());
		// if (aModel.getAnnotazione()!="")
		setAnnotazione(aModel.getAnnotazione());
		if (aModel.getCodTipoAtto() != "")
			setCodTipoAtto(aModel.getCodTipoAtto());
		if (aModel.getCodTipoMittenteAtto() != "")
			setCodTipoMittenteAtto(aModel.getCodTipoMittenteAtto());
		if (aModel.getDescrMittente() != "")
			setDescrMittente(aModel.getDescrMittente());
		if (aModel.getCodSedeMittente() != "")
			setCodSedeMittente(aModel.getCodSedeMittente());
		if (aModel.getDescrSedeMittente() != "")
			setDescrSedeMittente(aModel.getDescrSedeMittente());

		// STUB 03/01/2005
		if (aModel.getCodOggettoProcedimento() != "")
			setCodOggettoProcedimento(aModel.getCodOggettoProcedimento());
		if (aModel.getCodTipoRegistro() != "")
			setCodTipoRegistro(aModel.getCodTipoRegistro());

		setDataFinePena(aModel.getDataFinePena());
		setCodPosGiuridica(aModel.getCodPosGiuridica());

		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());

		setCondizioneUpdate(aModel.getIdGeneraleProcedimento());
	}

	// 19/03/2008 Aggiornamento Note.
	public void setDAOFromModelForUpdateNote(GeneraleProcedimentoModel aModel) throws DAOException {

		setAnnotazione(aModel.getAnnotazione());

		setCondizioneUpdate(aModel.getIdGeneraleProcedimento());
	}

	public void setCondizione(GeneraleProcedimentoModel aModel) {

		String lCondizioni = new String();

		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	public void setCondizioneUpdate(BigDecimal key) {

		setCondition(" ID_GENERALE_PROCEDIMENTO = " + key);
	}

	// STUB 03/03/2005
	public void setCondizioneUpdateByIdFasSius(BigDecimal key) {

		setCondition(" FAS_SIU_ID_FASCICOLO_SIUS = " + key);
	}

	/**
	 * Verifica se esiste un record che abbia a fronte di un idGeneraleProcedimento con l'id udienza passata
	 * anch'essa come parametro.
	 *
	 * @param aIdGenProc
	 *            valore id generale procedimento.
	 * @param aIdUdi
	 *            valore id udienza.
	 * @return flag di stato vero o falso.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	public boolean isExistUdienzaByIdGenProcIdUdi(BigDecimal aIdGenProc, BigDecimal aIdUdi)
			throws DAOException {

		boolean lResponse = false;
		setCondition(" ID_GENERALE_PROCEDIMENTO = " + aIdGenProc + " AND " + " UDI_ID_UDIENZA = " + aIdUdi);

		start();

		if (next()) // In caso di esistenza record, imposta il boolenao di ritorno a true.
			lResponse = true;

		stop();

		return lResponse;
	}

}