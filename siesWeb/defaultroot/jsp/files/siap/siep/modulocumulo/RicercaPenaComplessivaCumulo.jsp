<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.List"%>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaComplessivaCumulo" %>
<%@ page import="siap.siep.modulocumulo.model.DettaglioPenaComplessivaCumuloModel" %>
<%@ page import="siap.siep.modulocumulo.model.PenaComplessivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiSanzioneSostitutivaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.ContinuazioneCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiContinuazioneCumulo"%>


<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>



<jsp:useBean id="IstruttoriaCumulo"           scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"              scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="dettaglioPenaComplessivaCum" scope="request" class="siap.siep.modulocumulo.model.DettaglioPenaComplessivaCumuloModel" />

<%

  PenaComplessivaCumuloModel lPenCom = dettaglioPenaComplessivaCum
      .getPenaComplessivaSanzioneSostitutivaCumulo()
      .getPenaComplessivaCumulo();
      
  SanzioneSostitutivaCumuloModel lSanSos = dettaglioPenaComplessivaCum
      .getPenaComplessivaSanzioneSostitutivaCumulo()
      .getSanzioneSostitutivaCumulo();
      
  if (lSanSos == null)
    lSanSos = new SanzioneSostitutivaCumuloModel();      
      
  List lListCont = dettaglioPenaComplessivaCum.getContinuazioni();
  
  String multa=new String("&nbsp;");
  if(lPenCom.getImportoMulta()!=null && lPenCom.getImportoMulta().compareTo(new BigDecimal(0))!=0 ) 
  		multa="€ "+ StringUtils.toEuroFormat(lPenCom.getImportoMulta());

  String ammenda=new String("&nbsp;");
  if(lPenCom.getImportoAmmenda()!=null && lPenCom.getImportoAmmenda().compareTo(new BigDecimal(0))!=0 ) 
	  ammenda="€ "+ StringUtils.toEuroFormat(lPenCom.getImportoAmmenda());

