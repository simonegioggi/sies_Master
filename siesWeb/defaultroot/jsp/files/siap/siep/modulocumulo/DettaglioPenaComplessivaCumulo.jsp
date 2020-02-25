<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siep.modulocumulo.model.DettaglioPenaComplessivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaComplessivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaComplessivaSanzioneSostitutivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.ContinuazioneCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaComplessivaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiSanzioneSostitutivaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>


<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<jsp:useBean id="IstruttoriaCumulo"     scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"        scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="dettaglioPenaComplessivaCum"   scope="request" class="siap.siep.modulocumulo.model.DettaglioPenaComplessivaCumuloModel"/>


<%
  PenaComplessivaCumuloModel lPenCom = (dettaglioPenaComplessivaCum!= null && dettaglioPenaComplessivaCum.getPenaComplessivaSanzioneSostitutivaCumulo()!= null) ? dettaglioPenaComplessivaCum.getPenaComplessivaSanzioneSostitutivaCumulo().getPenaComplessivaCumulo() : null;
  SanzioneSostitutivaCumuloModel lSanSos = (dettaglioPenaComplessivaCum!= null && dettaglioPenaComplessivaCum.getPenaComplessivaSanzioneSostitutivaCumulo() != null) ? dettaglioPenaComplessivaCum.getPenaComplessivaSanzioneSostitutivaCumulo().getSanzioneSostitutivaCumulo() : null;
  

  if(lSanSos == null)
    lSanSos = new SanzioneSostitutivaCumuloModel();
%>

<html>
<head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <title>[S.I.E.S.] - Dettaglio Pena Complessiva (CUMULO)</title>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>  
  <script language="JavaScript">  
  //==========================================================================
  // Ritorna alla Griglia dei dati analitici
  //==========================================================================
  function tornaIndietro(action)
  {
    document.DettaglioPenaComplessivaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
    document.DettaglioPenaComplessivaCumulo.submit();
  }  
</script>
</head>



<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Pena Complessiva Relativa al Titolo Cumulato</font>
      </td>
        
      <td class="LBG">
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActRicercaPenaComplessivaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
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

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="DettaglioPenaComplessivaCumulo" >
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  

<%if (lPenCom != null) { %>
  <table cellspacing=2 cellpadding=6 width=95%>
    <tr><td class="Titolo" colspan="4">Pena</td></tr>
    <tr>
      <td class="l" colspan="1">Reclusione</td>
      <% if (!lPenCom.isQuantumReclusioneZero()) { %>
      <td class="l" colspan="1">
        <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumAnniReclusione(), "0")%>&nbsp;</font>
        <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumMesiReclusione(), "0")%>&nbsp;</font>
        <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumGiorniReclusione(), "0")%></font>
      </td>
      <% } else { %>
      <td class="l" colspan="1">&nbsp;</td>
      <% } %>
    
      <td class="l" colspan="1">Multa</td>
      <%if (lPenCom.getImportoMulta() != null && lPenCom.getImportoMulta().intValue()>0) { %>
      <td class="l" colspan="1">
        <font class="campo"><%=StringUtils.toEuroFormat(lPenCom.getImportoMulta())%></font>&nbsp;
        <font class="l">Euro</font>
      </td>
      <% } else { %>
      <td class="l" colspan="1">&nbsp;</td> 
      <% } %> 
    </tr>
    
    <tr>
      <td class="l" colspan="1">Arresto</td>
      <% if ( !lPenCom.isQuantumArrestoZero() ) { %>
      <td class="l" colspan="1">
        <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumAnniArresto(), "0")%>&nbsp;</font>
        <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumMesiArresto(), "0")%>&nbsp;</font>
        <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumGiorniArresto(), "0")%></font>
      </td>
      <% } else { %>
      <td class="l" colspan="1">&nbsp;</td>       
      <% } %>
       
      <td class="l" colspan="1">Ammenda</td>
      <% if (lPenCom.getImportoAmmenda() != null && lPenCom.getImportoAmmenda().intValue()>0 ) {  %>
      <td class="l" colspan="1">
        <font class="campo"><%=StringUtils.toEuroFormat(lPenCom.getImportoAmmenda())%></font>&nbsp;
        <font class="l">Euro</font>
      </td>
      <% } else { %>
      <td class="l" colspan="1">&nbsp;</td>       
      <% } %>    
    </tr>
    
    
    <% 
    if (   lPenCom.getDescrTipoPenaDetentivaDB() != null
        && !lPenCom.getDescrTipoPenaDetentivaDB().equals("-")
       ) 
    { 
    %>
    <tr>
      <td class="l" colspan="1">Ergastolo</td>
      <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getDescrTipoPenaDetentivaDB())%></font>&nbsp;</td>
    </tr>
    <% } %>
    
    <tr>
