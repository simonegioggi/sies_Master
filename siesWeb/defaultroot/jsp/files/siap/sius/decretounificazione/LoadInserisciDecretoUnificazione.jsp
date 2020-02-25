<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sius.magistratorelatore.model.MagistratoRelatoreModel"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.decretounificazione.action.ICostantiDecretoUnificazione"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>

<jsp:useBean id="fasUnificante" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="fasDaUnificare" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="fascSospeso"   scope="request" class="java.lang.String"/>

<%
		//Magistrato Assegnatario precedentemente impostato
	MagistratoRelatoreModel magistratoUnificante = fasUnificante.getMagistratoRelatore();
    MagistratoRelatoreModel magistratoDaUnificare = fasDaUnificare.getMagistratoRelatore();

%>

<html>
  <head>
  <title>[S.I.E.S.] - Inserimento Decreto di Unificazione </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
    function Verify()
    {
      // Controllo della data di Unificazione
      var data_unificazione=document.LoadInserisciDecretoUnificazione.<%=ICostantiDecretoUnificazione.CAMPO_DATA_GG_UNIFICAZIONE%>.value+'/'+document.LoadInserisciDecretoUnificazione.<%=ICostantiDecretoUnificazione.CAMPO_DATA_MM_UNIFICAZIONE%>.value+'/'+document.LoadInserisciDecretoUnificazione.<%=ICostantiDecretoUnificazione.CAMPO_DATA_AAAA_UNIFICAZIONE%>.value;
      if (! ControllaData(data_unificazione))
      {
        alert('Data Unificazione non valida');
        return false;
      }
		// Controllo sul campo oggetto del decreto da unificare
		var oggettoDaUnificare= '<%=fasDaUnificare.getTenori().length%>';
		if (oggettoDaUnificare == null || oggettoDaUnificare==0)
		{
        alert('Azione non consentita.Procedimento da Unificare privo di Oggetti.');
        return false;
      }


      // Controllo della data unificazione <= data di sistema
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'

      if (! CompareDate(data_unificazione, data_sistema))
      {
        alert('Data unificazione > della data odierna');
        return false;
      }

      // Controllo della data arrivo in cancelleria <= data unificazione
      var data_arrivo='<%=DateUtils.getDateToString(fasUnificante.getGeneraleProcedimentoModel().getDataArrivoCancelleria(), "dd/MM/yyyy") %>';
      if (! CompareDate(data_arrivo, data_unificazione))
      {
        alert('Data di unificazione < della data arrivo in cancelleria');
        return false;
      }
      // STUB 24/06/2004 controllo soggetti.
      var soggettoUnificante = '<%=fasUnificante.getFascicoloSiusModel().getSoggetto().getNome()%>'+'<%=fasUnificante.getFascicoloSiusModel().getSoggetto().getCognome()%>';
      var soggettoDaUnificare = '<%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getNome()%>'+'<%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCognome()%>';
      if (soggettoUnificante != soggettoDaUnificare)
      {
        if(! confirm("I soggetti non sono riferiti allo stesso nominativo. Si vuole continuare ?" ) )
          return false;
      }
      
     // [EC] 15022018 AMOMALIA ME32 SEGNALATA DA GASBARRI IN TEST PRECOLLAUDO
     //controllo la presenza dei magistati per entrambi i fascicoli 
  	 <% 
	     if (magistratoUnificante == null || magistratoDaUnificare == null){%>
	    	 alert("Entrambi i procedimenti devono avere il Magistrato assegnato, provvedere prima dell'unificazione, all'assegnazione attraverso apposita funzione");
	      return false;
	  <% 
	 } %>
      

      if(! confirm("Saranno Unificati i due Procedimenti. Si vuole continuare ?" ) )
        return false;
    return true;
    }

    // Controllo sospensione fascicolo
    function ControlloSospensione()
    {
     <% if (fascSospeso.compareTo("SI") == 0)	{ %>
          if (! confirm("Attenzione: per questo procedimento è presente un'ordinanza di rimessione atti. Procedere con l'emissione di un nuovo provvedimento ?" ))
          {
              //str = "/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&" +a_parameter +"=" + a_entityname;
              	str = "/jsp/Main.jsp?Action=siap.sius.decretounificazione.action.ActLoadVerificaDecretoUnificazione&";
                  window.location.href=str;
                  return false
         	} else
             	return true
      <% } %>
      return true;
    }
      
  </script>

  </head>

  <body class="corpo" onload="Javascript:return ControlloSospensione()">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Inserimento Decreto Unificazione</font>
        </td>

        <!-- BOTTONE DI RITORNO -->
        <td class="LBG">
          <a href="javascript:history.go(-1);">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>

      </tr>
    </table>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadInserisciDecretoUnificazione'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.decretounificazione.action.ActInserisciDecretoUnificazione">
    <input type="HIDDEN" name="<%=ICostantiDecretoUnificazione.CAMPO_ID_FASCICOLO_UNIFICANTE%>" value="<%=fasUnificante.getFascicoloSiusModel().getIdFascicoloSius()%>">

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="Label"><font class="label">&nbsp; </font></td>
        <td class="Label"><font class="label">Procedimento Unificante</font></td>
        <td class="Label"><font class="label">Procedimento da Unificare</font></td>
     </tr>

      <tr>
        <td class="L"><font class="label">Cognome Nome Soggetto </font></td>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <td class="L"><font class="campo"><%=fasUnificante.getFascicoloSiusModel().getSoggetto().getCognome()%>&nbsp;<%=fasUnificante.getFascicoloSiusModel().getSoggetto().getNome()%></font></td>
        <td class="L"><font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCognome()%>&nbsp;<%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getNome()%></font></td>
     </tr>
      <tr>
        <td class="L"><font class="label">Data Nascita </font></td>
        <%
        if (DateUtils.getDateToString(fasUnificante.getFascicoloSiusModel().getSoggetto().getDataNascita(),"dd-MM-yyyy") == null)
        {%>
          <td class="L"><font class="campo">-</font></td>
      <%}else{%>
          <td class="L"><font class="campo"><%=DateUtils.getDateToString(fasUnificante.getFascicoloSiusModel().getSoggetto().getDataNascita(),"dd-MM-yyyy")%></font></td>
      <%}%>
        <%
        if (DateUtils.getDateToString(fasDaUnificare.getFascicoloSiusModel().getSoggetto().getDataNascita(),"dd-MM-yyyy") == null)
        {%>
          <td class="L"><font class="campo">-</font></td>
      <%}else{%>
          <td class="L"><font class="campo"><%=DateUtils.getDateToString(fasDaUnificare.getFascicoloSiusModel().getSoggetto().getDataNascita(),"dd-MM-yyyy")%></font></td>
      <%}%>
      </tr>
      <tr>
        <td class="L"><font class="label">Luogo Nascita </font></td>