%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Pena Complessiva</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    
    <script language="JavaScript">  
    
    //==========================================================================
    // Ritorna alla Griglia dei dati analitici
    //==========================================================================
    function tornaIndietro(action)
    {
      document.RicercaPenaComplessivaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.RicercaPenaComplessivaCumulo.submit();
    }
    
    function visualizzaMotivoCont(idRecord)
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
    // Richiama l'opportuna azione
    //==========================================================================
    function eseguiAzione(aTipoAzione, aIdPena, aStato, aMotivoModifica)
    {  
        if (aTipoAzione=='Dettaglio')
        {
          lAzione = "siap.siep.modulocumulo.action.ActLoadDettaglioPenaComplessivaCumulo";
          document.RicercaPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_ID_PENA_COMPLESSIVA_CUM%>.value = aIdPena;
          document.RicercaPenaComplessivaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.RicercaPenaComplessivaCumulo.submit();
        }
        else if (aTipoAzione=='Modifica')
        {
          lAzione = "siap.siep.modulocumulo.action.ActLoadModificaPenaComplessivaCumulo";
          document.RicercaPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_ID_PENA_COMPLESSIVA_CUM %>.value = aIdPena;
          document.RicercaPenaComplessivaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.RicercaPenaComplessivaCumulo.submit();
        }    
        else if (aTipoAzione=='Cancella')
        {
          if (aStato=='E' || aStato=='M')
          {
            // Cancellazione Logica Richiedo Motivazione
            lAzione = "siap.siep.modulocumulo.action.ActCancellaPenaComplessivaCumulo";
            document.RicercaPenaComplessivaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
            document.RicercaPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_ID_PENA_COMPLESSIVA_CUM %>.value = aIdPena;
            document.RicercaPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_FLAG_STATO %>.value = aStato;
            var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                 + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"RicercaPenaComplessivaCumulo"
                                 + "&" + "<%=ICostantiPenaComplessivaCumulo.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
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
              lAzione = "siap.siep.modulocumulo.action.ActCancellaPenaComplessivaCumulo";
              document.RicercaPenaComplessivaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
              document.RicercaPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_ID_PENA_COMPLESSIVA_CUM%>.value = aIdPena;
              document.RicercaPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_FLAG_STATO%>.value = aStato;
              document.RicercaPenaComplessivaCumulo.submit();
            }
          }
        
       } // Chiude Elsif aTipoAzione=='Cancella
      
    } // Chiude  function eseguiAzione

    //==========================================================================
    // Richiama l'opportuna azione per le Continuazioni
    //==========================================================================
    function eseguiAzioneCont(aTipoAzione, aIdPena, aIdCont, aStato, aMotivoModifica)
    {
    
        if (aTipoAzione=='Modifica')
        {
          lAzione = "siap.siep.modulocumulo.action.ActLoadModificaContinuazioneCumulo";
          document.RicercaPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_ID_PENA_COMPLESSIVA_CUM %>.value = aIdPena;
          document.RicercaPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ID_CONTINUAZIONE_CUM %>.value = aIdCont;
          document.RicercaPenaComplessivaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.RicercaPenaComplessivaCumulo.submit();
        }    
        else if (aTipoAzione=='Cancella')
        {
	          if (aStato=='E' || aStato=='M')
	          {
	              // Cancellazione Logica Richiedo Motivazione
	              lAzione = "siap.siep.modulocumulo.action.ActCancellaContinuazioneCumulo";
	              document.RicercaPenaComplessivaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
	              document.RicercaPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_ID_PENA_COMPLESSIVA_CUM %>.value = aIdPena;
	              document.RicercaPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ID_CONTINUAZIONE_CUM %>.value = aIdCont;
	              document.RicercaPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_FLAG_STATO_CON_CUM %>.value = aStato;
	              var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
	                                     + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"RicercaPenaComplessivaCumulo"
	                                     + "&" + "<%=ICostantiContinuazioneCumulo.CAMPO_MOTIVO_INS_MOD%>" + "="+aMotivoModifica
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
	                  lAzione = "siap.siep.modulocumulo.action.ActCancellaContinuazioneCumulo";
	                  document.RicercaPenaComplessivaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
	                  document.RicercaPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_ID_PENA_COMPLESSIVA_CUM%>.value = aIdPena;
	                  document.RicercaPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ID_CONTINUAZIONE_CUM %>.value = aIdCont;
	                  document.RicercaPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_FLAG_STATO_CON_CUM %>.value = aStato;
	                  document.RicercaPenaComplessivaCumulo.submit();
	                }
	            }
        
          } // Chiude Elsif aTipoAzione=='Cancella
      
    } // Chiude  function eseguiAzioneCont


    
    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function UltContinuaCumulo(aIdPena)
    {
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciUlterioriContinuazioniCumulo";
        document.RicercaPenaComplessivaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.RicercaPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_ID_PENA_COMPLESSIVA_CUM%>.value = aIdPena;
        document.RicercaPenaComplessivaCumulo.submit();
    }     

    
  </script>   
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>
        <font class="campo">Pena Complessiva relativa al Titolo Cumulato</font>
      </td>
    
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
        <jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>"/>
      </td>
    </tr>
  </table>  
  <br>
  
