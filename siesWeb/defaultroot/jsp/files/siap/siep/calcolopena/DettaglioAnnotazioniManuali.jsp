<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<html>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="java.util.Date" %>

<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="AnnotazioneOrdinanza" scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel" />
<jsp:useBean id="ListaAnnotazioni"     scope="request" class="java.util.Vector" />

<jsp:useBean id="AnnotazioneManualeInserita" scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" />
<jsp:useBean id="lPageGE"                    scope="request" class="java.lang.String" />

<%
//==============================================================================
// Finestra di visualizzazione del dettaglio delle Annotazioni Manuali legate
// alle Decisioni del GE.
// Visualizza il dettaglio dell'annotazione inserita e consente:
// - l'inserimento di un'altra annotazione (tasto 'Aggiungi')
// - di effettuare il calcolo della pena (tasto 'Calcolo Pena')
// - di passare direttamente alla pagina delle stampe (tasto 'Stampe')
//==============================================================================

Date dataScarcerazione = (Date)request.getAttribute("dataScarcerazione");
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
siesLogger.debug("dataScarcerazione = "+dataScarcerazione);

%>

<head>
  <title> [S.I.E.S.] - Dettaglio Calcolo Pena Benefici - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
    function funSubmit(azione)
    {
      switch(azione)
      {
        case "CALCOLO":
          if (CheckDataScarcerazione('f'))
            document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActCalcoloPenaComputo";
          else
            return false;
        break; //si ferma qui

        case "AGGIUNGI":
        <% if(lPageGE.equals("AMNI")) { %>
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActLoadNuovoCalcoloPenaBenefici";
        <% } else if(lPageGE.equals("DEPEN")) { %>
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActLoadGEDepen";
        <%} else {%>
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActLoadGEIncost";
        <% } %>
        break; //si ferma qui

        case "STAMPA":
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.annotazionemanuale.action.ActLoadStampeAnnotazioniBenefici";
        break; //si ferma qui
      }

      document.f.CALCOLO.disabled=true;
      document.f.AGGIUNGI.disabled=true;
      document.f.STAMPA.disabled=true;

      document.f.submit();
    }
    
    //==========================================================================
    // Verifica la correttezza della data di scarcerazione
    //==========================================================================
    function CheckDataScarcerazione(a_formname){
      var giornoScarcerazione = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value;
      var meseScarcerazione   = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value;
      var annoScarcerazione   = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;

      if (giornoScarcerazione.length==1) {
        giornoScarcerazione = "0"+giornoScarcerazione;
        document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value = giornoScarcerazione ;
      }
      
      if (meseScarcerazione.length==1) {
        meseScarcerazione = "0"+meseScarcerazione;
        document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value = meseScarcerazione ;
      }
      
      
      var dataScarcerazione = giornoScarcerazione+'/'+meseScarcerazione+'/'+annoScarcerazione;

      if (dataScarcerazione!="//"){
        if (!ControllaDataPassaVuota(dataScarcerazione) ) {
          alert('Data Scarcerazione non valida');
          document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE %>.focus();
          return false;
        }
      }
      return true;
    }
    
  </script>