<%
        if (fasUnificante.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita().compareTo("-")==0)
        {
%>
          <td class="L"><font class="campo"><%=fasUnificante.getFascicoloSiusModel().getSoggetto().getDescrStatoNascita()%> </font></td>
<%
        }
        else
        {%>
          <td class="L"><font class="campo"><%=fasUnificante.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita() + "  ("+fasUnificante.getFascicoloSiusModel().getSoggetto().getCodProvinciaNascita()+")" %></font></td>
      <%}%>
      <%
        if (fasDaUnificare.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita().compareTo("-")==0)
        {%>
          <td class="L"><font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getDescrStatoNascita()%> </font></td>
      <%}else{%>
          <td class="L"><font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita() + "  ("+fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCodProvinciaNascita()+")" %></font></td>
      <%}%>
      </tr>

      <tr>
        <td class="l">Numero SIEP</td>
        <td class="l"><font class="campo">
<%        if ((fasUnificante.getFascicoloSiusModel().getChiaveAnnoSIEP()!= null) )
          {%>
            <%=fasUnificante.getFascicoloSiusModel().getChiaveAnnoSIEP() %>/<%=fasUnificante.getFascicoloSiusModel().getChiaveProgrSIEP()%>
        <%}else{
        %>-&nbsp;<%}%>
          </font>
        </td>
        <td class="l"><font class="campo">
