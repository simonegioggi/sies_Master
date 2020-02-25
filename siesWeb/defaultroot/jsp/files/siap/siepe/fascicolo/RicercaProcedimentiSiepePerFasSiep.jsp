<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%--@ page import="java.util.Collection" --%>
<%--@ page import="java.util.Vector" --%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%--@ page import="siap.sico.security.action.ICostantiSecurity" --%>
<%--@ page import="siap.sico.soggetto.model.SoggettoModel" --%>
<%--@ page import="siap.sico.utente.model.UtenteModel" --%>
<%--@ page import="siap.sico.evento.model.EventoModel" --%>
<%--@ page import="siap.sico.evento.action.ICostantiEvento" --%>
<%--@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel" --%>

<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe" %>
<%--@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel" --%>
<%--@ page import="siap.sius.fascicolo.model.FascicoloGPModel" --%>
<%--@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento" --%>

<%--@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa" --%>
<%--@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione" --%>
<%@ page import="siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel" %>

<%@page import="org.apache.log4j.Logger"%>
<%@page import="f3b.log.LogF3B"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui"  scope="request" class="java.lang.String"/>

<%
	//String lCodUfficioUtente = UtenteConnesso.getUfficioUtente().getCodUfficio();
	//String lModificabile = "SI";

	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	//siesLogger.debug("---- Ingresso JSP ----");

%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Procedimenti di Esecuzione Penale Esterna</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr>
    	<td class="LBG">
    		<a href="Javascript:window.print();">
    			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
    		</a>
    	</td>

      <td class="LBG">
      	<font class=label>Funzione :</font>&nbsp;<font class="campo"> Elenco Procedimenti di Esecuzione Penale Esterna per Fascicolo Siep</font></td>
<%

			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			//siesLogger.debug("---- Ingresso JSP Fase 01----");
    	/*
			if(fascicoli != null && !fascicoli.isEmpty())
    	{
      	FascicoloSiepeEstesoModel fascicoloUno = (FascicoloSiepeEstesoModel) fascicoli.get(0);
    	}
    	*/
			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			//siesLogger.debug("---- Ingresso JSP Fase 02----");
%>
  	<!-- BOTTONE DI RITORNO -->
  	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

    </tr>
     <tr> </tr>
     <tr> </tr>

   		<tr>
   		<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
     </tr>

  	</table>
  <br>

<%

	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	//siesLogger.debug("---- Ingresso JSP Fase 02-1 ----");

	Iterator itx = fascicoli.iterator();

	String sUfficio = "";
  //String lUfficio = "";
  String prevUfficio = "";

  String wCol1="12%";
  String wCol2="12%";
  String wCol3="26%";
  String wCol4="24%";
  String wCol5="10%";
  String wCol6="10%";
  String wCol7="6%";

  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  //siesLogger.debug("---- Ingresso JSP Fase 03 ----");
%>
  <table cellspacing="2" cellpadding="2" width="90%">
    <tr>
      <td class="int" width="<%=wCol1%>">Numero SIEPE</td>
      <td class="int" width="<%=wCol2%>">Data Emissione</td>
      <td class="int" width="<%=wCol3%>">Tipo Atto</td>
      <td class="int" width="<%=wCol4%>">Oggetto</td>
      <td class="int" width="<%=wCol5%>">Esito Atto</td>
      <td class="int" width="<%=wCol6%>">Documento</td>
      <td class="int" width="<%=wCol7%>">Azioni</td>

      </tr>
	<%

		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		//siesLogger.debug("---- Ingresso JSP Fase 04----");
		//PARTE SIUS
    prevUfficio = "";

		//Vector lTenori = new Vector();

		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		//siesLogger.debug("---- Ingresso JSP Fase 05----");
    while ( itx.hasNext())
    {
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      //siesLogger.debug("---- Ingresso JSP Fase 05-1 ----");

      FascicoloSiepeEstesoModel fascicolo = (FascicoloSiepeEstesoModel)itx.next();
      //sUfficio = fascicolo.getFascicoloSiepe().getDescrUfficioInserimento()+fascicolo.getFascicoloSiepe().getDescrComuneUfficio();
      sUfficio = fascicolo.getFascicoloSiepe().getDescrUfficioInserimento();
      if (!(sUfficio.compareTo(prevUfficio)==0))
      {
        prevUfficio=sUfficio;
%>
        <tr>
          <td class="lVerdeNB" colspan="9">&nbsp;</td>
        </tr>
        <tr>
          <td class="lVerdeNB" colspan="9">Elenco Procedimenti di : <%=fascicolo.getFascicoloSiepe().getDescrUfficioInserimento()%>  iscritto da SIEPE</td>
        </tr>
<%
      }
%>
      	<tr>
        	<td class="c" width="<%=wCol1%>">
          	<font class="label">
              <%=fascicolo.getFascicoloSiepe().getChiaveAnno()%>
              /
              <%=fascicolo.getFascicoloSiepe().getChiaveProgr()%>
          	</font>
        	</td>

        	<td class="c" width="<%=wCol2%>"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getEvento().getDataEmissione(),"dd-MM-yyyy"),"-")%></font></td>

<%
					// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					//siesLogger.debug("---- Ingresso JSP Fase 05-2 ----");

					if (fascicolo.getEvento().getCodTipoProvvedimento().compareTo("-")==0)
        	{
%>
        		<td class="c" width="<%=wCol3%>"><font class="label"><%=fascicolo.getEvento().getDescrTipoEvento()%></font></td>
        		<td class="c" width="<%=wCol4%>"><font class="label"><%=fascicolo.getEvento().getDescrMotivo()%></font></td>
<%
      		}
					else if (fascicolo.getFascicoloSius()!=null)
					{
%>
        		<td class="c" width="<%=wCol3%>"><font class="label"><%=fascicolo.getEvento().getDescrTipoProvvedimento()%></font></td>
        		<td class="c" width="<%=wCol4%>"><font class="label"><%=fascicolo.getFascicoloSius().getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font></td>
<%
      		}else{%>
						<td class="c" width="<%=wCol3%>"><font class="label">-</font></td>
						<td class="c" width="<%=wCol4%>"><font class="label">-</font></td>
         <%}%>

        <td class="c" width="<%=wCol5%>"><font class="label"><%=fascicolo.getEvento().getDescrEsito()%></font></td>
        <td class="c" width="<%=wCol6%>"><font class="label">-</font></td>

        <%-- Bottone di dettaglio provvedimento.--%>
        <td class="c"  width="<%=wCol7%>">
        	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siepe.fascicolo.action.ActLoadDettaglioFascicoloSiepe&<%=ICostantiFascicoloSiepe.CAMPO_ID_FASCICOLO_SIEPE%>=<%=fascicolo.getFascicoloSiepe().getIdFascicoloSiepe()%>&TornaQui=<%=TornaQui%>" Title="Dettaglio Procedimento SIEPE" > <img src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" border=0 >
          </a>
        </td>
      </tr>
<%
		 	}
%>
    </table>
  </FORM>
  <br>
  </body>
</html>