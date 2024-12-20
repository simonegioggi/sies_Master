<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>


<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.esecuzionesanzionesostitutiva.action.ICostantiEsecuzioneSS" %>
<%@ page import="siap.siep.fascicolo.model.DettaglioFascicoloModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>

<jsp:useBean id="sanzioneSostitutiva" scope="request" class="siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel" />
<jsp:useBean id="fascicoloEsecuzione" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="sanzioni" scope="request" class="java.util.Vector" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="listaSanzioniSius" scope="request" class="java.util.ArrayList" />

<%
	// presenza del Link per il bottone di ritorno
	boolean retFlag = false;
	retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
	String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

  DettaglioFascicoloModel dettaglioFascSiep = (DettaglioFascicoloModel )request.getAttribute("dettaglioFascSiep");
  String lStrOrdDec = (fascicoloEsecuzione.getGeneraleProcedimentoModel().getCodTipoAtto().compareTo("04")==0 ) ? "Ordinanza N.ro : " : "Decreto N.ro : ";

  // MEV_2023-35
  String codContenuto = fascicoloEsecuzione.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
  
  String strTitoloFunzione = "";
  String strIntestazioneTabella1 = "";
  String strNumeroProcedimento = "";
  String strIntestazioneTabella2 = "";
  String strPeriodo = "";
  String strLuogo = "";
  
  if ("U019".equals(codContenuto)) {
    strTitoloFunzione = "Modifica dell'Esecuzione Sanzione Sostitutiva";
    strIntestazioneTabella1 = "Dati di dettaglio attuale dell'Esecuzione Sanzione Sostitutiva ";
    strNumeroProcedimento = "N.ro Procedimento E.S.S.";
    strIntestazioneTabella2 = "Dati dell'Esecuzione Sanzione Sostitutiva in modifica ";
    strPeriodo = "Periodo sanzione";
    strLuogo = "Luogo esecuzione sanzione";  
  }
  else if ("U126".equals(codContenuto)) {
    strTitoloFunzione = "Modifica dell'Esecuzione Pena Sostitutiva";
    strIntestazioneTabella1 = "Dati di dettaglio attuale dell'Esecuzione Pena Sostitutiva ";
    strNumeroProcedimento = "N.ro Procedimento E.P.S.";
    strIntestazioneTabella2 = "Dati dell'Esecuzione Pena Sostitutiva in modifica ";
    strPeriodo = "Periodo pena";
    strLuogo = "Luogo esecuzione pena";      
  }

%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Modifica dell'Esecuzione Sanzione Sostitutiva</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>" >
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> <%=strTitoloFunzione %> </font></td>

      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>

    <table>
      <br>
      <tr>
        <td class="Titolo" colspan="3"><%=strIntestazioneTabella1 %> </td>
      </tr>
      <tr>
        <td class="cVerde"><%=strNumeroProcedimento%> : <font class="cVerde"><%=fascicoloEsecuzione.getFascicoloSiusModel().getChiaveAnno()%>/<%=fascicoloEsecuzione.getFascicoloSiusModel().getChiaveProgr()%></font></td>
        <td class="c" colspan="2">  relativo a: <font class="campo"><%=sanzioneSostitutiva.getDescrTipoSanzione() %></font></td>
      </tr>
      <tr>
        <td class="c"><%=lStrOrdDec%> 
        	<font class="campo"><%=sanzioneSostitutiva.getAnnoS07()%>/<%=sanzioneSostitutiva.getProgrS07()%>
        	</font>
        </td>
        <td class="c" colspan="2">
        	<font class="campo"> <%=sanzioneSostitutiva.getDescrTipoAutoritaEmittOrd()%> - <%=sanzioneSostitutiva.getDescrLuogoAutoritaEmittOrd()%>
        	</font>
        	<font class="label">del: </font>
        	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sanzioneSostitutiva.getDataOrdinanza(),"dd-MM-yyyy"),"-")%>
      		</font>
      	</td> 
      </tr>
      <tr>
        <td class="c">Soggetto: 
        	<font class="campo"><%=fascicoloEsecuzione.getFascicoloSiusModel().getSoggetto().getCognome()%>
        		<%=fascicoloEsecuzione.getFascicoloSiusModel().getSoggetto().getNome()%>
        	</font>
        </td> 
        <td class="c" colspan="2">
        	<font class="label">  nato/a il: </font><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloEsecuzione.getFascicoloSiusModel().getSoggetto().getDataNascita(),"dd-MM-yyyy"),"-")%>
        	</font>
        	<font class="label">in : </font><font class="campo"><%=fascicoloEsecuzione.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita()%>
        	</font>
        </td> 
      </tr>

