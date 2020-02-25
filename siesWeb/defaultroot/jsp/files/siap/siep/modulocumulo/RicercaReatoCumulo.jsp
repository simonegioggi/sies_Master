<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.modulocumulo.model.ReatoCumuloModel" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiReatoCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.CircostanzaCumuloModel" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiCircostanzaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="reati"          scope="request" class="java.util.Vector" />
<jsp:useBean id="continuazioni"  scope="request" class="java.util.Hashtable"/>
<jsp:useBean id="circostanzeCum" scope="request" class="java.util.Vector" />


<%
//==================================================================================
// Form per la Ricerca e visualizzazione dei REATI legati a un certo Titolo.(CUMULO)
//
// La form presenta un elenco di REATI già presenti con la possibilità di 
// modificarli, cancellarli o inserirne dei nuovi 
//==============================================================================
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
 
    <title>[S.I.E.S.] - Ricerca Reato - di Totolo Cumulato</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
    //==========================================================================
    // Ritorna alla Griglia dei dati analitici
    //==========================================================================
    function tornaIndietro(action)
    {
      document.RicercaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.RicercaReatoCumulo.submit();
    }

    
    // -----------------------	R E A T I   C U M U L A T I    -------------------
    //==========================================================================
    // Visualizza, nasconde i 2 record con le note del motivo inserimento/modifica
    // del reato nell'istruttoria e le note del reato vere e propie 
    //==========================================================================
    function visualizzaNota(idRecord)
    {
      var riga = document.getElementById(idRecord);  
      if (riga.style.display =="none" )
      {
        riga.style.display = "block";
      }
      else 
      {
        riga.style.display = "none";
      }
    }
 
    //==========================================================================
    // Visualizza, nasconde i 2 record con le note del motivo inserimento/modifica
    // del reato nell'istruttoria e le note del reato vere e propie 
    //==========================================================================
    function visualizzaMotivo(idRecord)
    {
      var rigamot = document.getElementById(idRecord);  
      if (rigamot.style.display =="none" )
      {
        rigamot.style.display = "block";
      }
      else 
      {
        rigamot.style.display = "none";
      }
    }
 
    
    //==========================================================================
    // Richiama l'opportuna azione
    //==========================================================================
    function eseguiAzione(aTipoAzione, aIdRea, aStato, aMotivoModifica)
    {
        if (aTipoAzione=='Dettaglio')
        {
          lAzione = "siap.siep.modulocumulo.action.ActLoadDettaglioReatoCumulo";
          document.RicercaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ID_REATO_CUM %>.value = aIdRea;
          document.RicercaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.RicercaReatoCumulo.submit();
        }
        else if (aTipoAzione=='Modifica')
        {
          lAzione = "siap.siep.modulocumulo.action.ActLoadModificaReatoCumulo";
          document.RicercaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ID_REATO_CUM %>.value = aIdRea;
          document.RicercaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.RicercaReatoCumulo.submit();
        }
        else if (aTipoAzione=='Cancella')
        {
            if (aStato=='E' || aStato=='M')
            {
                // Cancellazione Logica Richiedo Motivazione
                lAzione = "siap.siep.modulocumulo.action.ActCancellaReatoCumulo";
                document.RicercaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
                document.RicercaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ID_REATO_CUM%>.value = aIdRea;
                document.RicercaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_FLAG_STATO %>.value = aStato;
                var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                     + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"RicercaReatoCumulo"
                                     + "&" + "<%=ICostantiReatoCumulo.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
                                     , "CancellaDatoAnalitico","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
                
                window.parent.close();
          
                // N.B. la submit viene effettuare direttamnete dalla finestra di popup
            }
            else if (aStato=='I')
            {
                // Cancellazione fisica richiedo conferma
                var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
                if (window.confirm(msgConfirm)) 
                {
                  lAzione = "siap.siep.modulocumulo.action.ActCancellaReatoCumulo";
                  document.RicercaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
                  document.RicercaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_ID_REATO_CUM%>.value = aIdRea;
                  document.RicercaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_FLAG_STATO%>.value = aStato;
                  document.RicercaReatoCumulo.submit();
                }
            }
        
          } // Chiude Elsif aTipoAzione=='Cancella
      
    } // Chiude  function eseguiAzione
    
    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function nuovoReatoCumulo()
    {
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciReatoCumulo";
        document.RicercaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.RicercaReatoCumulo.submit();
    }     

    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function OrganizzaReatoCumulo()
    {
        lAzione = "siap.siep.modulocumulo.action.ActLoadOrganizzaReatiCumulo";
        document.RicercaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.RicercaReatoCumulo.submit();
    }     

    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function ContinuaReatoCumulo()
    {
      lAzione = "siap.siep.modulocumulo.action.ActLoadModificaContinuazioneReatiCumulo";
      document.RicercaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.RicercaReatoCumulo.submit();
    }
    
    function cancellaContinuazione(progReato)
    {
      lAzione = "siap.siep.modulocumulo.action.ActCancellaContinuazioneReatiCumulo";
      document.RicercaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.RicercaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_PROGR_REATO%>.value = progReato;
      document.RicercaReatoCumulo.submit();
    } 
    
    // ---------- C I R C O S T A N Z E   A G G R A V A N T I -----------------
    //==========================================================================
    // Visualizza, nasconde i record con le motivazioni inserimento/modifica
    // della circostanza  
    //==========================================================================
    function visualizzaMotivoCirco(idRecordCirco)
    {
      var rigamot = document.getElementById(idRecordCirco);  
      if (rigamot.style.display =="none" )
      {
        rigamot.style.display = "block";
      }
      else 
      {
        rigamot.style.display = "none";
      }
    }
    
    //==========================================================================
    // Richiama la funzione di inserimento Nuova CIRCOSTANZE Aggravante
    //==========================================================================
    function nuovaCircosCumulo()
    {
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciCircostanzaCumulo";
        document.RicercaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.RicercaReatoCumulo.submit();
    }
    
    //==========================================================================
    // Richiama l'opportuna azione per le Iconcine CIRCOSTANZE
    //==========================================================================
    function eseguiAzioneCirco(aTipoAzione, aIdCirco, aStato, aMotivoModifica)
    {
        if (aTipoAzione=='Dettaglio')
        {
          lAzione = "siap.siep.modulocumulo.action.ActDettaglioCircostanzaCumulo";
          document.RicercaReatoCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ID_CIRCOSTANZA_CUMULO %>.value = aIdCirco;
          document.RicercaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.RicercaReatoCumulo.submit();
        }
        else if (aTipoAzione=='Modifica')
        {
          lAzione = "siap.siep.modulocumulo.action.ActLoadModificaCircostanzaCumulo";
          document.RicercaReatoCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ID_CIRCOSTANZA_CUMULO %>.value = aIdCirco;
          document.RicercaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.RicercaReatoCumulo.submit();
        }
        else if (aTipoAzione=='Cancella')
        {
            if (aStato=='E' || aStato=='M')
            {
                // Cancellazione Logica Richiedo Motivazione
                lAzione = "siap.siep.modulocumulo.action.ActCancellaCircostanzaCumulo";
                document.RicercaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
                document.RicercaReatoCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ID_CIRCOSTANZA_CUMULO%>.value = aIdCirco;
                document.RicercaReatoCumulo.<%=ICostantiTitoloCumulato.CAMPO_FLAG_STATO %>.value = aStato;
                var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                     + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"RicercaReatoCumulo"
                                     + "&" + "<%=ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
                                     , "CancellaDatoAnalitico","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
                
                window.parent.close();
          
                // N.B. la submit viene effettuata direttamnete dalla finestra di popup
            }
            else if (aStato=='I')
            {
                // Cancellazione fisica richiedo conferma
                var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
                if (window.confirm(msgConfirm)) 
                {
                  lAzione = "siap.siep.modulocumulo.action.ActCancellaCircostanzaCumulo";
                  document.RicercaReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
                  document.RicercaReatoCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ID_CIRCOSTANZA_CUMULO%>.value = aIdCirco;
                  document.RicercaReatoCumulo.<%=ICostantiTitoloCumulato.CAMPO_FLAG_STATO%>.value = aStato;
                  document.RicercaReatoCumulo.submit();
                }
            }
        
          } // Chiude Elsif aTipoAzione=='Cancella
      
    } // Chiude  function eseguiAzioneCirco
    
  </script>

