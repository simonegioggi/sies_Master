<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="f3b.security.model.ProfileModel" %>

<jsp:useBean id="UtenteConnesso" 	scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="SoggOmonimi" 		scope="request" class="java.util.Vector" />
<jsp:useBean id="AzioneChiamante" 	scope="request" class="java.lang.String" />
<jsp:useBean id="soggetto" 			scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="newsoggetto" 		scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="lTipoFunzione"     scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] Inserimento Soggetto - Ricerca omonimi</title>
    <script language="JavaScript" src="/html/conferma.js"></script>

	<script language="JavaScript">
  //==========================================================================
  // Richiama l'opportuna azione
  //==========================================================================
    function eseguiAzione(aTipoAzione, aIdSog)
    {
      	if (aTipoAzione=='Dettaglio')
      	{
        	lAzione = "siap.sico.soggetto.action.ActLoadDettaglioSoggetto";
        	document.LoadInserisciOmonimoSoggetto.<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO %>.value = aIdSog;
        	document.LoadInserisciOmonimoSoggetto.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        	document.LoadInserisciOmonimoSoggetto.submit();
      	}
	}
	</script>
  
  </head>

  <body class="corpo">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"> <font class="label"> Funzione :</font> <font class=campo> Inserimento Soggetto - Presenza Omonimie</font></td>
    </tr>
  </table>

  <br>
  <div align=center>
  <table>
    <tr>
      <td class="LBG"> <font class=campo> Soggetti omonimi già presenti in archivio</font></td>
    </tr>
  </table>
	<br>
  <table>
    <tr>
      <td class="int">Cognome Nome</td>
      <td class="int" width=5%>Sesso</td>
      <td class="int">Data Nascita</td>
      <td class="int">Luogo Nascita</td>
      <td class="int">Paternità</td>
      <td class="int" width=5% >N.Sinonimi</td>
    </tr>

<%
  int Misu = SoggOmonimi.size();
  Misu = Misu-1;	
  SoggOmonimi.setSize(Misu);

//  Iterator itx = SoggOmonimi.iterator();
//  while ( itx.hasNext())
//  {
//    SoggettoModel soggOmonimo = (SoggettoModel)itx.next();
   
  //  SoggettoModel lSogModel =  new SoggettoModel((SoggettoModel)lSoggetti.firstElement());
    
  	SoggettoModel soggOmonimo =  new SoggettoModel((SoggettoModel)SoggOmonimi.firstElement()); 
  
%>
	<tr>
    	<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      	<td class=l><%=soggOmonimo.getCognome()%>&nbsp;<%=soggOmonimo.getNome()%></td>
      	<td class=c><%=soggOmonimo.getSesso()%></td>
      	<td class=c>
      	<font class="campo">
