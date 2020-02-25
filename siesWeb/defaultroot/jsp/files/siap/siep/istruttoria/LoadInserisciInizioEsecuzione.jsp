<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>


<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="sentenza" scope="request" class="siap.siep.sentenza.model.SentenzaModel"/>


<jsp:useBean id="datairrevocabilita"   scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita"             scope= "request" class="java.lang.String" />

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Comunicazione Inizio Esecuzione</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

   function Verify()
   {

    if(document.LoadInserisciInizioEsecuzione.tipo[1].checked)
	{
    if(!document.LoadInserisciInizioEsecuzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled)
    {
      if (document.LoadInserisciInizioEsecuzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == '')
      {
         alert("Il Campo Autorità di destinazione è obbligatorio");
         return false;
      }
    } 

     
  if (document.LoadInserisciInizioEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciInizioEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciInizioEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
    if (document.LoadInserisciInizioEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciInizioEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciInizioEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

  var data_to_verify = document.LoadInserisciInizioEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciInizioEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciInizioEsecuzione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (!ControllaData(data_to_verify) )
  {
        alert('Data richiesta non valida');
    return false;
  }
      
    if(!CompareDate("<%=datairrevocabilita%>",data_to_verify))
    {
        alert("La Data richiesta del Documento non può essere Inferiore alla Data di Irrevocabilità della Sentenza");
        document.LoadInserisciInizioEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
    }
}

  }
  
  function radio()
  {
       var nodecancelleria =document.getElementById('cancelleria');
       var nodeistituto =document.getElementById('istituto');       
       if(document.LoadInserisciInizioEsecuzione.tipo[0].checked)
       {
         nodeistituto.style.display='none'; 
         nodecancelleria.style.display='block'; 
       }
       else
       {
         nodeistituto.style.display='block'; 
         nodecancelleria.style.display='none'; 
       }
  
   }

  </script>

  </head>

 <body class="corpo" onload="radio();">
 <FORM method="POST" name="LoadInserisciInizioEsecuzione" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoria.action.ActInserisciInizioEsecuzione">
   <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%=  evento.getIdEvento() %>">
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Dettagli Comunicazione Inizio Esecuzione</font>


      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>



<table width='100%' cellspacing=4 cellpadding=4>
  
	
<%
	String lData;
	lData = StringUtils.toStringJSP(DateUtils.getSysDate("dd-MM-yyyy"));%>
	<tr><td class="Titolo" colspan=3>Tipo Comunicazione  </td></tr>
	  <tr>
	    <td class="c" colspan='3'>
	     Comunicazione inizio esecuzione &nbsp;<input type="radio" id="tipo" name="tipo" value="IE" checked onclick="radio();">
	     &nbsp; Comunicazione esecutività sentenza &nbsp; <input type="radio" id="tipo" name="tipo" value="ES" onclick="radio();"> 
	    </td>
	   </tr>     
    <tr>
      <td width='30%' class="l">Data Emissione</td>
      <td class="L" colspan=5>
        <font class="campo"><%=lData%></font>&nbsp;
      </td>
    </tr>
</table>
<div id="cancelleria" style="display:none; float:left; position:relative; width:100%;">
<table width="100%">
    <tr>
       <td class="l">Autorita Destinatario</td>
       <!-- a7/rr/168 -->
      <td class="L" colspan=5>
      <font class="campo">Cancelleria&nbsp;
      <%if(sentenza.getDescrTipoAutoritaEmittente().toUpperCase().startsWith("SEZIONE") ||
    		sentenza.getDescrTipoAutoritaEmittente().toUpperCase().startsWith("PROCURA")||
    		sentenza.getDescrTipoAutoritaEmittente().toUpperCase().startsWith("CORTE")
    		){%>
      	della
      <%}else{%>
      	del
      	<%} %>
       	<%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%>
		&nbsp;di
       	<%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%>
        </font>
      </td>
     </tr>
     </table>
</div>
<div id="istituto" style="display:none; float:left; position:relative; width:100%;">
<table width='100%'>
	<tr>
	
	<td class="l" >Autorita Destinazione <font class=ob>(*)</font></td>
      <td class="l" colspan=5>
        <input readonly Title="Istituto" name="Comune" value="" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
          <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciInizioEsecuzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            <img src="/images/filefolder.gif" border=0>
          </a>
      </td>
     
     </tr>
</table>
</div>


<table width='100%'>
   <tr>
       <td class="lNoBord" colspan="2">
       <br><br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();"/>
       </td>
   </tr>


</table>
</form>


  </body>
</html>
  