</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class=label>Funzione :</font> <font class=campo>Elenco Reati relativi al Titolo Cumulato</font></td>

        <td class="LBG"><!-- Tasto indietro alla Griglia dei dati analitici -->
          <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaDatiAnalitici')">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>             
    </tr>
  </table>
    
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
 
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
        <td>
          <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
        </td>
    </tr>
    <tr>
        <td>
          <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
        </td>
    </tr>
  </table>  
  
  <br>
  
<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="RicercaReatoCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  
  <!-- Campi valorizzati dinamicamente dalla Funzione eseguiAzione() -->
  <input type="hidden" name="<%=ICostantiReatoCumulo.CAMPO_ID_REATO_CUM%>" value="">
  <input type="hidden" name="<%=ICostantiReatoCumulo.CAMPO_FLAG_STATO%>" value="">
  <input type="hidden" name="<%=ICostantiReatoCumulo.CAMPO_MOTIVO_MODIFICA%>" value="" >
  <input type="hidden" name="<%=ICostantiReatoCumulo.CAMPO_PROGR_REATO%>" value="">
  
  <!-- Campi valorizzati dinamicamente dalla Funzione eseguiAzioneCirco() -->
  	<input type="hidden" name="<%=ICostantiCircostanzaCumulo.CAMPO_ID_CIRCOSTANZA_CUMULO %>" value="">
  	<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_FLAG_STATO%>" value="">
  	<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>" value="" >