<%
        if(soggOmonimo.getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(soggOmonimo.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-"+StringUtils.toStringJSP(soggOmonimo.getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(soggOmonimo.getAnnoNascita(), "****")%>&nbsp;

<%
        }
%>
        </font>
<%
       if(soggOmonimo.getDataNascitaPresunta() != null && soggOmonimo.getDataNascitaPresunta().equals("S"))
        {
%>
          <font class="campo"> (Data Presunta)</font>
<%      }

%>

      </td>

      <%if (soggOmonimo.getDescrComuneNascita().compareTo("-")==0)
        {%>
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--td class="l"><-%=soggOmonimo.getDescComuneNascitaEstero()%>  (<-%=soggOmonimo.getDescrStatoNascita().toUpperCase()%>)&nbsp; </td--%>
         <%if (soggOmonimo.getDescComuneNascitaEstero().compareTo("------------------------------")==0 || soggOmonimo.getDescComuneNascitaEstero().equals("") )
        {%>
        <td class="l">
          -
        </td>
         <%}else{%>
           <td class="l">
          <%=StringUtils.pulisciCampo(soggOmonimo.getDescComuneNascitaEstero(),soggOmonimo.getDescrStatoNascita().toUpperCase())%>  (<%=soggOmonimo.getDescrStatoNascita().toUpperCase()%>)&nbsp;
        </td>

          <%}%>
      <%}else
        {%>
        <td class="l"><%=soggOmonimo.getDescrComuneNascita()%> (<%=soggOmonimo.getCodProvinciaNascita()%>)&nbsp;</td>
      <%}%>
      <td class=c><%=soggOmonimo.getPaternita()%>&nbsp;</td>
      <td class=c><%=Misu%></td>

    </tr>
<%
 // }
%>
    </table>


</div>
  <br>

  <table>
	<tr>
		<td>
 			<font class="cRosso"> SOGGETTO INSERITO CORRETTAMENTE! Per proseguire, clickare sulla icona dettaglio</font>
		</td>
        <td class=c>
        	<a href="javascript:eseguiAzione('Dettaglio',<%=newsoggetto.getIdSoggetto() %> )">
        		<img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Soggetto" border="0">
        	</a>
      	</td>       
	</tr>    
  </table>
  
  <form  method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciOmonimoSoggetto">
  	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
   
    <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->
  	<input type="hidden" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value="">
  	
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l"><font class="label">Cognome e Nome</font></td>
      <td class="l"><font class="campo"><%=soggetto.getCognome() %>&nbsp;&nbsp;<%=soggetto.getNome() %></font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Sesso</font></td>
      <td class="l"><font class="campo"><%=soggetto.getSesso()%>&nbsp;</font></td>
    </tr>
	<tr>
      <td class="l" width="25%">
        <font  class="label">Data di nascita</font>
      </td>
      <td class="l" width="25%">
        <font class="campo">
<%
        if(soggetto.getDataNascita() != null)
        {
%>
          <%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        {
%>
          <%="**-"+StringUtils.toStringJSP(soggetto.getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(soggetto.getAnnoNascita(), "****")%>&nbsp;
<%
        }
%>
        </font>
      </td>
      <td class="l" width="25%">
        <font class="label">Data Presunta</font>
      </td>
      <td class="l" width="25%">
        <font class="campo"><%=StringUtils.toStringJSP(soggetto.getDataNascitaPresunta())%></font>
      </td>
    </tr>

<%-- MERGE v10 COLLAUDO: modifica alla gestione del codice --%>
    <%
 	// Si ricava il profilo dell'utente connesso
  	ProfileModel lProfilo = (ProfileModel)UtenteConnesso.getUserProfile();
 	UfficioModel lUfficio = UtenteConnesso.getUfficioUtente();
 	String codTipoUfficio = lUfficio.getCodTipoUfficio();

	// MEV_57: esclusi anche sige minorenni
	boolean isSigeMinorenni = lProfilo.isSige() && ("CAPSM".equals(codTipoUfficio) || "DIBM".equals(codTipoUfficio) || "GIPM".equals(codTipoUfficio) || "GUPM".equals(codTipoUfficio) || "PMM".equals(codTipoUfficio));
	if (!lProfilo.isSius() && !isSigeMinorenni) { %>
    <tr>
      <td class="l"><font class="label">Età Presunta</font></td>
      <td class="l"><font class="campo">
	<% 	if (soggetto.getEtaPresuntaAnni()!=null){ %> 
	      	<%=StringUtils.toStringJSP(soggetto.getEtaPresuntaAnni())%>&nbsp;</font> anni
	<% 		if (soggetto.getEtaPresuntaMesi()!=null){%> 
	      		e <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaMesi())%>&nbsp;</font> mesi
	<%		} %> 
	<%	} %>
		&nbsp;
      </td>
    </tr>
<%  } %>  
 
<%
  	  // Data commesso reato deve essere visibile solo per 
  	  // gli utenti SIUS (Minorenni), no per gli utenti SIEP
  	  // MEV_57: aggiunti anche gli uffici sige minorenni
	  if ((!lProfilo.isSiep() && ("TDSM".equals(codTipoUfficio) || "UDSM".equals(codTipoUfficio))) || isSigeMinorenni) {
	  %> 
		<tr>
		      <td class="l"><font class="label">Età Presunta</font></td>
		      <td class="l"><font class="campo">
		<% if (soggetto.getEtaPresuntaAnni()!=null){%> 
		      	<%=StringUtils.toStringJSP(soggetto.getEtaPresuntaAnni())%>&nbsp;</font> anni
		<% if (soggetto.getEtaPresuntaMesi()!=null){%> 
		      	e <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaMesi())%>&nbsp;</font> mesi
		<%} %> 
		<%} %>
				&nbsp;
		      </td>
	      	  
	      	  <td class="l" width="25%">
	        	<font  class="label">Data commesso reato</font>
	      	  </td>
	      	  <td class="l" width="25%">
	        	<font class="campo">
	        	<%
				        if(soggetto.getDataReatoSius() != null)
				        {
				%>
				          <%=DateUtils.getDateToString(soggetto.getDataReatoSius(),"dd-MM-yyyy")%>&nbsp;
				<%
				        }
				        else
				        {
				%>
				          <%="**-"+StringUtils.toStringJSP(soggetto.getDataReatoSius(), "**")+"-"+StringUtils.toStringJSP(soggetto.getDataReatoSius(), "****")%>&nbsp;
				<%
				        }
				%>
	        	</font>
	    	  </td>
		</tr>
    <%
	}
	%>

    <tr>
      <td class="l"><font  class="label">Comune Nascita</font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(soggetto.getDescrComuneNascita() )%>
<%        if( (soggetto.getDescrComuneNascita() != null)
              && (!(soggetto.getDescrComuneNascita().equals("")))
              && (!(soggetto.getDescrComuneNascita().equals("-"))) )
          {
%>
            (<%=soggetto.getCodProvinciaNascita()%>)
<%
          }
%>
          &nbsp;
        </font>
      </td>
    </tr>
	<tr>
	  <td class="l"><font class="label">Stato Cittadinanza</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrNazionalita())%>&nbsp;</font></td>
      <td class="l"><font  class="label">Stato Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrStatoNascita())%>&nbsp;</font></td>
    </tr>
	 <tr>
      <td class="l"><font class="label">Comune Di Nascita Estero</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescComuneNascitaEstero()).toUpperCase()%>&nbsp;</font></td>
    </tr>
	  <tr>
      <td class="l"><font class="label">Paternità</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getPaternita())%>&nbsp;</font></td>
    </tr>
	 <tr>
      <td class="l"><font class="label">Nome Madre</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNomeMadre())%>
      <%=StringUtils.toStringJSP(soggetto.getCognomeMadre())%>&nbsp;</font></td>
    </tr>
	<tr><td colspan=4 class=l>&nbsp;</td></tr>
    <tr>
      <td class="l"><font class="label">Codice Fiscale</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodFiscale())%>&nbsp;</font></td>
      <td class="l"><font class="label">Atto Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getAttoNascita()) %>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Codice Fascicolo Rosso</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodCs())%>&nbsp;</font></td>
      <td class="l"><font class="label">Codice CUI</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodAfis() )%>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l"><font  class="label">Note</font></td>
      <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNote())%>&nbsp;</font></td>
    </tr>
</table>

</form>
  <br>



  </body>
</html>