<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>


<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>

<%//evento = ordinanza/decreto %>
<jsp:useBean id="evento"      scope="request" class="siap.sico.evento.model.EventoModel"/>
<%//eventoProv = ultimo evento che punta l'ordinanza (di fatto non utilizzato) %>
<jsp:useBean id="eventoProv"  scope="request" class="siap.sico.evento.model.EventoModel"/>
<%//misalt = MA di concessione %>
<jsp:useBean id="misalt"      scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>

<%// verbaleData ?????????????%>
<jsp:useBean id="verbaleData"        scope="request" class="siap.siep.verbale.model.VerbaleDataInizioModel"/>

<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="vedoDataIntermedia" scope="request" class="java.lang.String" />
<jsp:useBean id="lTotGiorniConcessi" scope="request" class="java.lang.String"/>
<jsp:useBean id="lTotGiorniRDConcessi"  scope="request" class="java.lang.String"/>

<jsp:useBean id="cssa"               scope="request" class="siap.sico.cssa.model.CSSAModel" />
<jsp:useBean id="istitutodetenzione" scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel" />

<html>
<head>
<title>[S.I.E.S.] - Gestione Verbale Sottoscrizione </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
  function Verifica()
  {
      if (document.LoadInserisciVariazVerbaleSott.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value.length==1)
          document.LoadInserisciVariazVerbaleSott.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value='0'+document.LoadInserisciVariazVerbaleSott.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value;
      if (document.LoadInserisciVariazVerbaleSott.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value.length==1)
          document.LoadInserisciVariazVerbaleSott.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value='0'+document.LoadInserisciVariazVerbaleSott.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value;
      var data_to_verify = document.LoadInserisciVariazVerbaleSott.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value+'/'+document.LoadInserisciVariazVerbaleSott.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value+'/'+document.LoadInserisciVariazVerbaleSott.<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>.value;

      if (!ControllaData(data_to_verify))
      {
          document.LoadInserisciVariazVerbaleSott.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.focus();
          alert('Data Pervenimento Richiesta Variazione non valida');
          return false;
      }
      
      if (document.LoadInserisciVariazVerbaleSott.<%=ICostantiVerbale.CAMPO_NOTE%>.value=="")
      {
          document.LoadInserisciVariazVerbaleSott.<%=ICostantiVerbale.CAMPO_NOTE%>.focus();
          alert('Inserire le Motivazioni della Variazione Data Inizio Misura');
          return false;
      }
      
      if (document.LoadInserisciVariazVerbaleSott.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value.length==1)
          document.LoadInserisciVariazVerbaleSott.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value='0'+document.LoadInserisciVariazVerbaleSott.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value;
      if (document.LoadInserisciVariazVerbaleSott.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value.length==1)
          document.LoadInserisciVariazVerbaleSott.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value='0'+document.LoadInserisciVariazVerbaleSott.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value;

      var data_to_verify_m = document.LoadInserisciVariazVerbaleSott.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value+'/'+document.LoadInserisciVariazVerbaleSott.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value+'/'+document.LoadInserisciVariazVerbaleSott.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value;
      if (!ControllaData(data_to_verify_m))
      {
          document.LoadInserisciVariazVerbaleSott.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.focus();
          alert('Data Inizio Misura non valida');
          return false;
      }
      
      <% // AMBROSINO 04/2013 MAC 06-RR-090  %> 
      
      if (document.LoadInserisciVariazVerbaleSott.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA %>.value != ""
        ||document.LoadInserisciVariazVerbaleSott.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA %>.value != ""
        ||document.LoadInserisciVariazVerbaleSott.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA %>.value != "" )
      {
        var dataMisu = document.LoadInserisciVariazVerbaleSott.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA %>.value+'-'+document.LoadInserisciVariazVerbaleSott.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA %>.value+'-'+document.LoadInserisciVariazVerbaleSott.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA %>.value;
        if (  document.LoadInserisciVariazVerbaleSott.<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value != ""
            ||document.LoadInserisciVariazVerbaleSott.<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value != ""
            ||document.LoadInserisciVariazVerbaleSott.<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value != "" )
        {
            var dataPena = document.LoadInserisciVariazVerbaleSott.<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value+'-'+document.LoadInserisciVariazVerbaleSott.<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'-'+document.LoadInserisciVariazVerbaleSott.<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
            if(dataMisu > dataPena)
            {
              //alert("ERRORE : E' STATA DIGITATA UNA NUOVA DATA INIZIO MISURA, MAGGIORE DELLA DATA FINE PENA!\n IMMETTERE UNA DATA CORRETTA");
              document.LoadInserisciVariazVerbaleSott.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA %>.focus();
              var esito = window.confirm("Attenzione! E' STATA DIGITATA UNA NUOVA DATA INIZIO MISURA, MAGGIORE DELLA DATA FINE PENA!\n Si vuole procedere?");
              //alert ("esito = "+esito);
              return esito;
            } 
        }
      }
      
      <% // FINE AMBROSINO 04/2013 MAC 06-RR-090  %>

      return true;
 
  }    <% // CHIUDE funcyion VERIFICA  %>
  
    </script>

