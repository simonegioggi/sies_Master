package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActModificaDepositoOrdinanzaPc</p>
* <p>Description: Classe Action per la modifica di DepositoOrdinanzaPc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActModificaDepositoOrdinanzaPc extends ActionSiap implements ICostantiDepositoOrdinanzaPc
{
/**
* Azione di Modifica del DepositoOrdinanzaPc
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
  public String processRequest() throws Exception
  {
 		 String lId = getRequestStringParameter(CAMPO_ID_DEPOSITO_ORDINANZA_PC);
 		 // riempie il model
 		 DepositoOrdinanzaPcModel lDepMod = new DepositoOrdinanzaPcModel ();

 		 lDepMod.setIdDepositoOrdinanzaPc(new BigDecimal(lId));
		 lDepMod.setIdDepositoOrdinanzaPc( getRequestBigDecimalParameter( CAMPO_ID_DEPOSITO_ORDINANZA_PC) );
		 lDepMod.setAnnoS3( getRequestBigDecimalParameter( CAMPO_ANNO_S3) );
		 lDepMod.setNumS3( getRequestBigDecimalParameter( CAMPO_NUM_S3) );
		 lDepMod.setOggettoProcedimento( getRequestStringParameter( CAMPO_OGGETTO_PROCEDIMENTO) );
		 lDepMod.setDataUdienza( getRequestDateParameter( CAMPO_ANNO_DATA_UDIENZA,CAMPO_MESE_DATA_UDIENZA,CAMPO_GIORNO_DATA_UDIENZA) );
		 lDepMod.setDataCameraConsiglio( getRequestDateParameter( CAMPO_ANNO_DATA_CAMERA_CONSIGLIO,CAMPO_MESE_DATA_CAMERA_CONSIGLIO,CAMPO_GIORNO_DATA_CAMERA_CONSIGLIO) );
		 lDepMod.setDataDeposito( getRequestDateParameter( CAMPO_ANNO_DATA_DEPOSITO,CAMPO_MESE_DATA_DEPOSITO,CAMPO_GIORNO_DATA_DEPOSITO) );
		 lDepMod.setCodNaturaProvvedimento( getRequestStringParameter( CAMPO_COD_NATURA_PROVVEDIMENTO) );
		 lDepMod.setIdCssaComp( getRequestBigDecimalParameter( CAMPO_ID_CSSA_COMP) );
		 lDepMod.setCodUfficioMagistratoComp( getRequestStringParameter( CAMPO_COD_UFFICIO_MAGISTRATO_COMP) );
		 lDepMod.setLuogoSvolgimentoProva( getRequestStringParameter( CAMPO_LUOGO_SVOLGIMENTO_PROVA) );
		 lDepMod.setServizioTerapeuticoComp( getRequestStringParameter( CAMPO_SERVIZIO_TERAPEUTICO_COMP) );
		 lDepMod.setNumGiorniDetenzioneDom( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_DETENZIONE_DOM) );
		 lDepMod.setNumMesiDetenzioneDom( getRequestBigDecimalParameter( CAMPO_NUM_MESI_DETENZIONE_DOM) );
		 lDepMod.setNumAnniDetenzioneDom( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_DETENZIONE_DOM) );
		 lDepMod.setNumGiorniPermessoAccordati( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_PERMESSO_ACCORDATI) );
		 lDepMod.setNumGiorniRiduzionePena( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_RIDUZIONE_PENA) );
		 lDepMod.setNumGiorniRiduzioneUsufruiti( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_RIDUZIONE_USUFRUITI) );
		 lDepMod.setCodUffTdsConcessoRiduzione( getRequestStringParameter( CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE) );
		 lDepMod.setDataInserimento( getRequestDateParameter( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lDepMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		 lDepMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());		 lDepMod.setDataAggiornamento(DateUtils.getSysDate());
		 lDepMod.setDataAggiornamento(DateUtils.getSysDate());
		 lDepMod.setGenPridGeneraleProcedimento( getRequestBigDecimalParameter( CAMPO_GEN_PRID_GENERALE_PROCEDIMENTO) );

		 // chiama il controller
		 IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		 DepositoOrdinanzaPcModel llDepModRet = lCtrl.ExModificaDepositoOrdinanzaPc(lDepMod);

		 setRequestAttribute("modalita", "M");
		 setRequestAttribute("depositoordinanzapc", llDepModRet);

		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.depositoordinanzapc.action.ActLoadDettaglioDepositoOrdinanzaPc&"+CAMPO_ID_DEPOSITO_ORDINANZA_PC+"="+llDepModRet.getIdDepositoOrdinanzaPc().toString();
		 return lPage;
	 }



}