<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="RicercaPenaComplessivaCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  
  <!-- Campi valorizzati dinamicamente dalla eseguiAzione() e dalla eseguiAzioneCont()=-->
  <input type="hidden" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_ID_PENA_COMPLESSIVA_CUM %>" value="">
  <input type="hidden" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_MOTIVO_MODIFICA%>" value="">
  <input type="hidden" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_FLAG_STATO%>" value="">  
  
  <input type="hidden" name="<%=ICostantiContinuazioneCumulo.CAMPO_ID_CONTINUAZIONE_CUM %>" value=""> 
  <input type="hidden" name="<%=ICostantiContinuazioneCumulo.CAMPO_FLAG_STATO_CON_CUM %>" value="">
  <input type="hidden" name="<%=ICostantiContinuazioneCumulo.CAMPO_MOTIVO_INS_MOD %>" value=""> 
  
  <table width=95%>
    <tr>
      <td class="Titolo" colspan="8">Pena Complessiva/Sanzione Sostitutiva</td>
    </tr>
    <tr>
      <td class="int">Ergastolo</td>
      <td class="int">Reclusione</td>
      <td class="int">Multa</td>
      <td class="int">Arresto</td>
      <td class="int">Ammenda</td>
      <td class="int">Tipo Sanzione Sostitutiva</td>
      <td class="int">Durata Sanzione Sostitutiva</td>
      <td class="int" width="10%">Azioni</td>
    </tr>
     
    <tr>
      <td class="l">
        <%=StringUtils.toStringJSP(lPenCom.getDescrTipoPenaDetentivaDB())%>&nbsp;
      </td>
      <td class="l">
        <% if (!lPenCom.isQuantumReclusioneZero()) { %>
        Anni&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumAnniReclusione(),"0")%>&nbsp;
        Mesi&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumMesiReclusione(),"0")%>&nbsp;
        Giorni&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumGiorniReclusione(),"0")%>
        <% } else { %>
        &nbsp;
        <% } %>        
      </td>
      <td class="l"><center><%=multa%></center></td>
      <td class="l">
        <% if (!lPenCom.isQuantumArrestoZero()) { %>
        Anni&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumAnniArresto(), "0")%>&nbsp;
        Mesi&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumMesiArresto(), "0")%>&nbsp;
        Giorni&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumGiorniArresto(),"0")%>
        <% } else { %>
        &nbsp;
        <% } %> 
      </td>
      <td class="l"><center><%=ammenda%></center></td>
      <td class="l">
        <%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%>&nbsp;
      </td>
      <td class="l">
        <% if (!lSanSos.isDurataSanzioneZero()) { %>
        Anni&nbsp;<%=StringUtils.toStringJSP(lSanSos.getNumAnni(), "0")%>&nbsp;
        Mesi&nbsp;<%=StringUtils.toStringJSP(lSanSos.getNumMesi(), "0")%>&nbsp;
        Giorni&nbsp;<%=StringUtils.toStringJSP(lSanSos.getNumGiorni(),"0")%>&nbsp;
        <% } %>        
        <% if (lSanSos.getSanzionePecuniariaMulta()!=null) { %>
        Multa&nbsp;<%=StringUtils.toStringJSP(lSanSos.getSanzionePecuniariaMulta())%>&nbsp;
        <% } %>
        <% if (lSanSos.getSanzionePecuniariaAmmenda()!=null) { %>
        Ammenda&nbsp;<%=StringUtils.toStringJSP(lSanSos.getSanzionePecuniariaAmmenda())%>&nbsp;
        <% } %>        
        &nbsp;
      </td>
      <td class="c">
        <a href="javascript:eseguiAzione('Dettaglio',<%=lPenCom.getIdPenaComplessivaCum() %> )">
          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0"></a>
        
        <%
        //======================================================================
        // Modifiche consentite solo ad istruttoria aperta e su dati non Cancellati
        //======================================================================

        if (IstruttoriaCumulo.getFlagStato().equals("A")
            && !lPenCom.getFlagStato().equals("C")) {
        %>
          <a href="javascript:eseguiAzione('Modifica',<%=lPenCom.getIdPenaComplessivaCum()%>)">
            <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
          <a href="javascript:eseguiAzione('Cancella',<%=lPenCom.getIdPenaComplessivaCum() %>,'<%=lPenCom.getFlagStato() %>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lPenCom.getMotivoModifica()),"") %>')">
            <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
        <% } %>
      </td>
    </tr>
  </table>

