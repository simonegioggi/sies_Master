<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneEstesaModel"%>

<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>
<%@ page import="siap.sius.penapecuniaria.action.ICostantiSiusPenaPecuniaria"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>

<jsp:useBean id="richiesteconversioni" scope="request" class="java.util.Vector" />

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>


<%
  TenoreModel[] tenori = datiOrdinanza.getTenori(); 

  String lCodTipoProv = ICostantiProvvedimento.COD_ORDINANZA;

  Iterator itx = richiesteconversioni.iterator();
  while (itx.hasNext())
  {
    RichiestaConversioneEstesaModel lRicConEstesa = (RichiestaConversioneEstesaModel)itx.next();
    String lTitoloRichiestaCPP = ""; 
    if (lRicConEstesa.getRichiestaConversione().getFasSieIdFascicoloSiep() != null &&
        lRicConEstesa.getRichiestaConversione().getFasSiuIdFascicoloSius() == null) 
      lTitoloRichiestaCPP = "Pena Pecuniaria relativa al N. SIEP "+lRicConEstesa.getFasSiep().getChiaveAnno()+" / "+lRicConEstesa.getFasSiep().getChiaveProgr();
    else
      lTitoloRichiestaCPP = "Pena Pecuniaria inserita dall' UDS";

    %>
      <table cellspacing="2" cellpadding="2" width="90%">
        <tr>
          <td class="Titolo" width="100%" colspan="6" > <%=lTitoloRichiestaCPP%></td>
        </tr>

        <tr>
          <td rowspan="3" width="20%" class="l"> 
            <font class="label"> Importo non pagato:</font>&nbsp;<br/>
            <% if (lRicConEstesa.getRichiestaConversione().getImportoMulta()!= null) { %>
              <font class="campo"> <%=StringUtils.toEuroFormat(lRicConEstesa.getRichiestaConversione().getImportoMulta()) %></font>&nbsp;Euro<br>
            <%}%>
            
          </td>
          
          <td rowspan="3" width="80%" class="l"> 
            <font class="label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; C o n v e r t i t o &nbsp;&nbsp; i n<br><br></font>
            <font class="label">&nbsp;Anni: </font>
            <font class="crosso"> <%=StringUtils.toStringJSP(lRicConEstesa.getRichiestaConversione().getDurataEsitoAnni()) %> </font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <font class="label">&nbsp;Mesi: </font>
            <font class="crosso"> <%=StringUtils.toStringJSP(lRicConEstesa.getRichiestaConversione().getDurataEsitoMesi()) %> </font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <font class="label">&nbsp;Giorni: </font>
            <font class="crosso"> <%=StringUtils.toStringJSP(lRicConEstesa.getRichiestaConversione().getDurataEsitoGiorni()) %> </font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <font class="label"> di&nbsp;&nbsp; </font>
            
            <% if (lRicConEstesa.getRichiestaConversione().getCodTipoSanzione().compareTo("01")==0) {%>
            <font class="crosso"> Semiliberta' Sostitutiva </font>
            <%} else if (lRicConEstesa.getRichiestaConversione().getCodTipoSanzione().compareTo("02")==0)  {%>
            <font class="crosso"> Detenzione Domiciliare sostitutiva </font>
            <%} else if (lRicConEstesa.getRichiestaConversione().getCodTipoSanzione().compareTo("03")==0) {%>
            <font class="crosso"> Lavoro Pubblica Utilita' sostitutiva </font>
            <%}%>
          </td>
        </tr>
      </table> 
        
<%
      } //while
%>

<br>