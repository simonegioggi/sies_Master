package siap.siep.annotazionemanuale.action;


/**
* <p>Title: ActInserisciAnnotazioneManuale</p>
* <p>Attenzione tale action NON VIENE ATTUALMENTE UTILIZZATA (31/03/2006)</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciAnnotazioneManuale extends ActionSiap implements ICostantiAnnotazioneManuale
{
/**
 * Attenzione tale action NON VIENE ATTUALMENTE UTILIZZATA
 * tale classe è stata generata del CodeGenerator
 *  
 * Azione di Inserimento del AnnotazioneManuale
 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
 * @throws F3BException
 */
public String processRequest() throws F3BException
 		 {
 		 AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();

		 lAnnMod.setIdAnnotazioneManuale( getRequestBigDecimalParameter( CAMPO_ID_ANNOTAZIONE_MANUALE) );
		 lAnnMod.setCodTipoAnnotazione( getRequestStringParameter( CAMPO_COD_TIPO_ANNOTAZIONE) );
		 lAnnMod.setFlagPiuMeno( getRequestStringParameter( CAMPO_FLAG_PIU_MENO) );
		 lAnnMod.setNumAnniReclusione( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_RECLUSIONE) );
		 lAnnMod.setNumMesiReclusione( getRequestBigDecimalParameter( CAMPO_NUM_MESI_RECLUSIONE) );
		 lAnnMod.setNumGiorniReclusione( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_RECLUSIONE) );
		 lAnnMod.setImportoMulta( getRequestBigDecimalParameter( CAMPO_IMPORTO_MULTA) );
		 lAnnMod.setNumAnniArresto( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_ARRESTO) );
		 lAnnMod.setNumMesiArresto( getRequestBigDecimalParameter( CAMPO_NUM_MESI_ARRESTO) );
		 lAnnMod.setNumGiorniArresto( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_ARRESTO) );
		 lAnnMod.setImportoAmmenda( getRequestBigDecimalParameter( CAMPO_IMPORTO_AMMENDA) );

     lAnnMod.setDataArrestoDa( getRequestDateParameter( CAMPO_ANNO_DATA_ARRESTO_DA,CAMPO_MESE_DATA_ARRESTO_DA,CAMPO_GIORNO_DATA_ARRESTO_DA) );
     lAnnMod.setDataArrestoA( getRequestDateParameter( CAMPO_ANNO_DATA_ARRESTO_A,CAMPO_MESE_DATA_ARRESTO_A,CAMPO_GIORNO_DATA_ARRESTO_A) );
     lAnnMod.setDataReclusioneDa( getRequestDateParameter( CAMPO_ANNO_DATA_RECLUSIONE_DA,CAMPO_MESE_DATA_RECLUSIONE_DA,CAMPO_GIORNO_DATA_RECLUSIONE_DA) );
     lAnnMod.setDataReclusioneA( getRequestDateParameter( CAMPO_ANNO_DATA_RECLUSIONE_A,CAMPO_MESE_DATA_RECLUSIONE_A,CAMPO_GIORNO_DATA_RECLUSIONE_A) );


		 lAnnMod.setDataRicezioneDoc( getRequestDateParameter( CAMPO_ANNO_DATA_RICEZIONE_DOC,CAMPO_MESE_DATA_RICEZIONE_DOC,CAMPO_GIORNO_DATA_RICEZIONE_DOC) );
		 lAnnMod.setMotivazioni( getRequestStringParameter( CAMPO_MOTIVAZIONI) );
     lAnnMod.setNoteReclusione( getRequestStringParameter( CAMPO_NOTE_RECLUSIONE) );
		 lAnnMod.setAnnoGe( getRequestBigDecimalParameter( CAMPO_ANNO_GE) );
		 lAnnMod.setNumeroGe( getRequestStringParameter( CAMPO_NUMERO_GE) );
		 lAnnMod.setAnnoRege( getRequestBigDecimalParameter( CAMPO_ANNO_REGE) );
		 lAnnMod.setNumeroRege( getRequestStringParameter( CAMPO_NUMERO_REGE) );
		 lAnnMod.setAnnoMc( getRequestBigDecimalParameter( CAMPO_ANNO_MC) );
		 lAnnMod.setNumeroMc( getRequestStringParameter( CAMPO_NUMERO_MC) );
		 lAnnMod.setAnnoCda( getRequestBigDecimalParameter( CAMPO_ANNO_CDA) );
		 lAnnMod.setNumeroCda( getRequestStringParameter( CAMPO_NUMERO_CDA) );
		 lAnnMod.setAnnoCc( getRequestBigDecimalParameter( CAMPO_ANNO_CC) );
		 lAnnMod.setNumeroCc( getRequestStringParameter( CAMPO_NUMERO_CC) );
		 lAnnMod.setAnnoSiep( getRequestBigDecimalParameter( CAMPO_ANNO_SIEP) );
		 lAnnMod.setNumeroSiep( getRequestStringParameter( CAMPO_NUMERO_SIEP) );
		 lAnnMod.setCodTipoUfficioSiep( getRequestStringParameter( CAMPO_COD_TIPO_UFFICIO_SIEP) );
		 lAnnMod.setCodLuogoUfficioSiep( getRequestStringParameter( CAMPO_COD_LUOGO_UFFICIO_SIEP) );
		 lAnnMod.setDataIscrizioneSiep( getRequestDateParameter( CAMPO_ANNO_DATA_ISCRIZIONE_SIEP,CAMPO_MESE_DATA_ISCRIZIONE_SIEP,CAMPO_GIORNO_DATA_ISCRIZIONE_SIEP) );
		 lAnnMod.setCodFonte( getRequestStringParameter( CAMPO_COD_FONTE) );
		 lAnnMod.setAnnoFonte( getRequestBigDecimalParameter( CAMPO_ANNO_FONTE) );
		 lAnnMod.setNumeroFonte( getRequestStringParameter( CAMPO_NUMERO_FONTE) );
		 lAnnMod.setCodSottonumerazione( getRequestStringParameter( CAMPO_COD_SOTTONUMERAZIONE) );
		 lAnnMod.setComma( getRequestStringParameter( CAMPO_COMMA) );
		 lAnnMod.setLettera( getRequestStringParameter( CAMPO_LETTERA) );
		 lAnnMod.setNumero( getRequestStringParameter( CAMPO_NUMERO) );
		 lAnnMod.setArticolo( getRequestStringParameter( CAMPO_ARTICOLO) );
		 lAnnMod.setCodCausaleComputo( getRequestStringParameter( CAMPO_COD_CAUSALE_COMPUTO) );
		 lAnnMod.setCodDpr( getRequestStringParameter( CAMPO_COD_DPR) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lAnnMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
		 lAnnMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());		 lAnnMod.setDataInserimento(DateUtils.getSysDate());
		 lAnnMod.setDataInserimento(DateUtils.getSysDate());
		 lAnnMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );
		 lAnnMod.setReaIdReato( getRequestBigDecimalParameter( CAMPO_REA_ID_REATO) );
		 lAnnMod.setEveIdEvento( getRequestBigDecimalParameter( CAMPO_EVE_ID_EVENTO) );

		 //---Aggiungere in SIEPLookupRemote il metodo getAnnotazioneManualeRemote()

		 IAnnotazioneManuale lCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
		 AnnotazioneManualeModel llAnnModRet = lCtrl.ExInserisciAnnotazioneManuale(lAnnMod);		 // setta la risposta nella request
		 setRequestAttribute("annotazionemanuale", llAnnModRet);
		  //Prepara la pagina di destinazione
		 String lPage = "";
		 lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.annotazionemanuale.action.ActLoadDettaglioAnnotazioneManuale&"+CAMPO_ID_ANNOTAZIONE_MANUALE+"="+llAnnModRet.getIdAnnotazioneManuale().toString();
		 return lPage;
	 }



}