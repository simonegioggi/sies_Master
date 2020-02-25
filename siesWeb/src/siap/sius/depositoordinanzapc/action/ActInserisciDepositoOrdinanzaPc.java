package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import siap.sico.cssa.controller.ICSSA;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.prescrizione.action.ICostantiPrescrizione;
import siap.sius.tenore.action.ICostantiTenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
* <p>Title: ActInserisciDepositoOrdinanzaPc</p>
* <p>Description: Classe Action per l'inserimento di DepositoOrdinanzaPc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActInserisciDepositoOrdinanzaPc extends ActionSiap implements ICostantiDepositoOrdinanzaPc
{
  /**
  * Azione di Inserimento del DepositoOrdinanzaPc
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws Exception
  {
    FascicoloGPModel lFasGPMod = new FascicoloGPModel();
    lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");


    //**************************************************//
    // Parte di popolazione del DepositoOrdinazaPcModel //
    //**************************************************//
    DepositoOrdinanzaPcModel lDepMod = new DepositoOrdinanzaPcModel();

    // Imposta nel model la data camera di consiglio.
    // STUB Nel campo data camera di consiglio si mette la data di emissione ! Luigi 18-5-2004
    //lDepMod.setDataCameraConsiglio( lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio() );
    lDepMod.setDataCameraConsiglio( getRequestDateParameter(CAMPO_DATA_EMISSIONE,"dd/MM/yyyy") );

    lDepMod.setCodNaturaProvvedimento( "-" );

    // STUB 20/10/2003 Rework migrazione campo COD_COMUNE_CSSA_COMP a ID_CSSA_COMP
    // Controlla ed imposta il codice comune Cssa Competente.

    lDepMod.setIdCssaComp( getRequestBigDecimalParameter( CAMPO_ID_CSSA_COMP) );
    lDepMod.setDescrComuneCssaComp( getRequestStringParameter( CAMPO_COMUNE_CSSA_COMP).toUpperCase() );
    // 30/10/2003 Controllo Esistenza CSSA.
    if (getRequestStringParameter( CAMPO_COMUNE_CSSA_COMP).length()>1)
    {
      ICSSA lCSSACtrl = SICOLookupRemote.getCSSARemote();
      lDepMod.setIdCssaComp( ((CSSAModel)lCSSACtrl.getCSSAByDescrComune(getRequestStringParameter( CAMPO_COMUNE_CSSA_COMP).toUpperCase())).getIdCSSA());
    }
    else
      lDepMod.setIdCssaComp(new BigDecimal("9999"));

    // Controlla e imposta il codice ufficio magistrato competente.
    if (getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP).compareTo("")!=0)
      lDepMod.setCodUfficioMagistratoComp( getCodUfficioByCodTipoUfficioDescrComune("UDS" ,getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP) ));
    else
      lDepMod.setCodUfficioMagistratoComp("-");

    lDepMod.setLuogoSvolgimentoProva( getRequestStringParameter( CAMPO_LUOGO_SVOLGIMENTO_PROVA) );
    lDepMod.setServizioTerapeuticoComp( getRequestStringParameter( CAMPO_SERVIZIO_TERAPEUTICO_COMP) );
    lDepMod.setNumGiorniDetenzioneDom( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_DETENZIONE_DOM) );
    lDepMod.setNumMesiDetenzioneDom( getRequestBigDecimalParameter( CAMPO_NUM_MESI_DETENZIONE_DOM) );
    lDepMod.setNumAnniDetenzioneDom( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_DETENZIONE_DOM) );
    lDepMod.setNumGiorniPermessoAccordati( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_PERMESSO_ACCORDATI) );
   // Eliminati campi di input. Luigi 17-5-2004
  // lDepMod.setNumGiorniRiduzionePena( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_RIDUZIONE_PENA) );
  //  lDepMod.setNumGiorniRiduzioneUsufruiti( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_RIDUZIONE_USUFRUITI) );