<%    if ( dettaglioFascSiep !=null  )
      {
%>
        <tr>
          <td class="c">Titolo Esecutivo N.ro Siep : <font class="campo"><%=dettaglioFascSiep.getFascicoloSiep().getChiaveAnno()%>/<%=dettaglioFascSiep.getFascicoloSiep().getChiaveProgr()%></font></td>
          <td class="c" colspan="2"><font class="campo"> <%=dettaglioFascSiep.getFascicoloSiep().getDescrTipoUfficio()%> - <%=dettaglioFascSiep.getFascicoloSiep().getDescrComuneUfficio()%>
          </font><font class="label">del: </font><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(dettaglioFascSiep.getFascicoloSiep().getDataIscrizione(),"dd-MM-yyyy"),"-")%>
          </font></td>
        </tr>
      <%}%>

      <tr>
        <td class="c">Data inizio: 
        	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sanzioneSostitutiva.getDataInizioSanzione(),"dd-MM-yyyy"),"-")%>
        	</font>
        </td>
        <td class="c">Data termine (iniziale): 
        	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sanzioneSostitutiva.getDataTermineIniziale(),"dd-MM-yyyy"),"-")%>
        	</font>
        </td>
        <td class="c">Data termine (attuale): 
        	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sanzioneSostitutiva.getDataTermineAttuale(),"dd-MM-yyyy"),"-")%>
        	</font>
        </td>
      </tr>

      <tr>
        <td class="c">Luogo esecuzione da Ordinanza: <font class="campo">
<%
          if (sanzioneSostitutiva.getLuogoEsecuzioneSanzione()!=null    &&
              sanzioneSostitutiva.getLuogoEsecuzioneSanzione().trim().length()>1)
          {%>
            <%=sanzioneSostitutiva.getLuogoEsecuzioneSanzione().trim()%>
          <%}else{%>-<%}%>
        </font></td>
