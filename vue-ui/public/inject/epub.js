let reader = parent.document.reader;

function debounce(func, wait) { // 滚轮监听防抖函数
    let timeout;
    return function () {
        let context = this;
        let args = arguments;
        if (timeout) clearTimeout(timeout);
        timeout = setTimeout(() => {
            func.apply(context, args)
        }, wait);
    }
}

function handleScroll(e){
    let direction = e.deltaY > 0 ? "next" : "prev";
    reader.$emit("changePage", direction);
}

document.onclick = () => {
    reader.$emit("toggle");
}

window.addEventListener('mousewheel', debounce(handleScroll,300), true)
|| window.addEventListener("DOMMouseScroll",debounce(handleScroll,300),false);