/*
    if (getRequestStringParameter(CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE).compareTo("")!=0)
      lDepMod.setCodUffTdsConcessoRiduzione( getCodUfficioByCodTipoUfficioDescrComune("TDS",getRequestStringParameter( CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE ) ));
    else
 */
      lDepMod.setCodUffTdsConcessoRiduzione("-");

    lDepMod.setCodOperatoreInserimento( getCodUtenteConnesso() );
    lDepMod.setCodUfficioInserimento( getCodUfficioUtenteConnesso() );
    lDepMod.setDataInserimento( DateUtils.getSysDate() );
    lDepMod.setGenPridGeneraleProcedimento( lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento() );

    //******************************//
    // Parte di gestione dei tenori //
    //******************************//

		// lTenore.setCodOggettoTenore(getRequestStringParameter( ICostantiTenore.CAMPO_COD_OGGETTO_TENORE) );

		 ////////////// TO BE CONTINUED................
    // Preleva dalla request i campi id_tenore.
    String[] lArrayIdTenore = this.getRequestStringParameters(ICostantiTenore.CAMPO_ID_TENORE);

		TenoreModel lTenori[] = new TenoreModel[lArrayIdTenore.length];
		TenoreModel lTenorePrimo = new TenoreModel();

    // Spostato Luigi 13-11-2003
    IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();

		for (int i=0; i < lArrayIdTenore.length ; i++ )
    {
      TenoreModel lTenore = new TenoreModel();
			lTenore.setIdTenore(new BigDecimal(lArrayIdTenore[i]));
			lTenore.setGenPridGeneraleProcedimento(lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

    // 	lTenore.setCodOggettoTenore(getRequestStringParameters(ICostantiTenore.CAMPO_COD_OGGETTO_TENORE)[i]);

	//		Spostato Luigi 13-11-2003
      lTenore.setCodEsitoTenore(lDecCtrl.ExRicercaCodEsitiProvByCodTenore(getRequestStringParameters(ICostantiTenore.CAMPO_COD_ESITO_TENORE)[i]));
 //     lTenore.setCodEsitoTenore(getRequestStringParameters(ICostantiTenore.CAMPO_COD_ESITO_TENORE)[i]);


     lTenore.setData(getRequestDateParameter(CAMPO_DATA_EMISSIONE,"dd/MM/yyyy"));

//      lTenore.setData(getRequestDateParameter( ICostantiTenore.CAMPO_ANNO_DATA,ICostantiTenore.CAMPO_MESE_DATA,ICostantiTenore.CAMPO_GIORNO_DATA) );
      //lTenore.setCodDettaglioOggetto("-");

      lTenore.setProgrTenore(new BigDecimal((double)(i+1)));

      //--------GDV--------INserire il magistrato Relatore ----//

      lTenore.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			lTenore.setDataAggiornamento(DateUtils.getSysDate());
			lTenore.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());

      lTenori[i]   = lTenore;
			lTenorePrimo =  lTenore;

			if ( i > 0 )
			{
			   Integer lPrima = new Integer(lTenori[i-1].getCodEsitoTenore());
			   Integer lDopo = new Integer(lTenori[i].getCodEsitoTenore());

         if(lDopo.intValue() > lPrima.intValue())
			      lTenorePrimo = lTenori[i];
			   else
			      lTenorePrimo = lTenori[i-1];
			}
    }

    //*************************//
    // Parte creazione Evento. //
    //*************************//

    // Crea l'Evento
		EventoModel lEvento = new EventoModel();
		lEvento.setCodTipoEvento("01");
		lEvento.setCodTipoProvvedimento("03");

		//Esito Tenore---- GDV
		lEvento.setCodMotivo(lTenorePrimo.getCodOggettoTenore());

    // STUB: 20030707 . Se utilizziamo il codice a 2 cifre
    // si può evitare questo passaggio ?

 /*   Spostato Luigi 13-11-2003
		IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();
		String lCodEsito = lDecCtrl.ExRicercaCodEsitiProvByCodTenore(lTenorePrimo.getCodEsitoTenore());

		lEvento.setCodEsito(lCodEsito);
*/
    lEvento.setCodEsito(lTenorePrimo.getCodEsitoTenore());
		lEvento.setTenIdTenore(lTenorePrimo.getIdTenore());

		lEvento.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		lEvento.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
                lEvento.setDataEmissione(getRequestDateParameter(CAMPO_DATA_EMISSIONE,"dd/MM/yyyy") );
	//	lEvento.setDataEmissione(getRequestDateParameter( ICostantiTenore.CAMPO_ANNO_DATA,ICostantiTenore.CAMPO_MESE_DATA,ICostantiTenore.CAMPO_GIORNO_DATA) );
		lEvento.setFasSieIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		lEvento.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		lEvento.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEvento.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEvento.setDataInserimento(DateUtils.getSysDate());
		lEvento.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEvento.setCodTipoUfficioDestinatario("-");
		lEvento.setCodLuogoDestinatario("-");

		// Tipo di template da assegnare all'evento, solo per quelli
    // presenti nella cbx e sono diversi da 01 ( che generazione automantica )
    // inserisce l'id del template nel model evento.
    if( getRequestStringParameter( CAMPO_TIPO_ORDINANZA_DA_PRODURRE ).equals("02") )
      lEvento.setTemIdTemplate( TEMPLATE_ORDINANZA_GENERICO );
    else if(getRequestStringParameter( CAMPO_TIPO_ORDINANZA_DA_PRODURRE ).equals("03") )
      lEvento.setTemIdTemplate( TEMPLATE_ORDINANZA_RIGETTO_GENERICO );
    else if(getRequestStringParameter( CAMPO_TIPO_ORDINANZA_DA_PRODURRE ).equals("04") )
      lEvento.setTemIdTemplate( TEMPLATE_ORDINANZA_NLP_GENERICO );



    //************************************************************//
    // Popola con evento, ordinanza e Tenori l'aggregato di model //
    //************************************************************//
		OrdinanzaEventoTenoriModel lOrdinanzaEventoModel =  new OrdinanzaEventoTenoriModel();

    // Imposta nel model lOrdinanzaEventoModel : Evento, Ordinanza e Tenori.
		lOrdinanzaEventoModel.setEvento(lEvento);
		lOrdinanzaEventoModel.setOrdinanza(lDepMod);
		lOrdinanzaEventoModel.setTenori(lTenori);

		// Effettua l'inserimento del deposito ordinanza + evento update esito tenori.
    IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		OrdinanzaEventoTenoriModel lDepModRet = lCtrl.ExInserisciEventoDepositoOrdinanzaPc(lOrdinanzaEventoModel); // setta la risposta nella request

    lDepModRet.getOrdinanza().setDescrUfficioMagistratoComp( getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP) );
    lDepModRet.getOrdinanza().setLuogoSvolgimentoProva( getRequestStringParameter( CAMPO_LUOGO_SVOLGIMENTO_PROVA) );


    // STUB 20/10/2003 Rework migrazione campo COD_COMUNE_CSSA_COMP a ID_CSSA_COMP
    //lDepModRet.getOrdinanza().setDescrComuneCssaComp( getRequestStringParameter( CAMPO_COD_COMUNE_CSSA_COMP ));
    lDepModRet.getOrdinanza().setDescrComuneCssaComp( getRequestStringParameter( CAMPO_COMUNE_CSSA_COMP ));
    lDepModRet.getOrdinanza().setIdCssaComp( getRequestBigDecimalParameter( CAMPO_ID_CSSA_COMP) );


    //Prepara la pagina di destinazione
		String lPage = ICostantiPrescrizione.PG_LOAD_INSERISCIPRESCRIZIONE;

      // Passaggio di dati alla jsp
      setRequestAttribute("UffMagComp",lDepModRet.getOrdinanza().getDescrUfficioMagistratoComp());
      setRequestAttribute("LuogoProva",lDepModRet.getOrdinanza().getLuogoSvolgimentoProva());
      setRequestAttribute("ComuneCSSA",lDepModRet.getOrdinanza().getDescrComuneCssaComp());
      setRequestAttribute("IDEvento",lDepModRet.getEvento().getIdEvento().toString());
      setRequestAttribute("modalita", "I");
      setRequestAttribute("nextaction", "siap.sius.depositoordinanzapc.action.ActDettaglioEmissioneOrdinanza");

    //lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.prescrizione.action.ActLoadInserisciPrescrizione&"+CAMPO_ID_DEPOSITO_ORDINANZA_PC+"="+llDepModRet.getIdDepositoOrdinanzaPc().toString();
		return lPage;
  }
}