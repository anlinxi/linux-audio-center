let input = document.querySelector('input');
let label = document.querySelector('label');
input.onfocus = function () {
    label.style.display = 'none';
}
input.onblur = function () {
    if(input.value !== ''){
        return;
    }
    label.style.display = 'block';
}

let btn = document.querySelectorAll('#bigUl li');
let three = document.querySelector('.three');
for (let i = 0; i < btn.length; i++) {
    const element = btn[i];
    element.onclick = function() {
        for (let j = 0; j < btn.length; j++) {
            const e = btn[j];
            e.classList.remove('sel');
        }
        element.classList.add('sel');
        three.style.left = 175 + ( 47 * (1 + 2 * i) ) + 'px';
    }
}

let small = document.querySelectorAll('#small li');
for (let i = 0; i < small.length; i++) {
    const element = small[i];
    element.onclick = function () {
    for (let j = 0; j < small.length; j++) {
        const e = small[j];
        e.firstChild.classList.remove('selLi');
    }
    element.firstChild.classList.add('selLi');
    }
}

let tops = document.querySelector('#top');
tops.onclick = function() {
    window.scrollTo(0,0);
}
window.onscroll = function(){
    let getScrollTop = document.documentElement.scrollTop || document.body.scrollTop || window.pageYOffset;
    getScrollTop === 0 ? tops.style.display = 'none' : tops.style.display = 'block';
}