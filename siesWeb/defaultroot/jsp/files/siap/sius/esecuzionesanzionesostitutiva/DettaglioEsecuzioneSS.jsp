<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<%@ page import="siap.siep.fascicolo.model.DettaglioFascicoloModel" %>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>
<%@ page import="siap.sius.esecuzionesanzionesostitutiva.action.ICostantiEsecuzioneSS" %>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="sanzioneSostitutiva" scope="request" class="siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel" />
<jsp:useBean id="sanzioneUno" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="sanzioni" scope="request" class="java.util.Vector" />
<jsp:useBean id="IdFascicoloSius" scope="request" class="java.lang.Object" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="correlati" scope="request" class="java.util.Vector" />
<jsp:useBean id="lCodUfficioFascicolo"  scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

  // Il parametro IdFascicoloSius può arrivare come stringa.
  if (request.getParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS) != null )
      IdFascicoloSius = (String) request.getParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);

  DettaglioFascicoloModel dettaglioFascSiep = (DettaglioFascicoloModel )request.getAttribute("dettaglioFascSiep");

  String lStrOrdDec = (sanzioneUno.getGeneraleProcedimentoModel().getCodTipoAtto().compareTo("04")==0 ) ? "Ordinanza N.ro : " : "Decreto N.ro : ";
%>