<%
        String strLuogoEsecuzione="";
        Iterator itx1 = sanzioni.iterator();
        while ( itx1.hasNext())
        {
          FascicoloGPModel fascicoloGP = (FascicoloGPModel)itx1.next();
          if (fascicoloGP.getGeneraleProcedimentoModel().getDescrRichiestaDelegazione()!=null &&
              fascicoloGP.getGeneraleProcedimentoModel().getDescrRichiestaDelegazione().trim().length()>1)
          strLuogoEsecuzione=fascicoloGP.getGeneraleProcedimentoModel().getDescrRichiestaDelegazione();
        }
        if (strLuogoEsecuzione.length()>1)
        {%>
          <td class="c" colspan="2">Luogo esecuzione corrente: <font class="campo"><%=strLuogoEsecuzione%>
          </font></td>
      <%}%>
      </tr>
    </table>
  </table>

  <br>

  <table cellspacing=0 cellpadding=0>
      <tr>
        <td class="Titolo" colspan="2"><%=strIntestazioneTabella2 %> </td>
      </tr>
      <tr>
      	<%if (listaSanzioniSius.size() <=0)
      	{%>
			<td class="L" width="40%"><%=strPeriodo %>:</td>
			<td class="L" >Anni 
				<input title="Anni" size="2" maxlength="2" type="text"
					<%if (sanzioneSostitutiva.getNumAnniSanzione() !=null){%>
						value="<%=sanzioneSostitutiva.getNumAnniSanzione() %>"
					<%}%>
					name="<%=ICostantiEsecuzioneSS.CAMPO_ANNO_TERMINE_ATTUALE%>">
				Mesi 
				<input title="Mesi" size="2" maxlength="2" type="text"
					<%if (sanzioneSostitutiva.getNumMesiSanzione() !=null){%>
						value="<%=sanzioneSostitutiva.getNumMesiSanzione() %>"
					<%}%>
					name="<%=ICostantiEsecuzioneSS.CAMPO_MESE_TERMINE_ATTUALE%>">
				Giorni
				<input title="Giorni" size="2" maxlength="4" type="text"
					<%if (sanzioneSostitutiva.getNumGiorniSanzione() !=null){%>
						value="<%=sanzioneSostitutiva.getNumGiorniSanzione() %>"
					<%}%>
					name="<%= ICostantiEsecuzioneSS.CAMPO_GIORNO_TERMINE_ATTUALE%>">
			</td>
		<%}else{%>
			<td class="L" width="40%"><%=strPeriodo %>:</td>
			<td class="c">Anni 
				<%if (sanzioneSostitutiva.getNumAnniSanzione() !=null){%>		
	        		<font class="campo"><%=sanzioneSostitutiva.getNumAnniSanzione()%>
	        		</font>
	        	<%}%>
	        	Mesi
	        	<%if (sanzioneSostitutiva.getNumMesiSanzione() !=null){%>		
	        		<font class="campo"><%=sanzioneSostitutiva.getNumMesiSanzione()%>
	        		</font>
	        	<%}%>
	        	Giorni
	        	<%if (sanzioneSostitutiva.getNumGiorniSanzione() !=null){%>		
	        		<font class="campo"><%=sanzioneSostitutiva.getNumGiorniSanzione()%>
	        		</font>
	        	<%}%>
	        	<input title="Anni" size="2" maxlength="2" type="hidden"
					<%if (sanzioneSostitutiva.getNumAnniSanzione() !=null){%>
						value="<%=sanzioneSostitutiva.getNumAnniSanzione() %>"
					<%}%>
					name="<%=ICostantiEsecuzioneSS.CAMPO_ANNO_TERMINE_ATTUALE%>">
				 
				<input title="Mesi" size="2" maxlength="2" type="hidden"
					<%if (sanzioneSostitutiva.getNumMesiSanzione() !=null){%>
						value="<%=sanzioneSostitutiva.getNumMesiSanzione() %>"
					<%}%>
					name="<%=ICostantiEsecuzioneSS.CAMPO_MESE_TERMINE_ATTUALE%>">
				
				<input title="Giorni" size="2" maxlength="2" type="hidden"
					<%if (sanzioneSostitutiva.getNumGiorniSanzione() !=null){%>
						value="<%=sanzioneSostitutiva.getNumGiorniSanzione() %>"
					<%}%>
					name="<%= ICostantiEsecuzioneSS.CAMPO_GIORNO_TERMINE_ATTUALE%>">
        	</td>			
		<%}%>
	   </tr>
      <tr>
        <td class="l"><%=strLuogo %> </td>
        <td class="L">
          <input name="<%=ICostantiEsecuzioneSS.CAMPO_LUOGO_ESECUZIONE_SANZIONE%>"type="text" maxlength="200" size="50" Title="<%=strLuogo %>"
<%          if ( sanzioneSostitutiva.getLuogoEsecuzioneSanzione() !=null )
            {
%>            value="<%=sanzioneSostitutiva.getLuogoEsecuzioneSanzione()%>"
          <%}%>>

        </td>
      </tr>
    </table>
    <br>
    <table>
      <tr>
        <td>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.esecuzionesanzionesostitutiva.action.ActModificaEsecuzioneSS" >
    <input type="HIDDEN" name="<%=ICostantiEsecuzioneSS.CAMPO_ID_ESECUZIONE_SS%>" value="<%=sanzioneSostitutiva.getIdEsecuzioneSanzioneSost()%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" value="<%=fascicoloEsecuzione.getFascicoloSiusModel().getIdFascicoloSius()%>" >
    <input type="HIDDEN" name="TornaQui" value="<%=TornaQui%>" >
    <input type="HIDDEN" name="<%=ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP%>"
<%  if ( dettaglioFascSiep !=null )
    {%>value="<%=dettaglioFascSiep.getFascicoloSiep().getIdFascicoloSiep()%>"
  <%}%> >
  </FORM>
  
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("elenco");
  
    frmvalidator.addValidation("<%=ICostantiEsecuzioneSS.CAMPO_ANNO_TERMINE_ATTUALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiEsecuzioneSS.CAMPO_MESE_TERMINE_ATTUALE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiEsecuzioneSS.CAMPO_GIORNO_TERMINE_ATTUALE%>","numeric");

  </script>
  </body>
</html>