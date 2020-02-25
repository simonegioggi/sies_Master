<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>


<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.esecuzionemisurasicurezza.action.ICostantiEsecuzioneMS" %>
<%@ page import="siap.siep.fascicolo.model.DettaglioFascicoloModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>

<jsp:useBean id="misuraSicurezza" scope="request" class="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel" />
<jsp:useBean id="fascicoloEsecuzione" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="misure" scope="request" class="java.util.Vector" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="listaMisureSius" scope="request" class="java.util.ArrayList" />

<%
	// presenza del Link per il bottone di ritorno
	boolean retFlag = false;
	retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
	String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

  DettaglioFascicoloModel dettaglioFascSiep = (DettaglioFascicoloModel )request.getAttribute("dettaglioFascSiep");
  String lStrOrdDec = (fascicoloEsecuzione.getGeneraleProcedimentoModel().getCodTipoAtto().compareTo("04")==0 ) ? "Ordinanza N.ro : " : "Decreto N.ro : ";
%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Modifica dell'Esecuzione Misura Sicurezza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript">
    function Verify()
    {
      // Controllo della data termine (attuale)
      var data_termine_attuale=document.elenco.<%=ICostantiEsecuzioneMS.CAMPO_GIORNO_DATA_TERMINE_MISURA%>.value+'/'+elenco.<%=ICostantiEsecuzioneMS.CAMPO_MESE_DATA_TERMINE_MISURA%>.value+'/'+elenco.<%=ICostantiEsecuzioneMS.CAMPO_ANNO_DATA_TERMINE_MISURA %>.value;
      if (! ControllaData(data_termine_attuale) && data_termine_attuale.length>2)
      {
        alert('Data Termine (attuale) non valida');
        return false;
      }

      // Data inizio
      var data_inizio='<%=DateUtils.getDateToString(misuraSicurezza.getDataInizioMisura(), "dd/MM/yyyy")%>'

      if ( CompareDate(data_termine_attuale,data_inizio))
      {
        alert('Data Termine (attuale) deve essere superiore alla Data Inizio');
        return false;
      }
      return true;
    }
    </script>

  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>" >
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Modifica dell'Esecuzione Misura Sicurezza </font></td>

      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>

    <table>
      <br>
      <tr>
        <td class="Titolo" colspan="3">Dati di dettaglio attuale dell'Esecuzione Misura Sicurezza </td>
      </tr>
      <tr>
        <td class="cVerde">N.ro Procedimento E.M.S. : <font class="cVerde"><%=fascicoloEsecuzione.getFascicoloSiusModel().getChiaveAnno()%>/<%=fascicoloEsecuzione.getFascicoloSiusModel().getChiaveProgr()%></font></td>
        <td class="c" colspan="2">  relativo a: <font class="campo"><%=misuraSicurezza.getDescrTipoMisura() %></font></td>
      </tr>
      <tr>
        <td class="c"><%=lStrOrdDec%> 
        	<font class="campo"><%=misuraSicurezza.getAnnoS07()%>/<%=misuraSicurezza.getProgrS07()%>
        	</font>
        </td>
        <td class="c" colspan="2">
        	<font class="campo"> <%=misuraSicurezza.getDescrTipoAutoritaEmittOrd()%> - <%=misuraSicurezza.getDescrLuogoAutoritaEmittOrd()%>
        	</font>
        	<font class="label">del: </font>
        	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraSicurezza.getDataOrdinanza(),"dd-MM-yyyy"),"-")%>
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
        	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraSicurezza.getDataInizioMisura(),"dd-MM-yyyy"),"-")%>
        	</font>
        </td>
        <td class="c">Data termine (iniziale): 
        	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraSicurezza.getDataTermineIniziale(),"dd-MM-yyyy"),"-")%>
        	</font>
        </td>
		
		<!-- Modifica del 16/09/2013 mev "Revisione Misure di Sicurezza SIUS" Modifica Data Termine (Attuale) -->
        <td class="L">Data termine (attuale): </td>
        <td class="L">
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuraSicurezza.getDataTermineAttuale(),"dd")) %>" type="text" name="<%=ICostantiEsecuzioneMS.CAMPO_GIORNO_DATA_TERMINE_MISURA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuraSicurezza.getDataTermineAttuale(),"MM")) %>" type="text" name="<%=ICostantiEsecuzioneMS.CAMPO_MESE_DATA_TERMINE_MISURA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuraSicurezza.getDataTermineAttuale(),"yyyy")) %>" type="text" name="<%=ICostantiEsecuzioneMS.CAMPO_ANNO_DATA_TERMINE_MISURA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
        </td>
      </tr>

      <tr>
        <td class="c">Luogo esecuzione da Ordinanza: <font class="campo">
<%
          if (misuraSicurezza.getLuogoEsecuzioneMisura()!=null    &&
              misuraSicurezza.getLuogoEsecuzioneMisura().trim().length()>1)
          {%>
            <%=misuraSicurezza.getLuogoEsecuzioneMisura().trim()%>
          <%}else{%>-<%}%>
        </font></td>