<% 
    if (!lPenCom.isDurataIsolamentoDiurnoZero()) { 
%>
        <td class="l" colspan="1">Durata Isolamento Diurno</td>
        <td class="l" colspan="3">
            Anni <font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumAnniIsolamentoDiurno(), "0") %>&nbsp;</font>
            Mesi <font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumMesiIsolamentoDiurno(), "0")%>&nbsp;</font>
            Giorni <font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumGiorniIsolamentoDiurno(), "0")%>&nbsp;</font>
        </td>
<% 
    } 
    
    if (lPenCom.getDataPrescrizione() != null) 
    { 
%>
      <td class="l" colspan="1">Data Prescrizione</td>
      <td class="l" colspan="3">
        <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenCom.getDataPrescrizione(),"dd-MM-yyyy"))%>&nbsp;
        </font>
      </td>
<% 
    } 
%>
      </tr>
    </table>  
    
    
    
<%      
    //==============================================================
    // Descrizione dello stato del dato 'pena complessiva cumulo'
    //=============================================================
        String lDescStato = "";
        if      ( lPenCom.getFlagStato().equals("E")){lDescStato = "Dato Estratto dal fascicolo originale";}
        else if ( lPenCom.getFlagStato().equals("I")){lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
        else if ( lPenCom.getFlagStato().equals("M")){lDescStato = "Dato estratto modificato";}
        else if ( lPenCom.getFlagStato().equals("C")){lDescStato = "Dato estratto cancellato";}
%>
<table cellspacing=2 cellpadding=6 width=95%>
  <tr>
    <td class="l">
      <font class="label"><%=lDescStato %></font>
    </td> 
  </tr> 
</table>

    
 <%   
    if(lPenCom.getMotivoModifica() != null && !lPenCom.getMotivoModifica().equals(""))
    {
%>
  <table cellspacing=2 cellpadding=6 width=95%> 
    <tr>
      <td class="l" width=20%><font class="label">Motivo Inserimento </font></td>
      <td class="l" width=80%><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getMotivoModifica())%></font></td>
    </tr>
  </table>
  
<%  } %>

  <br>
      
<% 
//==============================================================================
//
//==============================================================================
if (   lSanSos.getDescrTipoSanzione() != null
    || lSanSos.getNumAnni() != null
    || lSanSos.getNumMesi() != null 
    || lSanSos.getNumGiorni() != null 
    || lSanSos.getSanzionePecuniariaMulta() != null 
    || lSanSos.getSanzionePecuniariaAmmenda() != null
   )  
{ %>
  <table cellspacing=2 cellpadding=4 width=95%>
    <tr>
      <td class="Titolo" colspan=4>Sanzione Sostitutiva</td>
    </tr>
  
    <% if (lSanSos.getDescrTipoSanzione() != null) { %>
    <tr>
        <td class="l" colspan=1 width=30%>Tipo Sanzione Sostitutiva</td>
        <td class="l" colspan=3 width=65%><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%></font>&nbsp;</td>
    </tr>
    <% } 
   
    if (   lSanSos.getNumAnni() != null 
        || lSanSos.getNumMesi() != null 
        || lSanSos.getNumGiorni() != null) 
    {   %>
    <tr>
        <td class="l" colspan=1 width=30%>Durata Sanzione Sostitutiva</td>
        <td class="l" colspan=3 width=65%>
          <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumAnni(), "0")%>&nbsp;</font>
          <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumMesi(), "0")%>&nbsp;</font>
          <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumGiorni(), "0")%></font>
        </td>
    </tr>
    <% } 
  
    if (lSanSos.getSanzionePecuniariaMulta() != null) 
    { %>
      <tr>
          <td class="l" colspan=1 width=30%>Pena Pecuniaria Sostitutiva: Multa</td>
          <td class="l" colspan=3 width=65%><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaMulta())%></font>&nbsp;<font class="l">Euro</font></td>
      </tr>
    <% } %>

    <% if (lSanSos.getSanzionePecuniariaAmmenda() != null) 
    { %>
      <tr>
          <td class="l" colspan=1 width=30%>Pena Pecuniaria Sostitutiva: Ammenda</td>
          <td class="l" colspan=3 width=65%><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
      </tr>
    <% } %>

  </table>
  <%  
    //==============================================================
    // Descrizione dello stato del dato 'Sanzione Sostitutiva cumulo'
    //=============================================================
    lDescStato = "";
    if      ( lSanSos.getFlagStato().equals("E")){lDescStato = "Dato Estratto dal fascicolo originale";}
    else if ( lSanSos.getFlagStato().equals("I")){lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
    else if ( lSanSos.getFlagStato().equals("M")){lDescStato = "Dato estratto modificato";}
    else if ( lSanSos.getFlagStato().equals("C")){lDescStato = "Dato estratto cancellato";}
  %>
  <table cellspacing=2 cellpadding=6 width=95%>
    <tr>
      <td class="l">
        <font class="label"><%=lDescStato %></font>
      </td> 
    </tr>
  </table> 

  <% if(lSanSos.getMotivoModifica() != null && !lSanSos.getMotivoModifica().equals("")) { %>
  <table cellspacing=2 cellpadding=4 width=95%> 
      <tr>
        <td class="l" colspan=1 width=20%><font class="label">Motivo Inserimento </font></td>
        <td class="l" colspan=3 width=80%><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getMotivoModifica())%></font></td>
      </tr>
  </table>  
  <%  } %>

 <%   
    } // End Sanzione sostitutiva
