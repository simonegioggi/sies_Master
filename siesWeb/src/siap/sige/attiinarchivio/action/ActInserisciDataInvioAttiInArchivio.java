package siap.sige.attiinarchivio.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActInserisciDataInvioAttiInArchivio extends ActionSige implements ICostantiAttiInArchivio, ICostantiEvento{
	public String processRequest() throws Exception {
	    
		gestioneRitorno();
	    EventoModel model=null;
		BigDecimal idEvento=super.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
	    if (idEvento==null) {
	    	model=this.insertEvent();
	    } else {
	    	model=this.updateEvento();
	    }

	    super.setRequestAttribute("lEve", model);
	    return PG_DETTAGLIO_INSERISCI_DATA_INVIO_ATTI_IN_ARCHIVIO;
    }
	
	private EventoModel insertEvent () throws F3BException{
		 FascicoloSigeEstesoModel lFascicoloEsteso = getFascicoloSigeEstesoInSessione();
		EventoModel lEveMod = new EventoModel();
	    lEveMod.setCodTipoEvento("-"); // Provvedimento
	    lEveMod.setCodTipoProvvedimento("-"); //Definizione Manuale
	    lEveMod.setCodMotivo("-"); //archiviazione per fascicolo iscritto per errore
	    lEveMod.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
	    lEveMod.setCodLuogoEmittente(getCodComuneUtenteConnesso());
	    lEveMod.setCodTipoUfficioDestinatario("-");
	    lEveMod.setCodLuogoDestinatario("-");
	    lEveMod.setCodEsito("-");
	    lEveMod.setCodTipologiaInvioAtti(super.getRequestStringParameter(ICostantiAttiInArchivio.CAMPO_COD_TIPO_INVIO_DATI_IN_ARCHIVIO));
	    
	    Date dataInvioAtti=getRequestDateParameter( CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE, CAMPO_GIORNO_DATA_EMISSIONE);
	    lEveMod.setDataInvioAtti(dataInvioAtti);
	    lEveMod.setDescrizioneInvioAtti(super.getRequestStringParameter(ICostantiAttiInArchivio.CAMPO_ULTERIORE_DESCRIZIONE));
	   
	    lEveMod.setFlagDocumentoRegistrato("N");
	    lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
	    lEveMod.setCodOperatoreInserimento(getCodUtenteConnesso());
	    lEveMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	    lEveMod.setDataInserimento(DateUtils.getSysDate());
	    if(lFascicoloEsteso.getMagAssegnatario() != null && lFascicoloEsteso.getMagAssegnatario().getMagCodMagistrato() != null){
	    	lEveMod.setCodMagistrato(lFascicoloEsteso.getMagAssegnatario().getMagCodMagistrato());
	    } 
	    
	    
	    ProvvedimentoSigeModel lProMod = new ProvvedimentoSigeModel();
	    lProMod.setFasIdFascicoloSige( lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige() );
	    lProMod.setDataEmissione( dataInvioAtti );
	    lProMod.setDataDeposito( dataInvioAtti );
	    lProMod.setCodTipoProvvedimento(ICostantiAttiInArchivio.COD_TIPO_PROVVEDIMENTO); //Definizione Manuale
	    lProMod.setCodTipoProvvedimentoSige(ICostantiAttiInArchivio.COD_TIPO_PROVVEDIMENTO_SIGE);
	    lProMod.setDefinitorio("S");
	    lProMod.setCodOperatoreInserimento(getCodUtenteConnesso());
	    lProMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	    lProMod.setDataInserimento(DateUtils.getSysDate());
	    lProMod.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy")));
	    
	    //lFasCtrl.ExDefinizioneManualeFascicoloSige(lProMod, lEveMod, lFascicolo);
	    
	    IFascicoloSige lFasCtrl = SIGELookupRemote.getFascicoloSigeRemote();
	    lFasCtrl.ExInserisciDataInvioAtti(lProMod, lEveMod);
	    return lFasCtrl.ExRicercaDataInvioAtti(lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige());
	}
	
	private EventoModel updateEvento () throws F3BException{
		FascicoloSigeEstesoModel lFascicoloEsteso = getFascicoloSigeEstesoInSessione();
		IFascicoloSige lFasCtrl = SIGELookupRemote.getFascicoloSigeRemote();
	    EventoModel lEveMod = lFasCtrl.ExRicercaDataInvioAtti(lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige());
		lEveMod.setIdEvento(super.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		lEveMod.setCodTipologiaInvioAtti(super.getRequestStringParameter(ICostantiAttiInArchivio.CAMPO_COD_TIPO_INVIO_DATI_IN_ARCHIVIO));
	    lEveMod.setDataInvioAtti(getRequestDateParameter( CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE, CAMPO_GIORNO_DATA_EMISSIONE));
	    lEveMod.setDescrizioneInvioAtti(super.getRequestStringParameter(ICostantiAttiInArchivio.CAMPO_ULTERIORE_DESCRIZIONE));
	    lFasCtrl.ExModificaDataInvioAtti(lEveMod);
	    return lFasCtrl.ExRicercaDataInvioAtti(lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige());
	}
}
