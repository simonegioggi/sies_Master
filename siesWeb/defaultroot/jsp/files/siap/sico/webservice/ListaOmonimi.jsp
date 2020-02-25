<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Date" %>
<%@ page import="siap.sico.webservice.model.OmonimiModel" %>
<%@ page import="org.apache.axis.encoding.Base64" %>

<jsp:useBean id="lListaOmonimi" scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco Soggetti Omonimi</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/gestisciUploadStampa2.js"></script>
    <script language="Javascript">
				function VisualizzaCertificato(lAzione)
				{
						// alert("Visualizza Certificato: "+lAzione);
					
      			var  hrefStampa = lAzione;
      			var lIndice = hrefStampa.indexOf("?");

      			var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
      			stampa2("/jsp/files/Stampa.jsp",  parametri);
				}
				
				function ConfermaSoggettoSIES()
				{
						//alert("ConfermaSoggettoSIES");
						location.href="/jsp/Main.jsp?Action=siap.sico.webservice.action.ActPrelevaDatiFascicolo&FromOmonimi=NO";
				}
		</script>		
  </head>

  <BODY class="corpo">
  
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
   
  <!--  <INPUT type="hidden" name="CertificatoSelezionato" value=""> -->
  <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  <%--<INPUT type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.webservice.action.ActNscToSiesVisualizzaCertificato"> --%>
 
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Soggetti Omonimi</font></td>
   </tr>
  </table>

  <br>

	<br>

  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="int">Cognome e Nome</td>
      <td class="int">Data di Nascita</td>
      <td class="int">Luogo Nascita</td>
      <td class="int">Paternità</td>
      <td class="int">Maternità</td>
      <td class="int">Atto di Nascita</td>
      <td class="int">Azioni</td>
    </tr>
<%
    Iterator itx = lListaOmonimi.iterator();
		int riga=0;
		long lPROG_ANAGRAFICA=0;
		String lDataNascitaVis="";
    //String sUfficio = "Procura";
    while ( itx.hasNext())
    {
      		OmonimiModel lOmonimo = (OmonimiModel)itx.next();
      		riga++;
      		lPROG_ANAGRAFICA= lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getPROGANAGRAFICA();
%>
	<tr>
    	<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      	<td class="c"><font class="label"><%=lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getPERSCOGNOME()%>&nbsp;<%=lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getPERSNOME()%></font></td>
      	<td class="c"><font class="label">
<%      if (lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getDATANASCITA()==null || lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getDATANASCITA().equals(""))
        {%>-<%}
        else
        {
          String lAnno, lMese, lGiorno;
          Date lDataNascita;
          lAnno = lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getDATANASCITA().getANNO();
          lMese = lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getDATANASCITA().getMESE();
          lGiorno = lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getDATANASCITA().getGIORNO();
          lDataNascita = DateUtils.getDate(lAnno,lMese, lGiorno);
          if (lGiorno.length() == 1)
          {
            lGiorno = "0" + lGiorno;
          }
          if (lMese.length() == 1)
          {
            lMese = "0" + lMese;
          }
          lDataNascitaVis= lAnno + lMese + lGiorno;%>
          <%=DateUtils.getDateToString(lDataNascita,"dd-MM-yyyy")%>
        <%}%>
        </font></td>
      <% if (lOmonimo.getDescLuogoNascita() == null || lOmonimo.getDescLuogoNascita().equals("")){%>
        <td class="l">
            <%if(lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getDESCCOMUNEESTERO() != null && !lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getDESCCOMUNEESTERO().equals(""))
              {%>
        					<%=lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getDESCCOMUNEESTERO()%>&nbsp;
        	    		<%if(lOmonimo.getDescStatoEstero() != null && !lOmonimo.getDescStatoEstero().equals("")) 
              	  {%>
        		  			(<%=lOmonimo.getDescStatoEstero().toUpperCase()%>)		
        					<%} %>
        			<%} %>	
        	&nbsp; 
        </td>
      <% }else {%>
       	<td class="l"><%=lOmonimo.getDescLuogoNascita()%> (<%=lOmonimo.getProvNascita()%>)&nbsp;</td> 
        <!--  <td class="l">VENEZIA (VE)&nbsp;</td> -->
      <%}%>
      <!--  -->
      <% if (lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getPERSPATERNITA() != null && !lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getPERSPATERNITA().equals("")) {%>
      		<td class="c"><font class="label"><%=lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getPERSPATERNITA()%></font>&nbsp;</td>
      <% } else {%>		
      		<td class="c"><font class="label">&nbsp;</font>&nbsp;</td>
     	<% } %>
     	 <!--  -->
     	<% if (lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getPERSCOGNOMEMADRE() != null && !lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getPERSCOGNOMEMADRE().equals("")  && 
     	       lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getPERSNOMEMADRE() != null && !lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getPERSNOMEMADRE().equals("")) {%>
     	       <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      			<td class="c"><font class="label"><%=lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getPERSCOGNOMEMADRE()%>&nbsp;<%=lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getPERSNOMEMADRE()%></font>&nbsp;</td>
      <% } else {%>		
      		<td class="c"><font class="label">&nbsp;</font>&nbsp;</td>
     	<% } %>  		
			 <!--  -->   
      <% if (lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getNUMEATTONASCITA() != null && !lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getNUMEATTONASCITA().equals("")) {%>
      		<td class="c"><font class="label"><%=lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getNUMEATTONASCITA()%></font>&nbsp;</td>
      <% } else {%>		
      		<td class="c"><font class="label">&nbsp;</font>&nbsp;</td>
     	<% } %>
      
      <td class="c"> 
          <a href="Javascript:VisualizzaCertificato('/jsp/Main.jsp?Action=siap.sico.webservice.action.ActNscToSiesLoadCertificato&IDCertificato=<%=lOmonimo.getIDCertificatoOmonimiNsc()%>')">
          				<img src="/images/wscertificato.gif" width="12" height="12" alt="Certificato" border="0"></a>&nbsp;
          <a href="/jsp/Main.jsp?Action=siap.sico.webservice.action.ActPrelevaDatiFascicolo&FromOmonimi=SI&Elemento=<%=riga%>&ProgAnagraficaOmonimo=<%=lPROG_ANAGRAFICA%>&CognomeOmonimo=<%=lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getPERSCOGNOME()%>&NomeOmonimo=<%=lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getPERSNOME()%>&DataNascitaOmonimo=<%=lDataNascitaVis%>&FlagSessoOmonimo=<%=lOmonimo.getOMONIMODocument().getDATIANAGRAFICI().getFLAGSESSOMF()%>">
                  <img src="/images/wsspunta.gif" width="12" height="12" alt="Conferma" border="0"></a>
      </td>
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--<INPUT type="hidden" id="<%=riga%>" value="<%=Base64.encode(lOmonimo.getOMONIMODocument().getCERTIFICATO(),0,lOmonimo.getOMONIMODocument().getCERTIFICATO().length)%>"> --%>
      </tr>
<%		
    }
%>
		<tr><td colspan="7">&nbsp;</td><tr>
		<tr>
    		 <td class="c" colspan="7" align=center>soggetto non trovato tra gli omonimi <INPUT onclick="javascript:ConfermaSoggettoSIES()" class="bottone" type="button"  name="Conferma" value="Conferma Soggetto SIES"></td>
    </tr>
    	
    </table>
  </FORM>
  </body>
</html>