</head>
<body class="corpo">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_EVE_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(AnnotazioneManualeInserita.getEveIdEvento())%>">
    <input type="HIDDEN" name="lFlagPage" value="<%=lPageGE%>">

    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>" value="<%=StringUtils.toStringJSP(AnnotazioneManualeInserita.getCodTipoAnnotazione())%>">
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	<%--
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%=StringUtils.toStringJSP(AnnotazioneOrdinanza.getEvento().getCodMotivo())%>">
	--%>
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <% if(lPageGE.equals("AMNI")) { %>
        <font class="campo">Dettaglio Amnistia / Indulto</font>
        <% } else if(lPageGE.equals("DEPEN")) { %>
        <font class="campo">Dettaglio Depenalizzazione</font>
        <% } else {%>
        <font class="campo">Dettaglio Incostituzionalità</font>
        <% } %>
      </td>
      <td class="LBG">
        <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>" />
           <jsp:param name="ValoreIdEntita" value="<%=AnnotazioneManualeInserita.getIdAnnotazioneManuale()%>" />
        </jsp:include>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <%
  //============================================================================
  //
  //============================================================================
  if(AnnotazioneOrdinanza.getAnnotazioneManuale() != null && AnnotazioneOrdinanza.getEvento() != null)
  {
    AnnotazioneManualeModel OrdinanzaGEAnn = AnnotazioneOrdinanza.getAnnotazioneManuale();
    EventoModel OrdinanzaGEEve = AnnotazioneOrdinanza.getEvento();
  %>
  <table width="70%">
    <tr><td colspan=3 class="Titolonocap">Decisione del Giudice dell' Esecuzione</td></tr>
    <tr>
      <td class="l">Declaratoria :</td>
      <%  if(OrdinanzaGEAnn.getChiaveAnnoSige()!= null && OrdinanzaGEAnn.getChiaveNumeroSige()!=null){%>
        <td class="l">
          Anno/Numero Procedimento SIGE
          <font class="campo">&nbsp;
            <%=StringUtils.toStringJSP( OrdinanzaGEAnn.getChiaveAnnoSige() )%>/<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getChiaveNumeroSige())%>
          </font>
        </td>
      <%  
      } else {
      %>
      <td class="l">
        Anno/Numero Ordinanza
        <font class="campo">&nbsp;
          <%=StringUtils.toStringJSP( OrdinanzaGEAnn.getAnnoGe() )%>/<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumeroGe())%>
        </font>
      </td>
      <% } %>
      <td class="l">
        <font class="label">in data </font>
        &nbsp;&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "dd-MM-yyyy"))%>&nbsp;
        </font>
      </td>
    </tr>

    <tr>
      <td class="l">Ufficio :</td>
      <td class="l" colspan=2>
        <font class="campo">
          <%=StringUtils.toStringJSP(OrdinanzaGEEve.getDescrUfficioEmittente())%>&nbsp;
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Sede :</td>
      <td class="l" colspan=2>
        <font class="campo">
          <%=StringUtils.toStringJSP(OrdinanzaGEEve.getDescrLuogoEmittente())%>&nbsp;
        </font>
      </td>
    </tr>
  </table>
  <% } %>

  <%
  //============================================================================
  // Lista dei periodi concessi
  //============================================================================
  if(!ListaAnnotazioni.isEmpty()) {%>
  <br>
  <table width="80%">
    <tr>
      <td colspan=9 class="Titolonocap">Contenuto Decisione</td>
    </tr>
    <%
    for (Iterator lIter = ListaAnnotazioni.iterator(); lIter.hasNext(); ) 
    {
      AnnotazioneManualeModel lAnnMan = (AnnotazioneManualeModel)lIter.next();

      if(lPageGE.equals("AMNI")) 
      { %>
        <tr>
          <td class="l">Computo beneficio :</td>
          <td class="l" >
            <font class="campo">
              <%=StringUtils.toStringJSP( lAnnMan.getDescrTipoAnnotazione(),"-")%>
            </font>
          </td>
          <td class="l">DPR :</td>
          <td class="l" >
            <font class="campo">
              <%=StringUtils.toStringJSP( lAnnMan.getDescrDpr(),"-")%>
            </font>
          </td>
        </tr>
        
    <%    if(lAnnMan.getFlagConforme().equals("R") ||
          lAnnMan.getFlagConforme().equals("I") ||
          lAnnMan.getFlagConforme().equals("U") ||
          lAnnMan.getFlagConforme().equals("C") ||
          lAnnMan.getFlagConforme().equals("D") ||
          lAnnMan.getFlagConforme().equals("-") )
          { %>
            <tr>
              <td class="l">Esito : </td>
              
          <%    if(lAnnMan.getFlagConforme().equals("R"))
              {     %>
                <td class="l" > 
                  <font color="red" > RIGETTO </font>
                </td>
                
          <%    }
              else if(lAnnMan.getFlagConforme().equals("I"))
              { %>
                <td class="l" > 
                  <font color="red" > DICHIARA INAMMISSIBILE  </font>
                </td>
        <%    }
            else if(lAnnMan.getFlagConforme().equals("U"))
              { %>
                <td class="l" > 
                  <font color="red" > RIUNISCE  </font>
                </td> 
          <%    } 
            else if(lAnnMan.getFlagConforme().equals("C"))
              { %>
                <td class="l" > 
                  <font color="red" > ACCOLTA IN CONFOMITA'  </font>
                </td> 
          <%    } 
            else if(lAnnMan.getFlagConforme().equals("D"))
              { %>
                <td class="l" > 
                  <font color="red" > ACCOLTA IN DIFFORMITA'  </font>
                </td> 
          <%    }
            else if(lAnnMan.getFlagConforme().equals("-"))
              { %>
                <td class="l" > 
                  <font color="red" > ACCOLTA  </font>
                </td> 
          <%    } 
              else
              {  
              }%>
                
            </tr> 
            
          <%  if(lAnnMan != null && lAnnMan.getMotivazioni() != null)
            { %>
              <tr>
                <td class="l">Motivazioni :</td>
              <td class="l" colspan=3>
                    <textarea cols="58" rows="4" name="<%=ICostantiAnnotazioneManuale.CAMPO_MOTIVAZIONI%>"><%=StringUtils.toStringJSP(lAnnMan.getMotivazioni())%></textarea>
                </td>
              </tr> 
          <%  }  %>

        <tr>
              <td colspan=9 class="Titolonocap"></td>
          </tr>

    <%    }
    
      } 
      if(lPageGE.equals("INCOST")) { %>
      <tr>
        <td class="l" width="45%">Sentenza Corte Costituzionale</td>
        <td class="l" >
          Anno/Numero
          <font class="campo">
          <%if(lAnnMan.getAnnoCc() == null || lAnnMan.getAnnoCc().compareTo(new BigDecimal(0))==0)  {%>
          -
          <% } else { %>
          <%=StringUtils.toStringJSP(lAnnMan.getAnnoCc() )%>
          <%} %>
          / <%=StringUtils.toStringJSP(lAnnMan.getNumeroCc(), "-")%>
         </font>
        &nbsp; in data
          &nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataCC(), "dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
      </tr>
    <%
    }

    if(lPageGE.equals("DEPEN")) { %>
      <tr>
        <td class="l" width="20%">Fonte </td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getDescrFonte(),"-")%>&nbsp;
          </font>
        </td>
        <td class="l">
        Anno
        <%if(lAnnMan.getAnnoFonte() == null || lAnnMan.getAnnoFonte().compareTo(new BigDecimal(0))==0){%>
            -
        <%}else{%>
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getAnnoFonte())%></font>
        <%}%>
          Num.
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumeroFonte(),"-")%></font>
          Art.
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getArticolo(),"-")%></font>
        </td>
      </tr>
      <tr>
        <td class="l" width="20%">Art. Qualificante </td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getDescrSottonumerazione(),"-")%>&nbsp;</font>
        </td>
        <td class="l">
          Comma
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getComma(),"-")%></font>
          Let.
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getLettera(),"-")%></font>
          Num.
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumero(),"-")%></font>
        </td>
      </tr>
      <%}%>
      
      
      <tr>
