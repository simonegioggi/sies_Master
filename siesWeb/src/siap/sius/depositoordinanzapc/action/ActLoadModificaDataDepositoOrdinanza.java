package siap.sius.depositoordinanzapc.action;

import siap.sius.provvedimento.action.ICostantiProvvedimento;


 /**
 * <p>Title: ActLoadModificaDataDepositoDecreto</p>
 * <p>Description: Classe Action per la modifica dei destinatari e data Deposito Decreto</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

 public class ActLoadModificaDataDepositoOrdinanza extends ActLoadInserisciDataDeposito
 implements ICostantiDepositoOrdinanzaPc, ICostantiProvvedimento
 {

   public ActLoadModificaDataDepositoOrdinanza()
   {
     // Valorizzazione della pagina di ritorno
     super(PG_LOAD_MODIFICADATADEPOSITOORDINANZA);
   }



   public String processRequest() throws Exception
  {
    return super.processRequest();
  }

    /*
      String lRetPage = PG_LOAD_MODIFICADATADEPOSITOORDINANZA;
      //  gestioneRitorno();
      BigDecimal lIdFasSius = null;
      FascicoloGPModel lFasGPMod = null;

      // Fascicolo Sius
      lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
      lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

      // Ricerca del Deposito Ordinanza dall'ID Evento
      IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
      DepositoOrdinanzaPcModel lDepOrdMod = lCtrl.ExRicercaDepositoOrdinanzaPcByEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

      if (lDepOrdMod==null)
        throw new F3BException(F3BException.USER_MESSAGE,"Nessun Decreto è stato emesso per il procedimento ");
      else
      {
          if (lDepOrdMod.getDataDeposito()!=null && this.isRequestParameterNullObj("Aggiungi") )
          {
          // Se il decreto è già stata depositato
            IDocumentoAllegato lDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
            DocumentoAllegatoModel lDocAll = lDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO),"03");

//            if (lDocAll != null && (lDocAll.getFlagDocumentoRegistrato().compareTo("S") == 0))
//            {
            // Se il Documento allegato è validato
              // Si passa al dettaglio
              RedirectTo lPage = new RedirectTo();
              lPage.setPage(IWebConstants.PG_MAIN);
              lPage.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDataDepositoDecreto");
              lPage.setParameter(CAMPO_ID_DOCUMENTO_ALLEGATO,"" + lDocAll.getIdDocumentoAllegato());
              // Passaggio al dettaglio del LINK di ritorno
              if(!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
                 lPage.setParameter(IWebConstants.LINK_RITORNO,getRequestStringParameter(IWebConstants.LINK_RITORNO));

              lRetPage = lPage.toString();
//            }
          }

	  //Preleva dati AVVOCATI e LUOGODETENZIONE
          ParserMessageRec lParser = null;
          IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
	  //Riempi l'Array contenente le tipologie di dati da prelevare
	  int[] aTipoDati = {
	      ICostantiStampaSius.TREE_LUOGODET,
	      ICostantiStampaSius.TREE_AVVOCATOSIUS};
	  //Crea il TreeModel con i dati che occorrono
	  TreeModel lTreeDati = lCtrlSta.ExPrelevaDatiVideo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(), aTipoDati);
	  //Converte i dati ottenuti per utilizzarli come model
	  lParser = new ParserMessageRec(lTreeDati);
	  LuogoDetenzioneModel lLuogoDetMod = lParser.getLuogoDetenzione();

	  // Preleva elenco degli altri destinatari.
	  Option lOptionAut = new Option();
	  lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(),75);

	  // LISTA UFFICI SOGGETTO
	  Option lOptionSog = new Option();
	  if (Utils.isNullObj(lLuogoDetMod))
	    lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), 75);
	  else
	    lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(),lLuogoDetMod.getIstitutoDetenzione().getCodTipoIstituto(), 75);

	  // LISTA UFFICI
	  Collection lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
	  String[] lStringFilter = { "-", "22"};
          Option lOptionAvv = new Option();
          if(this.isRequestParameterNullObj("Aggiungi"))
           {
             lOptionAvv = new Option(lTipoIstituto, "22", 75);
           }else{
	     lOptionAvv = new Option(lTipoIstituto, "-", 75);
	   }
	  lOptionAvv.setFilter(lStringFilter);

          if (lDepOrdMod.getDataDeposito()!=null )
	  {
	    // Lettura delle notifiche.
	    INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
	    Vector lVect = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento(lDepOrdMod.getIdEventoGenerato());
	    setRequestAttribute("notifiche", lVect);
	  }

          Collection lColl = DecodificheManager.getInstance().getDestinatarioDeposito();
          setRequestAttribute("destDeposito", lColl);
          setRequestAttribute("lDepositoOrdinanza", lDepOrdMod);
          setSessionAttribute("lDepositoOrdinanza", lDepOrdMod);
	  setRequestAttribute("luogodet", lLuogoDetMod);
	  setRequestAttribute("avvocato", lParser.getAvvocatoSius());
	  setRequestAttribute("TipiIstitutiSog", "" + lOptionSog);
	  setRequestAttribute("TipiIstituti1", "" + lOptionAvv);
	  setRequestAttribute("tipoAutorita", lOptionAut.toString());
      }
      return lRetPage; //restituisce la jsp di VIEW
    }
        */
 }