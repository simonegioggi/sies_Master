package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore;
import siap.sius.prescrizione.action.ICostantiPrescrizione;
import siap.sius.tenore.action.ICostantiTenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActInserisciOrdinanza_AmmProvv_AffiInProva_ServSoc</p>
* <p>Description: Action per l'inserimento di Applicazione provvisoria di misura alternativa</p>
* <p>			Ammissione provvisoria ad Affidamento in prova ai Servizi Sociali</p>
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActInserisciOrdinanza_AmmProvv_AffiInProva_ServSoc extends ActionSiap implements ICostantiDepositoOrdinanzaPc,ICostantiMagistratoRelatore 
{
  /**
  * Azione di Inserimento Applicazione provvisoria di misura alternativa
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */

	public String processRequest() throws Exception
  {
		
	String lRetPage = IWebConstants.PG_MESSAGE;
		
    FascicoloGPModel lFasGPMod = new FascicoloGPModel();
    lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    
    String CodMag = "";
    String CodMagRel = "";
    if(! isRequestParameterNullObj(ICostantiMagistratoRelatore.CAMPO_MAG_COD_MAGISTRATO))
    	CodMagRel = getRequestStringParameter(ICostantiMagistratoRelatore.CAMPO_MAG_COD_MAGISTRATO);
    
    if(!CodMagRel.equals(""))
    {
    	IMagistrato lctrl = SICOLookupRemote.getMagistratoRemote();
    	MagistratoModel lMagMod = lctrl.ExRicercaMagistratoByCod(CodMagRel);
    	if(lMagMod != null && lMagMod.getCodMagistrato() != null)
    		CodMag = lMagMod.getCodMagistrato();
    }	
    
    DepositoOrdinanzaPcModel lDepMod = new DepositoOrdinanzaPcModel();

    lDepMod.setDataCameraConsiglio( getRequestDateParameter(CAMPO_DATA_EMISSIONE,"dd/MM/yyyy") );
    lDepMod.setCodNaturaProvvedimento( "-" );
    lDepMod.setCodTipoOrdinanza("06");
    lDepMod.setUlterioreDescrizione(getRequestStringParameter(CAMPO_ULTERIORE_DESCRIZIONE));
    
    if(!isRequestParameterNullObj(CAMPO_NUM_MESI_ARRESTO_REV))
    	lDepMod.setNumMesiArrestoRev(getRequestBigDecimalParameter( CAMPO_NUM_MESI_ARRESTO_REV) );
    	
    if (!isRequestParameterNullObj(CAMPO_COD_UFFICIO_MAGISTRATO_COMP) && 
       (!getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP).equals("")) )
    		lDepMod.setCodUfficioMagistratoComp( getCodUfficioByCodTipoUfficioDescrComune("UDS" ,getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP) ));
    else
    		lDepMod.setCodUfficioMagistratoComp("-");
    
    if(!isRequestParameterNullObj(CAMPO_LUOGO_SVOLGIMENTO_PROVA))
    	lDepMod.setLuogoSvolgimentoProva( getRequestStringParameter( CAMPO_LUOGO_SVOLGIMENTO_PROVA) );
    else
    	lDepMod.setLuogoSvolgimentoProva("-");
    
    if(!isRequestParameterNullObj(CAMPO_COD_UFFICIO_TDS_COMP) &&
      (!getRequestStringParameter(CAMPO_COD_UFFICIO_TDS_COMP).equals(""))	)
    		lDepMod.setCodUffTdsConcessoRiduzione(getCodUfficioByCodTipoUfficioDescrComune("TDS", getRequestStringParameter(CAMPO_COD_UFFICIO_TDS_COMP)) );
    else
    		lDepMod.setCodUffTdsConcessoRiduzione("-");
    
    if(!isRequestParameterNullObj(CAMPO_AUTORITA_VIGILANTE) && 
      (!getRequestStringParameter(CAMPO_AUTORITA_VIGILANTE).equals(""))	)
    	lDepMod.setAutoritaVigilante(getCodUfficioByCodTipoUfficioDescrComune("PM",getRequestStringParameter(CAMPO_AUTORITA_VIGILANTE)) );

    if(!CodMag.equals(""))
     	lDepMod.setCodMagistrato(CodMag);
    else
    	lDepMod.setCodMagistrato("-");
    
    lDepMod.setCodOperatoreInserimento( getCodUtenteConnesso() );
    lDepMod.setCodUfficioInserimento( getCodUfficioUtenteConnesso() );
    lDepMod.setDataInserimento( DateUtils.getSysDate() );
    
    lDepMod.setGenPridGeneraleProcedimento( lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento() );

    //20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
    if (!isRequestParameterNullObj(CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE)) {
    	lDepMod.setCodTipoControlloEsecuzione(getRequestStringParameters(CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE)[0]);
    }
    
    //******************************//
    // Parte di gestione dei tenori //
    //******************************//

    // Preleva dalla request i campi id_tenore.
    String[] lArrayIdTenore = this.getRequestStringParameters(ICostantiTenore.CAMPO_ID_TENORE);

		TenoreModel lTenori[] = new TenoreModel[lArrayIdTenore.length];
		TenoreModel lTenorePrimo = new TenoreModel();

    // Spostato Luigi 13-11-2003
    IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();

	for (int i=0; i < lArrayIdTenore.length ; i++ )
    {
			TenoreModel lTenore = new TenoreModel();
			if(!lArrayIdTenore[i].equals("0"))
				lTenore.setIdTenore(new BigDecimal(lArrayIdTenore[i]));
			
			lTenore.setGenPridGeneraleProcedimento(lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			lTenore.setCodEsitoTenore(lDecCtrl.ExRicercaCodEsitiProvByCodTenore(getRequestStringParameters(ICostantiTenore.CAMPO_COD_ESITO_TENORE)[i]));
			
			lTenore.setData(getRequestDateParameter(CAMPO_DATA_EMISSIONE,"dd/MM/yyyy"));
			lTenore.setProgrTenore(new BigDecimal((double)(i+1)));
			
		    if(!CodMag.equals(""))
		    	lTenore.setCodMagistrato(CodMag);
			
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
		lEvento.setCodMotivo(lTenorePrimo.getCodOggettoTenore());

		lEvento.setCodEsito(lTenorePrimo.getCodEsitoTenore());
		lEvento.setTenIdTenore(lTenorePrimo.getIdTenore());

		if(!CodMag.equals(""))
			lEvento.setCodMagistrato(CodMag);
		else
			lEvento.setCodMagistrato("-");
		
		lEvento.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		lEvento.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
        lEvento.setDataEmissione(getRequestDateParameter(CAMPO_DATA_EMISSIONE,"dd/MM/yyyy") );
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

    if( getRequestStringParameter( CAMPO_TIPO_ORDINANZA_DA_PRODURRE ).equals("2008") )
        lEvento.setTemIdTemplate( TEMPLATE_MOD_ORDINANZA_MA_AFFIDAMENTO_IN_PROVA );
    
    // Popola con evento, ordinanza e Tenori l'aggregato di model //
 
		OrdinanzaEventoTenoriModel lOrdinanzaEventoModel =  new OrdinanzaEventoTenoriModel();

    // Imposta nel model lOrdinanzaEventoModel : Evento, Ordinanza e Tenori.
		lOrdinanzaEventoModel.setEvento(lEvento);
		lOrdinanzaEventoModel.setOrdinanza(lDepMod);
		lOrdinanzaEventoModel.setTenori(lTenori);

		// Effettua l'inserimento del deposito ordinanza + evento update esito tenori.
    IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		OrdinanzaEventoTenoriModel lDepModRet = lCtrl.ExInserisciEventoDepositoOrdinanzaPc(lOrdinanzaEventoModel); // setta la risposta nella request

	if (!isRequestParameterNullObj(CAMPO_COD_UFFICIO_MAGISTRATO_COMP))
		lDepModRet.getOrdinanza().setDescrUfficioMagistratoComp( getRequestStringParameter(CAMPO_COD_UFFICIO_MAGISTRATO_COMP) );
    
    if(!isRequestParameterNullObj(CAMPO_LUOGO_SVOLGIMENTO_PROVA))
    	lDepModRet.getOrdinanza().setLuogoSvolgimentoProva( getRequestStringParameter( CAMPO_LUOGO_SVOLGIMENTO_PROVA) );

    // dettaglio dell'ordinanza	Inserita
	// Preparazione della pagina di destinazione
	//  Apre la pagina delle Prescrizioni.
	if (!isRequestParameterNullObj(CAMPO_CK_PRESCRIZIONI) && isRequestChecked(CAMPO_CK_PRESCRIZIONI))
	{
	
			lRetPage = ICostantiPrescrizione.PG_LOAD_INSERISCIPRESCRIZIONE;
		// Passaggio di dati alla jsp
		//	setRequestAttribute("UffMagComp", mDescUffMagComp);
		//	setRequestAttribute("LuogoProva", mLuogo);
			//setRequestAttribute("ComuneCSSA", mDescComuneCSSA);
			setRequestAttribute("IDEvento", lOrdinanzaEventoModel.getEvento().getIdEvento().toString());
			setRequestAttribute("modalita", "I");
			this.setRequestAttribute("nextaction", "siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza");
	}
	else
	{	
	      RedirectTo lRedirectTo = new RedirectTo();
	      lRedirectTo.setPage(IWebConstants.PG_MAIN);
	      lRedirectTo.setAction("siap.sius.depositoordinanzapc.action.ActDettaglioOrdinanza_AmmProvv_AffiInProva_ServSoc");
	      lRedirectTo.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, lOrdinanzaEventoModel.getEvento().getIdEvento().toString() );
	      lRetPage = lRedirectTo.toString();
	} 
      
      return lRetPage;

  } // Chiude process
	
}	// Chiude clasee