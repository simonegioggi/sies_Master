<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>

<jsp:useBean id="IDEvento" scope="request" class="java.lang.String"/>
<jsp:useBean id="sedemagsor" scope="request" class="java.lang.String"/>
<jsp:useBean id="MagSor" scope="request" class="java.lang.String"/>


<html>
<head>
  <title>[S.I.E.S.] - Misure Sicurezza - Richiesta Accertamento pericolosita Sociale  </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
  <script language="JavaScript">
    var desktop;
    // 02/12/2010 Lista Uffici della Sorveglianza ( solo UDS)
    function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
    function  VerifyUfficioDest()
    {
      var ritorno = true;
      if (document.LoadTrasferisciPericolo.<%=ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_NOTIFICA%>.value.length == 0)  
      {
	        alert("Selezionare l'ufficio destinatario");
	        document.LoadTrasferisciPericolo.<%=ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_NOTIFICA%>.focus();
	        ritorno = false;
      }

      return ritorno;
    }
  </script>
</head>
  <body class="corpo">
  <form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadTrasferisciPericolo" onsubmit="document.forms[0].go.disabled=true">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        String lAction = new String();
        lAction = "siap.siep.misurasicurezza.action.ActLoadConfermaTrasmissioneRichiestaAccertaPericoloSociale";
%>
        <font class="campo">Trasferimento Richiesta Accertamento pericolosità Sociale</font>
    	</td>

    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <table cellspacing=2 cellpadding=2>
      <tr>
      	<td class="L">Destinatario<font class=ob>(*)</font></td>
   		<td class="L" >
       		<select  Title="MDS" class="small" name="<%=ICostantiOrdineEsecuzione.CAMPO_TIPO_UFFICIO_UDS%>">
        		<%=MagSor%>
       		</select>
      	</td>
      </tr>
      <tr>
      	 <td class="l">Sede Destinatario </td>
      	 <td class="L">	
          <input title="Sede Destinatario" type="text" value="<%=sedemagsor%>" name="<%= ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_NOTIFICA%>"  maxlength="35" size="35">
            <a href="Javascript:ListaUfficiComuni('LoadTrasferisciPericolo','<%=ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_NOTIFICA %>',document.LoadTrasferisciPericolo.<%=ICostantiOrdineEsecuzione.CAMPO_TIPO_UFFICIO_UDS %>[document.LoadTrasferisciPericolo.<%=ICostantiOrdineEsecuzione.CAMPO_TIPO_UFFICIO_UDS %>.options.selectedIndex].value);">
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

    </table>
  </form>

</body>
</html>