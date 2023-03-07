<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.siep.continuazione.model.ContinuazioneModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>


<jsp:useBean id="dettaglioPenaComplessiva" 	scope="request" class="siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel"/>
<jsp:useBean id="lTipoFunzione"       		scope="request" class="java.lang.String"/>
<jsp:useBean id="modo"       				scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     				scope="request" class="java.lang.String"/>
<jsp:useBean id="penaSigeModificabile"      scope="request" class="java.lang.String"/>
<jsp:useBean id="lPenaResMod"    	 	  	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<%
// Gestione funzione SIGE
	boolean modoSIGE = false;
	String validatoSige = "N";
	if (modo != null && modo.equalsIgnoreCase("SIGE"))
	{
		modoSIGE = true;
		// Se la penacomplessiva risulta non modificabile è come se fosse validato
		if(penaSigeModificabile.equalsIgnoreCase("NO"))
			validatoSige = "S";
	}

  FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
  
  PenaComplessivaModel lPenCom = (dettaglioPenaComplessiva!= null && dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva() != null) ? dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva() : null;
  SanzioneSostitutivaModel lSanSos = (dettaglioPenaComplessiva!= null && dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva() != null) ? dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva() : null;
  List lListCont = dettaglioPenaComplessiva.getContinuazioni();
	
  
   String lIdPenaCompStr = lPenCom != null ?  lPenCom.getIdPenaComplessiva().toString() : "";
  
  if(lSanSos == null)
    lSanSos = new SanzioneSostitutivaModel();
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Pena Complessiva </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<script language="JavaScript">
var aForm=null;
 function Verify()
  {
   alert("La funzione di Iscrizione Guidata è stata Interrotta");
   aForm=document.getElementById("Abbandona");
    Disabilita();
  }

 function DisabilitaPenAcc()
  {

   aForm=document.getElementById("PenAcc");
    Disabilita();
  }

 function Disabilita()
  {
 //   if (aForm==null)  /*perchè è stato eliminata la form Concessione Benefici CB */
 //      aForm=document.getElementById("CB");

    document.Abbandona.A.disabled = true;
    document.PenAcc.U.disabled = true;
   // document.CB.D.disabled = true;

   aForm.submit();
  }
</script>
<%}%>
</head>

<body class="corpo">
<FORM name="comandi" >
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Pena Complessiva</font>
      </td>
<%if(lTipoFunzione.equals("")) // lTipoFunzione per capire che si proviene da iscrizione guidata
{  %>
      <td class="LBG">
        <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_PENA_COMPLESSIVA%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiPenaComplessiva.CAMPO_ID_PENA_COMPLESSIVA%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lIdPenaCompStr%>" />
          <jsp:param name="FlagValidato" value="<%=( modoSIGE ? validatoSige : lFascicolo.getFlagValidato() )%>" />
        </jsp:include>
      </td>

<% } %>
	 <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

    </tr>
  </table>
  <br>
  <%if (!modoSIGE) { %>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <%} else {%>
   <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
	 <jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
  <%} %>
  <br>
</FORM>
<%if (lPenCom != null) { %>
  <table cellspacing=2 cellpadding=2 width=80%>
    <tr><td class="Titolo" colspan=4>Pena</td></tr>

    <tr>
    <% 
    if ((lPenCom.getNumAnniReclusione() != null) || (lPenCom.getNumMesiReclusione() != null)
      || (lPenCom.getNumGiorniReclusione() != null)) { %>
      <td class="l">Reclusione</td>
      <td class="l">
        <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumAnniReclusione(), "0")%>&nbsp;</font>
        <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumMesiReclusione(), "0")%>&nbsp;</font>
        <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumGiorniReclusione(), "0")%></font>
      </td>
    <% } %>
    <% if (lPenCom.getImportoMulta() != null) {%>
      <td class="l">Multa</td>
      <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(lPenCom.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
    <% } %>
    </tr>
    <tr>

  <% if ((lPenCom.getNumAnniArresto() != null) || (lPenCom.getNumMesiArresto() != null)
      || (lPenCom.getNumGiorniArresto() != null)) { %>
      <td class="l">Arresto</td>
      <td class="l">
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumAnniArresto(), "0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumMesiArresto(), "0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumGiorniArresto(), "0")%></font>
      </td>
   <% } %>
   <% if (lPenCom.getImportoAmmenda() != null) {%>
      <td class="l">Ammenda</td>
      <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(lPenCom.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
   <% } %>
    </tr>
<% 
		if (   (lPenCom.getDescrTipoPenaDetentivaDB() != null) 
		    && (!(lPenCom.getDescrTipoPenaDetentivaDB().equals("-")))) 
		{ 
%>
			<tr>
		  	<td class="l">Ergastolo</td>
		    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getDescrTipoPenaDetentivaDB())%></font>&nbsp;</td>
		  </tr>
<%
		}
%>
    <tr>
<% 
		if (lPenCom.getDataInizioIsolamentoDiurno() != null) 
		{ 
%>
      <td class="l">Data Inizio Isolamento Diurno</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenCom.getDataInizioIsolamentoDiurno(),"dd-MM-yyyy"))%>
        &nbsp;</font>
      </td>