<%
// MEV_2023-35: si gestisce in form anche l'esecuzione Pene sostitutive
String codOggettoProcedimentoES = sanzioneUno.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
String strTitoloFunzione = "";
String strLabelBtnIscrizione = "";
String strLabelBtnModifica = "";
String strProcedimentES = "";
String strDurataES = "";
String strElencoES = "";
String strEsecuzionePriva ="";
if ("U019".equals(codOggettoProcedimentoES)) {
  strTitoloFunzione     = "Elenco dei Procedimenti relativi all' Esecuzione della Sanzione Sostitutiva";
  strLabelBtnIscrizione = "Iscrizione procedimento di Esecuzione S.S.";
  strLabelBtnModifica   = "Modifica procedimento di Esecuzione S.S.";
  strProcedimentES      = "N.ro Procedimento E.S.S. : ";
  strDurataES           = "Durata sanzione: ";
  strElencoES           = "Elenco Periodi Sanzione Sostitutiva ";
  strEsecuzionePriva    = "Esecuzione Sanzione Sostitutiva priva di procedimenti";
} else if ("U126".equals(codOggettoProcedimentoES)) {
  strTitoloFunzione     = "Elenco dei Procedimenti relativi all' Esecuzione della Pena Sostitutiva";
  strLabelBtnIscrizione = "Iscrizione procedimento di Esecuzione P.S.";
  strLabelBtnModifica   = "Modifica procedimento di Esecuzione P.S.";
  strProcedimentES      = "N.ro Procedimento E.P.S. : ";
  strDurataES           = "Durata pena: ";
  strElencoES           = "Elenco Periodi Pena Sostitutiva ";
  strEsecuzionePriva    = "Esecuzione Pena Sostitutiva priva di procedimenti";
}
%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco dei Procedimenti relativi all' Esecuzione della Sanzione Sostitutiva</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
      var node;
      function effettoTree(a)
      {
        node=document.getElementById("elenco"+a);
        node.style.display = (node.style.display == "none")? "block" : "none";
        document.images["image"+a].src = (node.style.display == "none")? "<%=IWebConstants.IMAGES_DIR%>expand.gif" : "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
        return false;
      }
    </script>

    <script language="JavaScript">
    	function ListaSanzioni()
   		{
    		desktop = window.open("/jsp/Main.jsp?Action=siap.sius.sanzionesostitutiva.action.ActLoadListaSanzioniSostitutiveUDS&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=IdFascicoloSius%>" , "Lista_Date", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=600, height=500");
    	}
    </script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>" >
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;
      <font class="campo"> <%=strTitoloFunzione%> </font>
      </td>
<%
      BigDecimal IdSoggetto = sanzioneUno.getFascicoloSiusModel().getSoggetto().getIdSoggetto();
%>
      <!-- 30/06/2009 Abilitazione dei bottoni di Iscrizione Proc. di S.S. (figlio) 
      	e di modifica Proc. di E.S.S. solo se l'ufficio Connesso è titolare del fascicolo	-->
<%
			if (lCodUfficioFascicolo.length() == 0 ||
				 (lCodUfficioFascicolo.length() > 0 &&
					lCodUfficioFascicolo.compareTo(UtenteConnesso.getUfficioUtente().getCodUfficio()) == 0 ) )
			{
%>
	      <!-- BOTTONE DI ISCRIZIONE PROCEDIMENTO DI SS (FIGLIO) -->
	      <td class="LBG">
<%
	      if ( dettaglioFascSiep !=null  )
	      {%>
	        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadInsFascicoloDaSoggettoUDS&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=IdFascicoloSius%>&TornaQui=<%=TornaQui%>">
	          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="<%=strLabelBtnIscrizione %>" width="24" height="24" border="0">
	        </a>
	      <%}else{%>
	        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadInsFascicoloDaSoggettoUDS&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=IdFascicoloSius%>&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=IdSoggetto%>&TornaQui=<%=TornaQui%>">
	          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="<%=strLabelBtnIscrizione %>"  width="24" height="24" border="0">
	        </a>
	      <%}%>
	      </td>
	
	      <!-- BOTTONE DI MODIFICA PROCEDIMENTO DI ESS -->
	      <td class="LBG">
	      <%
	      if ( dettaglioFascSiep !=null  )
	      {%>
	        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.esecuzionesanzionesostitutiva.action.ActLoadModificaEsecuzioneSS&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=IdFascicoloSius%>&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=IdSoggetto%>&<%=ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP%>=<%=dettaglioFascSiep.getFascicoloSiep().getIdFascicoloSiep()%>&TornaQui=<%=TornaQui%>">
	          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="<%=strLabelBtnModifica %>" width="24" height="24" border="0">
	        </a>
	      <%}else{%>
	        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.esecuzionesanzionesostitutiva.action.ActLoadModificaEsecuzioneSS&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=IdFascicoloSius%>&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=IdSoggetto%>&TornaQui=<%=TornaQui%>">
	          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="<%=strLabelBtnModifica %>" width="24" height="24" border="0">
	        </a>
	      <%}%>
	      </td>
		<%}%>

      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
      </table>
      <table>
        <tr>
          <br>

          <td class="lVerde"><%=strProcedimentES %><font class="cVerde">
          <%--=sanzioneUno.getGeneraleProcedimentoModel().getAnnoS1()%>/<%=sanzioneUno.getGeneraleProcedimentoModel().getProgrS1()--%>
            <a class="CliccabileFermo" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=sanzioneUno.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>" Title="Dettaglio Procedimento SIUS">
              <%=sanzioneUno.getGeneraleProcedimentoModel().getAnnoS1()%>/<%=sanzioneUno.getGeneraleProcedimentoModel().getProgrS1()%>
            </a>

          </font></td>
          <td class="c" colspan="2">  relativo a: <font class="campo"><%=sanzioneSostitutiva.getDescrTipoSanzione() %></font></td>
        </tr>
        <tr>
          <td class="l"><%=lStrOrdDec%><font class="campo"><%=sanzioneSostitutiva.getAnnoS07()%>/<%=sanzioneSostitutiva.getProgrS07()%></font>
          <td class="c" colspan="2"><font class="campo"> <%=sanzioneSostitutiva.getDescrTipoAutoritaEmittOrd()%> - <%=sanzioneSostitutiva.getDescrLuogoAutoritaEmittOrd()%> </font>
         <font class="label">del: </font><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sanzioneSostitutiva.getDataOrdinanza(),"dd-MM-yyyy"),"-")%> </font>
        </td>
        </tr>
        <tr>
          <td class="l">Soggetto: <font class="campo"><%=sanzioneUno.getFascicoloSiusModel().getSoggetto().getCognome()%>&nbsp;
          <%=sanzioneUno.getFascicoloSiusModel().getSoggetto().getNome()%></font>

          <td class="c" colspan="2"><font class="label">