</head>


<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;      
        <font class="campo">Variazione Data Inizio Misura</font>
      </td>
    </tr>
  </table>
  
  <br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciVariazVerbaleSott">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.verbale.action.ActInserisciVariazioneVerbaleSottoscrizione">
  
  <table cellspacing=4 cellpadding=2 width=95%>
    <tr>
      <%  if(evento.getCodMotivo().equals("0001") || evento.getCodMotivo().equals("0002") || evento.getCodMotivo().equals("0003")) { %>
        <td class="l" colspan=2>Concessione Affidamento in Prova</td>
      <%} else if(evento.getCodMotivo().equals("2005")) { %>
        <td class="l" colspan=2>Ammissione Provvisoria a Detenzione Domiciliare</td>
      <%} else if(evento.getCodMotivo().equals("2006") || evento.getCodMotivo().equals("2008")) { %>
        <td class="l" colspan=2>Ammissione Provvisoria ad Affidamento in Prova</td>
      <%} else if(evento.getCodMotivo().equals("0005") || evento.getCodMotivo().equals("0010") || evento.getCodMotivo().equals("0013")) { %>
        <td class="l" colspan=2>Concessione Detenzione Domiciliare</td>
      <%} else if(evento.getCodMotivo().equals("0004")) {%>
        <td class="l" colspan=2>Concessione Semilibertà</td>
     <% } else if(evento.getCodMotivo().equals("2245")) {%>
        <td class="l" colspan=2>Concessione Sospensione Condizionata esecuzione parte finale pena detentiva</td>
     <% } else if(evento.getCodMotivo().equals("0011")) {%>
        <td class="l" colspan=2>Concessione Detenzione Domiciliare a Termine</td>
     <% 
  		// 20191120 [SG]: aggiunto codice per gestione ticket
		// Ticket#20191114019 - SIES - mancata registrazione data inizio misura
		// Ticket#20191112019 - 2019/11 Ancona Procura Minori non fa caricare inizio misura
		// Esecuzione presso domicilio della pena detentiva ( TdS )
     }else if(evento.getCodMotivo().equals("0610"))
         {%>
      <td class="l" colspan=2>Esecuzione presso domicilio della pena detentiva ( TdS )</td>
     <% }  %>    
    </tr>

    <% if(verbaleData.getDataPervenimento() != null){ %>
    <tr>
      <td class="l" colspan="1">Data pervenimento del verbale</td>
      <td class="l" colspan="1"><font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(verbaleData.getDataPervenimento(),"dd-MM-yyyy"))%>
        </font></td>
    </tr>
    <% } %>
 
 
 
