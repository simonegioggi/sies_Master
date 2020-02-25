NS6=false;
IE4=(document.all);
if (!IE4) {NS6=(document.getElementById);}
NS4=(document.layers);

function adjust() {
if ((NS4) || (NS6)) {
if (lastX==-1 || delayspeed==0)
{
lastX=window.pageXOffset + floatX;
lastY=window.pageYOffset + floatY;
}
else
{
var dx=Math.abs(window.pageXOffset+floatX-lastX);
var dy=Math.abs(window.pageYOffset+floatY-lastY);
var d=Math.sqrt(dx*dx+dy*dy);
var c=Math.round(d/10);
if (window.pageXOffset+floatX>lastX) {lastX=lastX+delayspeed+c;}
if (window.pageXOffset+floatX<lastX) {lastX=lastX-delayspeed-c;}
if (window.pageYOffset+floatY>lastY) {lastY=lastY+delayspeed+c;}
if (window.pageYOffset+floatY<lastY) {lastY=lastY-delayspeed-c;}
}
if (NS4){
document.layers['floatlayer'].pageX = lastX;
document.layers['floatlayer'].pageY = lastY;
}
if (NS6){
document.getElementById('floatlayer').style.left=lastX;
document.getElementById('floatlayer').style.top=lastY;
}
}
else if (IE4){
if (lastX==-1 || delayspeed==0)
{
lastX=document.body.scrollLeft + floatX;
lastY=document.body.scrollTop + floatY;
}
else
{
var dx=Math.abs(document.body.scrollLeft+floatX-lastX);
var dy=Math.abs(document.body.scrollTop+floatY-lastY);
var d=Math.sqrt(dx*dx+dy*dy);
var c=Math.round(d/10);
if (document.body.scrollLeft+floatX>lastX) {lastX=lastX+delayspeed+c;}
if (document.body.scrollLeft+floatX<lastX) {lastX=lastX-delayspeed-c;}
if (document.body.scrollTop+floatY>lastY) {lastY=lastY+delayspeed+c;}
if (document.body.scrollTop+floatY<lastY) {lastY=lastY-delayspeed-c;}
}
document.all['floatlayer'].style.posLeft = lastX;
document.all['floatlayer'].style.posTop = lastY;
} 
setTimeout('adjust()',50);
}

function define()
{
if ((NS4) || (NS6)) 
{ 
if (halign=="left") {floatX=ifloatX};
if (halign=="right") {floatX=window.innerWidth-ifloatX-layerwidth-20};
if (halign=="center") {floatX=Math.round((window.innerWidth-20)/2)-Math.round(layerwidth/2)};
if (valign=="top") {floatY=ifloatY};
if (valign=="bottom") {floatY=window.innerHeight-ifloatY-layerheight};
if (valign=="center") {floatY=Math.round((window.innerHeight-20)/2)-Math.round(layerheight/2)};
}
if (IE4) 
{
if (halign=="left") {floatX=ifloatX};
if (halign=="right") {floatX=document.body.offsetWidth-ifloatX-layerwidth-20}
if (halign=="center") {floatX=Math.round((document.body.offsetWidth-20)/2)-Math.round(layerwidth/2)}
if (valign=="top") {floatY=ifloatY};
if (valign=="bottom") {floatY=document.body.offsetHeight-ifloatY-layerheight}
if (valign=="center") {floatY=Math.round((document.body.offsetHeight-20)/2)-Math.round(layerheight/2)}
}
}
