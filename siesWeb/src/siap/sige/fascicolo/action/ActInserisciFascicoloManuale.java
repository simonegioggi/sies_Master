package siap.sige.fascicolo.action;

/**
 * <p>Title: ActInserisciFascicoloManuale</p>
 * <p>Description: Classe Azione di inserimento manuale del Fascicolo SIGE
 * </p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: </p>
 */
import org.apache.log4j.Logger;

import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.sige.detenzione.action.ICostantiFasSigeDetenzione;
import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.richiesta.action.ICostantiRichiestaSige;
import siap.sige.richiesta.model.RichiestaSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActInserisciFascicoloManuale extends ActionSiap implements ICostantiFascicoloSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	FascicoloSigeModel mFascicolo = null;
	RichiestaSigeModel mRichiesta = null;
	MagistratoAssegnatarioModel mMagAssegnatario = null;
	FasSigeDetenzioneModel lDetenzione = null;
	
  public String processRequest() throws Exception
  {
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug(getClass().getPackage().getName() + ".processRequest : inizio");

	  // Lettura dei dati dalla form
	  mFascicolo = leggiDatiFascicolo();
	  mRichiesta = leggiDatiRichiesta();
	  mMagAssegnatario = leggiDatiMagAssegnatario();
	  lDetenzione = leggiDatiDetenzione();
	  
	  // Viene istanziato il controller per attuare l'iscrizione del Fascicolo SIGE
	  IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
	  mFascicolo = lCtrl.ExInserisciFascicoloSigeManuale(mFascicolo, mRichiesta, mMagAssegnatario, lDetenzione);
	  
	  //Prepara la "pagina" di destinazione
	  RedirectTo lRedirigi = new RedirectTo();
	  lRedirigi.setPage( IWebConstants.PG_MAIN );
	  lRedirigi.setAction( "siap.sige.fascicolo.action.ActLoadDettaglioFascicolo" );
	  lRedirigi.setParameter(CAMPO_ID_FASCICOLO_SIGE,(mFascicolo.getIdFascicoloSige() != null ?  mFascicolo.getIdFascicoloSige().toString() : ""));
	  
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug(getClass().getPackage().getName() + ".processRequest : fine");
	  return lRedirigi.toString();  
   }
  
 
  /**
   * La funzione costruisce il FascicoloSigeModel a partire dai dati inseriti nella form di inserimento.
   * @return FascicoloSigeModel
   * @throws Exception
   */
  protected FascicoloSigeModel leggiDatiFascicolo() throws Exception 
  {
	  FascicoloSigeModel lFascicolo = new FascicoloSigeModel();
	  
	  String lCodUfficio = getCodUfficioUtenteConnesso();
	  String lCodUtente  = getCodUtenteConnesso();
	  String lIdSezioneString = getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE);
	  
	  // Valorizzazione FascicoloSige
	  lFascicolo.setChiaveAnno(getRequestBigDecimalParameter( CAMPO_CHIAVE_ANNO )); //Anno impostato nella form
	  lFascicolo.setChiaveProgr(getRequestBigDecimalParameter( CAMPO_CHIAVE_PROGR )); //Progressivo impostato nella form
	  lFascicolo.setChiaveUfficio(lCodUfficio);
	  lFascicolo.setNote(getRequestStringParameter(CAMPO_NOTE));

	  lFascicolo.setCodTipoGiudizio(getRequestStringParameter(CAMPO_COD_TIPO_GIUDIZIO));
	  lFascicolo.setCodPosizioneGiuridica(getRequestStringParameter(CAMPO_COD_POSIZIONE_GIURIDICA));
	  lFascicolo.setDataFinePena(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_PENA, CAMPO_MESE_DATA_FINE_PENA, CAMPO_GIORNO_DATA_FINE_PENA));
	  
	  // Sezione solo se valorizzato
	  if (lIdSezioneString != null && lIdSezioneString.trim().length() > 0 && !lIdSezioneString.equalsIgnoreCase("-"))
		  lFascicolo.setIdSezione(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));
	  
	  lFascicolo.setCodUfficioInserimento(lCodUfficio);
	  lFascicolo.setCodOperatoreInserimento(lCodUtente);
	  lFascicolo.setDataInserimento(DateUtils.getSysDate());
	  if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_ISCRIZIONE) &&  !isRequestParameterNullObj(CAMPO_MESE_DATA_ISCRIZIONE) && !isRequestParameterNullObj(CAMPO_GIORNO_DATA_ISCRIZIONE) )
		  lFascicolo.setDataIscrizione(getRequestDateParameter(CAMPO_ANNO_DATA_ISCRIZIONE, CAMPO_MESE_DATA_ISCRIZIONE, CAMPO_GIORNO_DATA_ISCRIZIONE));
	  else
		  lFascicolo.setDataIscrizione(lFascicolo.getDataInserimento());
	  lFascicolo.setSogIdSoggetto(getRequestBigDecimalParameter(CAMPO_SOG_ID_SOGGETTO));
	  
	  // Stato Fascicolo Iscritto
	  lFascicolo.setCodStatoFascicolo("02");

	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug("Fascicolo da inserire  -> " + lFascicolo.toString());

	  return lFascicolo;
  }
  
  /**
   * La funzione costruisce il RichiestaSigeModel a partire dai dati inseriti nella form di inserimento.
   * @return RichiestaSigeModel
   * @throws Exception
   */
  protected RichiestaSigeModel leggiDatiRichiesta() throws Exception
  {
	  RichiestaSigeModel lRichiesta = new RichiestaSigeModel();
	  
	  String lCodUfficio = getCodUfficioUtenteConnesso();
	  String lCodUtente  = getCodUtenteConnesso();

	  // Valorizzazione RichiestaloSige
	  lRichiesta.setCodUfficioInserimento(lCodUfficio);
	  lRichiesta.setCodOperatoreInserimento(lCodUtente);
	  lRichiesta.setDataInserimento(DateUtils.getSysDate());
	  lRichiesta.setDataEmissione(getRequestDateParameter(ICostantiRichiestaSige.CAMPO_ANNO_DATA_EMISSIONE, ICostantiRichiestaSige.CAMPO_MESE_DATA_EMISSIONE, ICostantiRichiestaSige.CAMPO_GIORNO_DATA_EMISSIONE));
	  lRichiesta.setCodTipoRichiedente(getRequestStringParameter(ICostantiRichiestaSige.CAMPO_COD_TIPO_RICHIEDENTE));
	  lRichiesta.setCodTipoAtto(getRequestStringParameter(ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO));
	  lRichiesta.setDataArrivoCancelleria(getRequestDateParameter(ICostantiRichiestaSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA,ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA, ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA));

	  // Mittente
	  if (!isRequestParameterNullObj(ICostantiRichiestaSige.CAMPO_SEDE_RICHIEDENTE) &&  getRequestStringParameter(ICostantiRichiestaSige.CAMPO_SEDE_RICHIEDENTE) != null && getRequestStringParameter(ICostantiRichiestaSige.CAMPO_SEDE_RICHIEDENTE).trim().length() > 0 )
		  lRichiesta.setCodSedeRichiedente(getCodComuneByDescr( getRequestStringParameter( ICostantiRichiestaSige.CAMPO_SEDE_RICHIEDENTE)).getCodComune());
	 
	  lRichiesta.setDescRichiedente(getRequestStringParameter( ICostantiRichiestaSige.CAMPO_DESC_RICHIEDENTE));
	  
	  // Ufficio interno
	  if (  !isRequestParameterNullObj(ICostantiRichiestaSige.CAMPO_COD_UFFICIO_RICHIEDENTE) &&  getRequestStringParameter(ICostantiRichiestaSige.CAMPO_COD_UFFICIO_RICHIEDENTE) != null && getRequestStringParameter(ICostantiRichiestaSige.CAMPO_COD_UFFICIO_RICHIEDENTE).trim().length() > 0 )
		  lRichiesta.setCodUfficioRichiedente(getRequestStringParameter( ICostantiRichiestaSige.CAMPO_COD_UFFICIO_RICHIEDENTE));

	  // ID Fascicolo SIEP
	  if ( getRequestStringParameter(ICostantiRichiestaSige.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) != null && getRequestStringParameter(ICostantiRichiestaSige.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP).trim().length() > 0 )
		  lRichiesta.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(ICostantiRichiestaSige.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP));
	  
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug("Richiesta da inserire  -> " + lRichiesta.toString());

	  return lRichiesta;
  }
  
  protected MagistratoAssegnatarioModel leggiDatiMagAssegnatario() throws Exception
  {
	  MagistratoAssegnatarioModel lMagistrato = null;
	  if ( getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO) != null && getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO).trim().length() > 0 )
	  {
		  lMagistrato = new MagistratoAssegnatarioModel();
		  lMagistrato.setMagCodMagistrato( getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO) );
		  lMagistrato.setCodRuoloMagistrato("03");
		  lMagistrato.setDataInizio(mFascicolo.getDataInserimento());
		  lMagistrato.setDataFine(null);
		  lMagistrato.setCodUfficioInserimento(mFascicolo.getCodUfficioInserimento());
		  lMagistrato.setCodOperatoreInserimento(mFascicolo.getCodOperatoreInserimento());
		  lMagistrato.setDataInserimento(mFascicolo.getDataInserimento());
	  }
	  return lMagistrato;
  }
  
  /**
   * La funzione legge i dati relativi al Luogo di Detenzione.
   * Se nella form di input risulta vistato il check che conferma 
   * il Luogo di Detenzione del Fascicolo SIEP, questa funzione 
   * inserirà in un oggetto FasSigeDetenzioneModel ID Luogo Detenzione 
   * ed ID AltraCausa presenti nella form.
   * @return FasSigeDetenzioneModel
   * @throws Exception
   */
  
  protected FasSigeDetenzioneModel leggiDatiDetenzione() throws Exception
  {
	  FasSigeDetenzioneModel lDetenzione = null;
	  
	  if ( isRequestChecked( CAMPO_VALIDA_LUOGO_DET ) )
	  {
		  // Viene istanziato il model
		  lDetenzione = new FasSigeDetenzioneModel();
		  
		  // Valorizzazione del Riferimento a Luogo Detenzione o a Altra Causa
		  lDetenzione.setLdIdLuogoDetenzione(getRequestBigDecimalParameter(ICostantiFasSigeDetenzione.CAMPO_LD_ID_LUOGO_DETENZIONE));
		  lDetenzione.setAcIdAltraCausa(getRequestBigDecimalParameter(ICostantiFasSigeDetenzione.CAMPO_AC_ID_ALTRA_CAUSA));
		
		  // Campi di sistema
		  lDetenzione.setCodUfficioInserimento(mFascicolo.getCodUfficioInserimento());
		  lDetenzione.setCodOperatoreInserimento(mFascicolo.getCodOperatoreInserimento());
		  lDetenzione.setDataInserimento(mFascicolo.getDataInserimento());
		  
		  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		  siesLogger.debug("dati del luogo di detenzione  -> " + lDetenzione.toString());
	  
	  }
	  return lDetenzione;
  }

}