<%if(evento.getCodMotivo() != null) { %>

    <% 
    if(   verbaleData != null && verbaleData.getDataInizioMisura() != null 
       && (   evento.getCodMotivo().equals("0001") 
           || evento.getCodMotivo().equals("0002") 
           || evento.getCodMotivo().equals("0003")
           || evento.getCodMotivo().equals("2006")
           || evento.getCodMotivo().equals("2008")
         	// 20191120 [SG]: aggiunto codice per gestione ticket
   			// Ticket#20191114019 - SIES - mancata registrazione data inizio misura
   			// Ticket#20191112019 - 2019/11 Ancona Procura Minori non fa caricare inizio misura
   			// Esecuzione presso domicilio della pena detentiva ( TdS )
           || evento.getCodMotivo().equals("0610")
          )
      )
    {
    %>
    <tr>
      <td class="l" colspan="1">Data Sottoscrizione Prescrizioni</td>
      <td class="l" colspan="1"><font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(verbaleData.getDataInizioMisura(),"dd-MM-yyyy"))%>
        </font>
      </td>  
    </tr>
      
    <tr>
      <td class="l">UEPE Competente che ha inviato il verbale</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(cssa.getComune())+ " "+StringUtils.toStringJSP(cssa.getIndirizzo()) %></font></td>
    </tr>    
    <% } %>    
 
    <% 
    if(   verbaleData != null && verbaleData.getIstDetIdIstitutoDetenzione()!= null 
       && !verbaleData.getIstDetIdIstitutoDetenzione().equals("-"))
    {
    %>
        <tr>
            <td class="l">Data Ingresso in istituto</td>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbaleData.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
        </tr>
        <tr>
            <td class="l">Istituto Competente che ha inviato il verbale</td>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getDescrTipoIstituto())+" di "+StringUtils.toStringJSP(istitutodetenzione.getDescrComune()) %></font></td>
        </tr>
    <% } %> 
 
 
    <%
    if(   verbaleData != null && verbaleData.getDataInizioMisura() != null 
       && (   evento.getCodMotivo().equals("0005")
           || evento.getCodMotivo().equals("0010")
           || evento.getCodMotivo().equals("0013")
           || evento.getCodMotivo().equals("2245")
           || evento.getCodMotivo().equals("0011")
           || evento.getCodMotivo().equals("2005")
          )
        )
    {
    %>
    <tr>
      <td class="l" colspan="1">Data Sottoposizione agli obblighi</td>
      <td class="l" colspan="1">
        <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(verbaleData.getDataInizioMisura(),"dd-MM-yyyy"))%> 
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Autorità Competente che ha inviato il verbale</td>
      <td class="l" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(verbaleData.getDescrTipoUfficioFirmatario())+ 
        " di "+StringUtils.toStringJSP(verbaleData.getDescrLuogoUfficioFirmatario())%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Indirizzo</td>
      <td class="l" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(verbaleData.getNote())%></font>
      </td>
    </tr>  
    <% } %>
 
 
 <% } %>
  </table>

<!----------------------------------------- PENA------------------------------------------------------------->
  
  <table cellspacing=4 cellpadding=2>
    <tr>
      <td class="Titolo" colspan=15><font class="label">Pena da Espiare</font></td>
    </tr>

<% if(penaresidua.getFlagErgastolo().equals("N")) {%>
  <tr> 
    <td class="l" colspan = 2><font class="label">Reclusione : </font></td>
    <td class="l"><font class="label">Anni</font></td>
    <td class="r"><font class="campo"><%=penaresidua.getNumAnniReclusione()%></font></td>
    <td class="l"><font class="label">Mesi</font></td>
    <td class="r"><font class="campo"><%=penaresidua.getNumMesiReclusione()%></font></td>
    <td class="l"><font class="label">Giorni</font></td>
    <td class="r"><font class="campo"><%=penaresidua.getNumGiorniReclusione()%></font></td>

    <td>&nbsp;&nbsp;&nbsp;</td>
    
    <td class="l" colspan = 2><font class="label">Arresto :</font></td>
    <td class="l"><font class="label">Anni</font></td>
    <td class="r"><font class="campo"><%=penaresidua.getNumAnniArresto()%></font></td>
    <td class="l"><font class="label">Mesi</font></td>
    <td class="r"><font class="campo"><%=penaresidua.getNumMesiArresto()%></font></td>
    <td class="l"><font class="label">Giorni</font></td>
    <td class="r"><font class="campo"><%=penaresidua.getNumGiorniArresto()%></font></td>
  </tr>

<%} else { %>
  <tr>
    <% if(penaresidua.getFlagErgastolo().equals("S")) {%>
    <td class="l"><font class="campo">ERGASTOLO</font></td>
    <%}else if(penaresidua.getFlagErgastolo().equals("D")){%>
    <td class="l"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font></td>
    <%}%>
  </tr>
<%}%>
</table>


<table>

<% if(penaresidua.getDataInizio() != null){%>
  <tr>
    <td class="l" colspan="1" ><font class="label">Data Decorrenza Pena: </font></td>
    <td class="l" colspan="1" > <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>
      </font></td>
<%  } 


