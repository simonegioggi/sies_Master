<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.evento.model.EventoModel" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento" %>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa" %>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="misuraaggregato" scope="request" class="java.util.Vector" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="HashFlagEventoRegistratoSIUS" scope="request" class="java.util.Hashtable"/>
<jsp:useBean id="SiusUtils" scope="request" class="siap.sius.fascicolo.util.FascicoloUtils"/>

<%
String lCodUfficioUtente = UtenteConnesso.getUfficioUtente().getCodUfficio();
String lModificabile = "SI";

// 23/09/2009 Si differenzia la funzione in base al parametro di Tipo Ufficio Connesso.
// Nel caso di sorveglianza (UDS e TDS) viene mantenuta l'attuale funzionalità.
// In tutti gli altri casi sarà visualizzata la stessa form senza i link ai fascicoli e ai provvedimenti definitori.
String lCodTipoUfficioUtente = UtenteConnesso.getUfficioUtente().getCodTipoUfficio();
%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Procedimenti di Sorveglianza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript">
var node;
function effettoTree(a)
{
  node=document.getElementById("elenco"+a);
  node.style.display = (node.style.display == "none")? "block" : "none";
  document.images["image"+a].src = (node.style.display == "none")? "<%=IWebConstants.IMAGES_DIR%>expand.gif" : "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
  return false;

}