<%   
//==============================================================================
//             Visualizzazione: ELENCO dei REATI
//==============================================================================
%>
 <div align=center>
  <table width="90%">
    <tr><td class="titolo" colspan="13" width="90%">Reati</td></tr>
    
    <tr>
        <td class="int" width=5%>N. Reato</td>
        <td class="int" width=20%>Data Reato</td>
        <td class="int">Fonte</td>
        <td class="int">Anno</td>
        <td class="int">Numero</td>
        <td class="int">Articolo</td>
        <td class="int">Art.Qual.</td>
        <td class="int">Comma</td>
        <td class="int">Comma Qual.</td>
        <td class="int">Lettera</td>
        <td class="int">Numero</td>
        <td class="int" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
        <td class="int">Azioni</td>
    </tr>

<%

  int id_record = 0;
  Iterator itx = reati.iterator();
  while ( itx.hasNext())
  {
    id_record = id_record +1;
      
    String lStato = "";
    String lDescStato = "";
    String lFontColor = "";

    ReatoCumuloModel reato = (ReatoCumuloModel)itx.next();
        
    if      (reato.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
    else if (reato.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
    else if (reato.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
    else if (reato.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
      
    String lProgressivo = "";
    
    if(reato.getProgrCircostanza().intValue() == 1) // Reato
      lProgressivo = (reato.getProgrNumeroManuale() != null) ? reato.getProgrNumeroManuale().toString() : reato.getProgrReato().toString();
%>
    <tr>
      <td class="c" <%=lFontColor%> ><%=StringUtils.toStringJSP(lProgressivo)%>&nbsp;</td>
      
    <%  
    if(  reato.getProgrCircostanza().intValue() > 1) {
    %>
    <td class="l">&nbsp;</td>
    <%
    }
    else if(  reato.getProgrCircostanza().intValue() == 1
           && (   reato.getGiornoInizio() != null || reato.getMeseInizio() != null || reato.getAnnoInizio() != null
               || reato.getGiornoFine() != null || reato.getMeseFine() != null || reato.getAnnoFine() != null 
              )
          )
      {
    %>
        <td class="c" <%=lFontColor%> >
        <%    
        if(reato.getGiornoInizio() != null || reato.getMeseInizio() != null || reato.getAnnoInizio() != null)
        {
            String lStrGGInizio = StringUtils.toStringJSP( reato.getGiornoInizio(), "**");
            if ( !lStrGGInizio.equals("**") && lStrGGInizio.length() == 1)
                lStrGGInizio = "0"+lStrGGInizio;

            String lStrMMInizio = StringUtils.toStringJSP( reato.getMeseInizio(), "**");
            if ( !lStrMMInizio.equals("**") && lStrMMInizio.length() == 1)
                lStrMMInizio = "0"+lStrMMInizio;

            String lStrAAInizio = StringUtils.toStringJSP( reato.getAnnoInizio(), "**");
%>
              <%=lStrGGInizio%>-<%=lStrMMInizio%>-<%=lStrAAInizio%>

<%
        }
    
        if((reato.getGiornoInizio() != null || reato.getMeseInizio() != null || reato.getAnnoInizio() != null)
          &&( reato.getGiornoFine() != null || reato.getMeseFine() != null || reato.getAnnoFine() != null))
      { %>
        /
<%      } %>

<%
      if(reato.getGiornoFine() != null || reato.getMeseFine() != null || reato.getAnnoFine() != null)
      {
            String lStrGGFine = StringUtils.toStringJSP( reato.getGiornoFine(), "**");
            if ( !lStrGGFine.equals("**") && lStrGGFine.length() == 1)
                lStrGGFine = "0"+lStrGGFine;

            String lStrMMFine = StringUtils.toStringJSP( reato.getMeseFine(), "**");
            if ( !lStrMMFine.equals("**") && lStrMMFine.length() == 1)
                lStrMMFine = "0"+lStrMMFine;

            String lStrAAFine = StringUtils.toStringJSP( reato.getAnnoFine(), "**");
%>
              
              <%=lStrGGFine%>-<%=lStrMMFine%>-<%=lStrAAFine%>
<%
        }
%>

      </td>
<%  }
    else
    { %>
    
      <td class="l">-</td>

<%  } // Chiude if GiornoDataInizio  %>

      <td class="c" <%=lFontColor%> ><%=StringUtils.toStringJSP(reato.getDescrFonte(),"-")%>&nbsp;</td>
      <td class="c" <%=lFontColor%> ><%=StringUtils.toStringJSP(reato.getAnnoFonte(),"-")%>&nbsp;</td>
      <td class="c" <%=lFontColor%> ><%=StringUtils.toStringJSP(reato.getNumeroFonte(),"-")%>&nbsp;</td>
      <td class="c" <%=lFontColor%> ><%=StringUtils.toStringJSP(reato.getArticolo(),"-")%>&nbsp;</td>
      <td class="c" <%=lFontColor%> ><%=StringUtils.toStringJSP(reato.getDescrSottonumerazione(),"-")%>&nbsp;</td>
      <td class="c" <%=lFontColor%> ><%=StringUtils.toStringJSP(reato.getComma(),"-")%>&nbsp;</td>
      <td class="c" <%=lFontColor%> ><%=StringUtils.toStringJSP(reato.getDescrCommaQualificante(),"-")%>&nbsp;</td>
      <td class="c" <%=lFontColor%> ><%=StringUtils.toStringJSP(reato.getLettera(),"-")%>&nbsp;</td>
      <td class="c" <%=lFontColor%> ><%=StringUtils.toStringJSP(reato.getNumero(),"-")%>&nbsp;</td>

      <% if (reato.getMotivoModificaNote()!=null && reato.getMotivoModificaNote().length()>0) { %>
      <td class="c"> 
          <a href="javascript:visualizzaMotivo('record_<%=id_record%>')" title="<%=lDescStato%>">
              <%=lStato%>
          </a>
      </td>    
      <%  } else { %>
      <td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>
      <%  } %>  

      <td class="c" style="text-align:left" nowrap> &nbsp;
        <%
        //======================================================================
        // Azioni possibili: Cancellazione/Annullamento, Modifica, Dettaglio
        //======================================================================
        %>
        <a href="javascript:eseguiAzione('Dettaglio',<%=reato.getIdReatoCum() %> )">
          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Reato" border="0"></a>
  
        <% 
        //======================================================================
        // Modifiche consentite solo ad istruttoria aperta e su dati non Cancellati
        //======================================================================        
        if(IstruttoriaCumulo.getFlagStato().equals("A") && !reato.getFlagStato().equals("C"))
        { 
        %>
        <a href="javascript:eseguiAzione('Modifica',<%=reato.getIdReatoCum() %>)">
            <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
        <a href="javascript:eseguiAzione('Cancella',<%=reato.getIdReatoCum() %>,'<%=reato.getFlagStato() %>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(reato.getMotivoModificaNote()),"") %>')">
            <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
    <%  } %>
      </td>
    </tr>

    <!-- Record Hidden con le motivazioni -->
    <tr style="display:none" id="record_<%=id_record%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(reato.getMotivoModificaNote(),"&nbsp;")%>
        </font>
      </td>
    </tr>    
     
<% 
  } // Chiude While itx.hasNext
%>    

<%
//==============================================================================
//             Visualizzazione delle eventuali CONTINUAZIONI
//==============================================================================

if (continuazioni.size() > 0) 
{
%>
	<tr><td>&nbsp;</td></tr>
    <tr><td class=titolo colspan="13" width="90%">Continuazione Reati</td></tr>
<%
    String strCont = "";
    String progReatoCont = "";
    String tipoCont = "";
    
    // n.b. i value sono Vector <String> in cui il primo elemento è il TipoContinuazione
    //      i successivi sono i prog reati in continuazione nel formato progr@!progr o progr@!progManuale 
    Collection coll = continuazioni.values(); 
    Iterator itxColl = coll.iterator();
    int conta = 0;
    
    Vector vectCont = new Vector();

    while (itxColl.hasNext()) // ciclo sulle continuazione
    {
      vectCont = (Vector)itxColl.next();
      Iterator itxVect = vectCont.iterator();
      conta = 0;
      %>
      <tr>
        <td class="l" colspan="12">
      <%
      while (itxVect.hasNext()) 
      {
        strCont = (String)itxVect.next();
        String[] arrStr = strCont.split("@!");
        
        if (conta == 0) {
          // Il primo elemento è il tipo continuazione
          if (strCont.equalsIgnoreCase("C2"))
            tipoCont = "CONTINUAZIONE";
          else if (strCont.equalsIgnoreCase("C1"))
            tipoCont = "CONCORSO FORMALE";
          
          %><%=tipoCont%> tra i reati di cui ai nr. <%
        }
        else {
          // i successivi elementi sono i progressivi. n.b. Visualizzo il Progr Manuale arrStr[1]
        %>
          <%=StringUtils.toStringJSP(arrStr[1])%> 
        <%
          progReatoCont = arrStr[0];
        }
        conta++;
      }
      %>
      </td>
      
      
      <%
      // Tasti di azione. Da rivedere le condizioni
      boolean isDatiModificabili = true;
      if (isDatiModificabili) {
      %>
          <td class=c>
            <table>
              <tr>
                <td>
                  <a href="javascript:ContinuaReatoCumulo()">
                      <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0">
                  </a>
                </td>
                <td>
                  <a href="Javascript:cancellaContinuazione('<%=progReatoCont%>');">
                      <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                  </a>
                </td>
              </tr>
            </table>
          </td>
      <% } %>
      </tr>
      <%
        
    } // Chiude While itxColl...
  
  } //  Chiude if continuazioni...
%>
</table>

<% // ===========  TITOLO PRIVO DI REATI ===================
   // ====================================================== %>
   
<table cellspacing="2" cellpadding="2" width="90%">
  <%  if (reati.size()==0) { %>
  <tr>
    <td align="left">Nessun Reato presente</td>
  </tr>
  <%  } %>

  <% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
  <tr>
      <td>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci nuovo Reato" onClick="javascript:nuovoReatoCumulo();">
      </td>
      <td>
        <INPUT class="bottone" type="button" name="ORGANIZZA" value="Organizzazione Reati" onClick="javascript:OrganizzaReatoCumulo();">
      </td>      
      <td>
        <INPUT class="bottone" type="button" name="GESTISCI" value="Gestione Continuazione Reati" onClick="javascript:ContinuaReatoCumulo();">
      </td>      
  </tr>
  <%  } %>    
</table>
<br>

<% 
//===============================================================================================
//        Visualizzazione: ELENCO eventuali CIRCOSTANZE Aggravanti soggettive/Attenuanti
//===============================================================================================
%>
<table width="90%">
  <tr><td class=titolo colspan=11>Aggravanti soggettive/Attenuanti</td></tr>
  <tr>
	<td class="int">Fonte</td>
    <td class="int">Anno</td>
    <td class="int">Numero</td>
    <td class="int">Articolo</td>
    <td class="int">Art.Qual.</td>
    <td class="int">Comma</td>
    <td class="int">Comma Qual</td>
    <td class="int">Lettera</td>
    <td class="int">Numero</td>
      <!--  td class="int">Note</td -->
    <td class="int" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
    <td class="int">Azioni</td>
  </tr>
<%
if (circostanzeCum.size()>0)
{
  String flagApp = null;
  String descBil = null;
  String noteBil = null;
  String flagGiu = null;

  String lStatoCir = "";
  String lDescStatoCir = "";
  String lFontColorCir = "";
  
  int id_record_Cir = 0;
  
  Iterator itx2 = circostanzeCum.iterator();
  while ( itx2.hasNext())
  {
	id_record_Cir = id_record_Cir +1;
	    
	lStatoCir = "";
	lDescStatoCir = "";
	lFontColorCir = "";
	  
    CircostanzaCumuloModel CiReato = (CircostanzaCumuloModel)itx2.next();
    flagApp = CiReato.getFlagSentenzaApplicazPena();
    descBil = CiReato.getDescrBilanciamentoCircostanze();
    noteBil = CiReato.getNoteBilanciamento();
    flagGiu = CiReato.getFlagGiudizioAbbreviato(); 
    
    if      (CiReato.getFlagStato().equals("E")){lStatoCir = "Estratto";   lDescStatoCir = "Dato Estratto originale";}
  	else if (CiReato.getFlagStato().equals("I")){lStatoCir = "Iscritto";   lDescStatoCir = "Inserito manualmente dopo l'estrazione";}
	else if (CiReato.getFlagStato().equals("M")){lStatoCir = "Modificato"; lDescStatoCir = "Dato estratto modificato";}
	else if (CiReato.getFlagStato().equals("C")){lStatoCir = "Cancellato"; lDescStatoCir = "Dato estratto cancellato"; lFontColorCir="style=\"color:gray\"";}

%>

    <tr>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getDescrFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getAnnoFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getNumeroFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getArticolo(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getDescrSottonumerazione(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getComma(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getCommaQualificante(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getLettera(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getNumero(),"-")%></td>

<% 	if (CiReato.getMotivoModifica()!=null && CiReato.getMotivoModifica().length()>0)
	{ %>
      <td class="c"> 
          <a href="javascript:visualizzaMotivoCirco('record_Cir_<%=id_record_Cir%>')" title="<%=lDescStatoCir%>">
              <%=lStatoCir%>
          </a>
      </td>    
<%  }
	else
	{ %>
      <td class="c" title="<%=lDescStatoCir%>">&nbsp;<%=lStatoCir%></td>
<%  } %>
      
      <td class="c" style="text-align:left" nowrap> &nbsp;
        <%
        //======================================================================
        // Azioni possibili: Cancellazione/Annullamento, Modifica, Dettaglio
        //======================================================================
        %>
        <a href="javascript:eseguiAzioneCirco('Dettaglio',<%=CiReato.getIdCircostanzaCumulo() %> )">
          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Circostanza" border="0"></a>
  
        <% 
        //=========================================================================
        // Modifiche consentite solo ad istruttoria aperta e su dati non Cancellati
        //=========================================================================        
        if(IstruttoriaCumulo.getFlagStato().equals("A") && !CiReato.getFlagStato().equals("C"))
        { 
        %>
        <a href="javascript:eseguiAzioneCirco('Modifica',<%=CiReato.getIdCircostanzaCumulo() %>)">
            <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
        <a href="javascript:eseguiAzioneCirco('Cancella',<%=CiReato.getIdCircostanzaCumulo() %>,'<%=CiReato.getFlagStato() %>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(CiReato.getMotivoModifica() ),"") %>')">
            <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
    <%  } %>
      </td>      
    </tr>
    
     <!-- Record Hidden con le motivazioni relative alle Circostanze Aggravanti-->
    <tr style="display:none" id="record_Cir_<%=id_record_Cir%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(CiReato.getMotivoModifica(),"&nbsp;")%>
        </font>
      </td>
    </tr>
    
<%
  } // Chiude While
%>
  </table>
  
  
<%
//==============================================================================
//
//==============================================================================
%>
<table cellspacing=2 cellpadding=2 width=90%>
  <tr>
    <td class="l" rowspan=2>Sentenza di applicazione pena</td>
    <td class="l" rowspan=2>
      <font class="campo">
      <% if(flagApp != null && flagApp.equals("S")) { %>
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
      <% } %>
      </font>&nbsp;
    </td>
        
    <td class="l">Bilanciamento circostanze</td>
    <td class="l">
    <%if(descBil!=null && !descBil.equals("")){%>
      <font class="campo">
        <%=descBil%>
      </font>
    <%}%>
      &nbsp;
    </td>
        
      <td class="l" rowspan=2>Giudizio abbreviato</td>
        <td class="l" rowspan=2>
            <font class="campo">
        <%
              if(flagGiu != null && flagGiu.equals("S"))
              {
        %>
                <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
        <%
              }
        %>
            </font>&nbsp;             
        </td>
        </tr>
        
        <tr>
          <td class="l">Note Bilanciamento circostanze</td>
            <td class="l">
            <%if(noteBil!=null && !noteBil.equals("")){%>
              <font class="campo">
                <%=noteBil%>
              </font>
            <%}%>
              &nbsp;
            </td>
          </tr>

<% }   //  Chiude if(circostanzeCum.size()>0) %>

  </table>
  
<% // ===========  TITOLO PRIVO DI CIRCOSTANZE AGGRAVANTI	=====================
   // =========================================================================== %>

<table cellspacing="2" cellpadding="2" width="90%">
  <%  if (circostanzeCum.size()==0) { %>
  <tr>
    <td align="left">Nessun dato presente</td>
  </tr>
  <%  } %>

  <% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
  <tr>
      <td>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci nuova Circostanza" onClick="javascript:nuovaCircosCumulo();">
      </td>
  </tr>
  <%  } %>    
</table>

</div>
  
 </FORM>

  </body>
</html>