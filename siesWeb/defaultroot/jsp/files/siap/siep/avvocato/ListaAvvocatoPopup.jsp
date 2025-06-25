<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>

<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="avvocato" scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<html>
<head>
	<title>[S.I.E.S.] - Lista Avvocati</title>
	<link rel="STYLESHEET" type="text/css" href="/css/style.css">

	<script language="JavaScript">
 function controlla(id)
  {


         if(id=="S")
         {
          alert("Il difensore risulta sospeso");
          return false;
         }
         if(id=="R")
         {
          alert("Il difensore risulta radiato");
          return false;
         }
         if(id=="A")
         {
          alert("Il difensore risulta non in attività");
          return false;
         }
         if(id=="B")
         {
          return true;
         }
 }

 function avvocati()
    {
      if ("<%=avvocato.size()%>" == 0) 
      {		// 20210610 MEV_21 in caso di chiusura infruttuosa della ricerca Avv. su SIES, si chiude la popup di ricerca.
        alert('Attenzione! Nessun Difensore trovato.');
        window.parent.close();
      }
      if ("<%=avvocato.size()%>" == 200)
        alert('Attenzione! Visualizzati solo i primi 200 Difensori individuati. Perfezionare la ricerca!');
    }

	function insertIT(id,cognome,nome,foro,stato)
	{
     var flag=controlla(stato);
     var filtro='<%=request.getParameter("filtro")%>';
     
     if(flag)
     {
         if(filtro ==  "completo")
         {
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO%>.value=id;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value=cognome;
          if(nome == "-")
          {
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value="";
          }else
          {
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value=nome;
          }
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FORO%>.value=foro;
         }
         
         if(filtro ==  "parziale")
         {
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO_PRESENTANTE%>.value=id;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_NOME_COGNOME_AVVOCATO%>.value=cognome+" "+nome;
         }
         
          window.parent.close();
         
      }
	}
	</script>

</head>

<body class=corpo onload ="avvocati();">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Avvocati</font></td>
    </tr>
  </table>
<form name="f">
<%if(avvocato.size()>0){%>
 <Table width="100%">
  <tr>
  <td class=int  width=40%>Nome</td>
  <td class=int  width=10%>Foro</td>
  <td class=int  width=40%>Indirizzo</td>

  <% if (! modalita.equals("NoPop"))
  { %>
    <td class=int width=10%>Seleziona</td>
  <% } %>
  </tr>
 <%
  	Iterator itx = avvocato.iterator();

  	while ( itx.hasNext())
  	{
    	AvvocatoModel lAvv = (AvvocatoModel)itx.next();
	%>
	<tr>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getCognome()) + " " +  StringUtils.toStringJSP(lAvv.getNome())%></td>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getForo())%></td>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getIndirizzo()) + " - " + StringUtils.toStringJSP(lAvv.getDescComuneResidenza())%></td>
<%
String stato=null;
if(lAvv.getDataSospensione()!= null)
{
     stato="S";

}else if(lAvv.getDataRadiazione()!= null)
{
     stato="R";

}else if(!lAvv.getCodNonAttivita().equals("-"))
{
     stato="A";

}else
{
      stato="B";
}

%>
        <% if (! modalita.equals("NoPop"))
        { %>
        <td class=c><a href="Javascript:insertIT('<%=lAvv.getIdAvvocato()%>','<%=StringUtils.cStrForJS(lAvv.getCognome())%>','<%=StringUtils.cStrForJS(lAvv.getNome())%>','<%=StringUtils.cStrForJS(lAvv.getForo())%>','<%=StringUtils.cStrForJS(stato)%>');"><img align="middle" src="/images/fileselected.gif" border=0></a></td>
        <% } %>
        </tr>
	<%
	}
%>

</table>
<%}%>
</form>
</body>
</html>