<%
        String strLuogoEsecuzione="";
        Iterator itx1 = misure.iterator();
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
        <td class="Titolo" colspan="2">Dati dell'Esecuzione Misura Sicurezza in modifica </td>
      </tr>
      <tr>
      	<%if (listaMisureSius.size() <=0)
      	{%>
			<td class="L" width="40%">Periodo misura:</td>
			<td class="L" >Anni 
				<input title="Anni" size="2" maxlength="2" type="text"
					<%if (misuraSicurezza.getNumAnniMisura() !=null){%>
						value="<%=misuraSicurezza.getNumAnniMisura() %>"
					<%}%>
					name="<%=ICostantiEsecuzioneMS.CAMPO_ANNO_TERMINE_ATTUALE%>">
				Mesi 
				<input title="Mesi" size="2" maxlength="2" type="text"
					<%if (misuraSicurezza.getNumMesiMisura() !=null){%>
						value="<%=misuraSicurezza.getNumMesiMisura() %>"
					<%}%>
					name="<%=ICostantiEsecuzioneMS.CAMPO_MESE_TERMINE_ATTUALE%>">
				Giorni
				<input title="Giorni" size="2" maxlength="4" type="text"
					<%if (misuraSicurezza.getNumGiorniMisura() !=null){%>
						value="<%=misuraSicurezza.getNumGiorniMisura() %>"
					<%}%>
					name="<%= ICostantiEsecuzioneMS.CAMPO_GIORNO_TERMINE_ATTUALE%>">
			</td>
		<%}else{%>
			<td class="L" width="40%">Periodo misura:</td>
			<td class="c">Anni 
				<%if (misuraSicurezza.getNumAnniMisura() !=null){%>		
	        		<font class="campo"><%=misuraSicurezza.getNumAnniMisura()%>
	        		</font>
	        	<%}%>
	        	Mesi
	        	<%if (misuraSicurezza.getNumMesiMisura() !=null){%>		
	        		<font class="campo"><%=misuraSicurezza.getNumMesiMisura()%>
	        		</font>
	        	<%}%>
	        	Giorni
	        	<%if (misuraSicurezza.getNumGiorniMisura() !=null){%>		
	        		<font class="campo"><%=misuraSicurezza.getNumGiorniMisura()%>
	        		</font>
	        	<%}%>
	        	<input title="Anni" size="2" maxlength="2" type="hidden"
					<%if (misuraSicurezza.getNumAnniMisura() !=null){%>
						value="<%=misuraSicurezza.getNumAnniMisura() %>"
					<%}%>
					name="<%=ICostantiEsecuzioneMS.CAMPO_ANNO_TERMINE_ATTUALE%>">
				 
				<input title="Mesi" size="2" maxlength="2" type="hidden"
					<%if (misuraSicurezza.getNumMesiMisura() !=null){%>
						value="<%=misuraSicurezza.getNumMesiMisura() %>"
					<%}%>
					name="<%=ICostantiEsecuzioneMS.CAMPO_MESE_TERMINE_ATTUALE%>">
				
				<input title="Giorni" size="2" maxlength="2" type="hidden"
					<%if (misuraSicurezza.getNumGiorniMisura() !=null){%>
						value="<%=misuraSicurezza.getNumGiorniMisura() %>"
					<%}%>
					name="<%= ICostantiEsecuzioneMS.CAMPO_GIORNO_TERMINE_ATTUALE%>">
        	</td>			
		<%}%>
	   </tr>
      <tr>
        <td class="l">Luogo esecuzione misura </td>
        <td class="L">
          <input name="<%=ICostantiEsecuzioneMS.CAMPO_LUOGO_ESECUZIONE_MISURA%>"type="text" maxlength="200" size="50" Title="Luogo Esecuzione Misura"
<%          if ( misuraSicurezza.getLuogoEsecuzioneMisura() !=null )
            {
%>            value="<%=misuraSicurezza.getLuogoEsecuzioneMisura()%>"
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
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.esecuzionemisurasicurezza.action.ActModificaEsecuzioneMS" >
    <input type="HIDDEN" name="<%=ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS%>" value="<%=misuraSicurezza.getIdEsecuzioneMisuraSicurezza()%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" value="<%=fascicoloEsecuzione.getFascicoloSiusModel().getIdFascicoloSius()%>" >
    <input type="HIDDEN" name="TornaQui" value="<%=TornaQui%>" >
    <input type="HIDDEN" name="<%=ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIEP%>"
<%  if ( dettaglioFascSiep !=null )
    {%>value="<%=dettaglioFascSiep.getFascicoloSiep().getIdFascicoloSiep()%>"
  <%}%> >
  </FORM>
  
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("elenco");
  
    frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_ANNO_TERMINE_ATTUALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_MESE_TERMINE_ATTUALE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_GIORNO_TERMINE_ATTUALE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_GIORNO_DATA_TERMINE_MISURA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_MESE_DATA_TERMINE_MISURA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_ANNO_DATA_TERMINE_MISURA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_ANNO_DATA_TERMINE_MISURA%>","minlen=4","La lunghezza del campo Anno della data termine (attuale) deve essere di 4 caratteri");

    frmvalidator.setAddnlValidationFunction("Verify");
    
  </script>
  </body>
</html>