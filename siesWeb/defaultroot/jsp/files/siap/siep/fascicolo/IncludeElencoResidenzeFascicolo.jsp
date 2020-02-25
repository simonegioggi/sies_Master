<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.residenza.model.ResidenzaModel"%>
<%@ page import="siap.sico.residenza.action.ICostantiResidenza"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="residenze" scope="request" class="java.util.Vector" />
<jsp:useBean id="soggetto" scope="session" class="siap.sico.soggetto.model.SoggettoModel" />


 <% if (residenze != null && residenze.size() > 0 )
 {	%>	
	   <table>
	    <tr>
	      <td class="int">Indirizzo</td>
	      <td class="int">CAP</td>
	      <td class="int">Luogo</td>
	      <td class="int">Comune Estero</td>
	      <td class="int">Stato</td>
	      <td class="int" width=5%>Azioni</td>
	    </tr>
	<%
	  Iterator itx = residenze.iterator();
	  while ( itx.hasNext())
	  {
	    ResidenzaModel residenza = (ResidenzaModel)itx.next();
	%>
	
	    <tr>
	      <td class="l"><%=StringUtils.toStringJSP(residenza.getIndirizzo(),"-")%></td>
	      <td class="l"><%=StringUtils.toStringJSP(residenza.getCap(),"-")%></td>
	      <td class="l"><%=StringUtils.toStringJSP(residenza.getDescrComune(),"-")%></td>
	      <td class="l"><%=StringUtils.toStringJSP(residenza.getDescComuneEstero())%></td>
	      <td class="l"><%=StringUtils.toStringJSP(residenza.getDescrStato(),"-")%></td>
	      <td class="c">
	<%
	      String modificabile = "";
	      if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(soggetto.getCodUfficioInserimento()))
	         {modificabile = "SI";}
	      else
	         {modificabile = "NO";}
	%>
	
	 <table>
    <tr>

              <td>
             	     <a href="Javascript:insertIT('<%=residenza.getIdResidenza()%>','<%=StringUtils.cStrForJS(residenza.getIndirizzo())%>','<%=StringUtils.cStrForJS(residenza.getCap())%>','<%=StringUtils.cStrForJS(residenza.getDescrComune())%>','<%=StringUtils.toStringJSP(residenza.getDescComuneEstero())%>','<%=StringUtils.cStrForJS(residenza.getCodStato())%>');">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                </a>

            
              </td>


    </tr>
  </table>

	<%
	  }
	%>
	    </table>
  	<%
	  }
	%>  
    
  