if (vedoDataIntermedia.equals("S")) { %>
    <td class="l" colspan="1"><font  class="label">Data Fine Reclusione : </font></td>
    <td class="l" colspan="1"> <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFineReclusione(),"dd-MM-yyyy"))%>
      </font></td>
  </tr>

  <tr>
    <td class="l" colspan="1"><font  class="label">Data Inizio Arresto : </font></td>
    <td class="l" colspan="1"> <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizioArresto(),"dd-MM-yyyy"))%>
      </font></td>
 <% }  
 
  if(penaresidua.getFlagErgastolo().equals("N"))
  {
        if(penaresidua.getDataFinePresunta() != null)
        {
%>
              <td class="l" colspan="1" ><font  class="label">Data Fine Pena : </font></td>
              <td class="l" colspan="1" >
                  <font class="campo">
                    <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%>
                  </font>
                </td>
         <% // AMBROSINO 04/2013 - mac a6-rr-090   (i 3 campi servonoper un controolo in javascript) %>

              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
        <% // FINE AMBROSINO 04/2013 - mac a6-rr-090  %>

              </tr>
  <%      
              if( lTotGiorniConcessi!=null && !"0".equals(lTotGiorniConcessi) )
              {
    %>
                  <tr>
                    <td class="l">Giorni di Lib. ant. concessi</td>
                    <td class="l">
                      <font class="campo"> <%=StringUtils.toStringJSP(lTotGiorniConcessi)%></font>
                    </td>
                  </tr>
<%
              }
              
          if( lTotGiorniRDConcessi!=null && !"0".equals(lTotGiorniRDConcessi) ) {%>
            <tr>
              <td class="l" nowrap>Giorni di Risarcimento D.L. 92/2014 concessi</td>
              <td class="l" >
                <font class="campo"> <%=StringUtils.toStringJSP(lTotGiorniRDConcessi)%></font>
              </td>
            </tr>
        <% }
         
       } // chiude  if(penaresidua.getDataFinePresunta() != null)
         
  } // Chiude if(penaresidua.getFlagErgastolo().equals("N"))
%>
    </table>
  <br>
<!--------------------------------MISURA ALTERNATIVA ------------------------------------------------>

  <table cellspacing=4 cellpadding=2>
    <tr>
      <td class="l" colspan="1">Data Inizio Misura</td>
      <td class="l" colspan="1"> <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(misalt.getDataInizioMisura(),"dd-MM-yyyy"))%>
      </font></td>
      
      <td class="l" colspan="1">Data Fine Misura</td>
      <td class="l" colspan="1"> <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(misalt.getDataFineMisura(),"dd-MM-yyyy"))%>
      </font></td>  
    </tr>
  
    <tr>
      <td class="l" >Data Pervenimento Richiesta Variazione<font class=ob>(*)</font></td>
      <td class="l">
        <input title="Giorno Pervenimento" size=2 maxlength=2 value="" type="text" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input title="Mese Pervenimento" size=2 maxlength=2 value="" type="text" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input title="Anno Pervenimento" size=4 maxlength=4 value="" type="text" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

    <tr>
      <td class="l">Motivazioni<font class=ob>(*)</font></td>
      <td class="L" colspan="3">
        <TEXTAREA title="Note" name="<%=ICostantiVerbale.CAMPO_NOTE%>" cols=80 rows=2 ></textarea>
      </td>
    </tr> 
    
    <tr>
      <td class="l" >Nuova Data Inizio Misura<font class=ob>(*)</font></td>
      <td class="l">
        <input title="Nuovo Giorno Inizio Misura" size=2 maxlength=2 value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input title="Nuovo Mese Inizio Misura" size=2 maxlength=2 value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input title="Nuovo Anno Inizio Misura" size=4 maxlength=4 value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>    
  </table>
  
  
  <table>    
    <tr>
      <td>
        <br><INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma">
      </td>
    </tr>
  </table>
<!------------  Se non c'è verbale devo usare id evento 04 Provvedimento---------------------->
    <input type="hidden" name="provve"  value="<%=StringUtils.toStringJSP(eventoProv.getIdEvento())%>">
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciVariazVerbaleSott");

    frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>","lt=31");

    frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>","lt=12");

    frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>","gt=1900");
    frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>","lt=2099");
    
    frmvalidator.addValidation("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>","lt=31");

    frmvalidator.addValidation("<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>","lt=12");

    frmvalidator.addValidation("<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","gt=1900");
    frmvalidator.addValidation("<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","lt=2099");

    frmvalidator.setAddnlValidationFunction("Verifica");
  </script>
</body>
</html>