<%        if ((fasDaUnificare.getFascicoloSiusModel().getChiaveAnnoSIEP()!= null) )
          {%>
            <%=fasDaUnificare.getFascicoloSiusModel().getChiaveAnnoSIEP() %>/<%=fasDaUnificare.getFascicoloSiusModel().getChiaveProgrSIEP()%>
        <%}else{
            %>-&nbsp;<%}%>
          </font>
        </td>

      </tr>

      <tr>
          <td class="l">Numero SIUS</td>
          <td class="l"><font class="campo"><%=fasUnificante.getFascicoloSiusModel().getChiaveAnno() %>/<%=fasUnificante.getFascicoloSiusModel().getChiaveProgr() %></font></td>
          <td class="l"><font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getChiaveAnno() %>/<%=fasDaUnificare.getFascicoloSiusModel().getChiaveProgr() %></font></td>
      </tr>
      <tr>
          <td class="l">Data Fine Pena</td>
            <%
            if (DateUtils.getDateToString(fasUnificante.getGeneraleProcedimentoModel().getDataFinePena(),"dd-MM-yyyy") == null)
            {%>
              <td class="L"><font class="campo">-</font></td>
          <%}else{%>
          <td class="l"><font class="campo"><%=DateUtils.getDateToString(fasUnificante.getGeneraleProcedimentoModel().getDataFinePena(),"dd-MM-yyyy")%></font></td>
          <%}
            if (DateUtils.getDateToString(fasDaUnificare.getGeneraleProcedimentoModel().getDataFinePena(),"dd-MM-yyyy") == null)
            {%>
              <td class="L"><font class="campo">-</font></td>
          <%}else{%>
          <td class="l"><font class="campo"><%=DateUtils.getDateToString(fasDaUnificare.getGeneraleProcedimentoModel().getDataFinePena(),"dd-MM-yyyy")%></font></td>
          <%}%>
      </tr>
      <tr>
          <td class="l">Posizione Giuridica</td>
          <td class="l"><font class="campo"><%=fasUnificante.getGeneraleProcedimentoModel().getDescrPosGiuridica() %></font></td>
          <td class="l"><font class="campo"><%=fasDaUnificare.getGeneraleProcedimentoModel().getDescrPosGiuridica() %></font></td>
      </tr>
      <tr>
          <td class="l">Data Iscrizione</td>
          <td class="l"><font class="campo"><%=DateUtils.getDateToString(fasUnificante.getFascicoloSiusModel().getDataIscrizione(),"dd-MM-yyyy")%></font></td>
          <td class="l"><font class="campo"><%=DateUtils.getDateToString(fasDaUnificare.getFascicoloSiusModel().getDataIscrizione(),"dd-MM-yyyy")%></font></td>
      </tr>
      <tr>
          <td class="l">Tipo Atto</td>
          <td class="l"><font class="campo"><%=fasUnificante.getGeneraleProcedimentoModel().getDescrTipoAtto() %></font></td>
          <td class="l"><font class="campo"><%=fasDaUnificare.getGeneraleProcedimentoModel().getDescrTipoAtto() %></font></td>
      </tr>
      <tr>
          <td class="l">Data Atto</td>
<%
		if (DateUtils.getDateToString(fasUnificante.getGeneraleProcedimentoModel().getDataRichiesta(),"dd-MM-yyyy") == null) {
%>          
          <td class="L"><font class="campo">-</font></td>
<%
		} else {
%>          
          <td class="l"><font class="campo"><%=DateUtils.getDateToString(fasUnificante.getGeneraleProcedimentoModel().getDataRichiesta(),"dd-MM-yyyy") %></font></td>
<%
		}
		if (DateUtils.getDateToString(fasDaUnificare.getGeneraleProcedimentoModel().getDataRichiesta(),"dd-MM-yyyy") == null) {
%>          
          <td class="L"><font class="campo">-</font></td>
<%
		} else {
%>          
          <td class="l"><font class="campo"><%=DateUtils.getDateToString(fasDaUnificare.getGeneraleProcedimentoModel().getDataRichiesta(),"dd-MM-yyyy") %></font></td>
<%
		}
%>      
      </tr>
      <tr>
          <td class="l">Autorità Mittente</td>
          <td class="l"><font class="campo"><%=fasUnificante.getGeneraleProcedimentoModel().getDescrTipoMittenteAtto() %></font></td>
          <td class="l"><font class="campo"><%=fasDaUnificare.getGeneraleProcedimentoModel().getDescrTipoMittenteAtto() %></font></td>
      </tr>
      <tr>
          <td class="l">Sede Mittente</td>
          <td class="l"><font class="campo"><%=fasUnificante.getGeneraleProcedimentoModel().getDescrSedeMittente() %></font></td>
          <td class="l"><font class="campo"><%=fasDaUnificare.getGeneraleProcedimentoModel().getDescrSedeMittente() %></font></td>
      </tr>
      <tr>
          <td class="l">Contenuto</td>
          <td class="l"><font class="campo"><%=fasUnificante.getGeneraleProcedimentoModel().getDescrOggettoProcedimento() %></font></td>
          <td class="l"><font class="campo"><%=fasDaUnificare.getGeneraleProcedimentoModel().getDescrOggettoProcedimento() %></font></td>
      </tr>

      <tr>
          <td class="l">Oggetto</td>
          <td class="l">
            <font class="campo">
<%
              int lSize = fasUnificante.getTenori().length;
              for( int x=0; x<lSize; x++ )
              {
%>
                <%=fasUnificante.getTenori()[x].getDescrOggettoTenore()%><BR>
<%
              }
%>
            &nbsp;</font>
          </td>

          <td class="l">
            <font class="campo">
