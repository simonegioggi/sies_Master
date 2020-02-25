<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<html>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>

<jsp:useBean id="ListaAnnotazioni" scope="request" class="java.util.Vector" />

<jsp:useBean id="AnnotazioneManualeInserita" scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" />

<jsp:useBean id="lFlagPage" scope="request" class="java.lang.String" />
<jsp:useBean id="lFlagRich" scope="request" class="java.lang.String" />

<jsp:useBean id="posizioneluogoaltra"    scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>

<jsp:useBean id="UltimaPenRes" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />

<%
//==============================================================================
// jsp utilizzata SOLO per il dettaglio delle 'Richieste al GE'
//==============================================================================
//RICH_AMNI
//RICH_DEPEN
//RICH_INCOST

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();

	Date dataScarcerazione = (Date)request.getAttribute("dataScarcerazione");
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("dataScarcerazione = "+dataScarcerazione);
%>

<head>
  <title> [S.I.E.S.] - Dettaglio Anticipazioni  - </title>
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
          //document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActCalcoloPenaGE";
          if (CheckDataScarcerazione('f'))
            document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActCalcoloPenaComputo";
          else
            return false;
        break; //si ferma qui

        case "AGGIUNGI":
        <% if(lFlagRich.equals("RICH_AMNI")) { %>
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadRichiestaAmnistiaIndulto";
        <% } else if(lFlagRich.equals("RICH_DEPEN")) { %>
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadRichiestaDepenalizzazione";
        <% } else { %>
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadRichiestaIncostituzionalita";
        <% } %>
        break; //si ferma qui

        case "STAMPA":
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.annotazionemanuale.action.ActLoadStampeAnnotazioniRichieste";
        break; //si ferma qui
      }

      <% if(AnnotazioneManualeInserita.isAnticipazione()) { %>
        document.f.CALCOLO.disabled=true;
      <% } %>

      //document.f.AGGIUNGI.disabled=true;

      <%
      // In caso di anticipazione degli effetti è obbligatorio passare per il
      // calcolo della pena per accedere alle stampe
      if(!AnnotazioneManualeInserita.isAnticipazione()) { %>
      document.f.STAMPA.disabled=true;
      <% } %>

      document.f.submit();
    }

    //==========================================================================
    // Verifica la correttezza della data di scarcerazione
    //==========================================================================
    function CheckDataScarcerazione(a_formname){
      if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>==undefined){
        // manca il campo data scarcerazione
        return true;
      }
      else {
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
    }
  </script>
</head>

<body class="corpo">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>" value="<%=StringUtils.toStringJSP(AnnotazioneManualeInserita.getIdAnnotazioneManuale())%>">
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_EVE_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(AnnotazioneManualeInserita.getEveIdEvento())%>">
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>" value="<%=StringUtils.toStringJSP(AnnotazioneManualeInserita.getCodTipoAnnotazione())%>">

    <input type="HIDDEN" name="lFlagPage" value="<%=lFlagPage%>">
    <input type="HIDDEN" name="lFlagRich" value="<%=lFlagRich%>">

    <input type="HIDDEN" name="isAnticipazione" value="<%=AnnotazioneManualeInserita.isAnticipazione()%>">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <% if( lFlagRich.equals("RICH_AMNI") ) { %>
        <font class="campo">Dettaglio Anticipazioni Amnistia / Indulto</font>
        <% } else if( lFlagRich.equals("RICH_DEPEN") ) {%>
        <font class="campo">Dettaglio Anticipazioni Depenalizzazione</font>
        <% } else { %>
        <font class="campo">Dettaglio Anticipazioni Incostituzionalità</font>
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
//==============================================================================
//                             SEZIONE DELLE ANNOTAZIONI
//==============================================================================
  if(!ListaAnnotazioni.isEmpty())
  {
  %>
    <br>
    <table width="90%">
      <tr><td colspan=9 class="Titolonocap">Periodi</td></tr>
      <%
      for (Iterator lIter = ListaAnnotazioni.iterator(); lIter.hasNext(); )
      {
        AnnotazioneManualeModel lAnnMan = (AnnotazioneManualeModel)lIter.next();

        if(lAnnMan.isAnticipazione()) { %>
          <tr>
            <td class="l" colspan="2"><font class="campo"> Anticipazione degli effetti</font></td>
          </tr>
        <%
        }

        // Richiesta depenalizzazione
        if(lFlagRich.equals("RICH_DEPEN"))
        { %>
          <tr>
            <td class="l" width="20%">Fonte </td>
            <td class="l">
              <font class="campo">
                <%=StringUtils.toStringJSP(lAnnMan.getDescrFonte(),"-")%>&nbsp;</font>
            </td>
            <td class="l">
              Anno
              <%
              if(lAnnMan.getAnnoFonte() == null || lAnnMan.getAnnoFonte().compareTo(new BigDecimal(0))==0) {%>
                -
              <% } else { %>
                <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getAnnoFonte())%></font>
              <% } %>
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
      
      <% // MEV37 Depenalizzazione - Inizio  
      	 //			In caso di Richiesta Depenalizzazione potrebbero NON essere stati Inseriti i quantum -	
      		if(lAnnMan.getFlagPiuMeno() == null )
      		{ 
      			lAnnMan.setFlagPiuMeno("-"); 
      		}	
      		// MEV37 Depenalizzazione - Fine %>

<%     }  // fine  RICH_DEPEN
      %>
      
      <tr>
        <td class="l" width="30%">Data Richiesta </td>
         <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataRichiesta(),"dd-MM-yyyy"),"-")%>&nbsp;</font>
        </td>
      </tr>

      <%if(lFlagRich.equals("RICH_AMNI")){%>
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
      <%}%>

      <%if(lFlagRich.equals("RICH_INCOST")){%>
      <tr>
        <td class="l" width="45%">Sentenza Corte Costituzionale</td>
        <td class="l" >
          Anno/Numero
          <font class="campo">
          <%if(lAnnMan.getAnnoCc() == null || lAnnMan.getAnnoCc().compareTo(new BigDecimal(0))==0){%>
            -
          <%}else{%>
            <%=StringUtils.toStringJSP(lAnnMan.getAnnoCc() )%>
          <%}%>
          / <%=StringUtils.toStringJSP(lAnnMan.getNumeroCc(), "-")%>
          </font>
        &nbsp; in data
          &nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataCC(), "dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
      </tr>
      <%}%>

      <!-- cambio dicitura  da 'Tipo Beneficio' a...  -->
