<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.rinnovo.action.ICostantiRinnovo" %>
<%@ page import="siap.siep.rinnovo.model.RinnovoModel" %>
<%@ page import="siap.siep.verbale.model.VerbaleModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<jsp:useBean id="rinnovo" scope="request" class="java.util.Vector" />
<jsp:useBean id="notifica" scope="request" class="java.lang.String" />

<html>
<head>
	<title>[S.I.E.S.] - Elenco Omesse Notifiche</title>
	<link rel="STYLESHEET" type="text/css" href="/css/style.css">
</head>

<body class="corpo">
 <table>
    <tr>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Elenco Omesse Notifiche</font>
      </td>
    </tr>
 </table>
 <Table width="100%">
<% 	Iterator itx = rinnovo.iterator();
    while ( itx.hasNext())
    {
     RinnovoModel lRinMod = (RinnovoModel)itx.next();
     if( notifica != null && notifica.equals("FP"))
     {
      if(lRinMod != null && lRinMod.getCodTipoRinnovo() != null && lRinMod.getCodTipoRinnovo().equals("R"))
       {%>
	     <tr>
        <td class=l>Data Rinnovo Notifica</td>
        <td class=l><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRinMod.getDataRinnovo(),"dd-MM-yyyy"))%></font>&nbsp;</td>
       </tr>
	     <tr>
         <td class=l>Autorità incaricata delle ricerche</td>
         <td class=l><font class="campo"><%=StringUtils.toStringJSP(lRinMod.getDescrTipoAutoritaRinnovo())%></font></td>
         <td class=l>Luogo</td>
         <td class=l><font class="campo"><%=StringUtils.toStringJSP(lRinMod.getDescrLuogoRinnovo())%></font></td>
      </tr>
     <%if(lRinMod.getNote() != null)
      {%>
	     <tr>
        <td class=l>Indirizzo</td>
        <td class=l><font class="campo"><%=StringUtils.toStringJSP(lRinMod.getNote())%></font>&nbsp;</td>
       </tr>
    <%}%>
	    <tr>
       <td> &nbsp;</td>
      </tr>
<%    }
    }else  if( notifica != null && notifica.equals("UG"))
      {
       if(lRinMod != null && lRinMod.getCodTipoRinnovo() != null && lRinMod.getCodTipoRinnovo().equals("A"))
       {%>
	       <tr>
          <td class=l>Data Rinnovo Notifica</td>
          <td class=l><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRinMod.getDataRinnovo(),"dd-MM-yyyy"))%></font>&nbsp;</td>
         </tr>

	       <tr>
           <td class=l>Autorità incaricata delle ricerche</td>
           <td class=l><font class="campo"><%=StringUtils.toStringJSP(lRinMod.getDescrTipoAutoritaRinnovo())%></font></td>
           <td class=l>Luogo</td>
           <td class=l><font class="campo"><%=StringUtils.toStringJSP(lRinMod.getDescrLuogoRinnovo())%></font></td>
        </tr>
     <%}else if(lRinMod != null && lRinMod.getCodTipoRinnovo() != null && lRinMod.getCodTipoRinnovo().equals("N"))
        {%>
	       <tr>
          <td class=l>Data Rinnovo Notifica</td>
          <td class=l><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRinMod.getDataRinnovo(),"dd-MM-yyyy"))%></font>&nbsp;</td>
         </tr>
		     <tr>
          <td class=l>Ufficiali Giudiziari</td>
          <td class=l><font class="campo"><%=StringUtils.toStringJSP(lRinMod.getDescrLuogoRinnovo())%></font></td>
         </tr>
<%      }
      if(lRinMod.getNuovoLuogoNotifica() != null)
       {%>
	      <tr>
         <td class=l>Luogo Nuova Notifica</td>
         <td class=l><font class="campo"><%=StringUtils.toStringJSP(lRinMod.getNuovoLuogoNotifica())%></font>&nbsp;</td>
        </tr>
    <% }%>
	       <tr>
          <td> &nbsp;</td>
         </tr>
   <% }
    }
%>
</table>
</body>
</html>