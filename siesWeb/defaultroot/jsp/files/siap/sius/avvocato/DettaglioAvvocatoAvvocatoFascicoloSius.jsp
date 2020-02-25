<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sius.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoFascicoloSiusModel"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>


<jsp:useBean id="modalita"       scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAvvocato" scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocato"    scope="request" class="siap.sius.avvocato.model.AvvocatoSiusModel"/>
<jsp:useBean id="lIstMod"    scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>

<html>
<head>
<title>[S.I.E.S.] - GestioneAvvocato </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>

<script language="JavaScript">
var aForm=null
 function Verify()
  {
   alert("La funzione di Iscrizione Guidata è stata Interrotta");
   aForm=document.getElementById("Abbandona");
    Disabilita();
  }

 function DisabilitaAltro()
  {
   aForm=document.getElementById("AltroDif");
    Disabilita();
  }

function Disabilita()
  {
    if (aForm==null)
       aForm=document.getElementById("CapoImput");

    document.Abbandona.A.disabled = true;
    document.CapoImput.C.disabled = true;
    document.AltroDif.D.disabled = true;

   aForm.submit();
  }
</script>
</head>

<%
AvvocatoModel lAvvocato = new AvvocatoModel(avvocato.getAvvocato());
AvvocatoFascicoloSiusModel lAvvFascSius = new AvvocatoFascicoloSiusModel(avvocato.getAvvocatoFascicoloSiusModel());
%>

<body class="corpo">
<table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>

        <td class=LBG><font class="label">Funzione :</font>&nbsp;&nbsp;
          <font class="campo">Dettaglio Avvocato</font>
        </td>

        <td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.sius.avvocato.action.ActStampaAvvocato&<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>=<%=lAvvocato.getIdAvvocato()%>" >
            <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
          </a>
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
        </td>

        <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" />
             <jsp:param name="ValoreIdEntita" value="<%=lAvvocato.getIdAvvocato() %>" />
          </jsp:include>
        </td>
        <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
</table>

<br>
<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
<br>
<table cellspacing=2 cellpadding=2>
    <input type="hidden" name="idAvvocato" value="<%=lAvvocato.getIdAvvocato() %>">
		<tr>
				<td class="l"><font class="label">Cognome</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getCognome()) %></font></td>
		</tr>
		<tr>
				<td class="l"><font class="label">Nome</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getNome()) %>&nbsp;</font></td>
		</tr>
<tr>
				<td class="l"><font class="label">Luogo di Nascita</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getDescLuogoNascita()) %>&nbsp;</font></td>
		</tr>
<tr>
				<td class="l"><font class="label">Data di Nascita</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvvocato.getDataNascita(),"dd-MM-yyyy")) %>&nbsp;</font></td>
		</tr>
		<tr>
				<td class="l"><font class="label">Foro</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getForo()) %>&nbsp;</font></td>
		</tr>
		<tr>
				<td class="l"><font class="label">Indirizzo</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getIndirizzo()) %>&nbsp;</font></td>
		</tr>
<tr>
				<td class="l"><font class="label">Comune di Residenza</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getDescComuneResidenza()) %>&nbsp;</font></td>
		</tr>

		<tr>
				<td class="l"><font class="label">Telefono</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getTelefono()) %>&nbsp;</font></td>
		</tr>
		<tr>
				<td class="l"><font class="label">Fax</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getFax()) %>&nbsp;</font></td>
		</tr>
		<tr>
				<td class="l"><font class="label">EMail</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getEMail()) %>&nbsp;</font></td>
		</tr>
		<tr>
				<td class="l"><font class="label">Codice Fiscale</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getCodiceFiscale()) %>&nbsp;</font></td>
		</tr>
    <tr>
				<td class="l"><font class="label">Tipo</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getDescrTipo()) %>&nbsp;</font></td>
		</tr>
<%if(lAvvocato.getDescrTipo().equalsIgnoreCase("DI FIDUCIA") || lAvvocato.getDescrTipo().equalsIgnoreCase("D'UFFICIO")){%>
<tr>
<%if(lAvvocato.getDescrTipo().equalsIgnoreCase("DI FIDUCIA")){%>

       <td class="l"><font class="label">Data Nomina</font></td>
<%}%>
       <%if(lAvvocato.getDescrTipo().equalsIgnoreCase("D'UFFICIO")){%>

       <td class="l"><font class="label">Data Designazione</font></td>
<%}%>
       <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvvFascSius.getDataInizioValidita(),"dd-MM-yyyy")) %>&nbsp;</font></td>
  </tr>
<%}%>
<%if( lAvvocato.getDescrTipo().equalsIgnoreCase("D'UFFICIO")){%>
<%if(!lAvvFascSius.getDescrMotivoDesignazione().equals("-")){%>

 <tr>
       <td class="l"><font class="label">Motivo Designazione</font></td>
       <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvFascSius.getDescrMotivoDesignazione()) %>&nbsp;</font></td>
  </tr>
<%}%>

<%if(lAvvFascSius.getNote()!= null){%>

 <tr>
      <td class="l"><font class="label">Note</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvFascSius.getNote()) %>&nbsp;</font></td>
  </tr>
<%}%>

<%if(!lAvvFascSius.getDescrTipoAutorita().equals("-")){%>

<tr>
				<td class="l"><font class="label">Autorità per la notifica al Condannato </font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvFascSius.getDescrTipoAutorita()) %> di <%=StringUtils.toStringJSP(lAvvFascSius.getComuneTipoAutorita()) %>  </font>
				<font class="campo"><%=StringUtils.toStringJSP(lAvvFascSius.getIndirizzoTipoAutorita()) %>  </font></td>
</tr>
<%}%>
<%if(!lIstMod.getDescrTipoIstituto().equals("")){%>

<tr>
				<td class="l"><font class="label">Istituto Detenzione</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lIstMod.getDescrTipoIstituto()) %> di <%=StringUtils.toStringJSP(lIstMod.getDescrComune()) %> &nbsp;</font></td>
		</tr>
<%}%>
<%if(!lAvvFascSius.getDescrTipoAutoritaDif().equals("-")){%>

<tr>
				<td class="l"><font class="label">Autorità per la notifica al Difensore </font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvFascSius.getDescrTipoAutoritaDif()) %> di <%=StringUtils.toStringJSP(lAvvFascSius.getComuneTipoAutoritaDif()) %>  </font></td>

</tr>
<%}%>
<%}%>
</table>
	</body>
</html>