<% 
		} 

		if (lPenCom.getDataFineIsolamentoDiurno() != null) 
		{ 
%>
      <td class="l">Data Fine Isolamento Diurno</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenCom.getDataFineIsolamentoDiurno(),"dd-MM-yyyy"))%>
        &nbsp;</font>
      </td>
<% 
		} 
%>
    </tr>
    <tr>
<% 
		if (	 (lPenCom.getNumAnniIsolamentoDiurno() != null) || (lPenCom.getNumMesiIsolamentoDiurno() != null)
    		|| (lPenCom.getNumGiorniIsolamentoDiurno() != null)) 
		{ 
%>
	    <td class="l">Durata Isolamento Diurno</td>
	    <td class="l">Anni
	      <font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumAnniIsolamentoDiurno(), "0") %>&nbsp;</font>
	    		Mesi
	      <font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumMesiIsolamentoDiurno(), "0")%>&nbsp;</font>
	    		Giorni
	     	<font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumGiorniIsolamentoDiurno(), "0")%>&nbsp;</font>
      </td>
<% 
		} 

		if (lPenCom.getDataPrescrizione() != null) 
		{ 
%>
   		<td class="l">Data Prescrizione</td>
			<td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenCom.getDataPrescrizione(),"dd-MM-yyyy"))%>
        &nbsp;</font>
      </td>
<% 
		} 
%>
   </tr>
   
   
   
   <% if (   lSanSos.getDescrTipoSanzione() != null
          || lSanSos.getNumAnni() != null
          || lSanSos.getNumMesi() != null
          || lSanSos.getNumGiorni() != null
          || lSanSos.getSanzionePecuniariaMulta() != null
          || lSanSos.getSanzionePecuniariaAmmenda() != null
         ) 
  {   
    String titolo = "Sanzione Sostitutiva";
    String descTipoSanzione = "Tipo Sanzione Sostitutiva";
    String durataLabel = "Durata Sanzione Sostitutiva";
    String descPenaPecuniariaMulta = "Pena Pecuniaria Sostitutiva Multa";
    
    if (lSanSos.isPenaSostitutiva ()) {
      titolo                  = "Pene sostitutive Pene Detentive Brevi";
      descTipoSanzione        = "Tipo Pena Sostitutiva";
      durataLabel             = "Durata Pena Sostitutiva";
      descPenaPecuniariaMulta = "Pena Pecuniaria Sostitutiva";
    }
  %>
  
    <tr><td class="Titolo" colspan="4"><%=titolo%></td></tr>
    
    <% if (lSanSos.getDescrTipoSanzione() != null) { %>
    <tr>
      <td class="l"><%=descTipoSanzione%></td>
      <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%></font>&nbsp;</td>
    </tr>
   <% } %>
   
   <% if ( lSanSos.getNumAnni() != null || lSanSos.getNumMesi() != null || lSanSos.getNumGiorni() != null) { %>
    <tr>
      <td class="l"><%=durataLabel%></td>
      <td class="l" colspan="3">
        <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumAnni(), "0")%>&nbsp;</font>
        <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumMesi(), "0")%>&nbsp;</font>
        <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumGiorni(), "0")%></font>
      </td>
    </tr>
  <% } %>
  
  <% if (lSanSos.getSanzionePecuniariaMulta() != null) { %>
    <tr>
      <td class="l" ><%=descPenaPecuniariaMulta%></td>
      <td class="l" colspan="2"><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaMulta())%></font>&nbsp;<font class="l">Euro</font></td>
    </tr>
  <% } %>
  
  <% if (lSanSos.getSanzionePecuniariaAmmenda() != null) { %>
    <tr>
      <td class="l">Pena Pecuniaria Sostitutiva Ammenda</td>
      <td class="l" colspan="2"><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
    </tr>
  <% } %>
  
<% } %>
  </table>
  
  
  