<%
        if (sanzioneUno.getFascicoloSiusModel().getSoggetto().getSesso().compareTo("F")==0)
        {
%>
          &nbsp; nata il
<%
        }
        else
        {
%>
         &nbsp; nato il
<%
        }
%>
        </font>

     <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sanzioneUno.getFascicoloSiusModel().getSoggetto().getDataNascita(),"dd-MM-yyyy"),"-")%>
       </font><font class="label">&nbsp; in : </font>

         <font class="campo">
<%
       if (sanzioneUno.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita().compareTo("-")==0)
       {
%>
         <%=sanzioneUno.getFascicoloSiusModel().getSoggetto().getDescrStatoNascita()%>
<%
       }
       else
       {
%>
         <%=sanzioneUno.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita() + "  ("+sanzioneUno.getFascicoloSiusModel().getSoggetto().getCodProvinciaNascita()+")" %>
<%
       }
%>
          </font>
        </td>
        </tr>

<%      if ( dettaglioFascSiep !=null  )
        {
%>
          <tr>
            <td class="l">Titolo Esecutivo N.ro Siep : <font class="campo"><%=dettaglioFascSiep.getFascicoloSiep().getChiaveAnno()%>/<%=dettaglioFascSiep.getFascicoloSiep().getChiaveProgr()%></font></td>
            <td class="c" colspan="2" nowrap><font class="campo"> <%=dettaglioFascSiep.getFascicoloSiep().getDescrTipoUfficio()%> - <%=dettaglioFascSiep.getFascicoloSiep().getDescrComuneUfficio()%>
            </font><font class="label"> del: </font><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(dettaglioFascSiep.getFascicoloSiep().getDataIscrizione(),"dd-MM-yyyy"),"-")%>
            </font></td>
          </tr>
        <%}%>

        <tr>
          <td class="l">Data inizio: <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sanzioneSostitutiva.getDataInizioSanzione(),"dd-MM-yyyy"),"-")%>
          </font></td>
          <td class="c">Data termine (iniziale): <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sanzioneSostitutiva.getDataTermineIniziale(),"dd-MM-yyyy"),"-")%>
          </font></td>
          <td class="c">Data termine (attuale): <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sanzioneSostitutiva.getDataTermineAttuale(),"dd-MM-yyyy"),"-")%>
          </font></td>
        </tr>
 		<tr>
			<td class="L" width="40%"><%=strDurataES %>: &nbsp;&nbsp;Anni
				<%if (sanzioneSostitutiva.getNumAnniSanzione() !=null){%>
	        		<font class="campo"><%=sanzioneSostitutiva.getNumAnniSanzione()%>
	        		</font>
	        	<%}%>
	        	&nbsp;&nbsp;Mesi
	        	<%if (sanzioneSostitutiva.getNumMesiSanzione() !=null){%>
	        		<font class="campo"><%=sanzioneSostitutiva.getNumMesiSanzione()%>
	        		</font>
	        	<%}%>
	        	&nbsp;&nbsp;Giorni
	        	<%if (sanzioneSostitutiva.getNumGiorniSanzione() !=null){%>
	        		<font class="campo"><%=sanzioneSostitutiva.getNumGiorniSanzione()%>
	        		</font>
	        	<%}%>
	        </td>

					<td class="l">
						<a class="CliccabileFermo" href="Javascript:ListaSanzioni();">
		   				<%=strElencoES %>
						</a>
					</td>

        </tr>
        <tr>
          <td class="l">Luogo esecuzione da Ordinanza: <font class="campo">
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

  <br>

<%
  // Elenco dei procedimenti Correlati
  Vector procCorrelati = new Vector();
  Iterator itxProc = correlati.iterator();
  int jESS =0;

%>

  <table cellspacing=2 cellpadding=2 width=100%>
    <tr>
      <td class="int" width=8%>Numero SIUS</td>
      <td class="int" width=10%>Data Iscrizione</td>
      <td class="int" width=20%>Contenuto</td>
      <td class="int" width=10%>Provvedimento</td>
      <td class="int" width=10%>Data Emissione</td>
      <td class="int" width=20%>Motivo Provvedimento</td>
      <td class="int" width=22%>Esito</td>
    </tr>