function conferma(a_action, a_parameter, a_entityname ,a_parameter2 ,a_entityname2)
{
var documentoRegistrato = a_entityname2;

    if (documentoRegistrato=="S")
  {
       var  desktop = window.open("<%= IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=" + a_action + "&" + a_parameter + "=" +a_entityname, "Cancella_provvedimento","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
         window.parent.close();
   }else{
      str = "/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActCancellaOrdinanzeDecreti&" +a_parameter +"=" + a_entityname;
        if (window.confirm('Confermi la cancellazione ?'))
        {
               window.location.href=str;
        }
    }
}
function chiama(idEvento)
{
    window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadCancellaOrdinanzeDecreti&IdEvento="+idEvento,"Cancella_provvedimento", "top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
}



function pr()
{
   sign = prompt("Confermi la cancellazione?");
}

</script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>

<% if  (lCodTipoUfficioUtente.compareTo("PM")==0   ||  lCodTipoUfficioUtente.compareTo("PGCAP")==0) {
  // Paolo Cherubini inserisco titolo per siep 09/03/2011%>

      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Elenco Procedimenti Sorveglianza e GE </font></td>
<%}else{%>
  <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Ricerca Procedimenti per Numero Siep </font></td>
<%}%>

  <!-- BOTTONE DI RITORNO -->
  <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>


    </tr>
     <tr> </tr>
     <tr> </tr>

   <tr>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
     </tr>


  </table>

  <br>

<%
  Iterator itx = fascicoli.iterator();
  String sUfficio = "";
  String lUfficio = "";
  String prevUfficio = "";

  String wCol1="8%";
  String wCol2="18%";
  String wCol3="7%";
  String wCol4="13%";
  String wCol5="8%";
  String wCol6="17%";
  String wCol7="13%";
  String wCol8="10%";
  String wCol9="6%";
%>
  <table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      <td class="int" width="<%=wCol1%>">Numero SIUS</td>
      <td class="int" width="<%=wCol2%>">Contenuto</td>
      <td class="int" width="<%=wCol3%>">Data Udienza</td>
      <td class="int" width="<%=wCol4%>">Provvedimento</td>
      <td class="int" width="<%=wCol5%>">Data Emissione</td>
      <td class="int" width="<%=wCol6%>">Oggetto Provvedimento</td>
      <td class="int" width="<%=wCol7%>">Esito Provvedimento</td>
      <td class="int" width="<%=wCol8%>">Documento Validato</td>
      <td class="int" width="<%=wCol9%>">Azioni</td>

      </tr>


</table>
<%
    //PARTE SIEP
    Iterator iter = misuraaggregato.iterator();
    while ( iter.hasNext())
    {
      MisuraAlternativaAggregatoModel lAggre = new MisuraAlternativaAggregatoModel();
      lAggre = (MisuraAlternativaAggregatoModel)iter.next();
      EventoModel lEve = new EventoModel(lAggre.getEventoNotifica().getEvento());
      lUfficio = lEve.getDescrUfficioEmittente()+lEve.getDescrLuogoEmittente();
%>
      <table cellspacing=2 cellpadding=2 width="100%">
<%
      if (!(lUfficio.compareTo(prevUfficio)==0))
      {
        prevUfficio=lUfficio;
%>
		<tr>
          	<td class="lVerdeNB" colspan="9">&nbsp;</td>
        </tr>
        <tr>
        	<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
		    <%
		    	String descrTipoUfficio = lEve.getDescrUfficioEmittente();
		    	String codiceTipoUfficio = UtenteConnesso.getUfficioUtente().getCodTipoUfficio();
		    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
		    			"UDSM".equals(lEve.getCodTipoUfficioEmittente())) {
		    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
		    	}
		    %>
          	<td class="lVerdeNB" colspan="9">Elenco Procedimenti di : <%=descrTipoUfficio%> di <%=lEve.getDescrLuogoEmittente()%> iscritto da SIEP</td>
		</tr>
<%
      }
%>
      <tr>
        <td class="c" width="<%=wCol1%>"><font class="label">
<%
         if(lAggre.getMisuraAlternativa() != null &&
            lAggre.getMisuraAlternativa().getChiaveAnnoFascicoloSius() != null)
         {
%>
             <%=lAggre.getMisuraAlternativa().getChiaveAnnoFascicoloSius()%>
              /
             <%=lAggre.getMisuraAlternativa().getChiaveProgrFascicoloSius()%>
       <%}
         else
         {%>
            -
       <%}%>
         </font>
        </td>
<%if ( lEve.getCodMotivo().equals("1112")||
    lEve.getCodMotivo().equals("1113")||
    lEve.getCodMotivo().equals("1114")||
    lEve.getCodMotivo().equals("1115")||
    lEve.getCodMotivo().equals("1116")||
    lEve.getCodMotivo().equals("1117")||
    lEve.getCodMotivo().equals("1118")||
    lEve.getCodMotivo().equals("1119")) {
%>
        <td class="c" width="<%=wCol2%>"><font class="label">Decisione del Giudice dell'Esecuzione</font></td>
<%} else {
  %>
        <td class="c" width="<%=wCol2%>"><font class="label"><%=StringUtils.toStringJSP(lEve.getDescrProvvedimento())%></font></td>
<%} %>
        <td class="c" width="<%=wCol3%>"><font class="label"> - </font></td>
        <td class="c" width="<%=wCol4%>"><font class="label"><%=StringUtils.toStringJSP(lEve.getDescrTipoProvvedimento())%></font></td>
        <td class="c" width="<%=wCol5%>"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getDataEmissione(),"dd-MM-yyyy"),"-")%></font></td>
