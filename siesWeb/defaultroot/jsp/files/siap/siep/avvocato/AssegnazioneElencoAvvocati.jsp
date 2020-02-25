<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="avvocato" scope="request" class="java.util.Vector" />
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>

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
<%if(lTipoFunzione.equals(""))
{%>
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
<%}%>
   if(id==1)
  {
          document.LoadRicercaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.avvocato.action.ActDeassegnaDifensore";

  }else if(id==2)
  {

          document.LoadRicercaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.avvocato.action.ActLoadSostituzioneDifensore";

  }else if(id==3)
  {
          document.LoadRicercaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.avvocato.action.ActLoadInserisciAssegnaAvvocato";

  }else if(id==4)
  {
          document.LoadRicercaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.reato.action.ActLoadInserisciReato";
  }


}

	</script>

</head>

<body class=corpo onLoad="verify();">
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaAvvocato" >
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Assegnazione Difensore</font></td>
    </tr>
  </table>

 <BR>
 <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
 <BR>

 <Table width="100%">
  <tr>
  <td class=int >Cognome e Nome</td>
  <td class=int >Foro</td>
  <td class=int>Tipo Difensore</td>
  <td class=int >Data Designazione/Nomina</td>
<%if(lTipoFunzione.equals(""))
{%>
  <td class=int >Selezione</td>
<%}%>
  </tr>
 <%
  	Iterator itx = avvocato.iterator();
 int i =0;
  	while ( itx.hasNext())
  	{


    	AvvocatoModel lAvv = (AvvocatoModel)itx.next();
	%>
	<tr>
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--td class=l><%=StringUtils.toStringJSP(lAvv.getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getNome(),"-")%></td--%>
        <td class=c><%=StringUtils.toStringJSP(lAvv.getCognome()) + " " +  StringUtils.toStringJSP(lAvv.getNome())%></td>
        <td class=c><%=StringUtils.toStringJSP(lAvv.getForo())%></td>
        <td class=c><%=StringUtils.toStringJSP(lAvv.getDescrTipo())%></td>
        <input type="hidden" name="tipoDifensore" value="<%=StringUtils.toStringJSP(lAvv.getDescrTipo())%>">

        <td class=c><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvv.getDataInizioValidita(),"dd-MM-yyyy"))%></td>
<%if(lTipoFunzione.equals(""))
{%>
        <td class="c"><input type="radio" name="tipo" value="<%=lAvv.getIdAvvocato()%>" >
<%}%>
        </tr>
	<%
i++;
	}
%>
<tr><td>&nbsp;</td></tr>

    <tr>
        <input type="hidden" name="numeroDifensori" value="<%=avvocato.size()%>">
<%if(lTipoFunzione.equals(""))
{%>
        <td>
        <input class=bottone  type="submit" name="conferma"  value="Dismissione Mandato" onclick="javascript:return VerifyChiamate(1);"> 
        </td>
        <td>
        <input class=bottone  type="submit" name="conferma" value="Sostituzione" onclick="javascript:return VerifyChiamate(2);">
        </td>
<%}%>
        <td>
        <input class=bottone  type="submit" name="conferma" value="Assegnazione" onclick="javascript:return VerifyChiamate(3);">
        </td>
<%if(!lTipoFunzione.equals(""))
{%>
      <td colspan=2>
        <input type="submit"  class="bottone"  name="CAPO IMPUTAZIONE" value="Capo Imputazione" onClick="javascript:return VerifyChiamate(4);">
      </td>
<%}%>
  <input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">
    </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
 var frmvalidator  = new Validator("LoadRicercaAvvocato");

</script>

</body>
</html>