<%
    if (sanzioni.isEmpty()){
%>
      <tr>
        <td class="c" colspan=7><font class="label">
         <%=strEsecuzionePriva %> 
        </font></td>
      </tr>
<%
    }else{
     Iterator itx = sanzioni.iterator();
     String sUfficio = "Procura";

     while ( itx.hasNext())
     {
      FascicoloGPModel fascicolo = (FascicoloGPModel)itx.next();
      if (fascicolo.getFascicoloSiusModel().getIdFascicoloSiusOrigine()==null)
      {
%>
        <tr>
          <td class="c" width=8%><font class="label">
            <a class="CliccabileFermo" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicolo.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>" Title="Dettaglio Procedimento SIUS">
              <%=fascicolo.getFascicoloSiusModel().getChiaveAnno()%>/<%=fascicolo.getFascicoloSiusModel().getChiaveProgr()%>
            </a>
<%          if (fascicolo.getFascicoloSiusModel().getNumeroFascicoliUnificati() != null &&
                fascicolo.getFascicoloSiusModel().getNumeroFascicoliUnificati().intValue() > 0 )
            {%>
            <a href="#1" onClick="return effettoTree(<%=jESS%>)"><img name="image<%=jESS%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Elenco dei Procedimenti Correlati" ></a>
          <%}%>
          </font></td>
          <td class="c" width=10%><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy"),"-")%></font></td>
          <!-- Utilizzo setDescrMittente come vettore per Evento.Descr_Tipo_Provvedimento -->
          <td class="c" width=20%><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrPosGiuridica()%></font></td>
<% 				if(fascicolo.getFascicoloSiusModel().getDescrStatoFascicolo().compareToIgnoreCase("01") == 0) { %>
        	  <td class="c" width=10%><font class="crosso"><%= DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getStatoFascicolo(), fascicolo.getFascicoloSiusModel().getDescrStatoFascicolo())%></font></td>
						<td class="c" width=10%><font class="label"></font></td>
						<td class="c" width=20%><font class="label"></font></td>
						<td class="c" width=22%><font class="label"></font></td>
<% 					} else { %>
          <td class="c" width=10%><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font></td>
          <!-- Utilizzo setDataDefinizione come vettore per Evento.DataEmissione -->
          <td class="c" width=10%><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataDefinizione(),"dd/MM/yyyy"),"-")%></font></td>
          <!-- Utilizzo setDescrDefinizione come vettore per Motivo Provvedimento -->
          <td class="c" width=20%><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrDefinizione()%></font></td>
          <!-- Utilizzo setDescrTipoAtto come vettore per Evento.DescrProvvedimento -->
          <td class="c" width=22%><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrTipoAtto()%> </font></td>
<% 				} %>
        </tr>
      <%}%>

<%    //Caricamento dei procedimenti del Tribunale Correlati.
      if (fascicolo.getFascicoloSiusModel().getNumeroFascicoliUnificati() != null &&
          fascicolo.getFascicoloSiusModel().getNumeroFascicoliUnificati().intValue()>0)
      {
        itxProc = correlati.iterator();
        while ( itxProc.hasNext())
        {
          FascicoloGPModel fascicoloGPCorrelato = (FascicoloGPModel)itxProc.next();
          if (fascicoloGPCorrelato.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null  &&
              fascicoloGPCorrelato.getFascicoloSiusModel().getIdFascicoloSiusOrigine().compareTo(fascicolo.getFascicoloSiusModel().getIdFascicoloSius())==0 )
          {
            procCorrelati.add(fascicoloGPCorrelato);
          }
        }
%>
    		</table>
    			<div id="elenco<%=jESS%>" style="display:none; width:100%;">
      			<%@include file="/jsp/files/siap/sius/esecuzionesanzionesostitutiva/ListaProcedimentiCorrelati.jspf" %>
    			</div>
<%	    	procCorrelati.clear();
    			jESS++;
					%><table><%
      }
		}
  }
%>

  </FORM>
  </body>
</html>