<%if ( lEve.getCodMotivo().equals("1114")||
    lEve.getCodMotivo().equals("1115")||
    lEve.getCodMotivo().equals("1116")||
    lEve.getCodMotivo().equals("1117")||
    lEve.getCodMotivo().equals("1118")||
    lEve.getCodMotivo().equals("1119")) {
%>
  <td class="c" width="<%=wCol6%>"><font class="label">
  <%=StringUtils.toStringJSP(lEve.getDescrProvvedimento())%>-<%=StringUtils.toStringJSP(lEve.getDescrMotivo())%>
  </font></td>
<%} else {
  %>
  <td class="c" width="<%=wCol6%>"><font class="label"><%=StringUtils.toStringJSP(lEve.getDescrMotivo())%></font></td>
<%} %>
<%
        // STUB 07/05/2008 - Rif. ASIR a8/rr/077 Aggiunto criterio di individuazione Liberazione Anticipata.
         //if( (lEve.getDescrMotivo()!= null && lEve.getDescrMotivo().trim().compareTo("Liberazione Anticipata")==0 )
          if ((lEve.getLicenzaLibAnticipata()!= null && lEve.getDescrMotivo().trim().compareTo("Liberazione Anticipata")==0 && lEve.getLicenzaLibAnticipata().getNumeroGiorni()!= null)
          && (lEve.getLicenzaLibAnticipata().getFlagConcesso()!= null && lEve.getLicenzaLibAnticipata().getFlagConcesso().equals("C") ) )
          {
            if( (lEve.getLicenzaLibAnticipata().getFlagElaborato() != null && lEve.getLicenzaLibAnticipata().getFlagElaborato().equals("S")))
            {
  %>
               <td class="c" width="<%=wCol7%>"><font class="label">Liberazione Anticipata Concessa in giorni</font>
                  <font class="campo"><%=StringUtils.toStringJSP(lEve.getLicenzaLibAnticipata().getNumeroGiorni())%></font>
               </td>
  <%
            }
            else if( lEve.getLicenzaLibAnticipata().getFlagElaborato() == null || lEve.getLicenzaLibAnticipata().getFlagElaborato().equals("N") ||lEve.getLicenzaLibAnticipata().getFlagElaborato().equals("E")  )
          {
  %>
              <td class="c" width="<%=wCol7%>"><font class="label">Liberazione Anticipata Da Concedere in giorni</font>
                <font class="campo"><%=StringUtils.toStringJSP(lEve.getLicenzaLibAnticipata().getNumeroGiorni())%></font>
              </td>
  <%
            }
         }
         else
         {
           if(lAggre.getTenori() != null && lAggre.getTenori().length>0 && lAggre.getTenori()[0]!= null && lAggre.getTenori()[0].getCodEsitoTenore() != null &&
             !lAggre.getTenori()[0].getCodEsitoTenore().equals(""))
           {
  %>
             <td class="c" width="<%=wCol7%>"><font class="label"><%=lAggre.getTenori()[0].getDescrEsitoTenore()%></font></td>
  <%
           }
           else
           { // AMBROS a8-rr-222
  
             if(lEve.getCodTipoProvvedimento() != null && lEve.getCodTipoProvvedimento().equals("03")
            && ("0284").equals(lEve.getCodMotivo()))
          //  && ("S").equals(lEve.getFlagDocumentoRegistrato()))
             {   
            if(lEve.getCodEsito().equals("-"))
            {
  %>            
              <td class="c" width="<%=wCol7%>"><font class="campo">Accolta </font></td>
  <%          }
                  else if(lEve.getCodEsito().equals("C"))
                  {               
  %>    
              <td class="c" width="<%=wCol7%>"><font class="campo">Accolta in Conformità</font></td>
  <%          }
                  else if(lEve.getCodEsito().equals("D"))
                  {               
  %>    
              <td class="c" width="<%=wCol7%>"><font class="campo">Accolta in Difformità</font></td>
  <%          }
                  else if(lEve.getCodEsito().equals("R"))
                  {               
  %>    
              <td class="c" width="<%=wCol7%>"><font class="campo">Rigetto </font></td>
  <%          }
                  else if(lEve.getCodEsito().equals("I"))
                  {               
  %>    
              <td class="c" width="<%=wCol7%>"><font class="campo">Inammissibile </font></td>
  <%          }
                  else if(lEve.getCodEsito().equals("U"))
                  {               
  %>    
              <td class="c" width="<%=wCol7%>"><font class="campo">Riunisce</font></td>
<%            }
            
              }
              else if( lEve.getCodTipoProvvedimento() != null && lEve.getCodTipoProvvedimento().equals("65")
                      && ("1132").equals(lEve.getCodMotivo()) && lEve.getDescrEsito() !=null)
              {%>               
              	<td class="c" width="<%=wCol7%>"><font class="label"><%=lEve.getDescrEsito()%></font></td>
<%            } else {
%>
		<td class="c" width="<%=wCol7%>"><font class="label">-</font></td>
<% }
           }
       }
