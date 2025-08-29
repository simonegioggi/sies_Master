<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.rinnovo.action.ICostantiRinnovo" %>
<%@ page import="siap.siep.rinnovo.model.RinnovoModel" %>


<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"  />
<jsp:useBean id="soggetto"  scope="session" class="siap.sico.soggetto.model.SoggettoModel"  />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>

<jsp:useBean id="ordineIngiunzione" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="notifica"          scope="request" class="siap.siep.notifica.model.NotificaModel"  />
<jsp:useBean id="listaSolleciti"      scope="request" class="java.util.Vector"  />

<jsp:useBean id="tipoAutoritaAltra" scope="request" class="java.lang.String"  />

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
// LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

if (lPosizione == null)
	lPosizione = new PosizioneGiuridicaModel();

if (lAltraCausa == null)
	lAltraCausa = new AltraCausaModel();

int contaRinnovi = listaSolleciti.size();
int contaDaValidare = 0;
for (int i = 0; i < listaSolleciti.size(); i++) {
	RinnovoModel lRinnovo = (RinnovoModel) listaSolleciti.elementAt(i);
  	if (lRinnovo.getFlagDocumentoRegistrato() == null 
       		|| lRinnovo.getFlagDocumentoRegistrato().equals("N") )
    	contaDaValidare++; 
}
String strDaValidare = "";
if (contaDaValidare > 0)
	strDaValidare = " (da validare "+contaDaValidare+")";
%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Ordine Ingiunzione </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>  
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
    <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }    
    
      function ListaSolleciti(){
        var nodeListaSolleciti = document.getElementById('divListaSolleciti');
        if ( nodeListaSolleciti.style.display=='block')
          nodeListaSolleciti.style.display='none';
        else
          nodeListaSolleciti.style.display='block';
      }
    
      function CancellaSollecito(idRinnovo){
        if (window.confirm("Confermi l'eliminazione del Sollecito?")) {
          document.cancellaSollecito.<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>.value = idRinnovo;
          document.cancellaSollecito.submit();
        }
      }
      
      function downloadStampaSollecito (idRinnovo)
      {
        var lAzione = "<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadDocumentoRinnovoRicercheOIPP";
        var parametri = lAzione+"&<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>="+idRinnovo;
        stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
      }
      
      function Verify()
      {
        if (document.LoadInserisciSollecitiOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value.length==1)
          document.LoadInserisciSollecitiOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value='0'+document.LoadInserisciSollecitiOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value;
        if (document.LoadInserisciSollecitiOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value.length==1)
          document.LoadInserisciSollecitiOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value='0'+document.LoadInserisciSollecitiOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value;

        var data_to_verify = document.LoadInserisciSollecitiOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value+'-'+document.LoadInserisciSollecitiOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value+'-'+document.LoadInserisciSollecitiOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data Sollecito non valida');
          document.LoadInserisciSollecitiOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.focus();
          return false;
        }

        if (document.LoadInserisciSollecitiOIPP.<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO%>.value=='-') {
          alert("Indicare l'Organo da sollecitare");
          document.LoadInserisciSollecitiOIPP.<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO%>.focus();
          return false; 
        }

        if (document.LoadInserisciSollecitiOIPP.<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO%>.value.length==0) {
          alert("Indicare il luogo");
          document.LoadInserisciSollecitiOIPP.<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO%>.focus();
          return false; 
        }

        return true;
      }
      
      function gestisciCampi(){
    	  <% if (contaDaValidare>0) { %>
    	  $('#divInserimento').find("input, select, textarea").attr('disabled','disabled');
    	  $('#divInserimento').find("img").hide();
    	  <% } %>
      }
      
      
    </script>
  </head>
  
<body class="corpo" onLoad="gestisciCampi();">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Solleciti&nbsp;</font>
      </td>
      <td class="LBG">
        <a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActLoadInserisciSollecitiOIPP">
          <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>      
      </td>         
    </tr>
  </table>
  <br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="cancellaSollecito">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActCancellaSollecitiOIPP">
  <input type="HIDDEN" name="<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>" value="">
</form>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciSollecitiOIPP">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActInserisciSollecitiOIPP">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=ordineIngiunzione.getEvento().getIdEvento()%>">
  <input type="hidden" name="<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>" value="<%=notifica.getIdNotifica()%>">

  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan="5">
        <font class="campo">
        <%
          if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
        %>
          DETENUTO PER ALTRA CAUSA - <%=lAltraCausa.getDescrTipoPosGiuridica()%>
        <% } else { %>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
        <% } %>
        </font>
        <input type="HIDDEN" title="Codice Posizione" 
               value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" 
               name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"   >
      </td>
    </tr>
  </table>
  
  <table>
    <tr>
      <td class="L" colspan="5">
        <font class="campo">
              <%=ordineIngiunzione.getEvento().getDescrTipoProvvedimento()%>
              &nbsp;
              <%=ordineIngiunzione.getEvento().getDescrMotivo()%>
              &nbsp;emesso in data&nbsp;
              <%=DateUtils.getDateToString(ordineIngiunzione.getEvento().getDataEmissione(), "dd-MM-yyyy")%>
        </font>
      </td>
    </tr>
  </table>

