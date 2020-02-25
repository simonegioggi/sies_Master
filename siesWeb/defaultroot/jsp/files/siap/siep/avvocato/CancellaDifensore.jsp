<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="f3b.util.StringUtils"%>

<jsp:useBean id="avvocati"       scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita"       scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaAvvocato"       scope="request" class="java.lang.String"/>
<jsp:useBean id="nome"    scope="request" class="java.lang.String"/>
<jsp:useBean id="cognome"    scope="request" class="java.lang.String"/>
<jsp:useBean id="foro"    scope="request" class="java.lang.String"/>


<html>
<head>
<title>[S.I.E.S.] - Gestione Avvocato </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript">
  var desktop;

 function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }


</script>

</head>
<%
AvvocatoModel lAvv = new AvvocatoModel();
lAvv=(AvvocatoModel)avvocati.get(0);


%>


		<body class="corpo">
		<table>
			<tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			<td class=LBG><font class="label">Funzione :</font>&nbsp;&nbsp;
 <%
			 AvvocatoModel lAvvocato = null;
			 String lAction = new String();
			 if( modalita.equals("M") )
			 {
			   lAction = "siap.siep.avvocato.action.ActCancellaDifensore";
			   lAvvocato=new AvvocatoModel();
%>

			<font class="campo">Cancella Difensore</font>
			 <%

			   }

			   %>
</td>
</tr>
</table>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadModificaAvvocato" onSubmit="javascript:document.LoadModificaAvvocato.conferma.disabled=true">

<table cellspacing=2 cellpadding=2>

		<tr>
				<td class="l" >Cognome </td>
				<td class="l" ><font class="campo"><%=lAvv.getCognome()%></font></td>
		</tr>
		<tr>
				<td class="l">Nome </td>
				<td class="l"><font class="campo"><%=lAvv.getNome()%></font></td>
		</tr>
 <tr>
        <td class="l">Luogo di  Nascita </td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(lAvv.getDescLuogoNascita())%></font>
        </td>
      </tr>

<tr>
				<td class="l"><font class="label">Data Nascita</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvv.getDataNascita(),"dd-MM-yyyy")) %>&nbsp;</font></td>
		</tr>
		<tr>
				<td class="l"><font class="label">Foro</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvv.getForo()) %></font></td>
		</tr>
		<tr>
				<td class="l"><font class="label">Indirizzo</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvv.getIndirizzo()) %></font>&nbsp;</td>
		</tr>
   <tr>
				<td class="l"><font class="label">Con Studio in</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvv.getDescComuneResidenza()) %></font>&nbsp;</td>
		</tr>
		<tr>
				<td class="l"><font class="label">Telefono</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvv.getTelefono()) %></font>&nbsp;</td>
		</tr>
		<tr>
				<td class="l"><font class="label">Fax</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvv.getFax()) %></font>&nbsp;</td>
		</tr>
		<tr>
				<td class="l"><font class="label">EMail</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvv.getEMail()) %></font>&nbsp;</td>
		</tr>
		<tr>
				<td class="l"><font class="label">Codice Fiscale</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvv.getCodiceFiscale()) %></font>&nbsp;</td>
		</tr>
    <tr>
				<td class="l"><font class="label">Sospeso fino al</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvv.getDataSospensione(),"dd-MM-yyyy"))%></font>&nbsp;</td>
		</tr>
   <tr>
				<td class="l"><font class="label">Radiato dal</font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvv.getDataRadiazione(),"dd-MM-yyyy"))%></font>&nbsp;</td>
		</tr>
<tr>
				<td class="l"><font class="label">Non in attività per </font></td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvv.getDescrNonAttivita())%></font>&nbsp;</td>
		</tr>
<tr>
 <td  class="l">Note</td>
   <td  class="L"><font class="campo">
     <%=StringUtils.toStringJSP(lAvv.getNote())%></font>
    &nbsp;</td>
</tr>

<tr><td>&nbsp;</td></tr>
    <tr>
        <td colspan=2>
		    <input type="Hidden" name="Action" value="<%=lAction%>">

        <input type="hidden" name="campoNome" value="<%=nome%>">
        <input type="hidden" name="campoCognome" value="<%=cognome%>">
        <input type="hidden" name="campoForo" value="<%=foro%>">
         <input type="hidden" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" value="<%=lAvv.getIdAvvocato()%>">

        <input class=bottone  type="submit" value="Conferma"  name="conferma" onclick="">
        </td>
    </tr>
  </table>

</form>
</body>
</html>