let imgs = document.querySelector('.rotation img');
    bigBc = document.querySelector('.rotation-body');
    leftBtn = document.querySelector('.rotation-left');
    rightBtn = document.querySelector('.rotation-right');
    index = 1;
function lunbo(next){
  next ?index-- : index++;
  if(index > 4) index = 1;
  imgs.src = `./imgs/lb${ index }.jpg`;
  bigBc.style.background = `url(./pngs/dlb${ index }.jpg)`;
  bigBc.style.backgroundSize = '6000px';
  bigBc.style.backgroundPosition = 'center center';
}
setInterval(lunbo, 5000);


rightBtn.onclick = () => {
  lunbo();
}

leftBtn.onclick = () => {
  lunbo(1);
}