<%
              int lSize2 = fasDaUnificare.getTenori().length;
              for( int x=0; x<lSize2; x++ )
              {%>
                <%=fasDaUnificare.getTenori()[x].getDescrOggettoTenore()%><BR>
            <%}%>
            &nbsp;</font>
          </td>
      </tr>

      <tr>
      	<td class="l">Magistrato</td>
        <td class="l">
	        <font class="campo">
	        <%  if ((fasUnificante.getMagistratoRelatore()!= null) && (fasUnificante.getMagistratoRelatore().getMagistrato()!= null))
              {
			%>

	        <%=fasUnificante.getMagistratoRelatore().getMagistrato().getCognome() %>&nbsp;<%=fasUnificante.getMagistratoRelatore().getMagistrato().getNome() %>
	        
	        
	        <%
              }
              else
              {
                %>&nbsp;<%}%>
               
	        </font>
        </td>
        <td class="l">
	        <font class="campo">
	        <%  if ((fasDaUnificare.getMagistratoRelatore()!= null) && (fasDaUnificare.getMagistratoRelatore().getMagistrato() != null))
              {
			%>
			
	        <%=fasDaUnificare.getMagistratoRelatore().getMagistrato().getCognome() %>&nbsp;<%=fasDaUnificare.getMagistratoRelatore().getMagistrato().getNome() %>
	        
	        <%
              }
              else
              {
                %>&nbsp;<%}%>
	        </font>
        </td>
      </tr>
      
      <tr>
          <td class="l">Note</td>
          <td class="l">
            <font class="campo">
<%            if ((fasUnificante.getGeneraleProcedimentoModel().getAnnotazione()!= null) && (fasUnificante.getGeneraleProcedimentoModel().getAnnotazione().length()>0))
              {
%>
                <%=fasUnificante.getGeneraleProcedimentoModel().getAnnotazione()%>
<%
              }
              else
              {
                %>-&nbsp;<%}%>

            </font>
          </td>

          <td class="l">
            <font class="campo">
<%            if ((fasDaUnificare.getGeneraleProcedimentoModel().getAnnotazione()!= null) && (fasDaUnificare.getGeneraleProcedimentoModel().getAnnotazione().length()>0))
              {
%>
                <%=fasDaUnificare.getGeneraleProcedimentoModel().getAnnotazione()%>
<%
              }
              else
              {
                %>-&nbsp;<%}%>

            </font>
          </td>
      </tr>

    </table>

    <BR>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Data di Unificazione <font class=ob>(*)</font></td>
        <td class="l">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiDecretoUnificazione.CAMPO_DATA_GG_UNIFICAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiDecretoUnificazione.CAMPO_DATA_MM_UNIFICAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiDecretoUnificazione.CAMPO_DATA_AAAA_UNIFICAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
        </td>
      </tr>

    </table>
    <BR>
    <table cellspacing=2 cellpadding=2>

      <tr>
        <td>
          <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
        </td>
      </tr>

    </table>

    <input type="HIDDEN" name="<%= ICostantiDecretoUnificazione.CAMPO_ANNO_DA_UNIF %>" value="<%=fasDaUnificare.getFascicoloSiusModel().getChiaveAnno()%>">
    <input type="HIDDEN" name="<%= ICostantiDecretoUnificazione.CAMPO_NUMERO_DA_UNIF %>" value="<%=fasDaUnificare.getFascicoloSiusModel().getChiaveProgr()%>">
    <input type="HIDDEN" name="<%= ICostantiDecretoUnificazione.CAMPO_ANNO_UNIFICANTE %>" value="<%=fasUnificante.getFascicoloSiusModel().getChiaveAnno()%>">
    <input type="HIDDEN" name="<%= ICostantiDecretoUnificazione.CAMPO_NUMERO_UNIFICANTE %>" value="<%=fasUnificante.getFascicoloSiusModel().getChiaveProgr()%>">

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciDecretoUnificazione");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazione.CAMPO_DATA_GG_UNIFICAZIONE%>","req","Il campo Giorno della Data Unificazione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazione.CAMPO_DATA_GG_UNIFICAZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazione.CAMPO_DATA_GG_UNIFICAZIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazione.CAMPO_DATA_GG_UNIFICAZIONE%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiDecretoUnificazione.CAMPO_DATA_MM_UNIFICAZIONE%>","req","Il campo Mese della Data Unificazione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazione.CAMPO_DATA_MM_UNIFICAZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazione.CAMPO_DATA_MM_UNIFICAZIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazione.CAMPO_DATA_MM_UNIFICAZIONE%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiDecretoUnificazione.CAMPO_DATA_AAAA_UNIFICAZIONE%>","req","Il campo Anno della Data Unificazione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazione.CAMPO_DATA_AAAA_UNIFICAZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazione.CAMPO_DATA_AAAA_UNIFICAZIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazione.CAMPO_DATA_AAAA_UNIFICAZIONE%>","lt=2999");
  </script>
  </body>
</html>