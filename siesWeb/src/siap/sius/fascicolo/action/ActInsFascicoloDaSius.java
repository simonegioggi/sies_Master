package siap.sius.fascicolo.action;

/**
 * <p>Title: ActInsFascicoloDaSius</p>
 * <p>Description: Classe Azione di inserimento del Fascicolo SIUS da Atto Pervenuto da Sius
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Bull</p>
 */
import java.math.BigDecimal;
import java.util.StringTokenizer;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActInsFascicoloDaSius extends ActionSiap implements ICostantiFascicoloSius
{
  public String processRequest() throws Exception
  {
    //Recupero l'utente e il Soggetto dalla sessione
    ///UtenteModel lUtenteMod = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
    ///SoggettoModel lSoggettoMod = (SoggettoModel)getSessionAttribute("soggetto");

    //Si Recupera l'utente e il Fascicolo SIEP dalla sessione.
    UtenteModel lUtenteMod = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
    FascicoloSiepModel lFasSiepMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");

    //Istanzio il Model che incapsula il FascicoloSIUS e il GeneraleProcedimento
    FascicoloGPModel lFasGPMod = new FascicoloGPModel();

    //Caricamento Fascicolo SIUS
    lFasGPMod.getFascicoloSiusModel().setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); //Anno corrente
    lFasGPMod.getFascicoloSiusModel().setChiaveUfficio( lUtenteMod.getUfficioUtente().getCodUfficio()); //Ufficio dell'operatore che inserisce
    //Il progressivo del fascicolo (in base all'anno e all'ufficio) viene calcolato applicativamente nel controller
    lFasGPMod.getFascicoloSiusModel().setDataInserimento( DateUtils.getSysDate() );
    lFasGPMod.getFascicoloSiusModel().setDataIscrizione( DateUtils.getSysDate() );
    lFasGPMod.getFascicoloSiusModel().setCodStatoFascicolo("02"); //Stato Fascicolo SIUS settato ad aperto
    lFasGPMod.getFascicoloSiusModel().setCodOperatoreInserimento (lUtenteMod.getUserId()); //Codice dell'operatore che inserisce
    lFasGPMod.getFascicoloSiusModel().setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio()); //Codice dell'operatore che inserisce
    lFasGPMod.getFascicoloSiusModel().setSogIdSoggetto(lFasSiepMod.getSoggetto().getIdSoggetto());  //Foreign KEY del soggetto.
    lFasGPMod.getFascicoloSiusModel().setFasSieIdFascicoloSiep(lFasSiepMod.getIdFascicoloSiep());  //Foreign KEY del fascicolo SIEP.
    lFasGPMod.getFascicoloSiusModel().setChiaveAnnoSIEP(lFasSiepMod.getChiaveAnno());
    lFasGPMod.getFascicoloSiusModel().setChiaveProgrSIEP(lFasSiepMod.getChiaveProgr());
    lFasGPMod.getFascicoloSiusModel().setChiaveUfficioSIEP(lFasSiepMod.getChiaveUfficio());
    lFasGPMod.getFascicoloSiusModel().setSoggetto(lFasSiepMod.getSoggetto() );    //Soggetto recuperato dalla sessione
    // STUB 11/05/2004 Recupero del IdFascicoloSiusIrigine.
    lFasGPMod.getFascicoloSiusModel().setIdFascicoloSiusOrigine(new BigDecimal( getRequestStringParameter( "idFascicoloInviato" ) ));

    ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter( CAMPO_COD_SEDE_MITTENTE )) );

    //Caricamento Generale Procedimento
    lFasGPMod.getGeneraleProcedimentoModel().setCodSedeMittente(new String( lComMod.getCodComune()));
    lFasGPMod.getGeneraleProcedimentoModel().setAnnoS1(new BigDecimal(DateUtils.getSysDate("yyyy"))); //Anno corrente
    //Il progressivo S1 viene calcolato applicativamente nel controller
    lFasGPMod.getGeneraleProcedimentoModel().setCodOggettoProcedimento(getRequestStringParameter( CAMPO_COD_CONTENUTO) );
    lFasGPMod.getGeneraleProcedimentoModel().setDataRichiesta(getRequestDateParameter(CAMPO_ANNO_DATA_ATTO, CAMPO_MESE_DATA_ATTO, CAMPO_GIORNO_DATA_ATTO) );
    lFasGPMod.getGeneraleProcedimentoModel().setDataArrivoCancelleria(getRequestDateParameter(CAMPO_ANNO_DATA_ARRIVO, CAMPO_MESE_DATA_ARRIVO, CAMPO_GIORNO_DATA_ARRIVO) );
    lFasGPMod.getGeneraleProcedimentoModel().setCodTipoAtto(getRequestStringParameter( CAMPO_COD_TIPO_ATTO) );
    lFasGPMod.getGeneraleProcedimentoModel().setCodTipoMittenteAtto(getRequestStringParameter( CAMPO_COD_MITTENTE_ATTO) );
    lFasGPMod.getGeneraleProcedimentoModel().setCodSedeMittente(super.getCodComuneByDescr(getRequestStringParameter( CAMPO_DESCR_SEDE_MITTENTE)).getCodComune()) ;
    lFasGPMod.getGeneraleProcedimentoModel().setAnnotazione(getRequestStringParameter( CAMPO_NOTE) );
    lFasGPMod.getGeneraleProcedimentoModel().setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio()); //Codice dell'operatore che inserisce
    lFasGPMod.getGeneraleProcedimentoModel().setCodOperatoreInserimento (lUtenteMod.getUserId()); //Codice dell'operatore che inserisce
    lFasGPMod.getGeneraleProcedimentoModel().setDataInserimento( DateUtils.getSysDate() );
    lFasGPMod.getGeneraleProcedimentoModel().setDataFinePena( getRequestDateParameter(CAMPO_ANNO_FINE_PENA, CAMPO_MESE_FINE_PENA, CAMPO_GIORNO_FINE_PENA) );
    lFasGPMod.getGeneraleProcedimentoModel().setCodPosGiuridica( getRequestStringParameter(CAMPO_COD_POS_GIURIDICA) );
    // 18/12/2003 Impostazione del Cod_Magistrato in CodAutoritaDelegata.
    lFasGPMod.getGeneraleProcedimentoModel().setCodAutoritaDelegata( getRequestStringParameter( CAMPO_COD_MAGISTRATO) );
    // 14/01/2004 Impostazione della Descrizione del Mittente.
    lFasGPMod.getGeneraleProcedimentoModel().setDescrMittente( getRequestStringParameter( CAMPO_DESCR_MITTENTE ) );

    // Il campo FasSiuIdFascicoloSius di Generale Procedimento viene impostato nel controller.
    // Caricamento Tenore - Il campo Id_Generale_Procedimento di Tenore viene impostato nel controller
    // Preleva dalla request i codici e descrizioni dei tenori, impipati rispettivamente con separatore "|" e "\n".
    // Stabilisce la size dell'Array di Tenori da caricare in FascicoloGpModel.
    StringTokenizer lCodOggetto = new StringTokenizer(getRequestStringParameter( ICostantiFascicoloSius.CAMPO_COD_OGGETTO) ,"|");
    StringTokenizer lDescrOggetto = new StringTokenizer(getRequestStringParameter( ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO) ,"\n");
    // STUB 12/11/2003 Aggiunti i Codici Dettaglio Oggetti.
    String lStCodiceDet = new String(getRequestStringParameter( ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO) );

    int lSizeVector = lCodOggetto.countTokens();
    TenoreModel lTenori[] = new TenoreModel[lSizeVector];

    int lIndex = 0;

    while (lCodOggetto.hasMoreTokens())
    {
      TenoreModel lTenModel =  new TenoreModel();

      lTenModel.setCodOggettoTenore( lCodOggetto.nextToken());
      lTenModel.setDescrOggettoTenore( lDescrOggetto.nextToken());
      lTenModel.setCodUfficioInserimento( getCodUfficioUtenteConnesso()); //Codice dell'ufficio dell'operatore che inserisce
      lTenModel.setCodOperatoreInserimento (getCodUtenteConnesso()); //Codice dell'operatore che inserisce
      lTenModel.setDataInserimento( DateUtils.getSysDate() );
      lTenModel.setCodMagistrato( getRequestStringParameter( CAMPO_COD_MAGISTRATO) );
      lTenModel.setProgrTenore(new BigDecimal((double)(lIndex+1)));
      lTenModel.setCodEsitoTenore("-");
      //Il campo Id_Generale_Procedimento di Tenore viene impostato nel controller

      // 12/11/2003 Aggiunta la valorizzazione dell'eventuale Dettaglio Oggetto.
      if ( (lStCodiceDet).indexOf(lTenModel.getCodOggettoTenore()+"0")< 0 )
      {
        lTenModel.setCodDettaglioOggetto("-");
      }
      else
      {
        String lCodDettaglioCorrente = lStCodiceDet.substring(lStCodiceDet.indexOf(lTenModel.getCodOggettoTenore()+"0")+4,lStCodiceDet.indexOf(lTenModel.getCodOggettoTenore()+"0")+8);
        lTenModel.setCodDettaglioOggetto( lCodDettaglioCorrente);
      }

      //Setto l'Array su GPtenoreModel
      lTenori[lIndex] = lTenModel;
      lIndex++;
    }
    lFasGPMod.setTenori(lTenori);

    //STUB 09/01/2004 Gestione del Luogo detenzione.(Rivista il 21/10/2010)
    if (!isRequestParameterNullObj(ID_LUOGO_DETENZIONE ) &&
    		getRequestStringParameter( ID_LUOGO_DETENZIONE ).length()>0 &&
    		getRequestStringParameter( LUOGO_DETENZIONE ).length()>0 &&
        isRequestChecked( CAMPO_VALIDA_LUOGO_DET ) )
    {
      lFasGPMod.getGeneraleProcedimentoModel().setIdLuogoDetenzione(getRequestStringParameter( ID_LUOGO_DETENZIONE ));
      lFasGPMod.getGeneraleProcedimentoModel().setIdAltraCausa(getRequestStringParameter( ID_ALTRA_CAUSA ));
    }

    IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();

    // STUB 25/02/2004 Si esegue il metodo di inserimento di presa in carico.
    lFasGPMod = lCtrl.ExInserisciFascicoloDaSius(lFasGPMod, ((BigDecimal)getSessionAttribute("IdEventoInviato")+"") );

    // STUB 23/09/2004 Se il fascicolo Siep appartiene ad un ufficio fuori BDI, si trasferisce il RIFERIMENTO_FASCICOLO_SIUS, e poi si passa al dettaglio.
    /* STUB 11/12/2007 Annullato il Trasferimento del RIFERIMENTO_FASCICOLO_SIUS.
    UfficioModel lBDIDestinataria = this.getUfficioByCodUfficio(lFasSiepMod.getChiaveUfficio());
    UfficioModel lBDIMittente = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());
    if (lBDIMittente.getCodDistretto().trim().compareTo( lBDIDestinataria.getCodDistretto().trim())==0 )
      return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&"+CAMPO_ID_FASCICOLO_SIUS+"="+lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
    else
    {
      // STUB 23/03/2004 Metto in sessione il fascicolo SIUS per consentire la spedizione del riferimento Fascicolo Sius.
      setSessionAttribute("fascicoloSiusGP", lFasGPMod);
      //Prepara la "pagina" di destinAction
      RedirectTo lRedirigi = new RedirectTo();
      lRedirigi.setPage( IWebConstants.PG_MAIN );
      lRedirigi.setAction( "siap.sius.rifasius.action.ActTrasferisciRifFascicoloSius" );

      return lRedirigi.toString();
    } */
    return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&"+CAMPO_ID_FASCICOLO_SIUS+"="+lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
  }
}
