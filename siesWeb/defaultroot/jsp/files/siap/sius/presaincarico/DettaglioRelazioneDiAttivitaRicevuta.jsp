<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.prescrizione.model.PrescrizioneModel"%>

<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>

<jsp:useBean id="FascicoloSiepeEsteso" scope="request" class="siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel"/>
<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="attivita" scope="request" class="siap.siepe.attivita.model.AttivitaModel"/>
<jsp:useBean id="relazione" scope="request" class="siap.siepe.relazione.model.RelazioneModel"/>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Relazione di Attività Ricevuta</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

	  <script language="JavaScript">
  	function stampaSius(lAzione)
   	{
    	var  hrefStampa = "<%=IWebConstants.ACTION_FIELD%>="+lAzione;
    	stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  hrefStampa);
   	}
 		</script>
  </head>
  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr>
        	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Relazione di Attività Ricevuta</font>
          </td>

          <!-- BOTTONE DI STAMPA -->
     			<td class="LBG">
      			<a href="Javascript:stampaSius('siap.sius.presaincarico.action.ActVisualizzaRelazioneSiepeRicevuta&IdMessaggio=<%=Messaggio.getIdMessaggio()%>')" >
        			<img  align="middle" src="/images/print24.gif" alt="Stampa Relazione Ricevuta" width="24" height="24" border="0">
      			</a>
     			</td>
      		<!-- BOTTONE DI RITORNO -->
      		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
         </tr>
      </table>
    </FORM>
<%
  SoggettoModel soggetto = FascicoloSiepeEsteso.getSoggetto();
%>
  <table cellspacing=2 cellpadding=2 width=85%>
    <tr><td class="Titolo" colspan=4>Fascicolo SIEPE</td></tr>
    <tr>
      <td class="L">
        <font class="label">Procedimento N.</font>
        <font class="campo">
          <%=FascicoloSiepeEsteso.getFascicoloSiepe().getChiaveAnno()%>/<%=FascicoloSiepeEsteso.getFascicoloSiepe().getChiaveProgr()%>
          - <%=FascicoloSiepeEsteso.getFascicoloSiepe().getDescrUfficioInserimento()%><BR>
        </font>
        <font class="label"> relativo a: </font>
        <font class="campo">
          <%=FascicoloSiepeEsteso.getFascicoloSiepe().getDescrIncarico()%></font>
      </td>
    </tr>

    <tr><td class="Titolo" colspan=4>Attività</td></tr>
    <tr>
      <td class="L">
				<font class="l">Data Inizio : </font>
				<font class="campo">
					<%=DateUtils.getDateToString(attivita.getDataInizio(),"dd-MM-yyyy")%> </font>
					<% if (attivita.getDataChiusura()!=null)
          {%>
						<font class="l">Data Chiusura</font>
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(attivita.getDataChiusura(),"dd-MM-yyyy"),"-")%> </font>
				<%}%>
       </td>
		<tr>
				<td class="L">
          <font class="l">Tipo Attività : </font>
          <font class="campo"><%=attivita.getDescrTipoAttivita()%></font>
				<%if (attivita.getDataChiusura()!=null)
    			{%>
						<font class="l">Esito Attività : </font>
						<font class="campo"><%=StringUtils.toStringJSP(attivita.getDescrEsitoAttivita(),"-")%></font>
        </td>
		</tr>
		<tr>
    	<td class="L">
				<font class="l">Nota di Chiusura : </font>
				<font class="campo"><%=StringUtils.toStringJSP( attivita.getNotaChiusura())%></font>
      </td>
		</tr>
		<%}%>


		<!-- Sezione dati della Relazione -->

    <tr><td class="Titolo" colspan=4>Relazione </td></tr>
 		<tr>
   		<td class="L">
				<font class="l">Data Emissione : </font>
				<font class="campo">
					<%=DateUtils.getDateToString(relazione.getDataEmissione(),"dd-MM-yyyy")%>
				</font>
    	</td>
    </tr>
		<tr>
			<td class="L">
      	<font class="l">Nota relazione : </font>
        <font class="campo"><%=StringUtils.toStringJSP(relazione.getNote(), "-") %></font>
      </td>
		</tr>

		<!-- Fine dati della relazione -->
    <!-- 29/06/2007 prevista assenza Dati SIUS -->
