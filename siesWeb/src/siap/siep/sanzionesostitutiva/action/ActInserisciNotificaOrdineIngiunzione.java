package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-13: aggiunta classe
 * Title: ActGrigliaNotifiche
 * Description: Classe Action per la load ricerca di Omesse Notifica
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActInserisciNotificaOrdineIngiunzione extends ActionSiap
		implements ICostantiSanzioneSostitutiva {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// Effettuo l'inserimento della notifica e ricarico la form delle notifiche
		/*
		 * SOLO Se ho inserito la notifica la condannato allora viene attivato lo scadenzario
		 * 
		 */
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento eventoCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = eventoCtrl.ExRicercaEventoNotificaByKey(idEvento);

		// Recupero i destinatari previsti
		Vector<NotificaModel> listaNotDaAggiornare = new Vector<>();
		NotificaModel[] lNotifiche = lEveNotMod.getNotifiche();
		for (int i = 0; i < lNotifiche.length; i++) {
			NotificaModel lNotificaModel = lNotifiche[i];

			NotificaModel lNotMod = new NotificaModel();
            String IdNotifica = lNotificaModel.getIdNotifica().toString();

			String lGiornoNot = this.getRequestStringParameter(
					ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA + "_" + IdNotifica);
			String lMeseNot = this.getRequestStringParameter(
					ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA + "_" + IdNotifica);
			String lAnnoNot = this.getRequestStringParameter(
					ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA + "_" + IdNotifica);
			
			Date dataAvventaNotifica = null;
			if (lGiornoNot!="") {
			    // solo se presente la data di notifica vado ad aggiornare il record della notifica
			    dataAvventaNotifica = DateUtils.getDate(lAnnoNot, lMeseNot, lGiornoNot);
			}
			
            lNotMod.setDataAvvenutaNotifica(dataAvventaNotifica);
			
	        if (!isRequestParameterNullEmptyObj(ICostantiOrdineEsecuzione.ABILITA_NOTIFICA + "_" + lNotificaModel.getIdNotifica())) 
	        {
	            // L'utente ha modificato l'autorita che ha effettuato la notifica rispetto a quella prevista
	            siesLogger.debug("Modificata autorità di notifica per id = " + lNotificaModel.getIdNotifica());

                String lAutoritaDelegata = this.getRequestStringParameter(
                        ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA + "_" + IdNotifica);
                String lSedeAutoritaDelegata = this.getRequestStringParameter(
                        ICostantiAutoritaEsterna.CAMPO_COD_SEDE + "_" + IdNotifica);
                String lIndirizzo = this.getRequestStringParameter(
                        ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE + "_" + IdNotifica);

                AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

                lAutMod.setCodTipoAutorita(lAutoritaDelegata);
                ComuneModel lComModel = new ComuneModel(getCodComuneByDescr(lSedeAutoritaDelegata));
                lAutMod.setCodSede(lComModel.getCodComune());
                lAutMod.setDescrizione(lIndirizzo);

                lAutMod.setCodOperatoreInserimento(getCodUtenteConnesso());
                lAutMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
                lAutMod.setDataInserimento(DateUtils.getSysDate());

                lNotMod.setIstDetIdIstitutoDetenzione(""); // ????

                lNotMod.setAutoritaEsternaDelegata(lAutMod);
            }

                // Ho la data per cui devo 
                // Se mancano i dati devo confermare la notifica allo stesso destinatario iniziale
                
			lNotMod.setIdNotifica(new BigDecimal(IdNotifica));
			lNotMod.setEveIdEvento(this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

			lNotMod.setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
			lNotMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			lNotMod.setDataAggiornamento(DateUtils.getSysDate());

			if (lNotMod.getDataAvvenutaNotifica()!=null)
			    lNotMod.setCodEsito("03"); // Notificato
			else
			    lNotMod.setCodEsito(lNotificaModel.getCodEsito()); 
			
			if (lNotificaModel.getAvvIdAvvocatoFascicoloSiep() == null
					&& lNotificaModel.getIdCivilmenteObbligato() == null) {
				// Notifica la condannato
				lNotMod.setCodTipoNotifica("E");
			}

		    listaNotDaAggiornare.add(lNotMod);
			
		    // Cancela ??
			/*
		    if (!isRequestParameterNullEmptyObj(ICostantiOrdineEsecuzione.ABILITA_CANCELLA + "_" + lNotificaModel.getIdNotifica())) {
                NotificaModel lNotMod = new NotificaModel();

                String IdNotifica = lNotificaModel.getIdNotifica().toString();

                lNotMod.setCodEsito("-"); // Non Notificato
                lNotMod.setDataAvvenutaNotifica(null);
                lNotMod.setAutoritaEsternaDelegata(null);
                lNotMod.setAutEstIdAutoritaEstDeleg(null);
                lNotMod.setIstDetIdIstitutoDetenzione(""); // ????
                
                lNotMod.setIdNotifica(new BigDecimal(IdNotifica));
                lNotMod.setEveIdEvento(this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

                lNotMod.setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
                lNotMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
                lNotMod.setDataAggiornamento(DateUtils.getSysDate());

                if (lNotificaModel.getAvvIdAvvocatoFascicoloSiep() == null
                        && lNotificaModel.getIdCivilmenteObbligato() == null) {
                    // Notifica la condannato
                    lNotMod.setCodTipoNotifica("E");
                }			    
			    
			    listaNotDaAggiornare.add(lNotMod);
			}
			*/

		}

		siesLogger.debug("Notifiche da aggiornare = " + listaNotDaAggiornare.size());

		ISanzioneSostitutiva lCtrlSS = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		lCtrlSS.exAggiornaNotificheOrdineIngiunzione(lEveNotMod.getEvento(), listaNotDaAggiornare);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sanzionesostitutiva.action.ActLoadNotificheOrdineIngiunzione";

		return lPage;
	}

}