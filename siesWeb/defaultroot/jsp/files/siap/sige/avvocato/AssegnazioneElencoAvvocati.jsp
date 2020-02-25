<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige" %>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="avvocato" scope="request" class="java.util.Vector" />
<jsp:useBean id="elenco" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="modificabile"     scope="request" class="java.lang.String"/>

<%
boolean avvocatoModificabile = (modificabile.length() == 0 || modificabile.equalsIgnoreCase("SI")) ? true : false;
%>

<html>
<head>
	<title>[S.I.E.S.] - Lista Comuni</title>
	<link rel="STYLESHEET" type="text/css" href="/css/style.css">
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>


	<script language="JavaScript">

function verify()
{
 //document.LoadRicercaAvvocato.tipo[0].checked=true;


}
  function VerifyChiamate(id)
{
if(id==1 || id==2)
{
if( document.LoadRicercaAvvocato.numeroDifensori.value=="2")
{

  if(document.LoadRicercaAvvocato.tipo[0].checked==false && document.LoadRicercaAvvocato.tipo[1].checked==false)
 {
    alert("Selezionare un difensore");
    return false;
  }

}else
{
if(document.LoadRicercaAvvocato.tipo.checked==false)
 {
    alert("Selezionare il difensore");
    return false;
  }
}
}
   if(id==1)
  {
          document.LoadRicercaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sige.avvocato.action.ActDeassegnaDifensore";

  }else if(id==2)
  {

          document.LoadRicercaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sige.avvocato.action.ActLoadSostituzioneDifensore";

  }else if(id==3)
  {
          document.LoadRicercaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sige.avvocato.action.ActLoadInserisciAssegnaAvvocato";

  } /*else if(id==4)
  {
          document.LoadRicercaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sige.reato.action.ActLoadInserisciReato";
  } */


}

	</script>

</head>

<body class=corpo onLoad="verify();">
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaAvvocato" >
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
<%if(!elenco.equals("S")){%>

  <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Assegnazione Difensore</font></td>
<%}else{%>

  <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Difensori</font></td>
<%}%>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
</tr>
  </table>

 <br><jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/><br>

 <Table width="100%">
  <tr>
  <td class=int >Cognome e Nome</td>
  <td class=int >Foro</td>
  <td class=int>Tipo Difensore</td>
  <td class=int >Data Designazione/Nomina</td>
  <td class=int >Selezione</td>
  </tr>
 <%
  	Iterator itx = avvocato.iterator();
 int i =0;
  	while ( itx.hasNext())
  	{


    	AvvocatoSigeModel lAvv = (AvvocatoSigeModel)itx.next();
	%>
	<tr>
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--td class=l><%=StringUtils.toStringJSP(lAvv.getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getNome(),"-")%></td--%>
        <td class=c><%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome()) + " " +  StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%></td>
        <td class=c><%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%></td>
        <td class=c><%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%></td>
        <input type="hidden" name="tipoDifensore" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>">

        <td class=c><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvv.getAvvocatoFascicoloSigeModel().getDataInizioValidita(),"dd-MM-yyyy"))%></td>
        <td class="c"><input type="radio" name="tipo" value="<%=lAvv.getAvvocato().getIdAvvocato()%>" >
        <jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_BUTTONS_AVVOCATO%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lAvv.getAvvocato().getIdAvvocato()%>" />
        </jsp:include>

        </tr>
	<%
i++;
	}
%>
	<tr>
        <input type="hidden" name="numeroDifensori" value="<%=avvocato.size()%>">
        <td>
<%if( avvocatoModificabile && (avvocato != null && avvocato.size() > 0)){%>

        <input class=bottone  type="submit" name="conferma" value="Deassegnazione" onclick="javascript:return VerifyChiamate(1);">
        </td>
        <td>
        <input class=bottone  type="submit" name="conferma" value="Sostituzione" onclick="javascript:return VerifyChiamate(2);">
        </td>
<%}%>
<%if((!elenco.equals("S")) && avvocatoModificabile){%>

        <td>
        <input class=bottone  type="submit" name="conferma" value="Assegnazione" onclick="javascript:return VerifyChiamate(3);">
        </td>
		<td colspan="2">&nbsp;</td>
<%}%>


    </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
 var frmvalidator  = new Validator("LoadRicercaAvvocato");

</script>

</body>
</html>