<% //   <td class="l">Tipo beneficio</td>	%>
	<tr>
<%    if(lAnnMan.isAnticipazione())
	  { %>
            <td width="40%" class="l">Richiesta Concessione Indulto con Anticipazione degli Effetti :</td>
<%    }
	  else
	  {  %>	  
     		<td class="l">Richiesta Concessione Indulto :</td>
<% 	  }
	
	  if (lAnnMan.getFlagPiuMeno().equals("-")) 
	  {	%>
        <td class="l" ><font class="campo">CONCESSO</font></td>
<%	  }
	  else
	  { %>
        <td class="l" ><font color="red">REVOCATO</font></td>
<%	  } %>
	</tr>
	
    <tr>
      <%if(lFlagRich.equals("RICH_DEPEN") || lFlagRich.equals("RICH_AMNI")){%>
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
           &nbsp;&nbsp;&nbsp;Multa :
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getImportoMulta(),"-")%>&nbsp;
          </font>
        </td>
      </tr>

      <tr>
      <% if(lFlagRich.equals("RICH_DEPEN") || lFlagRich.equals("RICH_AMNI")) { %>
        <td class="l" colspan="2">Arresto :
      <%} else { %>
        <td class="l">Arresto :
      <% } %>
          Anni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumAnniArresto(),"-")%></font>
          Mesi
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumMesiArresto(),"-")%></font>
          Giorni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumGiorniArresto(),"-")%></font>
          &nbsp;&nbsp;&nbsp;Ammenda :
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getImportoAmmenda(),"-")%>&nbsp;
          </font>
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>

      <%
      } // end for
      %>
    </table>
<%
  }  // end if isEmpty
%>

  <br>
  <% if(AnnotazioneManualeInserita.isAnticipazione()) { %>
  <table>
    <tr>
      <td colspan="1">
        <INPUT class="bottone" type="button" name="CALCOLO" value="Calcolo Pena" onClick="javascript:funSubmit('CALCOLO');">
      </td>
      <% if (  //lPosizione!=null && !lPosizione.isLibero()
              UltimaPenRes.getIdPenaResidua()!=null
             && UltimaPenRes.getDataInizio()!=null
             && !DateUtils.isGreater(UltimaPenRes.getDataInizio(),DateUtils.getSysDate())
            )
       { %>
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
      <% } %>
    </tr>
  <% } %>
  </table>

  <br>

  <table>
    <tr>
      <!--td colspan="1">
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Aggiungi" onClick="javascript:funSubmit('AGGIUNGI');">
      </td-->
      <%
      // In caso di anticipazione degli effetti è obbligatorio passare per il
      // calcolo della pena per accedere alle stampe
      if(!AnnotazioneManualeInserita.isAnticipazione()) { %>
      <td colspan="1">
        <INPUT class="bottone" type="button" name="STAMPA" value="Stampe" onClick="javascript:funSubmit('STAMPA');">
      </td>
      <% } %>
    </tr>
  </table>
</form>
</body>
</html>