%>
    

 <br>
 
<%  
//==============================================================================
//                        Sentenze in Continuazione
//==============================================================================
List lListCont = dettaglioPenaComplessivaCum.getContinuazioni();
if(lListCont!= null && lListCont.size() != 0)
{ 
%>
  <table cellspacing=2 cellpadding=4 width=95%>

    <%
  
    Iterator iter = lListCont.iterator();
    while (iter.hasNext())
    {
      ContinuazioneCumuloModel lContMod = (ContinuazioneCumuloModel)iter.next();
    %>
    <tr>
      <td class="Titolo" colspan=4>Continuazione con altre sentenze</td>
    </tr>    
    <tr>
      <td class="l" colspan="1">Tipo Continuazione</td>
      <td class="l" colspan="3">
          <font class="campo"><%=StringUtils.toStringJSP(lContMod.getDescrTipoContinuazione())%></font>
      </td>
    </tr>
      <tr>
        <td class="l" colspan=1>Anno/Numero Sentenza</td>
        <td class="L" colspan="3">
            <font class="campo">
            <%=StringUtils.toStringJSP(lContMod.getAnnoSentenza())%>
            /
            <%=StringUtils.toStringJSP(lContMod.getNumSentenza())%>
            </font>
        </td>
      </tr>
      <tr>
        <td class="l" colspan=1>Data Sentenza</td>
        <td class="l" colspan="3">
            <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lContMod.getDataSentenza(),"dd-MM-yyyy"))%>
            &nbsp;
            </font>
        </td>
      </tr>
      <tr>
        <td class="l" colspan=1>Autorità Sentenza</td>
          <td class="l" colspan="3">
            <font class="campo"><%=StringUtils.toStringJSP(lContMod.getDescrTipoAutorita())%></font>
          </td>
      </tr>
      <tr>
        <td class="l" colspan=1>Luogo Sentenza</td>
        <td class="l" colspan="3">
            <font class="campo"><%=StringUtils.toStringJSP(lContMod.getDescrLuogoAutorita())%></font>
        </td>
      </tr>

      <tr>
        <% if (lContMod.getAnnoRegePm()!= null ) {%>
          <td class="l" colspan=1>Anno/Numero R.G.N.R.</td>
          <td class="l" colspan=1>
              <font class="campo">
              <%=StringUtils.toStringJSP(lContMod.getAnnoRegePm())%>
              /
              <%=StringUtils.toStringJSP(lContMod.getNumRegePm())%>
              </font>
          </td>
        <% } %>
    
        <%
        String anno_reg = StringUtils.toStringJSP(lContMod.getAnnoRegGen(),"");
        String num_reg  = StringUtils.toStringJSP(lContMod.getNumeroRegGen(),"");
        String tipo_reg = StringUtils.toStringJSP(lContMod.getTipoRegGen(),"");

        String lRegGen = anno_reg+"/"+num_reg+"&nbsp;"+tipo_reg;
        
        if (!lRegGen.equals("/&nbsp;") && !lRegGen.equals("/&nbsp;-")) 
        { %>
          <td class="l">Numero Reg.Gen.</td>
          <td class="L"><font class="campo"><%=lRegGen%></font></td>
        <% } %>
      </tr>

  <%  
    //==============================================================
    // Descrizione dello stato del dato 'Continuazione cumulo'
    //=============================================================
          lDescStato = "";
          if      ( lContMod.getFlagStato().equals("E")){lDescStato = "Dato Estratto dal fascicolo originale";}
          else if ( lContMod.getFlagStato().equals("I")){lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
          else if ( lContMod.getFlagStato().equals("M")){lDescStato = "Dato estratto modificato";}
          else if ( lContMod.getFlagStato().equals("C")){lDescStato = "Dato estratto cancellato";}
%>
  <tr>
    <td class="l" colspan="4">
      <font class="label"><%=lDescStato %></font>
    </td> 
  </tr>

<%
if(lContMod.getMotivoModifica()!= null && !lContMod.getMotivoModifica().equals(""))
{
%>
  <tr>
    <td class="l" colspan="1"><font class="label">Motivo Inserimento </font></td>
    <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lContMod.getMotivoModifica())%></font></td>
  </tr>  
<% }  %>

  <tr>
    <td class="l"></td>
  </tr>
  
<%
    } // Chiude While
%>   
       </table>
<%    
  } // Chiude if dettagliocont not null   
%>

 </FORM>

<%    
} // Chiude If (lPenCom != null)
else 
{ %>
  <table width="80%" >
    <tr>
      <td class="int" align="left">Pena Complessiva non definita </td>
    </tr>
  </table>  
<% }// end if/else lPenCom != null  %>

</body>
</html>