<%	if(FascicoloSiepeEsteso.getFascicoloSius() != null)
		{%>
    <tr><td class="Titolo" colspan=4>Fascicolo SIUS</td></tr>
    <tr>
      <td class="L">
        <font class="label">Procedimento N.</font>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <font class="campo">
          <%=FascicoloSiepeEsteso.getFascicoloSius().getFascicoloSiusModel().getChiaveAnno()%>/<%=FascicoloSiepeEsteso.getFascicoloSius().getFascicoloSiusModel().getChiaveProgr()%>
          - <%=FascicoloSiepeEsteso.getFascicoloSius().getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=FascicoloSiepeEsteso.getFascicoloSius().getFascicoloSiusModel().getDescrComuneUfficio()%><BR>
        </font>
        <font class="label"> relativo a: </font>
        <font class="campo">
          <%=FascicoloSiepeEsteso.getFascicoloSius().getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font>
      </td>
    </tr>
	<%}%>

    <tr>
      <td class="L" width=100%><font class="label">Soggetto:</font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a>
      </font>&nbsp;
<%
        if(soggetto.getSesso().compareTo("F")==0)
        {
%>
          <font class="label">nata il :</font>&nbsp;
<%
        }
        else
        {
%>
          <font class="label">nato il :</font>&nbsp;
<%
        }
%>
      <font class="campo"><%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;
      <font class="label">in : </font>
      <font class="campo">
<%
      if (soggetto.getDescrComuneNascita().compareTo("-")==0)
      {%>
        <%=soggetto.getDescrStatoNascita()%>
<%
      }else{%>
        <%=soggetto.getDescrComuneNascita()+ "  ("+soggetto.getCodProvinciaNascita()+")" %>
		<%}%>
      </font>
     </td>
    </tr>
<%	if(FascicoloSiepeEsteso.getFascicoloSius() != null)
		{
		if ((FascicoloSiepeEsteso.getFascicoloSius().getFascicoloSiusModel().getChiaveAnnoSIEP())!=null)
    {%>
    <tr>
      <td class="L">
        <font class="label">Titolo Esecutivo: N.ro SIEP </font>

        <font class="campo">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=FascicoloSiepeEsteso.getFascicoloSius().getFascicoloSiusModel().getFasSieIdFascicoloSiep()%>">
            <%=FascicoloSiepeEsteso.getFascicoloSius().getFascicoloSiusModel().getChiaveAnnoSIEP()%>/<%=FascicoloSiepeEsteso.getFascicoloSius().getFascicoloSiusModel().getChiaveProgrSIEP()%>
          </a>
        </font>&nbsp;
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <font class="campo"><%=fascicolo.getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getDescrComuneUfficio()%></font><font class="label"> del </font>&nbsp;
        <font class="campo"><%=DateUtils.getDateToString(FascicoloSiepeEsteso.getFascicoloSius().getFascicoloSiusModel().getDataInserimento(),"dd-MM-yyyy")%></font>&nbsp;
      </td>
    </tr>
	<%}else if (FascicoloSiepeEsteso.getFascicoloSiep()!=null) {%>
    <tr>
      <td class="L">
        <font class="label">Titolo Esecutivo: N.ro SIEP </font>

        <font class="campo">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=FascicoloSiepeEsteso.getFascicoloSiep().getIdFascicoloSiep()%>">
            <%=FascicoloSiepeEsteso.getFascicoloSiep().getChiaveAnno()%>/<%=FascicoloSiepeEsteso.getFascicoloSiep().getChiaveProgr()%>
          </a>
        </font>&nbsp;
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <font class="campo"><%=fascicolo.getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getDescrComuneUfficio()%></font><font class="label"> del </font>&nbsp;
        <font class="campo"><%=DateUtils.getDateToString(FascicoloSiepeEsteso.getFascicoloSiep().getDataInserimento(),"dd-MM-yyyy")%></font>&nbsp;
      </td>
    </tr>
  <%}}%>

<%	if(FascicoloSiepeEsteso.getFascicoloSius() != null)
		{%>
    <tr>
      <td class="L">
        <font class="label">Data Udienza : </font>
        <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString( FascicoloSiepeEsteso.getFascicoloSius().getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd-MM-yyyy"), "-" )%>
        </font>
      </td>
    </tr>
	<%}%>

  </table>
  <br>
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ConfermaPresaInCaricoAttivita">
  <br>
  	<table cellspacing=2 cellpadding=2 width=95%>
      <tr>
        <td>
          <input class=bottone  type="submit" value="Conferma Presa in Carico">
        </td>
      </tr>
  	</table>
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.presaincarico.action.ActConfermaPresaInCaricoRelazioneAttivitaSiepe">
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
  </form >
</body>
</html>