%>

      <td class="c" width="<%=wCol8%>">&nbsp;
<%
      if (lEve.getFlagDocumentoRegistrato()!=null)
      {
          if (lEve.getFlagDocumentoRegistrato().compareTo("S")==0)
          {
  %>
            <img src="/images/TickRed.gif">
  <%
          }
          else if(lEve.getFlagDocumentoRegistrato().compareTo("A")==0)
          {
  %>
            <a class="cliccabile" href="javascript:chiama('<%=lEve.getIdEvento()%>');" title="ANNULLAMENTO">
            <font class="cRosso">ANNULLATO</font></a>
<%
          }
      }
%>
    </td>
<%
      lModificabile = lCodUfficioUtente.equalsIgnoreCase(lEve.getCodUfficioInserimento()) ? "SI" : "NO";
%>
    <td class="c" width="<%=wCol9%>">
<%
      if("A".equalsIgnoreCase(lEve.getFlagDocumentoRegistrato()))
      {%>-<%
      }
      else
      {
        if(lEve.getCodTipoProvvedimento().equals("57")){
          %>
        <jsp:include page="<%=ICostantiOrdineEsecuzione.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita"  value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lEve.getIdEvento()%>" />
          <jsp:param name="CampoIdEntitaProvv" value="campo" />
          <jsp:param name="ValoreIdEntitaProvv" value="<%=lEve.getFlagDocumentoRegistrato()%>" />
          <jsp:param name="TipoProvvedimento" value="<%=lEve.getCodTipoProvvedimento()%>" />
          <jsp:param name="MotivoEvento" value="<%=lEve.getCodMotivo()%>" />
          <jsp:param name="TipoEvento" value="<%=lEve.getCodTipoEvento()%>" />
          <jsp:param name="TemIdTemplate" value="<%=lEve.getTemIdTemplate()%>" />
          <jsp:param name="modalita" value="R" />
          <jsp:param name="docRegistrato" value="N" />
          <jsp:param name="Evento" value="NO" />
          <jsp:param name="Modificabile" value="SI" />
          <jsp:param name="EventoCancellareAnnullare" value="<%=lEve.getIdEvento()%>" />
        </jsp:include>
            <%        
        }else{
        	%>
        	<jsp:include page="<%=ICostantiMisuraAlternativa.PG_BUTTONS_SORVEGLIANZA%>">
          <jsp:param name="CampoIdEntita"  value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lEve.getIdEvento()%>" />
          <jsp:param name="CampoIdEntitaProvv" value="campo" />
          <jsp:param name="ValoreIdEntitaProvv" value="<%=lEve.getFlagDocumentoRegistrato()%>" />
          <jsp:param name="TipoProvvedimento" value="<%=lEve.getCodTipoProvvedimento()%>" />
          <jsp:param name="MotivoEvento" value="<%=lEve.getCodMotivo()%>" />
          <jsp:param name="TipoEvento" value="<%=lEve.getCodTipoEvento()%>" />
          <jsp:param name="TemIdTemplate" value="<%=lEve.getTemIdTemplate()%>" />
          <jsp:param name="FlagDocumentoRegistrato" value="<%=lEve.getFlagDocumentoRegistrato()%>" />
          <jsp:param name="EventoCancellareAnnullare" value="<%=lEve.getIdEvento()%>" />
          <jsp:param name="Modificabile" value="<%=lModificabile%>" />
        </jsp:include>
      <%}%>
    <%}%>
      </td>
    </tr>
  </table>