<%
//==============================================================================
// Eventuali sentenze in continuazione
//==============================================================================
if (lListCont.size() != 0) {
%>
    <br>
    <table width=95%>
      <tr><td class="Titolo" colspan="100%">Sentenze in Continuazione</td></tr>
      <tr>
        <td class="int">&nbsp;</td>
        <td class="int">Tipo Continuazione</td>
        <td class="int">Anno/Numero Sentenza</td>
        <td class="int" nowrap>Data Sentenza</td>
        <td class="int">Autorità Sentenza</td>
        <td class="int">Luogo Sentenza</td>
        <td class="int" width="8%" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
        <td class="int" width=10%>Azioni</td>
      </tr>
<%
	int id_record = 0;

    Iterator iter = lListCont.iterator();
    while (iter.hasNext()) 
    {
   	  id_record = id_record +1;
      ContinuazioneCumuloModel lContMod = (ContinuazioneCumuloModel) iter.next();
      
      String lStato = "";
      String lDescStato = "";
      String lFontColor = "";
      String lAnnullata="";
      
      if      ( lContMod.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
      else if ( lContMod.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
      else if ( lContMod.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
      else if ( lContMod.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}

%>
      <tr>
        <td class="l">
        <% if (lContMod.getTitIdTitoloCumulatoCont()==null) { %>
        <img width="12" height="12" border="0" src="<%=IWebConstants.IMAGES_DIR%>attenzione.jpg"  title="Continuazione non ancora associata a un titolo in istruttoria. Selezionare 'modifica' per effettuare l'associazione">
        <% } else { %>
        &nbsp;
        <% } %>
        </td>
      
        <td class="l" <%=lFontColor%> >
          <%=StringUtils.toStringJSP(lContMod.getDescrTipoContinuazione())%>&nbsp;
        </td>
        <td class="l" <%=lFontColor%>>
          <%=StringUtils.toStringJSP(lContMod.getAnnoSentenza())%>
          /
          <%=StringUtils.toStringJSP(lContMod.getNumSentenza())%>
        </td>
        <td class="l" <%=lFontColor%>>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lContMod.getDataSentenza(),"dd-MM-yyyy"))%>&nbsp;
        </td>
        <td class="l" <%=lFontColor%>>
          <%=StringUtils.toStringJSP(lContMod.getDescrTipoAutorita())%>&nbsp;
        </td>
        <td class="l" <%=lFontColor%>>
          <%=StringUtils.toStringJSP(lContMod.getDescrLuogoAutorita())%>&nbsp;
        </td>
        
           <!--     Motivo Inserimento/Modifica   -->
<%  if (lContMod.getMotivoModifica()!=null && lContMod.getMotivoModifica().length()>0) 
  	{ %>
    	<td class="c">
        	<a href="javascript:visualizzaMotivoCont('rec_<%=id_record%>')" title="<%=lDescStato%>">
        	<%=lStato%>
        	</a>
      	</td>  
 <% }
  	else 
  	{%>
      	<td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>
 <% } %>
        
        <td class="c" >
          <a href="javascript:eseguiAzione('Dettaglio',<%=lPenCom.getIdPenaComplessivaCum() %> )">
            <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0"></a>
          <%
          //======================================================================
          // Modifiche consentite solo ad istruttoria aperta e su dati non Cancellati
          //======================================================================
          if (    IstruttoriaCumulo.getFlagStato().equals("A")
               && !lContMod.getFlagStato().equals("C")) 
          {
          %>
            <a href="javascript:eseguiAzioneCont('Modifica',<%=lPenCom.getIdPenaComplessivaCum()%>,<%=lContMod.getIdContinuazioneCum()%>)">
                <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>          
            <a href="javascript:eseguiAzioneCont('Cancella',<%=lPenCom.getIdPenaComplessivaCum()%>,<%=lContMod.getIdContinuazioneCum()%>,'<%=lContMod.getFlagStato() %>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lContMod.getMotivoModifica()),"") %>')">
                <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
          <% } %>
        </td>
     </tr>
     
   <!-- Record Hidden con le note di Motivo/Modifica-->
     <tr style="display:none" id="rec_<%=id_record%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(lContMod.getMotivoModifica(),"&nbsp;")%>
        </font>
      </td>
     </tr>
     
<%
      }
%>
    </table>
<% } %>

<% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
  <table cellspacing=2 cellpadding=2 width=100%>
    <tr>
        <td>
          <INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci Ulteriori Continuazioni" onClick="javascript:UltContinuaCumulo(<%=lPenCom.getIdPenaComplessivaCum() %>);">
        </td>      
    </tr>
  </table>  
<% } %>
    
  </body>
</html>