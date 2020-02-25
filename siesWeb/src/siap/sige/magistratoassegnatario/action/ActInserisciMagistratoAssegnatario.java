package siap.sige.magistratoassegnatario.action;


/**
* <p>Title: ActInserisciMagistratoAssegnatario</p>
* <p>Description: Classe Action per l'inserimento di MagistratoAssegnatario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioMagistratoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.util.SIGELookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


public class ActInserisciMagistratoAssegnatario extends ActionSiap implements ICostantiMagistratoAssegnatario
{
/**
* Azione di Inserimento del MagistratoAssegnatario
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws Exception
{
    //setLinkRitorno();
    this.gestioneRitorno();

	//generale
    FascicoloSigeEstesoModel lFascicoloSigeModel = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
    MagistratoAssegnatarioMagistratoModel lMagistrato = new MagistratoAssegnatarioMagistratoModel();

    //magistrato nuovo
    //lMagistrato.getMagistrato().setCognome(this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME));
    //lMagistrato.getMagistrato().setNome(this.getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME));
    //magistrato vecchio
    lMagistrato.getMagistrato().setCodMagistrato(this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO_VECCHIO));

    lMagistrato.getMagistratoAssegnatario().setMagCodMagistrato(this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
    lMagistrato.getMagistratoAssegnatario().setFasSigeIdFascicoloSige(lFascicoloSigeModel.getFascicoloSige().getIdFascicoloSige());
    lMagistrato.getMagistratoAssegnatario().setDataInizio( this.getRequestDateParameter(ICostantiMagistratoAssegnatario.CAMPO_ANNO_DATA_INIZIO,ICostantiMagistratoAssegnatario.CAMPO_MESE_DATA_INIZIO,ICostantiMagistratoAssegnatario.CAMPO_GIORNO_DATA_INIZIO));
    lMagistrato.getMagistratoAssegnatario().setCodRuoloMagistrato("03");
    lMagistrato.getMagistratoAssegnatario().setDataInserimento(DateUtils.getSysDate());
    lMagistrato.getMagistratoAssegnatario().setCodOperatoreInserimento(this.getCodUtenteConnesso());
    lMagistrato.getMagistratoAssegnatario().setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

   //---Aggiungere in SIGELookupRemote il metodo getMagistratoAssegnatarioRemote()
    IMagistratoAssegnatario lCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
    MagistratoAssegnatarioModel llMagModRet = lCtrl.ExInserisciAggiornaMagistratoAssegnatario(lMagistrato);

  // setta la risposta nella request
    setRequestAttribute("magistratoprecedente",this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO_VECCHIO));
    setRequestAttribute("magistratoassegnatario", llMagModRet);

    IMagistratoAssegnatario lMagCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
	lFascicoloSigeModel.setMagAssegnatario(lMagCtrl.ExRicercaEstesaMagAssCorrenteXFascicolo(lFascicoloSigeModel.getFascicoloSige().getIdFascicoloSige()));
    //Prepara la pagina di destinazione
    String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sige.magistratoassegnatario.action.ActRicercaMagistratoAssegnatario";
    lPage  = lPage .concat("&"+CAMPO_MAG_COD_MAGISTRATO+"="+llMagModRet.getMagCodMagistrato().toString());
    return lPage;
}



}
