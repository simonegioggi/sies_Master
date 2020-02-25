<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza" %>

<jsp:useBean id="uffici" scope="request" class="java.lang.String"/>
<jsp:useBean id="IDEvento" scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="sedetribunale" scope="request" class="java.lang.String"/>


<html>
<head>
  <title>[S.I.E.S.] - Gestione Ordine Esecuzione </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
  <script language="JavaScript">
    var desktop;
    function ListaComuniTds(formname,fieldname)
    {
    	var codTipoSede = document.LoadTrasferisciProvvedimentoLAlfano.<%=ICostantiNuovaIstanza.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>.value;
    	if(codTipoSede=="" || codTipoSede=='-')
  	    {
  	  		alert("Ufficio Destinatario è obbligatorio");       
  	    } else {
  	      var desktop;
  	      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname+"&typename="+codTipoSede, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	    }   
    }
    function  VerifyUfficioDest()
    {
      var ritorno = true;
      if ((document.LoadTrasferisciProvvedimentoLAlfano.<%=ICostantiNuovaIstanza.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>.value.length == 0)  || 
      		(document.LoadTrasferisciProvvedimentoLAlfano.<%=ICostantiNuovaIstanza.CAMPO_COD_LUOGO_DESTINATARIO%>.value.length == 0))
      {
        alert("Selezionare l'ufficio destinatario");
        ritorno = false;
      }
			var tipUff = document.LoadTrasferisciProvvedimentoLAlfano.<%=ICostantiNuovaIstanza.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>.value;
      document.LoadTrasferisciProvvedimentoLAlfano.tipoUfficioDestinatario.value = tipUff;
      var tipUff2 = document.LoadTrasferisciProvvedimentoLAlfano.tipoUfficioDestinatario.value;
      // alert("Tipo Uff2 = "+tipUff2);
      return ritorno;
    }
  </script>
</head>
  <body class="corpo">
  <form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadTrasferisciProvvedimentoLAlfano" onsubmit="document.forms[0].go.disabled=true">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        String lAction = new String();
        lAction = "siap.siep.ordineesecuzione.action.ActLoadConfermaTrasmissioneProvvedimento";
%>
        <font class="campo">Trasferimento <%=titolo%></font>
    	</td>

		<%-- BOTTONE DI STAMPA -->
			<td class="LBG">
    		<%--a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.ordineesecuzione.action.ActStampaTrasferimentoProvvedimento&IdEvento=<%=IDEvento%>&CodLuogoDestinatario=Javascript:document.LoadTrasferisciProvvedimentoLAlfano.<%=ICostantiNuovaIstanza.CAMPO_COD_LUOGO_DESTINATARIO%>.value&CodTipoUfficioDestinatario='Javascript:document.LoadTrasferisciProvvedimentoLAlfano.<%=ICostantiNuovaIstanza.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>.value'"-->
    		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.ordineesecuzione.action.ActStampaTrasferimentoProvvedimento&IdEvento=<%=IDEvento%>&CodLuogoDestinatario=Napoli&CodTipoUfficioDestinatario=UDS">
        	<img src="<%=IWebConstants.IMAGES_DIR%>print24.gif" onclick="Javascript:return VerifyUfficioDest();" alt="Stampa" width="24" height="24" border="0">
      	</a>
    	</td--%>

		<%-- BOTTONE DI STAMPA -->
		<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
			<jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActStampaLAlfanoLibero&autorita="+lCodTipoAutorita+"&fc="+fogliocomplementare+"&IdEvento="+lEve.getEvento().getIdEvento()%>"/>
		</jsp:include--%>


    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Destinatario </td >
        <td class="L">
         <select Title="Destinatario" name="<%=ICostantiNuovaIstanza.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" >
           <%=uffici%>
         </select>
        </td>
      </tr>
      <tr>
        <td class="l">Sede Destinatario </td><td class="L">
          <input title="Sede Destinatario"  value="<%=sedetribunale%>" type="text" name="<%= ICostantiNuovaIstanza.CAMPO_COD_LUOGO_DESTINATARIO %>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuniTds('LoadTrasferisciProvvedimentoLAlfano','<%= ICostantiNuovaIstanza.CAMPO_COD_LUOGO_DESTINATARIO %>');">
              <img src="/images/filefolder.gif" border=0>
            </a>
        </td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input name=go class=bottone  type="submit" value="Conferma" onclick="Javascript:return VerifyUfficioDest();">
        </td>
      </tr>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=IDEvento%>">
	    <input type="HIDDEN" name="tipoUfficioDestinatario" value="">
	    <input type="HIDDEN" name="sedeUfficioDestinatario" value="">
    </table>
  </form>

</body>
</html>