package siap.sius.depositosentenza.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.SICOException;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.util.GestioneFlussoOrdinanza;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.depositosentenza.model.SentenzaEventoTenoriGProcModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
import siap.sius.tenore.action.ICostantiTenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActInserisciSentenzaTDS extends ActionSius implements ICostantiDepositoSentenza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  // Variabili di sessione.
  protected FascicoloGPModel mFasGPMod = null;
  private String mCodiceOperatore = null;
  private String mCodiceUfficio   = null;
  private String mCodiceComune    = null;
  // Id Generale Procedimento
  private BigDecimal mIdGenProc = null;
  // Data odierna
  protected Date mOggi = null;
  // Cod. Magistrato Relatore
  private String mCodMagistrato = null;

  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("ActInserisciSentenzaTDS");
    String lRetPage = IWebConstants.PG_MESSAGE;
      
    // Inizializzazione data
    mOggi = DateUtils.getSysDate();

    // Si prelevano dati di sessione.
    mCodiceOperatore = getCodUtenteConnesso();
    mCodiceUfficio   = getCodUfficioUtenteConnesso();
    mCodiceComune    = getCodComuneUtenteConnesso();

    // Si preleva dalla sessione il fascicolo GPModel.
    if(this.isSessionAttributeNullObj("fascicoloSiusGP"))
    	throw new SIUSException(SIUSException.USER_MESSAGE,"fascicoloSiusGP non in sessione");
    mFasGPMod = new FascicoloGPModel((FascicoloGPModel)getSessionAttribute("fascicoloSiusGP"));

    // Preleva id generale procedimento.
    mIdGenProc = mFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
    if(mIdGenProc == null)
    	throw new SIUSException(SIUSException.USER_MESSAGE,"ID Generale Procedimento non in sessione");

    // Viene effettuato il controllo sulla preesistenza di un Provvedimento declaratorio
    // già emesso per il Fascicolo SIUS.
    // Se esiste almeno un provvedimento di questo tipo non può esserne emesso un altro.
    // Il controllo viene già effettuato nell?ActLoad.. ma viene qui ripetuto per sicurezza
    RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(mIdGenProc);
    boolean lEsistenzaDoc = lRicerca.verificaEsistenzaProv();
    if (lEsistenzaDoc)
       throw new SIUSException(SIUSException.USER_MESSAGE, "Per il procedimento indicato è già stato emesso un provvedimento. Non è consentito emettere un nuovo provvedimento");

	// Si richiama il lock per evitare inserimenti multipli
	lockApplicativo("EmissioneProvvedimento");
      
    //  Ricerca del Magistrato Relatore
    IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
    MagistratoRelatoreModel lMagRel = lMagCtrl.ExRicercaEstesaMagRelByFascicolo(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

    // Prelevare il codice Magistrato_Relatore
    if (lMagRel != null && lMagRel.getMagistrato() != null){
    	mCodMagistrato = lMagRel.getMagistrato().getCodMagistrato();
    }
    
    Date lDataEmissione = getRequestDateParameter(ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE, "dd/MM/yyyy");

    try
    {
    	SentenzaEventoTenoriGProcModel lSenEveTenGP = new SentenzaEventoTenoriGProcModel();
    	lSenEveTenGP.setEvento(generaEvento(lDataEmissione));
    	lSenEveTenGP.setSentenza(generaSentenza(lDataEmissione));
    	lSenEveTenGP.setTenori(generaTenori());
    	lSenEveTenGP.setGeneraleProcedimento(generaProcedimento());

    	// Lettura di ulteriori dati
    	lSenEveTenGP = generaDati(lSenEveTenGP);

    	lSenEveTenGP =  inserimento(lSenEveTenGP);

        // l'aggiornamento dei dati in sessione
        IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
        mFasGPMod = lFasCtrl.ExRicercaFascicoloByKey(mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
        setSessionAttribute("fascicoloSiusGP",mFasGPMod);
     
        // dettaglio della sentenza
        RedirectTo lRedirectTo = new RedirectTo();
        lRedirectTo.setPage(IWebConstants.PG_MAIN);
        lRedirectTo.setAction("siap.sius.depositosentenza.action.ActLoadDettaglioSentenza");
        lRedirectTo.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, lSenEveTenGP.getEvento().getIdEvento().toString() );
        lRetPage = lRedirectTo.toString();

    }
    catch (SICOException daoex)
    {
    	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    	siesLogger.debug("Exception: " + daoex );
    	if(daoex.getErrorCode()== F3BException.USER_MESSAGE)
    	{
    		lRetPage = IWebConstants.PG_MESSAGE;
    		setRequestAttribute(IWebConstants.MESSAGE_TEXT, daoex.getMessage());
    	} else
          throw daoex;
    }
    catch (Exception e)
    {
        throw e;
    }
    
    return lRetPage;
  }

  GeneraleProcedimentoModel generaProcedimento() throws F3BException
  {
    // Istanzia model generale procedimento.
    GeneraleProcedimentoModel lGenProcModel = new GeneraleProcedimentoModel();
    lGenProcModel.setIdGeneraleProcedimento( mIdGenProc );
    lGenProcModel.setCodOggettoProcedimento( getRequestStringParameter( ICostantiFascicoloSius.CAMPO_COD_CONTENUTO ) );
    lGenProcModel.setDataAggiornamento( mOggi );
    lGenProcModel.setCodUfficioAggiornamento( mCodiceUfficio );
    lGenProcModel.setCodOperatoreAggiornamento( mCodiceOperatore );

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("Generale Procedimento = " + lGenProcModel);

    return lGenProcModel;
  }

  private TenoreModel[] generaTenori() throws F3BException
  {
    TenoreModel[] lTenori = null;

    // Lettura campi Tenore.
    String[] lCodOggetti = getRequestStringParameters( ICostantiTenore.CAMPO_COD_OGGETTO_TENORE);
    String[] lDescOggetti = getRequestStringParameters( ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE);
    String[] lCodDettaglioOggetti = getRequestStringParameters( ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO);
    String[] lCodEsiti = getRequestStringParameters(ICostantiTenore.CAMPO_COD_ESITO_TENORE);

    if ( !( (lCodOggetti.length == lDescOggetti.length ) && (lDescOggetti.length == lCodDettaglioOggetti.length) && (lCodDettaglioOggetti.length== lCodEsiti.length)))
        throw new SIUSException(SIUSException.USER_MESSAGE, "Errore nella lettura dei Tenori");

    IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();

    int lSizeArray = lCodOggetti.length;
    if (lSizeArray > 0)
    {
      lTenori = new TenoreModel[lSizeArray];
      for (int i = 0; i < lSizeArray; i++)
      {
        TenoreModel lTenModel =  new TenoreModel();

        lTenModel.setProgrTenore(new BigDecimal((double)(i+1)));
        lTenModel.setCodOggettoTenore( lCodOggetti[i]);
        lTenModel.setDescrOggettoTenore( lDescOggetti[i]);
        lTenModel.setCodDettaglioOggetto(lCodDettaglioOggetti[i]);
        lTenModel.setCodEsitoTenore(lDecCtrl.ExRicercaCodEsitiProvByCodTenore(lCodEsiti[i]));

        lTenModel.setCodUfficioInserimento( mCodiceUfficio); //Codice dell'ufficio dell'operatore che inserisce
        lTenModel.setCodOperatoreInserimento (mCodiceOperatore); //Codice dell'operatore che inserisce
        lTenModel.setDataInserimento( mOggi );
        lTenModel.setGenPridGeneraleProcedimento(mIdGenProc);
        lTenModel.setCodMagistrato( mCodMagistrato );

        //Inserimenti i-esimo Tenore
        lTenori[i] = lTenModel;

         // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
         siesLogger.debug("Tenore n."+i+" = " + lTenori[i]);

      }
   }
   return lTenori;
  }

  /**
   * Prepara con i dati il Model per il Deposito Sentenza.
   * 
   * @param aDataEmissione Date data di emissione
   * @throws F3BException propaga errori di eccezione.
   * @return DepositoSentenzaModel ritorna istanza del model opportunamente popolato.
   */
  private DepositoSentenzaModel generaSentenza(Date aDataEmissione)
		  throws F3BException
  {
    // Prepara il model DepositoSentenza.
    DepositoSentenzaModel lDepSenModel = new DepositoSentenzaModel();
    
    lDepSenModel.setCodTipoSentenza( getRequestStringParameter(ICostantiDepositoSentenza.CAMPO_COD_TIPO_SENTENZA));
    
    if ( getRequestStringParameter( ICostantiFascicoloSius.CAMPO_COD_CONTENUTO ).equals("C015") ){
    	lDepSenModel.setCodTipoSentenza("01");
    } else if ( getRequestStringParameter( ICostantiFascicoloSius.CAMPO_COD_CONTENUTO ).equals("C016") ){
    	lDepSenModel.setCodTipoSentenza("02");
    } else if ( getRequestStringParameter( ICostantiFascicoloSius.CAMPO_COD_CONTENUTO ).equals("C018")){
    	lDepSenModel.setCodTipoSentenza("03");
    }

    lDepSenModel.setDataEmissione(aDataEmissione);
    lDepSenModel.setCodMagistrato(mCodMagistrato);
    lDepSenModel.setGenPridGeneraleProcedimento( mIdGenProc );
    lDepSenModel.setCodOperatoreInserimento( mCodiceOperatore );
    lDepSenModel.setCodUfficioInserimento( mCodiceUfficio );
    lDepSenModel.setDataInserimento( mOggi );
	
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("DepositoSentenza = " + lDepSenModel);
    return lDepSenModel;
  }

  /**
   * Prepara con i relativi dati, il Model per l'evento.
   * <p>
   * @param aDataEmissione Date data di emissione.
   * @throws F3BException propaga errore di eccezione.
   * @return EventoModel ritorna l'evento model.
   */
  private EventoModel generaEvento(Date aDataEmissione)
		  throws F3BException
  {
    //Prepara Model Evento.
    EventoModel lEvento = new EventoModel();
    lEvento.setCodTipoEvento("01"); // 01 = Provvedimento.
    lEvento.setCodTipoProvvedimento("01");  // 01 = Sentenza.
    lEvento.setCodLuogoEmittente( mCodiceComune );
    lEvento.setCodUfficioEmittente( mCodiceUfficio );
    lEvento.setDataEmissione( aDataEmissione);
    lEvento.setFasSieIdFascicoloSiep( mFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() );
    lEvento.setFasSiuIdFascicoloSius( mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius() );
    lEvento.setCodOperatoreInserimento( getCodUtenteConnesso() );
    lEvento.setCodUfficioInserimento( mCodiceUfficio );
    lEvento.setDataInserimento( mOggi );
    lEvento.setCodLuogoDestinatario("-");
    lEvento.setCodTipoUfficioDestinatario("-");
    lEvento.setCodUfficioDestinatario("-");
    lEvento.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
    lEvento.setCodMagistrato(mCodMagistrato);
    // Inserimento dell'Evento collegato (Revoca Riabilitazione Speciale)
    if(! isRequestParameterNullObj(ICostantiEvento.CAMPO_EVE_ID_EVENTO))
      lEvento.setEveIdEvento(this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_EVE_ID_EVENTO));

     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug("Evento = " + lEvento);
    return lEvento;
  }
  
  /**
   * Funzione di lettura dei dati opzionali
   * @param aModel
   * @return
   * @throws F3BException
   */
  private SentenzaEventoTenoriGProcModel generaDati(SentenzaEventoTenoriGProcModel aModel)
  throws F3BException
  {
     ///////////////////////////////////////////////////
     // Aggiornamento dei dati in DepositoSentenza.   //
     ///////////////////////////////////////////////////
	 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 siesLogger.debug("generaDati...");
     
	 DepositoSentenzaModel lDepSenModel = aModel.getSentenza();

     // Inserisce, ove sia definito, il valore del campo "Ulteriori Descrizioni".
     if( !isRequestParameterNullObj( ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE ) )
    	 lDepSenModel.setUlterioreDescrizione( getRequestStringParameter( ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE ) );

     if(!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_COD_NATURA_PROVVEDIMENTO))
    	 lDepSenModel.setCodNaturaProvvedimento(getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_COD_NATURA_PROVVEDIMENTO));
     
     // Aggiornamento Sentenza effettuato
     aModel.setSentenza(lDepSenModel);

     ///////////////////////////////////////////////////
     // Aggiornamento del template in Evento.         //
     ///////////////////////////////////////////////////
     // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     siesLogger.debug("Aggiornamento del template in Evento.");
     if(!isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_TIPO_ORDINANZA_DA_PRODURRE))
     {
       EventoModel lEvento = aModel.getEvento();
       // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       siesLogger.debug("CAMPO_TIPO_ORDINANZA_DA_PRODURRE = "+getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_TIPO_ORDINANZA_DA_PRODURRE));
       // Tipo di template da assegnare all'evento
       if (getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_TIPO_ORDINANZA_DA_PRODURRE).equals(
           "02"))
         lEvento.setTemIdTemplate(ICostantiDepositoOrdinanzaPc.TEMPLATE_ORDINANZA_GENERICO);
       else if (getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_TIPO_ORDINANZA_DA_PRODURRE).
                equals("03"))
         lEvento.setTemIdTemplate(ICostantiDepositoOrdinanzaPc.TEMPLATE_ORDINANZA_RIGETTO_GENERICO);
       else if (getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_TIPO_ORDINANZA_DA_PRODURRE).
                equals("04"))
         lEvento.setTemIdTemplate(ICostantiDepositoOrdinanzaPc.TEMPLATE_ORDINANZA_NLP_GENERICO);
       else if (getRequestStringParameter(ICostantiDepositoOrdinanzaPc.CAMPO_TIPO_ORDINANZA_DA_PRODURRE).
                equals("01"))
       // generazione automantica
       {
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.debug(">>>> Chiama la logica Decisionale per il template  ...");
         // Chiama la logica Decisionale per il template da associare.
         GestioneFlussoOrdinanza lGFO = new GestioneFlussoOrdinanza(aModel.
             getTenori());
         lGFO.start();
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.debug(">>>> Template Associato : " + lGFO.getTemplate());
         // Inserisce qui nell'evento l'id del template, prima dell'inserimento.
         lEvento.setTemIdTemplate(lGFO.getTemplate());
       }

        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("Evento  >>> " + lEvento);

       // Aggiornamento Evento effettuato
       aModel.setEvento(lEvento);
     }

     return aModel;
  }

  public SentenzaEventoTenoriGProcModel inserimento(SentenzaEventoTenoriGProcModel aSenEveTenGP)
		  throws F3BException
  {
    SentenzaEventoTenoriGProcModel lObjRet = null;
    IDepositoSentenza lDepSenCtrl = SIUSLookupRemote.getDepositoSentenzaRemote();

    // Variazione Fascicolo SIUS Origine
    if(!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS_ORIGINE))
    {
      BigDecimal lIdFascOrigineNew = getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS_ORIGINE);
      BigDecimal lIdFascOrigineOld = mFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine();
      // Aggiornamento ID Fascicolo Origine solo se variato
      if ( lIdFascOrigineOld == null || lIdFascOrigineNew.compareTo(lIdFascOrigineOld) != 0)
      {
        lObjRet = lDepSenCtrl.ExInserisciSentenza(aSenEveTenGP, lIdFascOrigineNew);
      }
      else
        lObjRet = lDepSenCtrl.ExInserisciSentenza(aSenEveTenGP);
    }
    else
      lObjRet = lDepSenCtrl.ExInserisciSentenza(aSenEveTenGP);

    return lObjRet;
    
  }
}