<%if(lPageGE.equals("DEPEN")){%>
        <td class="l" colspan="2">Reclusione :
<%}else{%>
        <td class="l">Reclusione :
<%}%>
          Anni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumAnniReclusione(),"-")%></font>
          Mesi
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumMesiReclusione(),"-")%></font>
          Giorni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumGiorniReclusione(),"-")%></font>
        </td>
        <td class="l">Multa :
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getImportoMulta(),"-")%>&nbsp;
          </font>
        </td>

      </tr>
      <tr>
<%if(lPageGE.equals("DEPEN")){%>

        <td class="l" colspan="2">Arresto :
<%}else{%>
        <td class="l">Arresto :
<%}%>
          Anni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumAnniArresto(),"-")%></font>
          Mesi
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumMesiArresto(),"-")%></font>
          Giorni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumGiorniArresto(),"-")%></font>
        </td>
        <td class="l">Ammenda :
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getImportoAmmenda(),"-")%>&nbsp;
          </font>
        </td>

      </tr>
<%
    }
%>
    </table>
<%
  }
%>
  <br>
  
  
  
  
  <table>
    <tr>
      <td>
        <INPUT class="bottone" type="button" name="CALCOLO" value="Calcolo Pena" onClick="javascript:funSubmit('CALCOLO');">
      </td>
      <td class="l" colspan="3">
        <font class="label">Data Eventuale Scarcerazione</font>
        &nbsp;&nbsp;
        <input type="text" maxlength="2" size="2" 
               name="<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" 
               <% if (dataScarcerazione!=null) { %>
               value="<%=DateUtils.getDayToString(dataScarcerazione)%>" 
               <% } else {%>
               value=""
               <% } %>
               onFocus="javascript:textboxSelect(this)" 
               onkeypress="return TicTabNumField(this,event)" 
               onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" maxlength="2" size="2"
               name="<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>"  
               <% if (dataScarcerazione!=null) { %>
               value="<%=DateUtils.getMonthToString(dataScarcerazione)%>" 
               <% } else {%>
               value=""
               <% } %>
               onFocus="javascript:textboxSelect(this)" 
               onkeypress="return TicTabNumField(this,event)" 
               onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" maxlength="4" size="4"
               name="<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>"  
               <% if (dataScarcerazione!=null) { %>
               value="<%=DateUtils.getYearToString(dataScarcerazione)%>" 
               <% } else {%>
               value=""
               <% } %>
               onFocus="javascript:textboxSelect(this)" 
               onkeypress="return TicTabNumField(this,event)" 
               onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>
  
  <table>
    <tr>
      <!--td colspan=2>
        <INPUT class="bottone" type="button" name="CALCOLO" value="Calcolo Pena" onClick="javascript:funSubmit('CALCOLO');">
      </td-->
      <td colspan=2>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Aggiungi" onClick="javascript:funSubmit('AGGIUNGI');">
      </td>
      <td colspan=2>
        <INPUT class="bottone" type="button" name="STAMPA" value="Stampe" onClick="javascript:funSubmit('STAMPA');">
      </td>
    </tr>
  </table>
  
</form>
</body>
</html>