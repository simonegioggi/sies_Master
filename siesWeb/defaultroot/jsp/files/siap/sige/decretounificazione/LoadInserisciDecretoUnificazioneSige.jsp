<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sige.decretounificazione.action.ICostantiDecretoUnificazioneSige"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeModel"%>

<jsp:useBean id="fasUnificante" scope="request" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="fasDaUnificare" scope="request" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="tenoriUnificato" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tenoriUnificante" scope="request" class="java.util.Vector"/>

<html>
  <head>
  <title>[S.I.E.S.] - Inserimento Decreto di Unificazione Sige</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
    function Verify()
    {
      // Controllo della data di Unificazione
      var data_unificazione=document.LoadInserisciDecretoUnificazioneSige.<%=ICostantiDecretoUnificazioneSige.CAMPO_DATA_GG_UNIFICAZIONE%>.value+'/'+document.LoadInserisciDecretoUnificazioneSige.<%=ICostantiDecretoUnificazioneSige.CAMPO_DATA_MM_UNIFICAZIONE%>.value+'/'+document.LoadInserisciDecretoUnificazioneSige.<%=ICostantiDecretoUnificazioneSige.CAMPO_DATA_AAAA_UNIFICAZIONE%>.value;
      if (! ControllaData(data_unificazione))
      {
        alert('Data Unificazione non valida');
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
      var data_arrivo='<%=DateUtils.getDateToString(fasUnificante.getRichiestaSige().getDataArrivoCancelleria(), "dd/MM/yyyy") %>';
      if (! CompareDate(data_arrivo, data_unificazione))
      {
        alert('Data di unificazione < della data arrivo in cancelleria');
        return false;
      }
      // STUB 24/06/2004 controllo soggetti.
      var soggettoUnificante = '<%=fasUnificante.getSoggetto().getNome()%>'+'<%=fasUnificante.getSoggetto().getCognome()%>';
      var soggettoDaUnificare = '<%=fasDaUnificare.getSoggetto().getNome()%>'+'<%=fasDaUnificare.getSoggetto().getCognome()%>';
      if (soggettoUnificante != soggettoDaUnificare)
      {
        if(! confirm("I soggetti non sono riferiti allo stesso nominativo. Si vuole continuare ?" ) )
          return false;
      }

      if(! confirm("Saranno Unificati i due Procedimenti. Si vuole continuare ?" ) )
        return false;
    return true;
    }

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = 
          window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
  </script>

  </head>

  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Inserimento Decreto Unificazione Sige</font>
        </td>

        <!-- BOTTONE DI RITORNO -->
        <td class="LBG">
          <a href="javascript:history.go(-1);">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>

      </tr>
    </table>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadInserisciDecretoUnificazioneSige'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.decretounificazione.action.ActInserisciDecretoUnificazioneSige">
    <input type="HIDDEN" name="<%=ICostantiDecretoUnificazioneSige.CAMPO_ID_FASCICOLO_UNIFICANTE%>" value="<%=fasUnificante.getFascicoloSige().getIdFascicoloSige()%>">

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="Label"><font class="label">&nbsp; </font></td>
        <td class="Label"><font class="label">Procedimento Sige Unificante</font></td>
        <td class="Label"><font class="label">Procedimento Sige da Unificare</font></td>
     </tr>

      <tr>
        <td class="L"><font class="label">Cognome Nome Soggetto </font></td>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <td class="L"><font class="campo"><%=fasUnificante.getSoggetto().getCognome()%>&nbsp;<%=fasUnificante.getSoggetto().getNome()%></font></td>
        <td class="L"><font class="campo"><%=fasDaUnificare.getSoggetto().getCognome()%>&nbsp;<%=fasDaUnificare.getSoggetto().getNome()%></font></td>
     </tr>
      <tr>
        <td class="L"><font class="label">Data Nascita </font></td>
        <%
        if (DateUtils.getDateToString(fasUnificante.getSoggetto().getDataNascita(),"dd-MM-yyyy") == null)
        {%>
          <td class="L"><font class="campo">-</font></td>
      <%}else{%>
          <td class="L"><font class="campo"><%=DateUtils.getDateToString(fasUnificante.getSoggetto().getDataNascita(),"dd-MM-yyyy")%></font></td>
      <%}%>
        <%
        if (DateUtils.getDateToString(fasDaUnificare.getSoggetto().getDataNascita(),"dd-MM-yyyy") == null)
        {%>
          <td class="L"><font class="campo">-</font></td>
      <%}else{%>
          <td class="L"><font class="campo"><%=DateUtils.getDateToString(fasDaUnificare.getSoggetto().getDataNascita(),"dd-MM-yyyy")%></font></td>
      <%}%>
      </tr>
      <tr>
        <td class="L"><font class="label">Luogo Nascita </font></td>
<%
        if (fasUnificante.getSoggetto().getDescrComuneNascita().compareTo("-")==0)
        {
%>
          <td class="L"><font class="campo"><%=fasUnificante.getSoggetto().getDescrStatoNascita()%> </font></td>
<%
        }
        else
        {%>
          <td class="L"><font class="campo"><%=fasUnificante.getSoggetto().getDescrComuneNascita() + "  ("+fasUnificante.getSoggetto().getCodProvinciaNascita()+")" %></font></td>
      <%}%>
      <%
        if (fasDaUnificare.getSoggetto().getDescrComuneNascita().compareTo("-")==0)
        {%>
          <td class="L"><font class="campo"><%=fasDaUnificare.getSoggetto().getDescrStatoNascita()%> </font></td>
      <%}else{%>
          <td class="L"><font class="campo"><%=fasDaUnificare.getSoggetto().getDescrComuneNascita() + "  ("+fasDaUnificare.getSoggetto().getCodProvinciaNascita()+")" %></font></td>
      <%}%>
      </tr>
  
      <tr>
        <td class="l">Numero SIEP</font></td>
        <td class="l"><font class="campo">
<%        if ((fasUnificante.getFascicoloSiep() != null && fasUnificante.getFascicoloSiep().getChiaveAnno()!= null) )
          {%>
            <%=fasUnificante.getFascicoloSiep().getChiaveAnno() %>/<%=fasUnificante.getFascicoloSiep().getChiaveProgr()%>
        <%}else{
        %>-&nbsp;<%}%>
          </font>
        </td>
        <td class="l"><font class="campo">
<%        if ((fasDaUnificare.getFascicoloSiep() != null && fasDaUnificare.getFascicoloSiep().getChiaveAnno()!= null) )
          {%>
            <%=fasDaUnificare.getFascicoloSiep().getChiaveAnno() %>/<%=fasDaUnificare.getFascicoloSiep().getChiaveProgr()%>
        <%}else{
            %>-&nbsp;<%}%>
          </font>
        </td>

      </tr>

      <tr>
          <td class="l">Numero SIGE</td>
          <td class="l"><font class="campo"><%=fasUnificante.getFascicoloSige().getChiaveAnno() %>/<%=fasUnificante.getFascicoloSige().getChiaveProgr() %></font></td>
          <td class="l"><font class="campo"><%=fasDaUnificare.getFascicoloSige().getChiaveAnno() %>/<%=fasDaUnificare.getFascicoloSige().getChiaveProgr() %></font></td>
      </tr>
      <tr>
          <td class="l">Data Fine Pena</td>
            <%
            if (DateUtils.getDateToString(fasUnificante.getFascicoloSige().getDataFinePena(),"dd-MM-yyyy") == null)
            {%>
              <td class="L"><font class="campo">-</font></td>
          <%}else{%>
          <td class="l"><font class="campo"><%=DateUtils.getDateToString(fasUnificante.getFascicoloSige().getDataFinePena(),"dd-MM-yyyy")%></font></td>
          <%}
            if (DateUtils.getDateToString(fasDaUnificare.getFascicoloSige().getDataFinePena(),"dd-MM-yyyy") == null)
            {%>
              <td class="L"><font class="campo">-</font></td>
          <%}else{%>
          <td class="l"><font class="campo"><%=DateUtils.getDateToString(fasDaUnificare.getFascicoloSige().getDataFinePena(),"dd-MM-yyyy")%></font></td>
          <%}%>
      </tr>
      <tr>
          <td class="l">Posizione Giuridica</td>
          <td class="l"><font class="campo"><%=fasUnificante.getFascicoloSige().getDescrPosizioneGiuridica() %></font></td>
          <td class="l"><font class="campo"><%=fasDaUnificare.getFascicoloSige().getDescrPosizioneGiuridica() %></font></td>
      </tr>
      <tr>
          <td class="l">Data Iscrizione</td>
          <td class="l"><font class="campo"><%=DateUtils.getDateToString(fasUnificante.getFascicoloSige().getDataIscrizione(),"dd-MM-yyyy")%></font></td>
          <td class="l"><font class="campo"><%=DateUtils.getDateToString(fasDaUnificare.getFascicoloSige().getDataIscrizione(),"dd-MM-yyyy")%></font></td>
      </tr>
      <tr>
          <td class="l">Tipo Atto</td>
          <td class="l"><font class="campo"><%=fasUnificante.getRichiestaSige().getDescrTipoAtto() %></font></td>
          <td class="l"><font class="campo"><%=fasDaUnificare.getRichiestaSige().getDescrTipoAtto() %></font></td>
      </tr>
      <tr>
          <td class="l">Data Atto</td>
          <% if ( DateUtils.getDateToString(fasUnificante.getRichiestaSige().getDataEmissione(),"dd-MM-yyyy") == null ) {%>
              <td class="L"><font class="campo">-</font></td>
          <%}else { %>
          <td class="l"><font class="campo"><%=DateUtils.getDateToString(fasUnificante.getRichiestaSige().getDataEmissione(),"dd-MM-yyyy") %></font></td>
          <% } %>
          <% if ( DateUtils.getDateToString(fasDaUnificare.getRichiestaSige().getDataEmissione(),"dd-MM-yyyy") == null ) {%>
              <td class="L"><font class="campo">-</font></td>
          <%}else { %>
          <td class="l"><font class="campo"><%=DateUtils.getDateToString(fasDaUnificare.getRichiestaSige().getDataEmissione(),"dd-MM-yyyy") %></font></td>
          <% } %>
      </tr>
      <tr>
          <td class="l">Autorità Mittente</td>
          <td class="l"><font class="campo"><%=fasUnificante.getRichiestaSige().getDescrTipoRichiedente() %></font></td>
          <td class="l"><font class="campo"><%=fasDaUnificare.getRichiestaSige().getDescrTipoRichiedente() %></font></td>
      </tr>
      <tr>
          <td class="l">Sede Mittente</td>
          <td class="l"><font class="campo"><%=fasUnificante.getRichiestaSige().getDescrSedeRichiedente() %></font></td>
          <td class="l"><font class="campo"><%=fasDaUnificare.getRichiestaSige().getDescrSedeRichiedente() %></font></td>
      </tr>

      <tr>
          <td class="l">Oggetto</td>
          <td class="l">
            <font class="campo">
<%
			  Iterator itx = tenoriUnificante.iterator();
              while( itx.hasNext() )
              {
            	  TenoreSigeModel tenSigeMod1 = (TenoreSigeModel)itx.next();
%>
                <%=tenSigeMod1.getDescrOggettoSige()%><BR>
<%
              }
%>
            &nbsp;</font>
          </td>

          <td class="l">
            <font class="campo">
<%
			  Iterator itx1 = tenoriUnificato.iterator();
			  while( itx1.hasNext() )
			  {
	  			TenoreSigeModel tenSigeMod2 = (TenoreSigeModel)itx1.next();
%>
  				<%=tenSigeMod2.getDescrOggettoSige()%><BR>
<%
			  }
%>
            &nbsp;</font>
          </td>
      </tr>

      <tr>
          <td class="l">Note</td>
          <td class="l">
            <font class="campo">
<%            if ((fasUnificante.getFascicoloSige().getNote()!= null) && (fasUnificante.getFascicoloSige().getNote().length()>0))
              {
%>
                <%=fasUnificante.getFascicoloSige().getNote()%>
<%
              }
              else
              {
                %>-&nbsp;<%}%>

            </font>
          </td>

          <td class="l">
            <font class="campo">
<%            if ((fasDaUnificare.getFascicoloSige().getNote()!= null) && (fasDaUnificare.getFascicoloSige().getNote().length()>0))
              {
%>
                <%=fasDaUnificare.getFascicoloSige().getNote()%>
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
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_GG_UNIFICAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_MM_UNIFICAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_AAAA_UNIFICAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
		
		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadInserisciDecretoUnificazioneSige','<%=ICostantiDecretoUnificazioneSige.CAMPO_DATA_AAAA_UNIFICAZIONE%>','<%=ICostantiDecretoUnificazioneSige.CAMPO_DATA_MM_UNIFICAZIONE%>','<%=ICostantiDecretoUnificazioneSige.CAMPO_DATA_GG_UNIFICAZIONE%>');">
      	      <img src="/images/calendario.gif" border=0>
       	  </a>
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

    <input type="HIDDEN" name="<%= ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF %>" value="<%=fasDaUnificare.getFascicoloSige().getChiaveAnno()%>">
    <input type="HIDDEN" name="<%= ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF %>" value="<%=fasDaUnificare.getFascicoloSige().getChiaveProgr()%>">
    <input type="HIDDEN" name="<%= ICostantiDecretoUnificazioneSige.CAMPO_ANNO_UNIFICANTE %>" value="<%=fasUnificante.getFascicoloSige().getChiaveAnno()%>">
    <input type="HIDDEN" name="<%= ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_UNIFICANTE %>" value="<%=fasUnificante.getFascicoloSige().getChiaveProgr()%>">

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciDecretoUnificazioneSige");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_GG_UNIFICAZIONE%>","req","Il campo Giorno della Data Unificazione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_GG_UNIFICAZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_GG_UNIFICAZIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_GG_UNIFICAZIONE%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_MM_UNIFICAZIONE%>","req","Il campo Mese della Data Unificazione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_MM_UNIFICAZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_MM_UNIFICAZIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_MM_UNIFICAZIONE%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_AAAA_UNIFICAZIONE%>","req","Il campo Anno della Data Unificazione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_AAAA_UNIFICAZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_AAAA_UNIFICAZIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiDecretoUnificazioneSige.CAMPO_DATA_AAAA_UNIFICAZIONE%>","lt=2999");
  </script>
  </body>
</html>