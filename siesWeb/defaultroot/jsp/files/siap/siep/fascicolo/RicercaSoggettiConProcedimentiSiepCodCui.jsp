<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--@ page import="java.util.Date" --%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRicerche" scope="request" class="java.lang.String" />
<jsp:useBean id="StrClassiFascicolo" scope="request" class="java.lang.String" />
<jsp:useBean id="fromDetail" scope="request" class="java.lang.String" />


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Soggetti con Procedimenti </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">
  <FORM method="POST" name="RicercaSoggettiConProc" action="<%=IWebConstants.PG_MAIN%>">
  <!-- Campi valorizzati in seguito -->
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value="">
  <input type="hidden" name="dett" value="<%=fromDetail%>">  

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Soggetti con Procedimenti RICERCA CODICE CUI</font></td> 
   	
  <td class="LBG">
        <a href="javascript:history.go(-1);">
         <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>

    
<%
       FascicoloSiepModel fascicoloUno = (FascicoloSiepModel) fascicoli.get(0);
%>
   </tr>
  </table>

  <br>
	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

<br>

  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="int">Cognome e Nome</td>
      <td class="int">Data di nascita</td>
      <td class="int">Eta' Presunta</td>
      <td class="int">Luogo Nascita</td>
      <td class="int">Paternità</td>
      <td class="int">Maternità</td>
      <td class="int">Cod. CUI</td>

      <td class="int">Procedimenti</td>
      <td class="int">Dettaglio</td>
    </tr>
<%
    Iterator itx = fascicoli.iterator();
    String sUfficio = "Procura";
    while ( itx.hasNext())
    {
      	FascicoloSiepModel fascicolo = (FascicoloSiepModel)itx.next();
%>
    <tr>
    	<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
    	<td class="c"><font class="label"><%=fascicolo.getSoggetto().getCognome()%>&nbsp;<%=fascicolo.getSoggetto().getNome()%></font></td>
    	<td class="c"><font class="label">
<%      if ((fascicolo.getSoggetto().getDataNascita())==null || fascicolo.getSoggetto().getDataNascita().equals(""))
        {%>-<%}
        else
        {%>
          	<%=DateUtils.getDateToString(fascicolo.getSoggetto().getDataNascita(),"dd-MM-yyyy")%>
        <%}%>
        </font></td>
        
        <td class=c>
	      <font  class=c>
			<% 
	        if(fascicolo.getSoggetto().getEtaPresuntaAnni() != null || fascicolo.getSoggetto().getEtaPresuntaMesi() != null)
	        { 
			%>
	        	<%="Anni "+StringUtils.toStringJSP(fascicolo.getSoggetto().getEtaPresuntaAnni(), "0")+"  Mesi "+StringUtils.toStringJSP(fascicolo.getSoggetto().getEtaPresuntaMesi(), "0")%>
			<%
	        }
	        else
	        {
			%>
	          	<%="-"%>	
			<%
	        }
			%>
	      </font>
      </td>
      
      <% if (fascicolo.getSoggetto().getDescrComuneNascita().compareTo("-")==0){%>
        <td class="l">
            <%if(fascicolo.getSoggetto().getDescComuneNascitaEstero().length()>0) 
              {%>
              
        		<%=fascicolo.getSoggetto().getDescComuneNascitaEstero()%>&nbsp;
        	    <%if(fascicolo.getSoggetto().getDescrStatoNascita().length()>1) 
              	  {%>
        		  	(<%=fascicolo.getSoggetto().getDescrStatoNascita().toUpperCase()%>)		
        		<%} %>
        	<%} %>	
        	&nbsp; 
        </td>
      <% }else {%>
        <td class="l"><%=fascicolo.getSoggetto().getDescrComuneNascita()%> (<%=fascicolo.getSoggetto().getCodProvinciaNascita()%>)&nbsp;</td>
      <%}%>
      <td class="c"><font class="label"><%=fascicolo.getSoggetto().getPaternita()%></font>&nbsp;</td>
      <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      <td class="c"><font class="label"><%=fascicolo.getSoggetto().getCognomeMadre()%>&nbsp;<%=fascicolo.getSoggetto().getNomeMadre()%></font>&nbsp;</td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getSoggetto().getCodAfis())%></font>&nbsp;</td>
      <td class="c"><font class="label"><%=fascicolo.getNumFascicoli()%></font></td>
      
      <td class="c" style="text-align:left"> &nbsp;
  
  <% 
        //======================================================================
        // Se Ha solo 1 procedimento c'è il Dettaglio -
        // Se ha + procedimenti c'è elenco procedimenti
        //======================================================================

  %>
       		<input type ="hidden" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value="fascicolo.getSogIdSoggetto()">
          	<jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           		<jsp:param name="CampoAzioneChiamante" value="NomeAzione" />
            	<jsp:param name="ValoreAzioneChiamante" value="<%=AzioneChiamante%>" />
         		<jsp:param name="CampoIdEntita" value="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>"/>
             	<jsp:param name="ValoreIdEntita" value="<%=fascicolo.getSogIdSoggetto()%>"/> 
           		<jsp:param name="CampoIdEntitaProvv" value="tipoRicerche"/>
            	<jsp:param name="ValoreIdEntitaProvv" value="<%=tipoRicerche%>"/>
         		<jsp:param name="CampoIdEntitaPP" value="StrClassiFascicolo"/>
             	<jsp:param name="ValoreIdEntitaPP" value="<%=StrClassiFascicolo%>"/>             	
             	<jsp:param name="CampoDet" value="campoDet"/>             	
 				<jsp:param name="ValoreDet" value="<%=fromDetail%>"/>
          </jsp:include>    
       </td>
      </tr>
<%
    }
%>
    </table>
  </FORM>
  </body>
</html>