<%
    }

    //PARTE SIUS
    prevUfficio = "";
    int jPA =0;
    Vector lTenori = new Vector();
    while ( itx.hasNext())
    {
      FascicoloGPModel fascicolo = (FascicoloGPModel)itx.next();
      sUfficio = fascicolo.getFascicoloSiusModel().getDescrTipoUfficio()+fascicolo.getFascicoloSiusModel().getDescrComuneUfficio();
      // 24/09/2008 Impostazione criterio protezione dati SIUS agli uffici non di Sorveglianza.
      boolean lProteggiSius = true;
      if ((lCodTipoUfficioUtente.compareTo("UDS")==0   ||
               lCodTipoUfficioUtente.compareTo("TDS")==0 ) ||
             (!(lCodTipoUfficioUtente.substring(1,2).equals("D")) &&
                 SiusUtils.IsFascicoloSiusDefinito(fascicolo.getFascicoloSiusModel().getDescrStatoFascicolo()) ) )
        lProteggiSius = false;
      if (lProteggiSius == false ||
         (lProteggiSius == true && 
          fascicolo.getFascicoloSiusModel().getCodStatoFascicolo()!=null ))//&& 
          //fascicolo.getFascicoloSiusModel().getCodStatoFascicolo().trim().length()>1 && 
          //fascicolo.getFascicoloSiusModel().getCodStatoFascicolo().substring(0,2).compareTo("06")==0) )
      {
%>
        <table cellspacing=2 cellpadding=2 width="100%">
<%
        if (!(sUfficio.compareTo(prevUfficio)==0))
        {
          prevUfficio=sUfficio;
%>
          <tr>
            <td class="lVerdeNB" colspan="9">&nbsp;</td>
          </tr>
          <tr>
          <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
            <td class="lVerdeNB" colspan="9">Elenco Procedimenti di : <%=fascicolo.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getFascicoloSiusModel().getDescrComuneUfficio()%> iscritto da SIUS</td>
          </tr>
<%
        }
%>
        <tr>
          <td class="c" width="<%=wCol1%>">
            <font class="label">
<%
            if (lProteggiSius == false)
            {%>
            <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
              <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicolo.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>" Title="<%=fascicolo.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getFascicoloSiusModel().getDescrComuneUfficio()%> - Dettaglio Procedimento" >
                <%=fascicolo.getFascicoloSiusModel().getChiaveAnno()%>
                /
                <%=fascicolo.getFascicoloSiusModel().getChiaveProgr()%>
              </a>
          <%}else{%>
                <%=fascicolo.getFascicoloSiusModel().getChiaveAnno()%>
                /
                <%=fascicolo.getFascicoloSiusModel().getChiaveProgr()%>
          <%}%>
            </font>
          </td>
          <td class="c" width="<%=wCol2%>"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrPosGiuridica()%></font></td>
          <td class="c" width="<%=wCol3%>"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd-MM-yyyy"),"-")%></font></td>
<%        if (lProteggiSius && (fascicolo.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("0601")!=0 && 
                                fascicolo.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("0603")!=0 &&
                                fascicolo.getFascicoloSiusModel().getCodStatoFascicolo().trim().compareTo("-")!=0   ) )
        { 
%>
            <td class="c" width="<%=wCol4%>"><font class="label">-</font></td>
            <td class="c" width="<%=wCol5%>"><font class="label">-</font></td>
            <td class="c" width="<%=wCol6%>"><font class="label">-</font></td>
            <td class="c" width="<%=wCol7%>"><font class="label">-</font></td>
            <td class="c" width="<%=wCol8%>"><font class="label">-</font></td>
        <%}else{%>
          <td class="c" width="<%=wCol4%>"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font></td>
          <td class="c" width="<%=wCol5%>"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataRichiesta(),"dd-MM-yyyy"),"-")%></font></td>
          <td class="c" width="<%=wCol6%>"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrDefinizione()%></font></td>
          <td class="c" width="<%=wCol7%>"><font class="label">
            <%
              if (fascicolo.getFascicoloSiusModel().getCodStatoFascicolo().trim().compareTo("-")==0 &&
                  fascicolo.getFascicoloSiusModel().getDescrStatoFascicolo().trim().compareTo("01")==0)
              {%>
              Archiviato/Definito
            <%}else{%>
              <%=fascicolo.getGeneraleProcedimentoModel().getDescrTipoAtto()%>
            <%}%>
            </font>
        <%       
          if(fascicolo.getTenori() != null &&  fascicolo.getTenori().length>1)
          {
%>
            <a href="#1" onClick="return effettoTree(<%=jPA%>)"><img name="image<%=jPA%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Altri Esiti" ></a>
        <%}%>
          </td>
          <td class="c" width="<%=wCol8%>"><font class="label">
<%
          if(fascicolo.getGeneraleProcedimentoModel().getSezione() != null && fascicolo.getGeneraleProcedimentoModel().getSezione().compareTo("A")==0 )
            {%> <font class="cRosso">ANNULLATO</font> <%}
          else if(fascicolo.getGeneraleProcedimentoModel().getSezione() != null && fascicolo.getGeneraleProcedimentoModel().getSezione().compareTo("S")==0 )
            {%> <img src="/images/TickRed.gif"> <%}
          else
            {%>-<%}%>
          </font></td>
          <%}%>        