<br>


<!--  
  <table>
    <tr>
      <td class="LGB">
        <a href="Javascript:ListaSolleciti();">Elenco Solleciti Precedenti (<%=contaRinnovi%>)<%=strDaValidare%></a>
      </td>
    </tr>
  </table>
-->

<% if (contaDaValidare > 0) {  %>
  <table>
    <tr>
      <td class="l" style="color:red;">
        Attenzione, esiste un sollecito ancora da validare
      </td>
    </tr>
  </table>
<% } %>  


  
  <div id="divListaSolleciti" style="width: 100%; display:block;" >
    <table width="100%">
      <tr>
        <td class="int">Tipo Sollecito</td>
        <td class="int">Data Sollecito</td>
        <td class="int">Organo da Sollecitare </td>
        <td class="int">Validato</td>
        <td class="int" style="width:50px;">Azioni</td>
      </tr>
      <% if (contaRinnovi == 0) {  %>
      <tr>
        <td class="L" colspan="5">Nessun Sollecito presente per il provvedimento selezionato</td>
      </tr>
      <% } %>       
      
      <% 
      for (int i = 0; i < listaSolleciti.size(); i++) 
      {
        RinnovoModel lRinnovo = (RinnovoModel) listaSolleciti.elementAt(i);
        String lActDettaglio = "siap.siep.sanzionesostitutiva.action.ActLoadDettaglioSollecitiOIPP";
        lActDettaglio += "&"+ICostantiRinnovo.CAMPO_ID_RINNOVO+"="+lRinnovo.getIdRinnovo();
      %>
      <tr>
        <td class="c"><%=StringUtils.toStringJSP(lRinnovo.getDescrTipoRinnovo())%></td>
        <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRinnovo.getDataRinnovo(),"dd-MM-yyyy"),"")%></td>
        <td class="c"><%=StringUtils.toStringJSP(lRinnovo.getDescrTipoAutoritaRinnovo())%> di <%=StringUtils.toStringJSP(lRinnovo.getDescrLuogoRinnovo())%></td>
        <td class="c">
        <% if ("S".equals(lRinnovo.getFlagDocumentoRegistrato())) { %>
          <img  alt="Validato" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0"></a>
        <% } else { %>
        &nbsp;<font style="color:red;">(da validare)</font>
        <% } %>
        </td>
        <td class="r" nowrap>
          <% if (!"S".equals(lRinnovo.getFlagDocumentoRegistrato())) { %>
          <a href="Javascript:CancellaSollecito('<%=lRinnovo.getIdRinnovo()%>')">
            <img  alt="Cancella" src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border="0"></a>
          <% } %>
          <% if ("S".equals(lRinnovo.getFlagDocumentoRegistrato()) || "N".equals(lRinnovo.getFlagDocumentoRegistrato())) { %>
          <a href="Javascript:downloadStampaSollecito('<%=lRinnovo.getIdRinnovo()%>')">
            <img  alt="Stampa" src="<%=IWebConstants.IMAGES_DIR%>print.gif" border="0"></a>
          <% } %>
          <a  href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>">
            <img  alt="Dettaglio" src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" border="0"></a>
        </td>
      </tr>
      <% } %>
    </table>
  </div>
  
  <br>


<div id="divInserimento">
  <table  width=100%>
    <tr>
      <td class="c">Sollecito Autorità di Polizia &nbsp;<input type="radio" name="TipoSollecito" value="SP" checked >
                    Sollecito Ufficiali Giudiziari &nbsp; <input type="radio" name="TipoSollecito" value="SU">
      </td>
    </tr>
  </table>
  
<%
// =====================================================================
//  
//  
// =====================================================================
%>
  <table width="100%">
    <tr>
      <td class="l">Data Sollecito <font class="ob">(*)</font></td>
      <td class="l" colspan="3">
        <input type="text" size="2" maxlength="2" title="Giorno Risposta"
               value="<%=DateUtils.getSysDate("dd")%>"
               name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
        -
        <input type="text" size="2" maxlength="2" title="Mese Risposta" 
               value="<%=DateUtils.getSysDate("MM")%>"  
               name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO %>"  
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" size="4" maxlength="4" title="Anno Risposta" 
               value="<%=DateUtils.getSysDate("yyyy")%>"          
               name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO %>"  
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    
    <tr>
      <td class="l">Organo da Sollecitare <font class="ob">(*)</font></td>
      <td class="l" colspan="3">
        <select title="TipoAutorita" name="<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO%>">
          <%=tipoAutoritaAltra%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Luogo <font class="ob">(*)</font></td>
      <td class="L">
        <input title="Luogo" type="text" name="<%=  ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO%>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciSollecitiOIPP','<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO %>');">
          <img src="/images/filefolder.gif" border=0></a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiRinnovo.CAMPO_NOTE%>" cols=30></textarea>
      </td>
    </tr>
  </table>


  <table>
    <tr>
      <td>
        <input class="bottone" type="submit" name="INSERISCI" value="Conferma">
      </td>
    </tr>
  </table>
</div>  
</form>


  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciSollecitiOIPP");  
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>





