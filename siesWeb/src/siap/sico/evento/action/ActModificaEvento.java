package siap.sico.evento.action;


/**
* <p>Title: ActModificaEvento</p>
* <p>Description: Classe Action per la modifica di Evento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActModificaEvento extends ActionSiap implements ICostantiEvento
{
/**
* Azione di Modifica del Evento
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
{

 		 String lId = getRequestStringParameter(CAMPO_ID_EVENTO);
 		 // riempie il model
 		 EventoModel lEveMod = new EventoModel ();

 		 lEveMod.setIdEvento(new BigDecimal(lId));
		 lEveMod.setIdEvento( getRequestBigDecimalParameter( CAMPO_ID_EVENTO) );
		 lEveMod.setCodTipoEvento( getRequestStringParameter( CAMPO_COD_TIPO_EVENTO) );
		 lEveMod.setCodTipoProvvedimento( getRequestStringParameter( CAMPO_COD_TIPO_PROVVEDIMENTO) );
		 lEveMod.setCodMotivo( getRequestStringParameter( CAMPO_COD_MOTIVO) );
		 lEveMod.setCodUfficioEmittente( getRequestStringParameter( CAMPO_COD_UFFICIO_EMITTENTE) );
		 lEveMod.setCodLuogoEmittente( getRequestStringParameter( CAMPO_COD_LUOGO_EMITTENTE) );
		 lEveMod.setNomeSoggettoPresentante( getRequestStringParameter( CAMPO_NOME_SOGGETTO_PRESENTANTE) );
     lEveMod.setCognomeSoggettoPresentante( getRequestStringParameter( CAMPO_COGNOME_SOGGETTO_PRESENTANTE) );

     lEveMod.setDataEmissione( getRequestDateParameter( CAMPO_GIORNO_DATA_EMISSIONE,CAMPO_MESE_DATA_EMISSIONE,CAMPO_ANNO_DATA_EMISSIONE) );
		 lEveMod.setCodEsito( getRequestStringParameter( CAMPO_COD_ESITO) );
		 lEveMod.setFlagPiuMeno( getRequestStringParameter( CAMPO_FLAG_PIU_MENO) );
		 lEveMod.setDataTrasmissioneAtti( getRequestDateParameter( CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI,CAMPO_MESE_DATA_TRASMISSIONE_ATTI,CAMPO_ANNO_DATA_TRASMISSIONE_ATTI) );
		 lEveMod.setDataRicezioneAtti( getRequestDateParameter( CAMPO_GIORNO_DATA_RICEZIONE_ATTI,CAMPO_MESE_DATA_RICEZIONE_ATTI,CAMPO_ANNO_DATA_RICEZIONE_ATTI) );
		 lEveMod.setCodUfficioDestinatario( getRequestStringParameter( CAMPO_COD_UFFICIO_DESTINATARIO) );
		 lEveMod.setCodLuogoDestinatario( getRequestStringParameter( CAMPO_CODICE_LUOGO_DESTINATARIO) );
		 lEveMod.setAnnoProtocollo( getRequestBigDecimalParameter( CAMPO_ANNO_PROTOCOLLO) );
		 lEveMod.setProgrProtocollo( getRequestBigDecimalParameter( CAMPO_PROGR_PROTOCOLLO) );
		 //lEveMod.setDocBlob( getRequestBlobParameter( CAMPO_DOC_BLOB) );
		 lEveMod.setDataInserimento( getRequestDateParameter( CAMPO_GIORNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_ANNO_DATA_INSERIMENTO) );
		 UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		 lEveMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		 lEveMod.setDataAggiornamento(DateUtils.getSysDate());
//		 lEveMod.setCodUfficioAggiornamento(lUtenteMod.getCodUtente());
		 lEveMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );
		 lEveMod.setFasSiuIdFascicoloSius( getRequestBigDecimalParameter( CAMPO_FAS_SIU_ID_FASCICOLO_SIUS) );
		// lEveMod.setFasSiuSogIdSoggetto( getRequestBigDecimalParameter( CAMPO_FAS_SIU_SOG_ID_SOGGETTO) );

		 // chiama il controller
		 IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		 lCtrl.ExModificaEvento(lEveMod);

		 setRequestAttribute("modalita", "M");
		 setRequestAttribute("evento", lEveMod);

		 return "";
	 }



}