<%
      if (lListCont.size() != 0)
      {
%>
        <table cellspacing=2 cellpadding=2 width=60%>
          <tr><td class="Titolo" colspan=4>Continuazione con altre sentenze</td></tr>
<%
      }

      Iterator iter = lListCont.iterator();
      while (iter.hasNext())
      {
        ContinuazioneModel lContMod = (ContinuazioneModel)iter.next();
%>
        <tr>
          <td class="l">
            <table cellspacing=2 cellpadding=2 width=100%>
              <tr>
                <td class="l">Tipo Continuazione</td>
                <td class="l" colspan="3">
                  <font class="campo"><%=StringUtils.toStringJSP(lContMod.getDescrTipoContinuazione())%></font>
                </td>
              </tr>
              <tr>
                <td class="l">Anno/Numero Sentenza</td>
                <td class="L" colspan="3">
                  <font class="campo">
                    <%=StringUtils.toStringJSP(lContMod.getAnnoSentenza())%>
                    /
                    <%=StringUtils.toStringJSP(lContMod.getNumSentenza())%>
                  </font>
                </td>
              </tr>
              <tr>
                <td class="l">Data Sentenza</td>
                <td class="l" colspan="3">
                  <font class="campo">
                    <%=StringUtils.toStringJSP(DateUtils.getDateToString(lContMod.getDataSentenza(),"dd-MM-yyyy"))%>
                    &nbsp;
                  </font>
                </td>
              </tr>
              <tr>
                <td class="l">Autorità Sentenza</td>
                  <td class="l" colspan="3">
                    <font class="campo"><%=StringUtils.toStringJSP(lContMod.getDescrTipoAutorita())%></font>
                  </td>
              </tr>
              <tr>
                <td class="l">Luogo Sentenza</td>
                <td class="l" colspan="3">
                  <font class="campo"><%=StringUtils.toStringJSP(lContMod.getDescrLuogoAutorita())%></font>
                </td>
              </tr>
              <tr>
<%if (lContMod!= null && lContMod.getAnnoRegePm()!= null )
{%>
                <td class="l">Anno/Numero R.G.N.R.</td>
                <td class="l">
                  <font class="campo">
                    <%=StringUtils.toStringJSP(lContMod.getAnnoRegePm())%>
                    /
                    <%=StringUtils.toStringJSP(lContMod.getNumRegePm())%>
                  </font>
               </td>
<%
}
String reg="";
String anno_reg="";
String num_reg="";
if (lContMod.getAnnoRegeCap()!=null)
{
  reg="CAP";
  anno_reg=lContMod.getAnnoRegeCap()+"";
  num_reg=lContMod.getNumRegeCap()+"";
}
if (lContMod.getAnnoRegeCas()!=null)
{
  reg="CAS";
  anno_reg=lContMod.getAnnoRegeCas()+"";
  num_reg=lContMod.getNumRegeCas()+"";
}
if (lContMod.getAnnoRegeDib()!=null)
{
  reg="DIB";
  anno_reg=lContMod.getAnnoRegeDib()+"";
  num_reg=lContMod.getNumRegeDib()+"";
}
if (lContMod.getAnnoRegeCasap()!=null)
{
  reg="CASAP";
  anno_reg=lContMod.getAnnoRegeCasap()+"";
  num_reg=lContMod.getNumRegeCasap()+"";
}
if (lContMod.getAnnoRegeGip()!=null)
{
  reg="GIP";
  anno_reg=lContMod.getAnnoRegeGip()+"";
  num_reg=lContMod.getNumRegeGip()+"";
}

if (!reg.equals(""))
{
%>

      <td class="l">Numero Reg.Gen.</td>
      <td class="L"><font class="campo">
      <%=anno_reg%> / <%=num_reg%>&nbsp;&nbsp;&nbsp;  <%=reg%></font>
      </td>

<%}%>
                </td>
              </tr>

            </table>
          </td>
        </tr>
<%
    }
    if (lListCont.size() != 0)
    {
%>
      </table>
<%
     }
%>

<%
    // richiesta asir a9/rr/075 04-06-2009 SIEP MEV - Warning sul primo calcolo della pena 
    // paolo cherubini lunedi 11/10/2010

     if (lPenaResMod!=null  && lPenaResMod.getFlagValidato().equals("N"))  {%>
    <FORM name="calcolopena" >   
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActLoadCalcoloPena">
	<table>
	<tr>
		<td class="lRosso">
			<font class="lRosso">
				Attenzione! Primo calcolo della pena già effettuato. Per computare eventuali modifiche effettuate,
				è necessario procedere nuovamente con il calcolo della pena
			</font>
		</td>
	</tr>
	 <tr>
       <td>
        <INPUT  class="bottone" type="submit" name="CALCOLA" value="Calcola Fine Pena" onClick="">
       </td>
     </tr>
	</table>
	</FORM>
 <%   }
    // fine a9/rr/075 
    
 %>


<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<table>
<tr>
<td class="lNoBord">
<FORM method="POST" name="PenAcc" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penaaccessoria.action.ActLoadInserisciPenaAccessoria&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="button" name="U" value="Prosegui" onclick="Javascript:DisabilitaPenAcc();">
 </FORM>
</td>

<%--<td class="lNoBord">
  <FORM method="POST" name="CB" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.beneficio.action.ActLoadInserisciBeneficio&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="button" name="D" value="Concessione Benefici" onclick="Javascript:Disabilita();">
  </FORM>
</td>
--%>

<td class="lNoBord">
<FORM method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penacomplessiva.action.ActLoadDettaglioPenaComplessiva&<%=ICostantiPenaComplessiva.CAMPO_ID_PENA_COMPLESSIVA%>=<%=lPenCom.getIdPenaComplessiva()%>&lTipoFunzione=ritornodettaglio">
      <br><INPUT class="bottone" type="button" name="A" value="Abbandona" onclick="Javascript:return Verify();">
 </FORM>
</td>
</tr>
</table>
<%}  
}else { %>
  <table width="80%" >
  <tr>
        <td class="int" align="left">Pena Complessiva non definita </td>
</tr>
<% }// endif lPenCom != null  %>
 </table>
</body>
</html>