<%
          // STUB 24/02/2005 Bottone di dettaglio provvedimento.
          if (fascicolo.getFascicoloSiusModel().getSogIdSoggetto() != null )
          {
            // Se HashFlagEventoRegistratoSIUS non è vuoto controlla che il valore sia "S" per far vedere
            // il pulsante del dettaglio
            if(HashFlagEventoRegistratoSIUS != null  && !HashFlagEventoRegistratoSIUS.isEmpty())
            {
              Object v = HashFlagEventoRegistratoSIUS.get(fascicolo.getFascicoloSiusModel().getSogIdSoggetto());
              // 03/10/2011 Aggiunto Controllo NULL value.
              if(v!=null &&
                (!v.equals("N")) &&
                (lProteggiSius == false) )
              {
%>
                <td class="c" width="<%=wCol9%>">
                 <jsp:include page="<%=ICostantiProvvedimento.PG_BUTTONS_DETTAGLIO_PROV%>">
                     <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
                     <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getFascicoloSiusModel().getSogIdSoggetto()%>" />
                 </jsp:include>
                </td>
<%
              }
              else{
%>
                <td class="c" width="<%=wCol9%>">
                 &nbsp;
                </td>
<%
              }
            }
            else
            {
              // se HashFlagEventoRegistratoSIUS è vuoto fa vedere il pulsante per il dettaglio
              // Per gli uffici esterni alla sorveglianza occorre anche che il provvedimento sia definitorio.
              if (lProteggiSius == false)
              { 
%>
                <td class="c" width="<%=wCol9%>">
                  <jsp:include page="<%=ICostantiProvvedimento.PG_BUTTONS_DETTAGLIO_PROV%>">
                    <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
                    <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getFascicoloSiusModel().getSogIdSoggetto()%>" />
                  </jsp:include>
                </td>
            <%}else {%>
                <td class="c" width="<%=wCol9%>">
                  &nbsp;
                </td>
<%
              }
            }
          }
          else
          {
%>
            <td class="c" width="<%=wCol9%>">
            -
            </td>
<%
          }
%>
          </tr>
<%
          //Caricamento Altri Esiti.
          if (fascicolo!=  null && fascicolo.getTenori() != null && fascicolo.getTenori().length>1)
          {
            for(int j=0;j<fascicolo.getTenori().length;j++)
            {
              TenoreModel lTenMod = new TenoreModel(fascicolo.getTenori()[j]);
              lTenori.add(lTenMod);
            }
%>
            </table>
              <div id="elenco<%=jPA%>" style="display:none; width:100%;">
                <%@include file="/jsp/files/siap/sius/tenore/ListaTenori.jspf" %>
              </div>
<%
              lTenori.clear();
              jPA++;
          }
          else
          {
%>
            </table>
<%
          }
        } // End if x controllo protezione SIUS.
      }
%>
  </FORM>
  <br>
  </body>
</html>