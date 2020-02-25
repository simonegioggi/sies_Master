package siap.sige.stampa.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.XModel;
import siap.sico.utente.model.UtenteModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.RicercaFascicoloSigeModel;
import siap.sige.richiestaatti.model.ParereModel;
import siap.sige.statistiche.model.RicercaFogliCompModel;
import siap.sige.statistiche.model.StatisticheFogliComplementariContainerModel;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

@SuppressWarnings("rawtypes")
public interface IStampaSige {

	// public ByteArrayOutputStream ExPreStampaAvvocato(BigDecimal aIdAvvocato, BigDecimal aIdFascicoloSige,
	// String aCodUff, String lTemIdTemplate, UtenteModel aUtenteModel)
	// throws F3BException;

	public TreeModel ExPrelevaDatiStampa(BigDecimal aIdFasSIGE, int[] aTipoDati, int aTipoStampa,
			String aCodiceUfficio, BigDecimal idEvento) throws F3BException;

	public TreeModel ExPrelevaDatiStampa(BigDecimal aIdEvento, BigDecimal aIdFasSIGE, int[] aTipoDati,
			int aTipoStampa, String aCodiceUfficio) throws F3BException;

	public TreeModel ExPrelevaDatiStampa(BigDecimal aIdEvento, BigDecimal aIdFasSIGE,
			BigDecimal aIdFasSIGEUnificante, int[] aTipoDati, int aTipoStampa, String aCodiceUfficio)
			throws F3BException;

	public ByteArrayOutputStream ExPreStampaPareri(ParereModel aParere, Vector aRicerche, UtenteModel aUtente)
			throws F3BException;

	public TreeModel ExPrelevaDatiStampaProcedimentixUdienza(BigDecimal aIdUdienza, BigDecimal aIdFascicolo,
			String aCodMagistrato, BigDecimal aIdEsperto, XModel aStampa, String aIdDocumento,
			String lOrderBy, UtenteModel aUtenteModel, String aStatoProcedimento, String aTipoProc,
			String aCodUfficioConnesso) throws F3BException;

	public TreeModel ExPrelevaDatiStampaProcedimentixDataUdienza(Date aDataUdienza, BigDecimal aIdFascicolo,
			String aCodMagistrato, BigDecimal aIdEsperto, XModel aStampa, String aIdDocumento,
			String lOrderBy, UtenteModel aUtenteModel, String aStatoProcedimento, String aTipoProc,
			String aCodUfficioConnesso) throws F3BException;

	public ByteArrayOutputStream ExPreStampaProcSigeXProv(RicercaFogliCompModel aFiltroRicerca,
			Vector aElenco, UtenteModel aUtente) throws F3BException;

	public ByteArrayOutputStream ExPreStampaStatisticheFC(
			StatisticheFogliComplementariContainerModel container) throws F3BException;

	public ByteArrayOutputStream ExStampaAttiInArchivio(EventoModel ev, FascicoloSigeEstesoModel fascicolo,
			UtenteModel utenteConnesso) throws F3BException;

	public ByteArrayOutputStream ExStampaSollecito(EventoNotificaModel ev, FascicoloSigeEstesoModel fascicolo,
			UtenteModel utenteConnesso) throws F3BException;

	/**
	 * MEV_65: aggiunto metodo di stampa
	 * 
	 * @param fascicoli
	 * @param rfsm
	 * @param um
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExStampaProcedimentiSigeConRicorsoOpposizione(
			Vector<FascicoloSigeEstesoModel> fascicoli, RicercaFascicoloSigeModel rfsm, UtenteModel um)
			throws F3BException;

  
  /**
   * MEtodo introdotto per 11.2.1
   * 
 * @param idUdienze
 * @param aIdFascicolo
 * @param aCodMagistrato
 * @param aIdEsperto
 * @param aStampa
 * @param aIdDocumento
 * @param lOrderBy
 * @param aUtenteModel
 * @param aStatoProcedimento
 * @param aTipoProc
 * @param aCodUfficioConnesso
 * @return
 * @throws F3BException
 */
public TreeModel ExPrelevaDatiStampaProcedimentixUdienza( String idUdienze,	BigDecimal aIdFascicolo, String  aCodMagistrato,  BigDecimal aIdEsperto,
		XModel aStampa, String aIdDocumento, String lOrderBy, UtenteModel aUtenteModel, String aStatoProcedimento, String aTipoProc, 
          String aCodUfficioConnesso ) throws F3BException ;
}