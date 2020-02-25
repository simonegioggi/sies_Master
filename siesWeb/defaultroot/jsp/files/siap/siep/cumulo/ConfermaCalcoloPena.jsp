<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>

<jsp:useBean id="PosizioneGiuridica"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />

<% 
	Collection tipologia = (Collection) request.getAttribute("tipologia");
%>

<head>
  <title> [S.I.E.S.] - Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  
  <script language="JavaScript">  
	  function Verify()
	  {
	  	  if (document.f.<%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>[document.f.<%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>.selectedIndex].value == '-')
	      {
	        alert("Il campo Tipologia è obbligatorio");
	        return false;
	      }
	  }
  </script>  
  
  
</head>

<body class="corpo">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name=f>
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.cumulo.action.ActRicalcoloPenaCumulo">
    <input type="hidden" name="BackToCumulo" value="1">
    <input type="hidden"  value="<%=PosizioneGiuridica.getIdPosizioneGiuridica()%>" name="<%=ICostantiPosizioneGiuridica.CAMPO_ID_POSIZIONE_GIURIDICA%>">
    
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Calcolo Pena Cumulo</font>
        </td>
      </tr>
    </table>
    
    <br>
    
    <table>
      <tr><td class=Titolo>Fascicolo Cumulante</td></tr>
      <tr>
        <td align=center><br>
          <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
        <br>
        </td>
      </tr>
      <tr>
        <td class=l>Posizione Giuridica : <font class="campo"><%=PosizioneGiuridica.getDescrPosizioneGiuridica()%></font></td>
      </tr>
    </table>
    
    <br>

	<table width="90%">
	   <tr>
	     <td class="Titolo" colspan=8> Provvedimento di esecuzione di pene concorrenti </td>
	   </tr>
	   <tr><td class="l" width=25%>Tipologia</td>
	     <td class="L" colspan="3">
	             <select Title="Autorita Esterna"  name="<%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>" >
<%
	          Iterator lIter = tipologia.iterator();
	          while(lIter.hasNext())
	          {
	            DecodificheModel lDecMod = (DecodificheModel)lIter.next();
%>
	            <option value="<%=lDecMod.getCodiceAlternativo()%>"/><%=lDecMod.getDescription()%>
<%
	          }
%>
	             </select>
	     </td>
	   </tr>
	</table>
  
	<br>
	
    <table>
      <tr>
        <td class=lNoBord>
          <br><br><input class="bottone" type=submit value="Conferma">
        </td>
      </tr>
    </table>
  </form>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");
  frmvalidator.setAddnlValidationFunction("Verify");
</script>

</body>
</html>

